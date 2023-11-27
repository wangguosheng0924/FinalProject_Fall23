package algonquin.cst2335.finalproject_fall23;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import algonquin.cst2335.finalproject_fall23.data.RecipeViewModel;
import algonquin.cst2335.finalproject_fall23.databinding.RecipeSearchBinding;

public class RecipeSearch extends AppCompatActivity {

    RecipeSearchBinding binding;
    RecipeDAO rDAO;
    RecipeViewModel recipeViewModel;
    ArrayList<Recipe> theRecipe = null;
    RecyclerView.Adapter myAdapter = null;
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RecipeSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }


}
