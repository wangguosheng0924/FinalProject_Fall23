package algonquin.cst2335.finalproject_fall23.data;

import androidx.lifecycle.MutableLiveData;

import java.lang.invoke.MutableCallSite;
import java.util.ArrayList;

import algonquin.cst2335.finalproject_fall23.Recipe;

public class RecipeViewModel {
    public ArrayList<Recipe>  theRecipe = new ArrayList<>();
    public MutableLiveData<Recipe> selectedRecipe =new MutableLiveData<>();

}
