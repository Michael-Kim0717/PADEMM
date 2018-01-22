package peem.michaelkim.padevomaterialmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class homescreen extends AppCompatActivity {

    // Widget declarations.
    Button toMonsterBox, testButton;
    ListView evoMaterials;

    // Variable declarations.
    String JSON_MONSTERS;

    // Database declarations.
    SQLiteDatabase sqLiteDatabase;
    monsterBoxDBHelper monsterBoxDBHelper;
    Cursor cursor;
    ArrayList<material> materials = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        // For the initial launch, create the table for the units.
        monsterBoxDBHelper = new monsterBoxDBHelper(getApplicationContext());
        sqLiteDatabase = monsterBoxDBHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + monsterBase.monsterBaseDB.TABLE_NAME + "("
                + monsterBase.monsterBaseDB.IMAGE_LINK + " TEXT,"
                + monsterBase.monsterBaseDB.MONSTER_ID + " TEXT,"
                + monsterBase.monsterBaseDB.MONSTER_NAME + " TEXT,"
                + monsterBase.monsterBaseDB.EVO_CHOICE + " TEXT,"
                + monsterBase.monsterBaseDB.PRIORITY + " TEXT,"
                + monsterBase.monsterBaseDB.MONSTER_ELEMENT + " TEXT,"
                + monsterBase.monsterBaseDB.EVO_CHOICES + " TEXT,"
                + monsterBase.monsterBaseDB.EVO_MATERIALS + " TEXT);");
        sqLiteDatabase.close();

        // Grab monster list.
        new getMonsters().execute();

        // Widget instantiations.
        toMonsterBox = (Button) findViewById(R.id.toMonsterBox);
        testButton = (Button) findViewById(R.id.testButton);
        evoMaterials = (ListView) findViewById(R.id.evoMatList);

        final homeLeftAdapter homeLeftAdapter = new homeLeftAdapter();
        evoMaterials.setAdapter(homeLeftAdapter);

        toMonsterBox.bringToFront();

        // Get all material information from the database.
        sqLiteDatabase = monsterBoxDBHelper.getReadableDatabase();
        cursor = monsterBoxDBHelper.getListOfMonsters(sqLiteDatabase);
        if (cursor.moveToFirst()){
            do {
                /*
                String element, link, id, name, evoChoice, evoChoices, evoMats, priority;
                link = cursor.getString(0);
                Log.e("link", link);
                id = cursor.getString(1);
                Log.e("id", id);
                name = cursor.getString(2);
                Log.e("name", name);
                evoChoice = cursor.getString(3);
                Log.e("evoChoice", evoChoice);
                priority = cursor.getString(4);
                Log.e("priority", priority);
                element = cursor.getString(5);
                Log.e("element", element);
                evoChoices = cursor.getString(6);
                */
                String evoMats = cursor.getString(7);

                /*monsterBase currentMonster = new monsterBase(link, id, name, evoChoice, priority, element, evoChoices, evoMats);
                monstersInBox.add(currentMonster);
                */
                try {
                    if (!evoMats.isEmpty()) {
                        JSONObject materialsObject = new JSONObject(evoMats);
                        JSONArray materialArray = materialsObject.optJSONArray("evoMatJSON");
                        for (int i = 0; i < materialArray.length(); i++) {
                            String materialID = materialArray.optString(i);
                            int materialIndex = indexOfID(materials, materialID);
                            if (materialIndex > -1){
                                materials.set(materialIndex, new material(materialID, materials.get(materialIndex).count + 1));
                            }
                            else {
                                materials.add(new material(materialID, 1));
                            }
                        }
                    }
                }
                catch (Exception e){
                    Log.e("Material JSON Exception", "Error in retrieving material JSON.");
                }
            }
            while (cursor.moveToNext());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    // Grab every single monster in the JSON array.
                    JSONArray jsonArray = new JSONArray(JSON_MONSTERS);
                    int jsonCount = 0;

                    while (jsonCount < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(jsonCount);

                        int indexOfID = indexOfID(materials, JO.getString("id"));
                        if (indexOfID > -1){
                            materials.set(indexOfID, new material("https://www.padherder.com" + JO.getString("image60_href"), materials.get(indexOfID).count));
                        }

                        jsonCount++;
                    }

                    homeLeftAdapter.notifyDataSetChanged();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 4000);

        // When the test view button is pressed, proceed to the test view screen.
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), testView.class)  );
            }
        });

        // When the monster box button is pressed, proceed to the monster box screen.
        toMonsterBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), monsterBoxScreen.class));
            }
        });

    }

    class getMonsters extends AsyncTask<Void, Void, String> {
        String json_monsters;

        @Override
        // Get the json of all the monsters.
        protected void onPreExecute() {
            json_monsters = "https://www.padherder.com/api/monsters/";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Establish the connection between all the monsters and build the json into a String.
                URL url = new URL(json_monsters);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_MONSTERS = bufferedReader.readLine()) != null){
                    stringBuilder.append(JSON_MONSTERS + "\n");
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
            JSON_MONSTERS = result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private class homeLeftAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return materials.size();
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
            view = getLayoutInflater().inflate(R.layout.listview_material_list, null);

            ImageView materialImage = (ImageView) view.findViewById(R.id.materialImage);
            final TextView materialCount = (TextView) view.findViewById(R.id.materialCount);
            Button addMaterial = (Button) view.findViewById(R.id.addMaterial);
            Button subtractMaterial = (Button) view.findViewById(R.id.subtractMaterial);

            // Populate image view with material icon.
            Picasso.with(getBaseContext()).load(materials.get(i).id).into(materialImage);

            // Populate text view with amount of materials you own / need.
            final int currentlyOwned = 0;

            materialCount.setText(Integer.toString(currentlyOwned) + " / " + Integer.toString(materials.get(i).count));

            // Upon clicking the add/subtract material button, set the amount of materials you own accordingly.
            addMaterial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    materialCount.setText("155 / " + Integer.toString(materials.get(i).count));
                }
            });

            subtractMaterial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentlyOwned == 0){
                        Toast.makeText(getApplicationContext(), "You have 0 of this material.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        materialCount.setText("155 / " + Integer.toString(materials.get(i).count));
                    }
                }
            });

            return view;
        }
    }

    private int indexOfID (ArrayList<material> materials, String id) {
        for (int count = 0; count < materials.size(); count ++) {
            if (materials.get(count).id.equals(id)){
                return count;
            }
        }
        return -1;
    }
}
