package algonquin.cst2335.finalproject_fall23;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject_fall23.databinding.DictionaryactivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DictionaryActivity extends AppCompatActivity {
    DictionaryactivityMainBinding binding;
    ArrayList<String> definitions = new ArrayList<>();
    DefinitionViewModel definitionModel;
    DefinitionDAO mDAO;
    RecyclerView.Adapter myAdapter;
    SharedPreferences prefs;
    int selectedIndex;

    //if not using binding you can get the elements by this way.
    //EditText searchEditText = findViewById(R.id.editText);
    //EditText searchEditText = binding.editText;
    //String searchTerm = searchEditText.getText().toString();

//    private void showToast(String errorMessage) {
//        Toast.makeText(DictionaryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //before using binding use this line.
        //setContentView(R.layout.dictionaryactivity_main);
        //use binding to access views defined in the layout.
        binding = DictionaryactivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditText searchEditText = binding.editText;
        String searchTerm = searchEditText.getText().toString();

        //apply SharedPreference
//        SharedPreferences prefs = getSharedPreferences("searchTerm", Context.MODE_PRIVATE);
//        searchEditText.setText(prefs.getString("searchTerm", ""));
        prefs=this.getSharedPreferences("com.yourapp.dictionary_preferences",MODE_PRIVATE);
        searchTerm=prefs.getString("LastSearchWord","");
        searchEditText.setText("searchTerm");

        //open a database
        DefinitionDB db = Room.databaseBuilder(getApplicationContext(), DefinitionDB.class, "databaseLocal").build();
        mDAO = db.cmDAO();

        definitionModel = new ViewModelProvider(this).get(DefinitionViewModel.class);
        definitions = definitionModel.definitions.getValue();

        //click listener for the search button
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {
            Log.d("DictionaryActivity", "Search Term: " + searchTerm);
// load the last search term
            //searchTerm = searchEditText.getText().toString();
            definitions.add(searchTerm);
            // save the last searched term
            SharedPreferences.Editor editor=prefs.edit();
            editor.putString("Last Search Term",searchTerm);
            editor.apply();

            searchEditText.setText("");
            myAdapter.notifyItemInserted(definitions.size()-1);

//            //use DictionaryApiService to connect the external url.
//            if (!searchTerm.isEmpty()) {
//                // Call the API to get definitions for the search term
//                DictionaryApiService apiService = DictionaryApi.getService();
//                Call<List<Definition>> call = apiService.getDefinitions(searchTerm);
//                call.enqueue(new Callback<List<Definition>>() {
//                    @Override
//                    public void onResponse(Call<List<Definition>> call, Response<List<Definition>> response) {
//                        String errorMessage;
//                        if (response.isSuccessful()) {
//                            // Update the UI with the definitions
//                            List<Definition> retrievedDefinitions = response.body();
//                            if (retrievedDefinitions != null && !retrievedDefinitions.isEmpty()) {
//                                // Display definitions in RecyclerView
//                                definitions.clear();
//                                definitions.addAll(retrievedDefinitions);
//                                Log.d("DictionaryActivity", "API Response Success");
//                                // Update RecyclerView adapter or any other UI updates
//                                // For example: recyclerViewAdapter.notifyDataSetChanged();
//                            } else {
//                                showToast("No definitions found.");
//                                Log.d("DictionaryActivity", "No found the term");
//                            }
//                        } else {
//                            showToast("Unsuccessful response. Please try again.");
//                            Log.d("DictionaryActivity", "API Response Unsuccessful: " + response.code());
//                        }
//
//                    }
//                    @Override
//                    public void onFailure(Call<List<Definition>> call, Throwable t) {
//                        showToast("Network request failure");
//                        Log.e("DictionaryActivity", "API Call Failure", t);
//                    }
//                });
//            } else {
//                showToast("Please enter a term to search");
//            }
        }); // end of the searchButton.setOnClickListener.


        //click listener for the save button
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

        //myRowHolder
        class MyRowHolder extends RecyclerView.ViewHolder {
            EditText searchEditText = binding.editText;
            //String definition;
            public MyRowHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(clk -> {

                int position = getAdapterPosition();
                String selectedTerm = definitions.get(position);
                definitionModel.selectedDefinition.postValue(selectedTerm);
                AlertDialog.Builder builder = new AlertDialog.Builder( DictionaryActivity.this );
                builder.setMessage("Do you want to delete the message:"+ selectedTerm);
                builder.setTitle("Question:");
                builder.setNegativeButton("No",(dialog,cl)->{});
                builder.setPositiveButton("Yes",(dialog,cl)->{

//                    Executor thread = Executors.newSingleThreadExecutor();
//                    thread.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                           mDAO.deleteMessage(m);
//                        }
//                    });
                    String  selectedDefinition = definitions.get(position);
                    definitions.remove(position);
                    myAdapter.notifyItemRemoved(position);
//
                    Snackbar.make(searchEditText,"You deleted message #"+ position, Snackbar.LENGTH_LONG).setAction("Undo", click->{
                        definitions.add(position, selectedDefinition);
                        myAdapter.notifyItemInserted(position);
//
//                        Executor thread1 = Executors.newSingleThreadExecutor();
//                        thread1.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                mDAO.insertMessage(m);
//                            }
//                        });
                    }).show();
                });
                builder.create().show();
                });
            }
        } // end of MyRowHolder

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // setAdapter here 213-237
        binding.recyclerView.setAdapter(myAdapter= new RecyclerView.Adapter<MyRowHolder>() {
            EditText searchEditText = binding.editText;
            String searchTerm = searchEditText.getText().toString();

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                DictionaryactivityMainBinding binding = DictionaryactivityMainBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                selectedIndex = position;
                String forRow = definitions.get(position).toString();
                holder.searchEditText.setText(forRow);
            }
            @Override
            public int getItemCount() {
                return definitions.size();
            }
            @Override
            public int getItemViewType(int position){
                return 0;
            }
        });// end of setAdapter
    } //end of onCreate()


}// end of the class
