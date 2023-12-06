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
 * @author Christy Guan,Lei Luo, Jingyi Zhou,Guosheng Wang
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /** Binding instance for ActivityMain. */
    ActivityMainBinding binding;

    /**
     * Called when the activity is starting.
     * Initializes the user interface and sets up click listeners for navigating to different features of the app.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
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

    /**
     * Initialize the contents of the Activity's standard options menu.
     * This method inflates the menu resource into the provided Menu object.
     * @param menu The options menu in which items are placed.
     * @return true for the menu to be displayed; if false, it will not be shown.
     */


    @Override //this initialized the toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Called whenever an item in the options menu is selected.
     * This method handles user interaction with the menu items.
     * @param item The menu item that was selected.
     * @return true to consume the menu selection here; false to allow normal menu processing to proceed.
     */
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



}