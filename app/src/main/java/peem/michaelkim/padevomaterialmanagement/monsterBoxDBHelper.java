package peem.michaelkim.padevomaterialmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Michael Kim on 11/24/2017.
 */

public class monsterBoxDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MONSTERBOX.DB";
    private static final int DATABASE_VERSION = 5;
    private static final String CREATE_QUERY =
            "CREATE TABLE " + monsterBase.monsterBaseDB.TABLE_NAME + "("
                    + monsterBase.monsterBaseDB.IMAGE_LINK + " TEXT,"
                    + monsterBase.monsterBaseDB.MONSTER_ID + " TEXT,"
                    + monsterBase.monsterBaseDB.MONSTER_NAME + " TEXT,"
                    + monsterBase.monsterBaseDB.EVO_CHOICE + " TEXT,"
                    + monsterBase.monsterBaseDB.PRIORITY + " TEXT,"
                    + monsterBase.monsterBaseDB.MONSTER_ELEMENT + " TEXT,"
                    + monsterBase.monsterBaseDB.EVO_CHOICES + " TEXT,"
                    + monsterBase.monsterBaseDB.EVO_MATERIALS + " TEXT);";

    public monsterBoxDBHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("DB OP", "Database Created.");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_QUERY);
        Log.e("DB OP", "Table Created.");
    }

    public void addMonsterToBox(String link, String id, String name, String evoChoice, String priority, String element, String evoChoices, String evoMats, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(monsterBase.monsterBaseDB.IMAGE_LINK, link);
        contentValues.put(monsterBase.monsterBaseDB.MONSTER_ID, id);
        contentValues.put(monsterBase.monsterBaseDB.MONSTER_NAME, name);
        contentValues.put(monsterBase.monsterBaseDB.EVO_CHOICE, evoChoice);
        contentValues.put(monsterBase.monsterBaseDB.PRIORITY, priority);
        contentValues.put(monsterBase.monsterBaseDB.MONSTER_ELEMENT, element);
        contentValues.put(monsterBase.monsterBaseDB.EVO_CHOICES, evoChoices);
        contentValues.put(monsterBase.monsterBaseDB.EVO_MATERIALS, evoMats);
        db.insert(monsterBase.monsterBaseDB.TABLE_NAME, null, contentValues);
        Log.e("DB OP", "Monster Added.");
    }

    public Cursor getListOfMonsters(SQLiteDatabase sqLiteDatabase){
        Cursor cursor;

        String[] projections =
                {
                        monsterBase.monsterBaseDB.IMAGE_LINK,
                        monsterBase.monsterBaseDB.MONSTER_ID,
                        monsterBase.monsterBaseDB.MONSTER_NAME,
                        monsterBase.monsterBaseDB.EVO_CHOICE,
                        monsterBase.monsterBaseDB.PRIORITY,
                        monsterBase.monsterBaseDB.MONSTER_ELEMENT,
                        monsterBase.monsterBaseDB.EVO_CHOICES,
                        monsterBase.monsterBaseDB.EVO_MATERIALS
                };

        cursor = sqLiteDatabase.query(monsterBase.monsterBaseDB.TABLE_NAME, projections, null, null, null, null, null);

        return cursor;
    }

    public int updateMonster (String link, String name, String id, String evoChoice, String priority, String element, String evoChoices, String evoMaterials, SQLiteDatabase sqLiteDatabase){
        ContentValues contentValues = new ContentValues();
        contentValues.put(monsterBase.monsterBaseDB.MONSTER_NAME, name);
        contentValues.put(monsterBase.monsterBaseDB.IMAGE_LINK, link);
        contentValues.put(monsterBase.monsterBaseDB.MONSTER_ID, id);
        contentValues.put(monsterBase.monsterBaseDB.EVO_CHOICE, evoChoice);
        contentValues.put(monsterBase.monsterBaseDB.PRIORITY, priority);
        contentValues.put(monsterBase.monsterBaseDB.MONSTER_ELEMENT, element);
        contentValues.put(monsterBase.monsterBaseDB.EVO_CHOICES, evoChoices);
        contentValues.put(monsterBase.monsterBaseDB.EVO_MATERIALS, evoMaterials);

        String selection = monsterBase.monsterBaseDB.IMAGE_LINK + " LIKE ?";
        String[] selection_args = {link};

        int count = sqLiteDatabase.update(monsterBase.monsterBaseDB.TABLE_NAME, contentValues, selection, selection_args);

        return count;
    }

    public void deleteMonster (String name, SQLiteDatabase sqLiteDatabase) {
        String selection = monsterBase.monsterBaseDB.MONSTER_NAME + " LIKE ?";
        String[] selection_args = {name};
        sqLiteDatabase.delete(monsterBase.monsterBaseDB.TABLE_NAME, selection, selection_args);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
