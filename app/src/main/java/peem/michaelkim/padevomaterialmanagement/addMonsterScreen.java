package peem.michaelkim.padevomaterialmanagement;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Michael Kim on 11/22/2017.
 */

public class addMonsterScreen extends AppCompatActivity{

    // JSON Object Declarations.
    String JSON_STRING = "";
    JSONArray jsonArray = null;

    // Widget Declarations.
    Button searchButton;
    ListView monsterList;
    EditText searchBar;

    // List of monsters to show.
    ArrayList<monsterBase> allMonsters = new ArrayList<>();

    // Database Variables.
    monsterBoxDBHelper monsterBoxDBHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_monster);

        // Create the JSON object to grab the list of monsters.
        new BackgroundTask().execute();

        monsterList = (ListView) findViewById(R.id.allMonstersList);
        final monsterListAdapter monsterListAdapter = new monsterListAdapter();
        monsterList.setAdapter(monsterListAdapter);

        searchBar = (EditText) findViewById(R.id.searchField);
        searchButton = (Button) findViewById(R.id.searchMonster);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the list upon every search.
                allMonsters.clear();
                // Error checking.
                if (searchBar.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter Your Search", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        // Grab every single monster in the JSON array.
                        jsonArray = new JSONArray(JSON_STRING);
                        int jsonCount = 0;
                        String imageLink, monsterID, monsterName;

                        while (jsonCount < jsonArray.length()) {
                            JSONObject JO = jsonArray.getJSONObject(jsonCount);

                            String searchValue = searchBar.getText().toString().toLowerCase();

                            // Check if the search entry is a number so we specifically search for the ID.
                            try {
                                int idSearch = Integer.parseInt(searchValue);
                                if (JO.getString("id").equals(searchValue)){
                                    imageLink = "https://www.padherder.com" + JO.getString("image60_href");
                                    monsterID = JO.getString("id");
                                    monsterName = JO.getString("name");

                                    monsterBase monsterBase = new monsterBase(imageLink, monsterID, monsterName, "?", "1");
                                    allMonsters.add(monsterBase);
                                }
                                monsterListAdapter.notifyDataSetChanged();
                            }
                            // Else grab the substring and check each individual monster's name.
                            catch (Exception e){
                                int searchValueLength = searchValue.length();
                                String searchToLower = JO.getString("name").toLowerCase();
                                if (searchToLower.contains(searchValue) && !JO.getString("type").equals("0")) {
                                    imageLink = "https://www.padherder.com" + JO.getString("image60_href");
                                    monsterID = JO.getString("id");
                                    monsterName = JO.getString("name");

                                    monsterBase monsterBase = new monsterBase(imageLink, monsterID, monsterName, "?", "1");
                                    allMonsters.add(monsterBase);
                                }
                                monsterListAdapter.notifyDataSetChanged();
                            }

                            jsonCount++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String json_url;
        @Override
        // Get the json of all the monsters.
        protected void onPreExecute() {
            json_url = "https://www.padherder.com/api/monsters/";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Establish the connection between all the monsters and build the json into a String.
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null){
                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        // Save the value into a global value.
        protected void onPostExecute(String result) {
            JSON_STRING = result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private class monsterListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return allMonsters.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.listview_all_monsters, null);

            ImageView monsterImage = (ImageView) view.findViewById(R.id.monsterImage);
            TextView monsterID = (TextView) view.findViewById(R.id.monsterID);
            TextView monsterName = (TextView) view.findViewById(R.id.monsterName);
            Button addMonsterButton = (Button) view.findViewById(R.id.addMonsterButton);

            addMonsterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    monsterBoxDBHelper = new monsterBoxDBHelper(getBaseContext());
                    sqLiteDatabase = monsterBoxDBHelper.getWritableDatabase();
                    monsterBoxDBHelper.addMonsterToBox(allMonsters.get(i).imageLink, allMonsters.get(i).monsterID, allMonsters.get(i).monsterName, "?", "1", sqLiteDatabase);
                    Toast.makeText(getBaseContext(), "Monster Added.", Toast.LENGTH_SHORT).show();
                    sqLiteDatabase.close();
                }
            });

            Picasso.with(getBaseContext()).load(allMonsters.get(i).imageLink).fit().into(monsterImage);
            monsterID.setText(allMonsters.get(i).monsterID);
            monsterName.setText(allMonsters.get(i).monsterName);

            return view;
        }
    }
}
