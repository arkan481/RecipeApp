package com.example.recipeapp.ui;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.recipeapp.R;
import com.example.recipeapp.model.RecipeModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {

    FloatingActionButton fabdetail;
    ArrayList<RecipeModel> recipeModels;
    RecipeModel updatingmodel;

    CardView addingridientscv,addpreparationscv;
    LinearLayout ingridientslayout,preparationslayout;
    EditText etingridients,etpreparations,foodtitleet;
    View ingridientscard,preparationscard;
    ImageView removeiconingridients,removeiconpreparations;
    ArrayList<EditText> EditTextIngridients = new ArrayList<>();
    ArrayList<EditText> EditTextPreparations = new ArrayList<>();
    Bundle extras;
    ArrayList<String> ingridients = new ArrayList<String>();
    ArrayList<String> preparations = new ArrayList<String>();
    boolean isupdating=false;


    public RecipeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        extras = getArguments();
        isupdating = extras.getBoolean("isupdating");
        recipeModels = (ArrayList<RecipeModel>) extras.getSerializable("Recipemodels");
        System.out.println(recipeModels);
        foodtitleet = v.findViewById(R.id.foodtitleetid);
        addingridientscv=v.findViewById(R.id.addingridientsbuttonid);
        addpreparationscv =v.findViewById(R.id.addpreparationsbuttonid);
        fabdetail = v.findViewById(R.id.fabdetailid);
        ingridientslayout=v.findViewById(R.id.ingridientslayoutid);
        preparationslayout=v.findViewById(R.id.preparationslayout);
        addingridientscv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingridientscard = LayoutInflater.from(getContext()).inflate(R.layout.ingridientscarditem,container,false);
                removeiconingridients = ingridientscard.findViewById(R.id.removeiconingridientsid);
                etingridients=ingridientscard.findViewById(R.id.edittextingridients);
                etingridients.setHint("Ingridients "+ingridientslayout.getChildCount());
                ingridientslayout.addView(ingridientscard,ingridientslayout.getChildCount()-1);
                EditTextIngridients.add(etingridients);
                removeiconingridients.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = ingridientslayout.indexOfChild((ViewGroup)view.getParent().getParent());
                        EditTextIngridients.remove(index);
                        ingridientslayout.removeViewAt(index);
                    }
                });
            }
        });
        addpreparationscv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preparationscard=LayoutInflater.from(getContext()).inflate(R.layout.preparationscarditem,container,false);
                removeiconpreparations = preparationscard.findViewById(R.id.removeiconpreparationsid);
                etpreparations = preparationscard.findViewById(R.id.edittextpreparations);
                etpreparations.setHint("Preparations "+preparationslayout.getChildCount());
                preparationslayout.addView(preparationscard,preparationslayout.getChildCount()-1);
                EditTextPreparations.add(etpreparations);
                removeiconpreparations.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = preparationslayout.indexOfChild((ViewGroup)view.getParent().getParent());
                        EditTextPreparations.remove(index);
                        preparationslayout.removeViewAt(index);
                    }
                });

            }
        });
        if (isupdating==true) {
            System.out.println("updating...");
            foodtitleet.setText(extras.getString("foodtitle"));
            ingridients = (ArrayList<String>) extras.getSerializable("ingridients");
            preparations = (ArrayList<String>) extras.getSerializable("preparations");
            updatingmodel = (RecipeModel) extras.getSerializable("updatingmodel");
            populateingridientscard(container);
            populatepreparationcard(container);
        }else {
            System.out.println("not updating...");
        }


        fabdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isupdating==true) {
                    ingridients = new ArrayList<String>();
                    preparations = new ArrayList<String>();
                    updatingmodel.setimage(R.drawable.saladimage);
                    updatingmodel.setTitle(foodtitleet.getText().toString());
                    updatingmodel.setIngridients(getingridientlist());
                    updatingmodel.setSteps(getPreparationslist());
                    updatingmodel.setLastedited("Just now");
                }else {
                    recipeModels.add(new RecipeModel(foodtitleet.getText().toString(),"02/20/2020",R.drawable.saladimage,getingridientlist(),getPreparationslist()));
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return v;

    }
    private void populateingridientscard(ViewGroup container) {
        for (int i =0; i<ingridients.size();i++) {
            ingridientscard = LayoutInflater.from(getContext()).inflate(R.layout.ingridientscarditem,container,false);
            etingridients = ingridientscard.findViewById(R.id.edittextingridients);
            etingridients.setText(ingridients.get(i));
            EditTextIngridients.add(etingridients);
            removeiconingridients = ingridientscard.findViewById(R.id.removeiconingridientsid);
            removeiconingridients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = ingridientslayout.indexOfChild((ViewGroup)view.getParent().getParent());
                    EditTextIngridients.remove(index);
                    ingridientslayout.removeViewAt(index);
                }
            });
            ingridientslayout.addView(ingridientscard,i);
        }
    }
    private void populatepreparationcard(ViewGroup container) {
        for (int i=0; i<preparations.size();i++) {
            preparationscard=LayoutInflater.from(getContext()).inflate(R.layout.preparationscarditem,container,false);
            etpreparations = preparationscard.findViewById(R.id.edittextpreparations);
            etpreparations.setText(preparations.get(i));
            EditTextPreparations.add(etpreparations);
            removeiconpreparations = preparationscard.findViewById(R.id.removeiconpreparationsid);
            removeiconpreparations.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = preparationslayout.indexOfChild((ViewGroup)view.getParent().getParent());
                    EditTextPreparations.remove(index);
                    preparationslayout.removeViewAt(index);
                }
            });
            preparationslayout.addView(preparationscard,i);
        }
    }
    ArrayList<String> getingridientlist() {
        for (int i =0;i<EditTextIngridients.size();i++) {
            System.out.println("the total i is "+i);
            ingridients.add(EditTextIngridients.get(i).getText().toString());
        }
        return  ingridients;
    }
    ArrayList<String> getPreparationslist() {
        for (int i =0;i<EditTextPreparations.size();i++) {
            preparations.add(EditTextPreparations.get(i).getText().toString());
        }
        return preparations;
    }

}
