package peem.michaelkim.padevomaterialmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Michael Kim on 1/23/2018.
 */

public class myMaterialsDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "MATERIALS.DB";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_QUERY =
            "CREATE TABLE " + material.materialDB.MATERIAL_TABLE + "("
                    + material.materialDB.MATERIAL_ID + " TEXT,"
                    + material.materialDB.MATERIAL_COUNT + " TEXT);";

    public myMaterialsDBHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("MATERIAL OPERATIONS", "Database Created.");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_QUERY);
        Log.e("MATERIAL OPERATIONS", "Table Created.");
    }

    public void addMaterial (String id, String count, SQLiteDatabase db){
        db.execSQL(
                "INSERT INTO " + material.materialDB.MATERIAL_TABLE + "(material_id, material_count)"
                + " SELECT " + id + ", " + count
                + " WHERE NOT EXISTS (SELECT 1 FROM " + material.materialDB.MATERIAL_TABLE + " WHERE material_id = " + id + ");");

        Log.e("MATERIAL OPERATIONS", "Material Added.");
    }

    public int updateCount (String id, String count, SQLiteDatabase sqLiteDatabase){
        ContentValues contentValues = new ContentValues();

        contentValues.put(material.materialDB.MATERIAL_ID, id);
        contentValues.put(material.materialDB.MATERIAL_COUNT, count);

        String selection = material.materialDB.MATERIAL_ID + " LIKE ?";
        String[] selection_args = {id};

        int returnValue = sqLiteDatabase.update(material.materialDB.MATERIAL_TABLE, contentValues, selection, selection_args);

        Log.e("MATERIAL OPERATIONS", "Material Updated.");

        return returnValue;
    }

    public Cursor getMaterials (SQLiteDatabase sqLiteDatabase){
        Cursor cursor;

        String[] projections =
                {
                        material.materialDB.MATERIAL_ID,
                        material.materialDB.MATERIAL_COUNT
                };

        cursor = sqLiteDatabase.query(material.materialDB.MATERIAL_TABLE, projections, null, null, null, null, null);

        Log.e("MATERIAL OPERATIONS", "Retrieving Materials...");

        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
