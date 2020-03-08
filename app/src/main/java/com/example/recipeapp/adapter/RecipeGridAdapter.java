package com.example.recipeapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.recipeapp.R;
import com.example.recipeapp.model.RecipeModel;
import com.example.recipeapp.ui.RecipeDetailFragment;
import com.example.recipeapp.ui.RecipeListFragment;
import com.example.recipeapp.utils.DatabaseHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class RecipeGridAdapter extends BaseAdapter implements Filterable {
    ArrayList<RecipeModel> recipeModels;
    ArrayList<RecipeModel> filteredRecipeModels;
    Context mycontext;
    DatabaseHelper databaseHelper;
    RecipeListFragment recipeListFragment;
    CustomFilter customFilter;
    public RecipeGridAdapter(Context context, ArrayList<RecipeModel> recipeModels, DatabaseHelper databaseHelper, RecipeListFragment recipeListFragment) {
        this.databaseHelper=databaseHelper;
        mycontext=context;
        this.recipeModels=recipeModels;
        this.recipeListFragment=recipeListFragment;
        this.filteredRecipeModels=recipeModels;
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
                myBundle.putInt("RecipeID",recipeModel.getId());
                myBundle.putString("foodtitle",recipeModel.getTitle());
                myBundle.putString("lastedited",recipeModel.getLastedited());
                myBundle.putSerializable("ingridients",recipeModel.getIngridients());
                myBundle.putSerializable("preparations",recipeModel.getSteps());
                myBundle.putString("recipeimage",recipeModel.getimage());
                myBundle.putSerializable("Recipemodels",recipeModels);
                myBundle.putSerializable("updatingmodel",recipeModel);
                myBundle.putString("appbartitle",recipeModel.getTitle());
                myBundle.putBoolean("isupdating",true);
                AppCompatActivity myactivity =(AppCompatActivity) view.getContext();
                Fragment myFragment = new RecipeDetailFragment(databaseHelper);
                myFragment.setArguments(myBundle);
                myactivity.getSupportFragmentManager().beginTransaction().replace(R.id.framehome,myFragment).addToBackStack(null).commit();

            }
        });
        trashbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.deleteRecipe(String.valueOf(recipeModel.getId()));
                recipeListFragment.refreshgridview();
            }
        });
        recipeimage.setImageURI(Uri.parse(recipeModel.getimage()));
        recipetitle.setText(recipeModel.getTitle());
        lasteditedrecipe.setText(recipeModel.getLastedited());
        return itemview;
    }

    @Override
    public Filter getFilter() {
        if (customFilter==null) {
            customFilter=new CustomFilter();
        }
        return customFilter;
    }
    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null&&constraint.length()>0) {
                constraint=constraint.toString().toUpperCase();
                ArrayList<RecipeModel> recipeModels = new ArrayList<RecipeModel>();
                for (int i=0;i<filteredRecipeModels.size();i++) {
                    if (filteredRecipeModels.get(i).getTitle().toUpperCase().contains(constraint)) {
                        RecipeModel recipeModel = new RecipeModel(filteredRecipeModels.get(i).getId(),filteredRecipeModels.get(i).getTitle(),filteredRecipeModels.get(i).getLastedited(),filteredRecipeModels.get(i).getimage(),filteredRecipeModels.get(i).getIngridients(),filteredRecipeModels.get(i).getSteps());
                        recipeModels.add(recipeModel);
                    }
                }
                filterResults.count=recipeModels.size();
                filterResults.values=recipeModels;
            }else {
                filterResults.count=filteredRecipeModels.size();
                filterResults.values=filteredRecipeModels;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recipeModels=(ArrayList<RecipeModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
