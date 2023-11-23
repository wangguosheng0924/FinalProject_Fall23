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
import algonquin.cst2335.finalproject_fall23.databinding.PostsearchlayoutBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DictionaryActivity extends AppCompatActivity {
    DictionaryactivityMainBinding binding;
    ArrayList<String> definitions;
    DefinitionViewModel definitionModel;
    DefinitionDAO mDAO;
    RecyclerView.Adapter myAdapter;

    //int selectedIndex;

    //if not using binding you can get the elements by this way.
    //EditText searchEditText = findViewById(R.id.editText);
    //EditText searchEditText = binding.editText;
    //String searchTerm = searchEditText.getText().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //before using binding use this line.
        //setContentView(R.layout.dictionaryactivity_main);
        //use binding to access views defined in the layout.
        binding = DictionaryactivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toast.makeText(this, "Welcome to Dictionary", Toast.LENGTH_LONG).show();
        EditText searchEditText = binding.editText;

        //binding.editText = findViewById(R.id.editText);
//        String searchTerm = searchEditText.getText().toString();
        //apply SharedPreference
        SharedPreferences prefs1 = getSharedPreferences("searchTerm", Context.MODE_PRIVATE);
        searchEditText.setText(prefs1.getString("searchTerm", ""));

        //prefs=this.getSharedPreferences("com.yourapp.dictionary_preferences",MODE_PRIVATE);
//        searchTerm=prefs.getString("LastSearchWord","");
//        searchEditText.setText("searchTerm");

        //open a database
        DefinitionDB db = Room.databaseBuilder(getApplicationContext(), DefinitionDB.class, "databaseLocal").build();
        mDAO = db.cmDAO();

        definitionModel = new ViewModelProvider(this).get(DefinitionViewModel.class);
        if(definitionModel.definitions.getValue()!=null){
            definitions =definitionModel.definitions.getValue();}
        else{definitions = new ArrayList<>();
        }

        //click listener for the search button
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {
            String searchTerm = binding.editText.getText().toString();
            definitions.add(searchTerm);

            // save the last searched term
            SharedPreferences prefs2=getSharedPreferences("searchTerm",Context.MODE_PRIVATE);

            SharedPreferences.Editor editor=prefs1.edit();
            editor.putString("searchTerm",searchTerm);
            editor.apply();

//            String searchTerm=prefs1.getString("LastSearchWord","");
//            Log.d("DictionaryActivity", "Search Term: " + searchTerm);
            //searchTerm = searchEditText.getText().toString();

//            binding.editText.setText("");
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
          //  newDefinition.setTerm(searchTerm);
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
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // setAdapter here
        binding.recyclerView.setAdapter(myAdapter= new RecyclerView.Adapter<MyRowHolder>() {
//            TextView searched_term = binding.searched_term;
//            String searchTerm = searched_term.getText().toString();
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                PostsearchlayoutBinding binding1 = PostsearchlayoutBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding1.getRoot());
            }
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
//                selectedIndex = position;
//                String forRow = definitions.get(position).toString();
                holder.searched_term.setText("forRow");
            }
            @Override
            public int getItemCount() {
                //return definitions.size();
                return definitions != null ? definitions.size() : 0;
            }
//            @Override
//            public int getItemViewType(int position){
//                return 0;
//            }
        });// end of setAdapter

    } //end of onCreate()

    //myRowHolder
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView searched_term;
        //String definition;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            searched_term=itemView.findViewById(R.id.searched_term);
            itemView.setOnClickListener(clk -> {

                int position = getAdapterPosition();
                String selectedTerm = definitions.get(position);
                definitionModel.selectedDefinition.postValue(selectedTerm);
                AlertDialog.Builder builder = new AlertDialog.Builder( DictionaryActivity.this );
                builder.setMessage("Do you want to delete the message:"+ selectedTerm);
                builder.setTitle("Question:");
                builder.setNegativeButton("No",(dialog,cl)->{});
                builder.setPositiveButton("Yes",(dialog,cl)->{
                    String  selectedDefinition = definitions.get(position);
                    definitions.remove(position);
                    myAdapter.notifyItemRemoved(position);
                    Snackbar.make(searched_term,"You deleted message #"+ position, Snackbar.LENGTH_LONG).setAction("Undo", click->{
                        definitions.add(position, selectedDefinition);
                        myAdapter.notifyItemInserted(position);
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

}// end of the class
