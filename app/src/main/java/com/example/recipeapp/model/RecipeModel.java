package com.example.recipeapp.model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeModel implements Serializable {
    private String title,lastedited;
    private byte[] image;
    private ArrayList<String> ingridients,steps;
    public RecipeModel(String title, String lastedited, byte[] image, ArrayList<String> ingridientlist, ArrayList<String> preparationslist) {
        this.title=title;
        this.lastedited=lastedited;
        this.image=image;
        this.ingridients=ingridientlist;
        this.steps=preparationslist;
    }

    public ArrayList<String> getIngridients() {
        return this.ingridients;
    }
    public ArrayList<String> getSteps() {
        return this.steps;
    }
    public String getTitle() {
        return this.title;
    }
    public byte[] getimage() {
        return this.image;
    }
    public String getLastedited() {
        return this.lastedited;
    }
    public void setTitle(String title) {
        this.title=title;
    }
    public void setLastedited(String lastedited) {
        this.lastedited=lastedited;
    }
    public void setimage(byte[] image) {
        this.image=image;
    }
    public void setIngridients(ArrayList<String>ingridients) {
        this.ingridients=ingridients;
    }
    public void setSteps(ArrayList<String>steps) {
        this.steps=steps;
    }
}
