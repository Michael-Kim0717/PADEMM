package peem.michaelkim.padevomaterialmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Michael Kim on 11/22/2017.
 */

public class monsterBoxScreen extends AppCompatActivity {

    ArrayList<String> monsterLinks = new ArrayList<>();
    ArrayList<monsterBase> monstersInBox = new ArrayList<>();

    // Widget declarations.
    FloatingActionButton addMonster;
    ListView boxList;

    // Database declarations.
    SQLiteDatabase sqLiteDatabase;
    monsterBoxDBHelper monsterBoxDBHelper;
    Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monster_box);

        addMonster = (FloatingActionButton) findViewById(R.id.addMonster);

        // When the add monster button is clicked, proceed to the add monster screen.
        addMonster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), addMonsterScreen.class));
            }
        });

        // Get the database information.
        monsterBoxDBHelper = new monsterBoxDBHelper(this);
        sqLiteDatabase = monsterBoxDBHelper.getReadableDatabase();
        cursor = monsterBoxDBHelper.getListOfMonsters(sqLiteDatabase);
        if (cursor.moveToFirst()){
            do {
                String link, id, name, evoChoice, priority;
                link = cursor.getString(0);
                id = cursor.getString(1);
                name = cursor.getString(2);
                evoChoice = cursor.getString(3);
                priority = cursor.getString(4);
                monsterBase currentMonster = new monsterBase(link, id, name, evoChoice, priority);
                monstersInBox.add(currentMonster);
            }
            while (cursor.moveToNext());
        }

        boxList = (ListView) findViewById(R.id.boxList);
        boxAdapter boxAdapter = new boxAdapter();
        boxList.setAdapter(boxAdapter);

        boxList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent changeEvoOptions = new Intent(getBaseContext(), monsterDetailsScreen.class);
                changeEvoOptions.putExtra("Link", monstersInBox.get(i).imageLink);
                changeEvoOptions.putExtra("Name", monstersInBox.get(i).monsterName);
                changeEvoOptions.putExtra("EvoChoice", monstersInBox.get(i).evoChoice);
                changeEvoOptions.putExtra("Priority", monstersInBox.get(i).priority);
                changeEvoOptions.putExtra("ID", monstersInBox.get(i).monsterID);
                startActivity(changeEvoOptions);
            }
        });
    }

    private class boxAdapter extends BaseAdapter{
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
            view = getLayoutInflater().inflate(R.layout.listview_monster_box, null);

            ImageView monster1 = (ImageView) view.findViewById(R.id.monster1);
            ImageView monsterToEvo = (ImageView) view.findViewById(R.id.monsterToEvo);
            TextView priority = (TextView) view.findViewById(R.id.priorityLevel);

            Picasso.with(getBaseContext()).load(monstersInBox.get(i).imageLink).fit().into(monster1);
            if (monstersInBox.get(i).evoChoice.equals("?")) {
                Picasso.with(getBaseContext()).load("http://puzzledragonx.com/en/img/thumbnail/0.png").fit().into(monsterToEvo);
            }
            else{
                Picasso.with(getBaseContext()).load(monstersInBox.get(i).evoChoice).fit().into(monsterToEvo);
            }
            priority.setText(monstersInBox.get(i).priority);

            return view;
        }
    }

}
