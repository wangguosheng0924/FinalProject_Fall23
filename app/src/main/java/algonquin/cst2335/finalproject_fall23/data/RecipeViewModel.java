package algonquin.cst2335.finalproject_fall23.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.ArrayList;

import algonquin.cst2335.finalproject_fall23.Recipe;


public class RecipeViewModel extends ViewModel{
    public MutableLiveData<ArrayList<Recipe>> recipeList = new MutableLiveData<>();

    public MutableLiveData<Recipe> selectedRecipe =new MutableLiveData<>();

}
