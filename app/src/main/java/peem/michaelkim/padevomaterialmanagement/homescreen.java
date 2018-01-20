package peem.michaelkim.padevomaterialmanagement;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class homescreen extends AppCompatActivity {

    // Widget declarations.
    Button toMonsterBox;
    Button testButton;

    SQLiteDatabase sqLiteDatabase;
    monsterBoxDBHelper monsterBoxDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        toMonsterBox = (Button) findViewById(R.id.toMonsterBox);
        toMonsterBox.bringToFront();

        testButton = (Button) findViewById(R.id.testButton);

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
    }
}
