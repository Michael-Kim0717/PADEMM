package peem.michaelkim.padevomaterialmanagement;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
 * Created by Michael Kim on 11/23/2017.
 */

public class monsterDetailsScreen extends AppCompatActivity {

    // Widget declarations.
    ImageView mainPic;
    ImageView evoMat1, evoMat2, evoMat3, evoMat4, evoMat5;
    Spinner prioritySpinner;
    ImageView evoChoice1, evoChoice2, evoChoice3, evoChoice4, evoChoice5, evoChoice6;
    Button saveButton;

    // JSON and Evo List declarations.
    String JSON_EVOS, JSON_MONSTERS;
    JSONArray jsonArray;
    ArrayList<String> listOfChoices = new ArrayList<>();
    ArrayList<evoChoice> evoChoices = new ArrayList<>();
    ArrayList<String> listOfChoiceLinks = new ArrayList<>();
    ArrayList<String> listOfMaterialIDS = new ArrayList<>();
    boolean eC1 = false, eC2 = false, eC3 = false, eC4 = false, eC5 = false, eC6 = false;

    // Database declarations.
    monsterBoxDBHelper monsterBoxDBHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monster_details);

        // Evo Materials instantiation.
        evoMat1 = (ImageView) findViewById(R.id.evoMat1);
        evoMat2 = (ImageView) findViewById(R.id.evoMat2);
        evoMat3 = (ImageView) findViewById(R.id.evoMat3);
        evoMat4 = (ImageView) findViewById(R.id.evoMat4);
        evoMat5 = (ImageView) findViewById(R.id.evoMat5);

        // Evo Choices instantiation.
        evoChoice1 = (ImageView) findViewById(R.id.evoChoice1);
        evoChoice2 = (ImageView) findViewById(R.id.evoChoice2);
        evoChoice3 = (ImageView) findViewById(R.id.evoChoice3);
        evoChoice4 = (ImageView) findViewById(R.id.evoChoice4);
        evoChoice5 = (ImageView) findViewById(R.id.evoChoice5);
        evoChoice6 = (ImageView) findViewById(R.id.evoChoice6);

        hideEvos();

        // Grab the clicked monsters image and display it on the very top image.
        String image = getIntent().getStringExtra("Link");
        mainPic = (ImageView) findViewById(R.id.monsterImage);
        Picasso.with(this).load(image).into(mainPic);

        // Grab both monster JSON and evolution JSON.
        new getEvolutions().execute();
        new getMonsters().execute();

        // Priority spinner and set spinner's value to whatever was saved.
        prioritySpinner = (Spinner) findViewById(R.id.prioritySpinner);
        String[] priorities = {"1", "2", "3", "4", "5"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, priorities);
        prioritySpinner.setAdapter(arrayAdapter);
        String priority = getIntent().getStringExtra("Priority");
        int position = arrayAdapter.getPosition(priority);
        prioritySpinner.setSelection(position);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String monsterID = getIntent().getStringExtra("ID");
                try {
                    // Populate the list of evolution choices with the ID's of evolutions that the clicked monster can evolve into.
                    // Populates an array with the list of ID's required to evolve the monster.
                    JSONObject jsonObject = new JSONObject(JSON_EVOS);
                    JSONArray jsonArray = jsonObject.getJSONArray(monsterID);
                    int jsonCount = 0;

                    while (jsonCount < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(jsonCount);

                        // Populates the array.
                        ArrayList<String> ids = prepareString(JO.getString("materials"));

                        // Enters in a value of the evolution's id and its materials.
                        evoChoices.add(new evoChoice(JO.getString("evolves_to"), ids, new ArrayList<String>()));

                        listOfChoices.add(JO.getString("evolves_to"));

                        jsonCount++;
                    }

                    // Using the previously populated array of ID's, we grab the images from the monster JSON.
                    // Populate the evolution choice images on the bottom with the images we grabbed.

                    // Grab every single monster in the JSON array.
                    jsonArray = new JSONArray(JSON_MONSTERS);
                    jsonCount = 0;

                    while (jsonCount < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(jsonCount);

                        if (listOfChoices.contains(JO.getString("id"))){
                            listOfChoiceLinks.add("https://www.padherder.com" + JO.getString("image60_href"));
                        }

                        for (int i = 0; i < evoChoices.size(); i++){
                            if (evoChoices.get(i).evoMaterials.contains(JO.getString("id"))){
                                for (int j = 0; j < evoChoices.get(i).evoMaterials.size(); j++) {
                                    if (evoChoices.get(i).evoMaterials.get(j).equals(JO.getString("id"))) {
                                        evoChoices.get(i).evoMaterialsLinks.add("https://www.padherder.com" + JO.getString("image60_href"));
                                    }
                                }
                            }
                        }

                        jsonCount++;
                    }
                    Log.e("SIZE", Integer.toString(listOfChoiceLinks.size()));
                    for (int i = 0; i < listOfChoiceLinks.size(); i++) {
                        if (i == 0) {
                            Picasso.with(getBaseContext()).load(listOfChoiceLinks.get(i)).into(evoChoice1);
                            if (getIntent().getStringExtra("EvoChoice").equals(listOfChoiceLinks.get(i))) {
                                eC1 = true;
                                evoChoice1.setAlpha((float) 1);
                                showMaterials(0);
                            }
                            else{
                                evoChoice1.setAlpha((float) 0.6);
                            }
                        }
                        else if (i == 1) {
                            Picasso.with(getBaseContext()).load(listOfChoiceLinks.get(i)).into(evoChoice2);
                            if (getIntent().getStringExtra("EvoChoice").equals(listOfChoiceLinks.get(i))) {
                                eC2 = true;
                                evoChoice2.setAlpha((float) 1);
                                showMaterials(1);
                            }
                            else{
                                evoChoice2.setAlpha((float) 0.6);
                            }
                        }
                        else if (i == 2) {
                            Picasso.with(getBaseContext()).load(listOfChoiceLinks.get(i)).into(evoChoice3);
                            if (getIntent().getStringExtra("EvoChoice").equals(listOfChoiceLinks.get(i))) {
                                eC3 = true;
                                evoChoice3.setAlpha((float) 1);
                                showMaterials(2);
                            }
                            else{
                                evoChoice3.setAlpha((float) 0.6);
                            }
                        }
                        else if (i == 3) {
                            Picasso.with(getBaseContext()).load(listOfChoiceLinks.get(i)).into(evoChoice4);
                            if (getIntent().getStringExtra("EvoChoice").equals(listOfChoiceLinks.get(i))) {
                                eC4 = true;
                                evoChoice4.setAlpha((float) 1);
                                showMaterials(3);
                            }
                            else{
                                evoChoice4.setAlpha((float) 0.6);
                            }
                        }
                        else if (i == 4) {
                            Picasso.with(getBaseContext()).load(listOfChoiceLinks.get(i)).into(evoChoice5);
                            if (getIntent().getStringExtra("EvoChoice").equals(listOfChoiceLinks.get(i))) {
                                eC5 = true;
                                evoChoice5.setAlpha((float) 1);
                                showMaterials(4);
                            }
                            else{
                                evoChoice5.setAlpha((float) 0.6);
                            }
                        }
                        else if (i == 5) {
                            Picasso.with(getBaseContext()).load(listOfChoiceLinks.get(i)).into(evoChoice6);
                            if (getIntent().getStringExtra("EvoChoice").equals(listOfChoiceLinks.get(i))) {
                                eC6 = true;
                                evoChoice6.setAlpha((float) 1);
                                showMaterials(5);
                            }
                            else{
                                evoChoice6.setAlpha((float) 0.6);
                            }
                        }
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 4000);

        // When the first evolution is clicked, set the evolution materials under to the corresponding materials.
        evoChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllEvoChoicesToDefault();
                setAllValuesToFalse();
                eC1 = true;
                evoChoice1.setAlpha((float) 1);
                if (evoChoices.size() >= 1) {
                    showMaterials(0);
                }
            }
        });

        // When the second evolution is clicked, set the evolution materials under to the corresponding materials.
        evoChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllEvoChoicesToDefault();
                setAllValuesToFalse();
                eC2 = true;
                evoChoice2.setAlpha((float) 1);
                if (evoChoices.size() >= 2) {
                    showMaterials(1);
                }
            }
        });

        // When the third evolution is clicked, set the evolution materials under to the corresponding materials.
        evoChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllEvoChoicesToDefault();
                setAllValuesToFalse();
                eC3 = true;
                evoChoice3.setAlpha((float) 1);
                if (evoChoices.size() >= 3) {
                    showMaterials(2);
                }
            }
        });

        // When the second evolution is clicked, set the evolution materials under to the corresponding materials.
        evoChoice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllEvoChoicesToDefault();
                setAllValuesToFalse();
                eC4 = true;
                evoChoice4.setAlpha((float) 1);
                if (evoChoices.size() >= 4) {
                    showMaterials(3);
                }
            }
        });

        // When the second evolution is clicked, set the evolution materials under to the corresponding materials.
        evoChoice5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllEvoChoicesToDefault();
                setAllValuesToFalse();
                eC5 = true;
                evoChoice5.setAlpha((float) 1);
                if (evoChoices.size() >= 5) {
                    showMaterials(4);
                }
            }
        });

        // When the last evolution is clicked, set the evolution materials under to the corresponding materials.
        evoChoice6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllEvoChoicesToDefault();
                setAllValuesToFalse();
                eC6 = true;
                evoChoice6.setAlpha((float) 1);
                if (evoChoices.size() >= 6) {
                    showMaterials(5);
                }
            }
        });

        saveButton = (Button) findViewById(R.id.saveDetails);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monsterBoxDBHelper = new monsterBoxDBHelper(getApplicationContext());
                sqLiteDatabase = monsterBoxDBHelper.getWritableDatabase();
                String evoChoice = "?";
                String evoMaterialList = "", evoChoiceList = "";
                try {
                    if (eC1) {
                        evoChoice = listOfChoiceLinks.get(0);

                        // Grabs the Evo Material and Choice Arraylist and converts them to Strings via JSON to save into the SQLiteDatabase.
                        JSONObject EvoMatJSON = new JSONObject();
                        EvoMatJSON.put("evoMatJSON", new JSONArray(evoChoices.get(0).evoMaterials));
                        evoMaterialList = EvoMatJSON.toString();
                    }
                    else if (eC2) {
                        evoChoice = listOfChoiceLinks.get(1);

                        JSONObject EvoMatJSON = new JSONObject();
                        EvoMatJSON.put("evoMatJSON", new JSONArray(evoChoices.get(1).evoMaterials));
                        evoMaterialList = EvoMatJSON.toString();
                    }
                    else if (eC3) {
                        evoChoice = listOfChoiceLinks.get(2);

                        JSONObject EvoMatJSON = new JSONObject();
                        EvoMatJSON.put("evoMatJSON", new JSONArray(evoChoices.get(2).evoMaterials));
                        evoMaterialList = EvoMatJSON.toString();
                    }
                    else if (eC4) {
                        evoChoice = listOfChoiceLinks.get(3);

                        JSONObject EvoMatJSON = new JSONObject();
                        EvoMatJSON.put("evoMatJSON", new JSONArray(evoChoices.get(3).evoMaterials));
                        evoMaterialList = EvoMatJSON.toString();
                    }
                    else if (eC5) {
                        evoChoice = listOfChoiceLinks.get(4);

                        JSONObject EvoMatJSON = new JSONObject();
                        EvoMatJSON.put("evoMatJSON", new JSONArray(evoChoices.get(4).evoMaterials));
                        evoMaterialList = EvoMatJSON.toString();
                    }
                    else if (eC6) {
                        evoChoice = listOfChoiceLinks.get(5);

                        JSONObject EvoMatJSON = new JSONObject();
                        EvoMatJSON.put("evoMatJSON", new JSONArray(evoChoices.get(5).evoMaterials));
                        evoMaterialList = EvoMatJSON.toString();
                    }
                }
                catch (Exception e) {
                    Log.e("invalid evoMatException", "Evo Material JSON exception");
                }


                try {
                    JSONObject EvoChoices = new JSONObject();
                    EvoChoices.put("evoChoices", new JSONArray(listOfChoices));
                    evoChoiceList = EvoChoices.toString();
                }
                catch (Exception e){
                    Log.e("invalid choiceException", "Evo Choice JSON exception");
                }

                int count = monsterBoxDBHelper.updateMonster(getIntent().getStringExtra("Link"),
                        getIntent().getStringExtra("Name"),
                        getIntent().getStringExtra("ID"),
                        evoChoice,
                        prioritySpinner.getSelectedItem().toString().trim(),
                        getIntent().getStringExtra("Element"),
                        evoChoiceList,
                        evoMaterialList,
                        sqLiteDatabase);
                Toast.makeText(getApplicationContext(), count + " contact updated.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    class getEvolutions extends AsyncTask<Void, Void, String> {
        String json_monsters, json_evos;

        @Override
        // Get the json of all the monsters.
        protected void onPreExecute() {
            json_monsters = "https://www.padherder.com/api/monsters/";
            json_evos = "https://www.padherder.com/api/evolutions/";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Establish the connection between all the monsters and build the json into a String.
                URL url = new URL(json_evos);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_EVOS = bufferedReader.readLine()) != null){
                    stringBuilder.append(JSON_EVOS + "\n");
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
            JSON_EVOS = result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
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

    private void setAllEvoChoicesToDefault(){
        int linksSize = listOfChoiceLinks.size();
        if (linksSize >= 1) {
            evoChoice1.setAlpha((float) 0.6);
        }
        if (linksSize >= 2) {
            evoChoice2.setAlpha((float) 0.6);
        }
        if (linksSize >= 3) {
            evoChoice3.setAlpha((float) 0.6);
        }
        if (linksSize >= 4) {
            evoChoice4.setAlpha((float) 0.6);
        }
        if (linksSize >= 5) {
            evoChoice5.setAlpha((float) 0.6);
        }
        if (linksSize == 6) {
            evoChoice6.setAlpha((float) 0.6);
        }
    }

    private ArrayList<String> prepareString(String materialsArray){
        ArrayList<String> returnedIDs = new ArrayList<>();
        String currArray, prepString;

        // [[1086,2],[251,1],[250,1],[151,1]]

        // [1086,2],[251,1],[250,1],[151,1]
        currArray = materialsArray.substring(1, materialsArray.length()-1);

        // While the ] is not the last item, continue to pull out array values and work with those.
        while (currArray.indexOf(']') != currArray.length()-1) {
            prepString = currArray.substring(0, currArray.indexOf(']') + 1);

            String idNumber = prepString.substring(1, prepString.indexOf(','));
            int count = Integer.parseInt(prepString.substring(prepString.indexOf(',')+1, prepString.indexOf(',')+2));

            for (int i = 0; i < count; i++){
                returnedIDs.add(idNumber);
            }

            currArray = currArray.substring(currArray.indexOf(']') + 2);
        }
        String idNumber = currArray.substring(1, currArray.indexOf(','));
        int count = Integer.parseInt(currArray.substring(currArray.indexOf(',')+1, currArray.indexOf(',')+2));

        for (int i = 0; i < count; i++){
            returnedIDs.add(idNumber);
        }

        return returnedIDs;
    }

    private void setAllValuesToFalse() {
        eC1 = false;
        eC2 = false;
        eC3 = false;
        eC4 = false;
        eC5 = false;
        eC6 = false;
    }

    private void hideEvos() {
        evoMat1.setAlpha((float) 0);
        evoMat2.setAlpha((float) 0);
        evoMat3.setAlpha((float) 0);
        evoMat4.setAlpha((float) 0);
        evoMat5.setAlpha((float) 0);

        evoChoice1.setAlpha((float) 0);
        evoChoice2.setAlpha((float) 0);
        evoChoice3.setAlpha((float) 0);
        evoChoice4.setAlpha((float) 0);
        evoChoice5.setAlpha((float) 0);
        evoChoice6.setAlpha((float) 0);
    }

    private void showMaterials(int evoChoice) {
        evoMat1.setAlpha((float) 1);
        Picasso.with(getApplicationContext()).load(evoChoices.get(evoChoice).evoMaterialsLinks.get(0)).into(evoMat1);
        evoMat2.setAlpha((float) 1);
        Picasso.with(getApplicationContext()).load(evoChoices.get(evoChoice).evoMaterialsLinks.get(1)).into(evoMat2);
        evoMat3.setAlpha((float) 1);
        Picasso.with(getApplicationContext()).load(evoChoices.get(evoChoice).evoMaterialsLinks.get(2)).into(evoMat3);
        evoMat4.setAlpha((float) 1);
        Picasso.with(getApplicationContext()).load(evoChoices.get(evoChoice).evoMaterialsLinks.get(3)).into(evoMat4);
        evoMat5.setAlpha((float) 1);
        Picasso.with(getApplicationContext()).load(evoChoices.get(evoChoice).evoMaterialsLinks.get(4)).into(evoMat5);
    }

}
