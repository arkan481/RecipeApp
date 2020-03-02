package com.example.recipeapp.ui;


import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.recipeapp.R;
import com.example.recipeapp.adapter.RecipeGridAdapter;
import com.example.recipeapp.model.RecipeModel;
import com.example.recipeapp.utils.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {

    Spinner SortSpinner;
    RelativeLayout searchlayout;
    GridView recipegrid;
    ArrayList<RecipeModel> recipeModels;
    RecipeGridAdapter gridAdapter;
    FloatingActionButton fabplus;
    ArrayAdapter<String> sortadapter;
    Bundle recipeModelsBundle = new Bundle();
    DatabaseHelper databaseHelper;


    public RecipeListFragment(DatabaseHelper databaseHelper) {
        // Required empty public constructor
        this.databaseHelper=databaseHelper;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recipe_list,container,false);
        recipegrid = v.findViewById(R.id.recipegrid);

        if (recipeModels==null) {
            recipeModels =new ArrayList<RecipeModel>();
            System.out.println("haisdsdsd"+recipeModels);
        }else {

        }
        gridAdapter = new RecipeGridAdapter(getActivity(),populaterecipegrid(),databaseHelper,this);
        recipegrid.setAdapter(gridAdapter);
        SortSpinner = v.findViewById(R.id.sortspinnerid);
        fabplus = v.findViewById(R.id.fabplusid);
        sortadapter = new ArrayAdapter<String>(
                getContext(),
                R.layout.customspinneritem,
                getResources().getStringArray(R.array.sortitem)
        );
        sortadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SortSpinner.setAdapter(sortadapter);
        fabplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeModelsBundle.putString("appbartitle","Add Recipe");
                recipeModelsBundle.putBoolean("isupdating",false);
                recipeModelsBundle.putSerializable("Recipemodels",(Serializable) recipeModels);
                Fragment fragment = new RecipeDetailFragment(databaseHelper);
                fragment.setArguments(recipeModelsBundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framehome,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        refreshgridview();
        return v;

    }

    public void refreshgridview() {
        recipeModels.clear();
        populaterecipegrid();
        gridAdapter.notifyDataSetChanged();
        recipegrid.invalidateViews();
        recipegrid.refreshDrawableState();
    }

   ArrayList<RecipeModel> populaterecipegrid() {
        System.out.println("this flag called");
        Cursor res = databaseHelper.fetchAllRecipedata();
        while (res.moveToNext()) {
            recipeModels.add(new RecipeModel(res.getInt(0),res.getString(1),res.getString(2),res.getString(3),getingridients(res.getInt(0)),getpreparations(res.getInt(0))));
        }
        return recipeModels;
    }
    ArrayList<String > getingridients(int id) {
        ArrayList<String>ingridients=new ArrayList<>();
        Cursor res = databaseHelper.fetchAllIngridients(String.valueOf(id));
        while (res.moveToNext()) {
            ingridients.add(res.getString(2));
        }
        return ingridients;
    }

    ArrayList<String > getpreparations(int id) {
        ArrayList<String>preparations=new ArrayList<>();
        Cursor res = databaseHelper.fetchAllPreparations(String.valueOf(id));
        while (res.moveToNext()) {
            preparations.add(res.getString(2));
        }
        return preparations;
    }

}
