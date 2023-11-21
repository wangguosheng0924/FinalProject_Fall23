package algonquin.cst2335.finalproject_fall23;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject_fall23.databinding.DictionaryactivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DictionaryActivity extends AppCompatActivity {

    DictionaryactivityMainBinding binding;
    ArrayList<Definition> definitions = new ArrayList<>();
    DefinitionViewModel definitionModel;
    DefinitionDAO mDAO;

    //myRowHolder
    //adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //before using binding use this line.
        //setContentView(R.layout.dictionaryactivity_main);
        //use binding to access views defined in the layout.
        binding = DictionaryactivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //if not using binding you can get the elements by this way.
        //EditText searchEditText = findViewById(R.id.editText);
        EditText searchEditText = binding.editText;
        String searchTerm = searchEditText.getText().toString();

        //open a database
        DefinitionDB db = Room.databaseBuilder(getApplicationContext(), DefinitionDB.class, "databaseLocal").build();
        mDAO = db.cmDAO();

        definitionModel = new ViewModelProvider(this).get(DefinitionViewModel.class);
        definitions = definitionModel.definitions.getValue();

        //click listener for the search button
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {
            Log.d("DictionaryActivity", "Search Term: " + searchTerm);
            if (!searchTerm.isEmpty()) {
                // Call the API to get definitions for the search term
                DictionaryApiService apiService = DictionaryApi.getService();
                Call<List<Definition>> call = apiService.getDefinitions(searchTerm);
                call.enqueue(new Callback<List<Definition>>() {
                    @Override
                    public void onResponse(Call<List<Definition>> call, Response<List<Definition>> response) {
                        String errorMessage;
                        if (response.isSuccessful()) {
                            // Update the UI with the definitions
                            List<Definition> retrievedDefinitions = response.body();
                            if (retrievedDefinitions != null && !retrievedDefinitions.isEmpty()) {
                                // Display definitions in RecyclerView
                                definitions.clear();
                                definitions.addAll(retrievedDefinitions);
                                Log.d("DictionaryActivity", "API Response Success");
                                // Update RecyclerView adapter or any other UI updates
                                // For example: recyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                showToast("No definitions found.");
                                Log.d("DictionaryActivity", "No found the term");
                            }
                        } else {
                            showToast("Unsuccessful response. Please try again.");
                            Log.d("DictionaryActivity", "API Response Unsuccessful: " + response.code());
                        }

                    }
                    @Override
                    public void onFailure(Call<List<Definition>> call, Throwable t) {
                        showToast("Network request failure");
                        Log.e("DictionaryActivity", "API Call Failure", t);
                    }
                });
            } else {
                showToast("Please enter a term to search");
            }
        });


        //click listener for the save button

        //in the search button definition, there already be definitions.addAll().
        //do i need to define save button?
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(clk->{
            Definition newDefinition = new Definition();
            newDefinition.setTerm(searchTerm);
//            newDefinition.setDefinition(definition);
//            long insertedId = cmDAO.insertDefinition(newDefinition);
//
//// Check if insertion was successful
//            if (insertedId > 0) {
//                showToast("Definition saved successfully");
//            } else {
//                showToast("Failed to save definition");
//            }

        });


        //click listener for the history button
         Button historyButton= findViewById(R.id.history_button);
         historyButton.setOnClickListener(clk->{
            // List<Definition> historyDefinitions = getAllDefinitions();

             // Assuming you have a RecyclerView and its adapter named 'recyclerView' and 'recyclerViewAdapter'

// Update the adapter's dataset with the retrieved definitions
//             recyclerViewAdapter.setDefinitions(allDefinitions);

// Notify the adapter that the dataset has changed
//             recyclerViewAdapter.notifyDataSetChanged();

         });

    } //end of onCreate()
    private void showToast(String errorMessage) {
        Toast.makeText(DictionaryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}// end of the class
