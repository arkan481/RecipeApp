package com.example.recipeapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.recipeapp.R;
import com.example.recipeapp.model.RecipeModel;
import com.example.recipeapp.ui.RecipeDetailFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class RecipeGridAdapter extends BaseAdapter {
    ArrayList<RecipeModel> recipeModels;
    Context mycontext;

    public RecipeGridAdapter(Context context,ArrayList<RecipeModel> recipeModels) {
        mycontext=context;
        this.recipeModels=recipeModels;
    }

    public int getCount() {
        return recipeModels.size();
    }

    @Override
    public RecipeModel getItem(int i) {
        return recipeModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View itemview=view;
        if (itemview==null) {
            itemview=LayoutInflater.from(mycontext).inflate(R.layout.recipegriditem,viewGroup,false);
        }
        CardView recipecardview = itemview.findViewById(R.id.cvrecipeitemid);
        ImageView recipeimage = itemview.findViewById(R.id.recipeimageid);
        TextView recipetitle = itemview.findViewById(R.id.recipetitleid);
        TextView lasteditedrecipe = itemview.findViewById(R.id.dateid);
        ImageView trashbutton = itemview.findViewById(R.id.trashbutton);
        final RecipeModel recipeModel = recipeModels.get(i);
        recipecardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle myBundle = new Bundle();
                myBundle.putString("foodtitle",recipeModel.getTitle());
                myBundle.putString("lastedited",recipeModel.getLastedited());
                myBundle.putSerializable("ingridients",recipeModel.getIngridients());
                myBundle.putSerializable("preparations",recipeModel.getSteps());
                myBundle.putSerializable("Recipemodels",recipeModels);
                myBundle.putSerializable("updatingmodel",recipeModel);
                myBundle.putBoolean("isupdating",true);
                AppCompatActivity myactivity =(AppCompatActivity) view.getContext();
                Fragment myFragment = new RecipeDetailFragment();
                myFragment.setArguments(myBundle);
                myactivity.getSupportFragmentManager().beginTransaction().replace(R.id.framehome,myFragment).addToBackStack(null).commit();

            }
        });
        trashbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeModels.remove(i);
                notifyDataSetChanged();
            }
        });
        recipeimage.setImageResource(recipeModel.getimage());
        recipetitle.setText(recipeModel.getTitle());
        lasteditedrecipe.setText(recipeModel.getLastedited());
        return itemview;
    }
}
