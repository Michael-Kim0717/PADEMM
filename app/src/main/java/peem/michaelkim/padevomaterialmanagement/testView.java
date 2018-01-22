package peem.michaelkim.padevomaterialmanagement;

/**
 * Created by Michael Kim on 1/19/2018.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class testView extends AppCompatActivity {

    ArrayList<monsterBase> monstersInBox = new ArrayList<>();

    ListView allMonsters;

    // Database declarations.
    SQLiteDatabase sqLiteDatabase;
    monsterBoxDBHelper monsterBoxDBHelper;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_view);

        // Get the database information.
        monsterBoxDBHelper = new monsterBoxDBHelper(this);
        sqLiteDatabase = monsterBoxDBHelper.getReadableDatabase();
        cursor = monsterBoxDBHelper.getListOfMonsters(sqLiteDatabase);
        if (cursor.moveToFirst()){
            do {
                String element, link, id, name, evoChoice, evoChoices, evoMats, priority;
                link = cursor.getString(0);
                id = cursor.getString(1);
                name = cursor.getString(2);
                evoChoice = cursor.getString(3);
                priority = cursor.getString(4);
                element = cursor.getString(5);
                evoChoices = cursor.getString(6);
                evoMats = cursor.getString(7);
                monsterBase currentMonster = new monsterBase(link, id, name, evoChoice, priority, element, evoChoices, evoMats);
                monstersInBox.add(currentMonster);
            }
            while (cursor.moveToNext());
        }

        allMonsters = (ListView) findViewById(R.id.allMonsters);
        monsterListAdapter monsterListAdapterAdapter = new monsterListAdapter();
        allMonsters.setAdapter(monsterListAdapterAdapter);
    }

    private class monsterListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return monstersInBox.size();
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
            view = getLayoutInflater().inflate(R.layout.listview_test_view, null);

            String materialIDS = "", choiceIDS = "";

            ImageView monsterImage = (ImageView) view.findViewById(R.id.originalMonster);
            ImageView monsterChoice = (ImageView) view.findViewById(R.id.evoChoiceMonster);
            TextView monsterID = (TextView) view.findViewById(R.id.monsterID);
            TextView monsterName = (TextView) view.findViewById(R.id.monsterName);
            TextView monsterPriority = (TextView) view.findViewById(R.id.monsterPriority);
            TextView monsterElement = (TextView) view.findViewById(R.id.monsterElement);
            TextView monsterEvoChoices = (TextView) view.findViewById(R.id.monsterEvoChoices);
            TextView monsterMaterials = (TextView) view.findViewById(R.id.monsterEvoMaterials);

            Picasso.with(getBaseContext()).load(monstersInBox.get(i).imageLink).fit().into(monsterImage);
            Picasso.with(getBaseContext()).load(monstersInBox.get(i).evoChoice).fit().into(monsterChoice);
            monsterID.setText(monstersInBox.get(i).monsterID);
            monsterName.setText(monstersInBox.get(i).monsterName);
            monsterPriority.setText(monstersInBox.get(i).priority);
            monsterElement.setText(monstersInBox.get(i).monsterElement);

            try {
                JSONObject evoChoiceJSON = new JSONObject(monstersInBox.get(i).evoChoices);
                JSONArray evoChoiceList = evoChoiceJSON.optJSONArray("evoChoices");
                for (int evoCount = 0; evoCount < evoChoiceList.length(); evoCount++){
                    choiceIDS = choiceIDS + " " + evoChoiceList.optString(evoCount);
                }

                JSONObject evoMatJSON = new JSONObject(monstersInBox.get(i).evoMats);
                JSONArray evoMatList = evoMatJSON.optJSONArray("evoMatJSON");
                for (int evoCount = 0; evoCount < evoMatList.length(); evoCount++){
                    materialIDS = materialIDS + " " + evoMatList.optString(evoCount);
                }
            }
            catch (Exception e){
                Log.e("Reading Error", "Failed to Read EVO JSON");
            }

            monsterEvoChoices.setText(choiceIDS);
            monsterMaterials.setText(materialIDS);

            return view;
        }
    }

}
