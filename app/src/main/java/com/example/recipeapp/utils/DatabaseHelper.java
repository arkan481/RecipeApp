package com.example.recipeapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String db_Name = "RecipeApp.db";
    public static final String db_Table_Recipe = "Recipe_tb";
    public static final String db_Col_Recipe_ID = "RecipeID";
    public static final String db_Col_Recipe_Name = "RecipeName";
    public static final String db_Col_Recipe_LastEdited = "RecipeLastEdited";
    public static final String db_Col_Recipe_Image = "RecipeImage";
    public static final String db_Col_Recipe_ServingSize = "RecipeServingSize";
    public static final String db_Table_Ingridients = "Ingridients_tb";
    public static final String db_Col_Ingridients_ID = "IngridientsID";
    public static final String db_Col_Ingridients_RecipeID = "RecipeID";
    public static final String db_Col_Ingridients_IngridientstName = "IngridientsName";
    public static final String db_Table_Preparations = "Preparations_tb";
    public static final String db_Col_Preparations_ID = "PreparationsID";
    public static final String db_Col_Preparations_RecipeID = "RecipeID";
    public static final String db_Col_Preparations_PreparationsName = "PreparationsName";
    public static int db_Version = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, db_Name, null, db_Version);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+db_Table_Recipe+"" +
                "(RecipeID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "RecipeName VARCHAR," +
                "RecipeLastEdited VARCHAR," +
                "RecipeImage VARCHAR," +
                "RecipeServingSize INTEGER)");
        db.execSQL("CREATE TABLE "+db_Table_Ingridients+"" +
                "(IngridientsID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "RecipeID INTEGER NOT NULL," +
                "IngridientsName VARCHAR," +
                "FOREIGN KEY (RecipeID) REFERENCES "+db_Table_Recipe+("RecipeID") +
                ")");
        db.execSQL("CREATE TABLE "+db_Table_Preparations+"" +
                "(PreparationsID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "RecipeID INTEGER NOT NULL," +
                "PreparationsName VARCHAR," +
                "FOREIGN KEY (RecipeID) REFERENCES "+db_Table_Recipe+("RecipeID") +
                ")");
        //TODO : ADD CONSTRAINT TO DELETE ON UPDATE FOR THE FOREIGN KEY
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+db_Table_Recipe);
        db.execSQL("DROP TABLE IF EXISTS "+db_Table_Ingridients);
        db.execSQL("DROP TABLE IF EXISTS "+db_Table_Preparations);
        onCreate(db);
    }
    public Cursor fetchAllRecipedata() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from "+db_Table_Recipe,null);
        return res;
    }
    public Cursor fetchAllIngridients(String RecipeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from "+db_Table_Ingridients+" where RecipeID = (?)",new String[]{RecipeID});
        return res;
    }
    public Cursor fetchAllPreparations(String RecipeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+db_Table_Preparations+" WHERE RecipeID = (?)",new String[]{RecipeID});
        return res;
    }
    public void insertRecipe (String RecipeName,String RecipeLastEdited,String RecipeImage,String RecipeServingSize) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO "+db_Table_Recipe+" (RecipeName,RecipeLastEdited,RecipeImage,RecipeServingSize) VALUES (?,?,?,?)",new String[]{RecipeName,RecipeLastEdited,RecipeImage,RecipeServingSize});
    }
    public void deleteRecipe(String RecipeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+db_Table_Recipe+" WHERE RecipeID = (?)",new String[]{RecipeID});
    }
    public void insertingridients(String RecipeID,String IngridientsName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO "+ db_Table_Ingridients+" (RecipeID,IngridientsName) VALUES (?,?)",new String[] {RecipeID,IngridientsName});
    }
    public void insertPreparations(String RecipeID,String PreparationsName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO "+ db_Table_Preparations+" (RecipeID,PreparationsName) VALUES (?,?)",new String[] {RecipeID,PreparationsName});
    }
    public String fetchlatestRecipeID() {
        String id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT MAX(RecipeID) FROM "+ db_Table_Recipe,null);
        res.moveToNext();
        id=res.getString(0);
        return id;
    }
}
