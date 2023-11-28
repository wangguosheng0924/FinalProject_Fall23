package algonquin.cst2335.finalproject_fall23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.finalproject_fall23.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding =ActivityMainBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());

//        binding.sunriseButton.setOnClickListener( click -> {
//            startActivity(new Intent(this,SunriseMain.class));
//        });

        binding.recipeButton.setOnClickListener( click -> {
            startActivity(new Intent(this,RecipeSearch.class));
        });

    }
}