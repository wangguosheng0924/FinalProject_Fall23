package algonquin.cst2335.finalproject_fall23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject_fall23.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject_fall23.databinding.ActivityMainBinding;

/**
 * DictionaryActivity represents the main activity for a dictionary app.
 * It allows users to search for word definitions, save them locally, and view their search history.
 *
 * The activity includes functionality for:
 * - Searching for word definitions using an online dictionary API.
 * - Saving and deleting word definitions locally using a Room database.
 * - Viewing and managing the search history.
 * - Displaying word definitions in a RecyclerView.
 *
 * The activity also utilizes the ViewModel architecture for managing UI-related data.
 *
 * This class implements the HistoryFragment.HistoryClickListener interface to handle clicks
 * on items in the search history.
 *
 * @author Christy Guan
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize the toolbar.Android will call onCreateOptionsMenu().
        setSupportActionBar(binding.mainToolbar);

        // Inflate the layout for this view and set it as the content view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.dictionaryButton.setOnClickListener(click -> {
            startActivity(new Intent(this, DictionaryActivity.class));
        });

        binding.sunriseButton.setOnClickListener(click -> {
            startActivity(new Intent(this, SunriseMain.class));
        });

        binding.recipeButton.setOnClickListener(click -> {
            startActivity(new Intent(this, RecipeSearch.class));
        });

        binding.songButton.setOnClickListener(click -> {
            startActivity(new Intent(this, ArtistsSearch.class));
        });

    }


//    public void onDictionaryButtonClick(View view){
//        Intent intent = new Intent(this, DictionaryActivity.class);
//        startActivity(intent);
//        binding.songButton.setOnClickListener(click -> {
//            startActivity(new Intent(this, ArtistsSearch.class));
//        });
//
//        //initialize the toolbar.Android will call onCreateOptionsMenu().
//        setSupportActionBar(binding.mainToolbar);
//    }
//
    @Override //this initialized the toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override // this defines when users select a menuItem
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch( item.getItemId() )
        {
            case R.id.item_dictionary:
                Snackbar.make(binding.mainToolbar,"You will go to the dictionary",Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(this, DictionaryActivity.class);
                startActivity(intent);
                break;

            case R.id.item_about:
                Toast.makeText(this, "created by Christy", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


//    public void onDictionaryButtonClick(View view){
//        Intent intent = new Intent(this, DictionaryActivity.class);
//        startActivity(intent);
//    }
}