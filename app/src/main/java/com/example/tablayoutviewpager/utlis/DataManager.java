package com.example.tablayoutviewpager.utlis;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Window;

import androidx.core.content.ContextCompat;

import com.example.tablayoutviewpager.Activities.MainActivity;
import com.example.tablayoutviewpager.Model.Category;
import com.example.tablayoutviewpager.Model.Pictures;
import com.example.tablayoutviewpager.R;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static DataManager instance;
    private List<Pictures> picturesList;

    private List<Category> categoryList;

    private SharedPreferences sharedPreferences;
    Context context;
    private static final String PREF_NAME = "checkbox_prefs";
    private static final String KEY_CHECKBOX_STATE = "key_checkbox_state";

    public DataManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        picturesList = new ArrayList<>();

        // Category Added
        categoryList = new ArrayList<>();
        categoryList.add(new Category(1,"vegetables","vegetables"));
        categoryList.add(new Category(2,"Fruits","fruit"));
        categoryList.add(new Category(3,"Split chickpeas","split"));



        picturesList.add(new Pictures(1,"apple", "Apple", false,2));
        picturesList.add(new Pictures(2,"orange", "Orange", false,2));
        picturesList.add(new Pictures(3,"pineaplle", "PineApple", false,2));
        picturesList.add(new Pictures(4,"berry", "Berry", false,2));
        picturesList.add(new Pictures(5,"strawberry", "StrawBerry", false,2));
        picturesList.add(new Pictures(6,"bluebarry", "Blueberry", false,2));
        picturesList.add(new Pictures(7,"grapes", "Grapes", false,2));
        picturesList.add(new Pictures(8,"pomegranate", "Pomegranate", false,2));


        picturesList.add(new Pictures(9,"onione", "Onion", false,1));
        picturesList.add(new Pictures(10,"dani", "Celeriac Leaf", false,1));
        picturesList.add(new Pictures(11,"khira", "Cucumber", false,1));
        picturesList.add(new Pictures(12,"tom", "Tomato", false,1));




        picturesList.add(new Pictures(13,"pea", "Lentil Split pea", false,3));
        picturesList.add(new Pictures(14,"cuisine", "Cuisine Chickpea  ", false,3));
        picturesList.add(new Pictures(15,"cereal", "Lentil Legume", false,3));
        picturesList.add(new Pictures(16,"bean", "Bean Legume", false,3));
        initPictures();
    }
    public void initPictures() {
        for (Pictures picture : picturesList) {
            boolean isSavedState = getCheckBoxState(picture.getId());
            picture.setFavorite(isSavedState);
        }
    }

    public boolean getCheckBoxState(int pictureId) {
        return sharedPreferences.getBoolean(KEY_CHECKBOX_STATE + pictureId, false);



    }
    public List<Pictures> getPicturesByCategory(int categoryId) {
        List<Pictures> picturesByCategory = new ArrayList<>();
        for (Pictures picture : picturesList) {
            if (picture.getCatId() == categoryId) {
                picturesByCategory.add(picture);
            }
        }
        return picturesByCategory;
    }
    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager(instance.context);
        }
        return instance;
    }
    public void addCategory(Category category) {
        categoryList.add(category);
    }
    public List<Category> getAllCategories() {
        return categoryList;
    }
    public List<Pictures> getAllPictures() {
        return picturesList;
    }


    public void saveCheckBoxState(int itemId, boolean isChecked,Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String key = KEY_CHECKBOX_STATE + itemId;
        Log.d("Item Save",""+itemId);
        editor.putBoolean(key, isChecked);
        editor.apply();
    }
}
