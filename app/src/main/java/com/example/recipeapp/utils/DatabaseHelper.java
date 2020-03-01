package com.example.recipeapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+db_Table_Recipe);
        db.execSQL("DROP TABLE IF EXISTS "+db_Table_Ingridients);
        db.execSQL("DROP TABLE IF EXISTS "+db_Table_Preparations);
        onCreate(db);
    }
}
