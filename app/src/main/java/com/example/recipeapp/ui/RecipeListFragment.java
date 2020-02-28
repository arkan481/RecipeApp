package com.example.recipeapp.ui;


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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {

    Spinner SortSpinner;
    TextView appbartitle;
    RelativeLayout searchlayout;
    GridView recipegrid;
    ArrayList<RecipeModel> recipeModels;
    RecipeGridAdapter gridAdapter;
    FloatingActionButton fabplus;
    ArrayAdapter<String> sortadapter;
    Bundle recipeModelsBundle = new Bundle();


    public RecipeListFragment() {
        // Required empty public constructor
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
        populaterecipegrid();
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
                recipeModelsBundle.putBoolean("isupdating",false);
                recipeModelsBundle.putSerializable("Recipemodels",(Serializable) recipeModels);
                Fragment fragment = new RecipeDetailFragment();
                fragment.setArguments(recipeModelsBundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framehome,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return v;

    }

    private void populaterecipegrid() {
        gridAdapter = new RecipeGridAdapter(getActivity(),recipeModels);
        recipegrid.setAdapter(gridAdapter);
    }

}
