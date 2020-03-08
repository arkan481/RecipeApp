package com.example.recipeapp.ui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeapp.R;
import com.example.recipeapp.model.RecipeModel;
import com.example.recipeapp.utils.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    TextView appbartitle;
    CardView addingridientscv,addpreparationscv;
    LinearLayout ingridientslayout,preparationslayout;
    EditText etingridients,etpreparations,foodtitleet;
    View ingridientscard,preparationscard;
    ImageView removeiconingridients,removeiconpreparations,RecipeImageView;
    ArrayList<EditText> EditTextIngridients = new ArrayList<>();
    ArrayList<EditText> EditTextPreparations = new ArrayList<>();
    Bundle extras;
    ArrayList<String> ingridients = new ArrayList<String>();
    ArrayList<String> preparations = new ArrayList<String>();
//    byte[] tempimage;
    boolean isupdating=false;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_PICK_CODE = 1001;
    String RecipeImage;
    DatabaseHelper databaseHelper;
    int dbid;
    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd/MM/yyyy");

    public RecipeDetailFragment(DatabaseHelper databaseHelper) {
        // Required empty public constructor
        this.databaseHelper=databaseHelper;
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
        appbartitle=v.findViewById(R.id.appbartitledetailid);
        appbartitle.setText(extras.getString("appbartitle"));
        foodtitleet = v.findViewById(R.id.foodtitleetid);
        RecipeImageView = v.findViewById(R.id.recipeimageviewid);
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
            dbid=extras.getInt("RecipeID");
            RecipeImage=extras.getString("recipeimage");
            RecipeImageView.setImageURI(Uri.parse(RecipeImage));
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
                if (foodtitleet.getText().toString().equals("")||RecipeImage==null) {
                    Toast.makeText(getContext(),"Please Specify the food title and the food image",Toast.LENGTH_LONG).show();
                }else {
                    if (isupdating==true) {
                        databaseHelper.updateRecipeAll(foodtitleet.getText().toString(),RecipeImage, simpleDateFormat.format(Calendar.getInstance().getTime()),"4",String.valueOf(dbid));
                        databaseHelper.deletePreparationsAll(String.valueOf(dbid));
                        databaseHelper.deleteIngridientsAll(String.valueOf(dbid));
                        updateingridienttodb();
                        updatepreparationtodb();

//                        ingridients = new ArrayList<String>();
//                        preparations = new ArrayList<String>();
////                    updatingmodel.setimage(convertimagetobytearray(RecipeImageView.getDrawable()));
//                        updatingmodel.setimage(RecipeImage);
//                        updatingmodel.setTitle(foodtitleet.getText().toString());
//                        updatingmodel.setIngridients(getingridientlist());
//                        updatingmodel.setSteps(getPreparationslist());
//                        updatingmodel.setLastedited("Just now");
                    }else {
                        databaseHelper.insertRecipe(foodtitleet.getText().toString(),simpleDateFormat.format(Calendar.getInstance().getTime()),RecipeImage,"4");
                        insertingridienttodb();
                        insertpreparationtodb();
                    }
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
        });
        RecipeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED) {
                        //permission denied
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup
                        requestPermissions(permissions,PERMISSION_PICK_CODE);
                    }else {
                        //permission granted
                        pickImageFromGallery();

                    }
                }else {
                    // lower than marshmallow
                    pickImageFromGallery();
                }
            }
        });
        return v;

    }

    private void pickImageFromGallery() {
        //intent to gallery
        Intent intenttogallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intenttogallery.setType("image/*");
        startActivityForResult(intenttogallery,IMAGE_PICK_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==getActivity().RESULT_OK&&requestCode==IMAGE_PICK_CODE){
            Uri test = data.getData();
            getContext().getContentResolver().takePersistableUriPermission(test,Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            RecipeImageView.setImageURI(data.getData());
            // TODO : URI IS FIXED BY USING ACTION_OPEN_DOCUMENT BUT THERE MIGHT BE A BETTER WAY SEE BOOKMARK.
            RecipeImage =test.toString();

        }else {
            Toast.makeText(getContext(),"Error setting the image",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_PICK_CODE:{
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                }else {
                    Toast.makeText(getContext(),"Storage permission denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
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
    public void insertingridienttodb() {
        for (int i =0;i<EditTextIngridients.size();i++) {
            System.out.println("the total i is "+i);
            databaseHelper.insertingridients(databaseHelper.fetchlatestRecipeID(),EditTextIngridients.get(i).getText().toString());
        }
    }
    public void updateingridienttodb() {
        for (int i=0;i<EditTextIngridients.size();i++) {
            databaseHelper.insertingridients(String.valueOf(dbid),EditTextIngridients.get(i).getText().toString());
        }
    }
    public void updatepreparationtodb() {
        for (int i=0;i<EditTextPreparations.size();i++) {
            databaseHelper.insertPreparations(String.valueOf(dbid),EditTextPreparations.get(i).getText().toString());
        }
    }
    public void insertpreparationtodb() {
        for (int i =0;i<EditTextPreparations.size();i++) {
            System.out.println("the total i is "+i);
            databaseHelper.insertPreparations(databaseHelper.fetchlatestRecipeID(),EditTextPreparations.get(i).getText().toString());
        }
    }
//    ArrayList<String> getPreparationslist() {
//        for (int i =0;i<EditTextPreparations.size();i++) {
//            preparations.add(EditTextPreparations.get(i).getText().toString());
//        }
//        return preparations;
//    }


}
