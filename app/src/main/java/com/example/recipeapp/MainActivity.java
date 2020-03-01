package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.recipeapp.ui.RecipeListFragment;
import com.example.recipeapp.utils.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager myfragment = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = myfragment.beginTransaction();
        fragmentTransaction.add(R.id.framehome, new RecipeListFragment());
        fragmentTransaction.commit();
        databaseHelper = new DatabaseHelper(this);
    }
}
