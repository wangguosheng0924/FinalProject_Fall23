package algonquin.cst2335.finalproject_fall23;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject_fall23.databinding.DictionaryactivityMainBinding;
import algonquin.cst2335.finalproject_fall23.databinding.PostsearchlayoutBinding;


public class DictionaryActivity extends AppCompatActivity implements HistoryFragment.HistoryClickListener {
    DictionaryactivityMainBinding binding;

    PostsearchlayoutBinding binding1;
    ArrayList<Definition> definitionsLocal;
    DefinitionViewModel definitionModel;
    DefinitionDAO mDAO;
    RecyclerView.Adapter myAdapter;

    protected RequestQueue queue = null;
    private boolean saveOrDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //before using binding use this line: setContentView(R.layout.dictionaryactivity_main);
        //use binding to access the views defined in the layout.
        binding = DictionaryactivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EditText searchEditText = binding.editText;
        //how to use toast.
        Toast.makeText(this, "Welcome to Dictionary", Toast.LENGTH_LONG).show();
        //apply SharedPreference
        SharedPreferences prefs1 = getSharedPreferences("searchTerm", Context.MODE_PRIVATE);
        searchEditText.setText(prefs1.getString("searchTerm", ""));

        // create a volley object that will connect to a server
        //This part goes at the top of the onCreate function:
        queue = Volley.newRequestQueue(this);
        //open a database
        DefinitionDB db = Room.databaseBuilder(getApplicationContext(), DefinitionDB.class, "databaseLocal").build();
        mDAO = db.cmDAO();
        //definition initialization
        definitionModel = new ViewModelProvider(this).get(DefinitionViewModel.class);
        if(definitionModel.definitionsLocal.getValue()!=null){
            definitionsLocal =definitionModel.definitionsLocal.getValue();}
        else{definitionsLocal = new ArrayList<>();
        }

        //search button:
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {

            hideHistoryFragment();
            setButtonSaveOrDelete(true);

            String searchTerm = binding.editText.getText().toString();

            // save the last searched term
            SharedPreferences prefs2 = getSharedPreferences("searchTerm", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs1.edit();
            editor.putString("searchTerm", searchTerm);
            editor.apply();

            //myAdapter.notifyItemInserted(definitionsLocal.size() - 1);

            // use volley to retrieve data form the server
            String stringURL = "https://api.dictionaryapi.dev/api/v2/entries/en/" + searchTerm;

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        //get the data back from the response
                        int size = response.length();
                        definitionsLocal.clear();
                        for(int i = 0; i<size; i++){
                            try {
//                                definitionsLocal.clear();
                                JSONObject entry = response.getJSONObject(i);
                                JSONArray meanings = entry.getJSONArray("meanings");

                                for (int j = 0; j< meanings.length(); j++){
                                    JSONObject meaning = meanings.getJSONObject(j);
                                    JSONArray definitions = meaning.getJSONArray("definitions");

                                    for(int k = 0; k< definitions.length(); k++){
                                        JSONObject obj= definitions.getJSONObject(k);
                                        String def = obj.getString("definition");
                                       // new a Definition database object and add this object to the local database
                                        Definition definitionFromAPI = new Definition(searchTerm, def);
                                        definitionsLocal.add(definitionFromAPI);
                                    }
                                    myAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e){
                                throw new RuntimeException(e);
                            }
                        }// end of the outer for
                        // locate the textView and setText in the textView
                    }, // end of response()
                    error -> {
                    }
            );// end of JsonObjectRequest
            queue.add(request);
        }); // end of the searchButton.setOnClickListener.


        //save button: should I put this definitions as <string> or <definition>?
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(clk -> {
            if (saveOrDelete) {
                for (Definition definition : definitionsLocal) {
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() -> {
                        mDAO.insertDefinition(definition);
                        Log.d("TAG", "The word and definition saved is:" + definition);
                    });
                }
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            } else {
                for (Definition definition : definitionsLocal) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        mDAO.deleteDefinition(definition);
                    });
                }
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        //history button:
        Button historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(clk -> {
            String saved_word;
            Set<String> saved_word_set = new HashSet<>();
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                for (Definition item : mDAO.getAllDefinitions()) {
                    String word = item.getTerm();
                    saved_word_set.add(word);
                }
                runOnUiThread(() -> {
                    // Convert the Set to a List if necessary
                    ArrayList<String> saved_word_list = new ArrayList<>(saved_word_set);

                    // Create a new instance of the HistoryFragment and pass the saved_word_list
                    HistoryFragment historyFragment = new HistoryFragment(saved_word_list);

                    // Replace the current layout with the HistoryFragment
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, historyFragment)
                            .addToBackStack(null)
                            .commit();
                });
            });
        });
        // set a Linear layout manager as the layout manager for the recycleView.
        // the items in the recyclerView can be arranged either vertical or horizontal.
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // setAdapter for recyclerView
        binding.recyclerView.setAdapter(myAdapter= new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            //adapter is like a bridge to create a new view-myrowholder
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                binding1 = PostsearchlayoutBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding1.getRoot()); // put binding1 into this viewHolder for items.
            }
            @Override
            //adapter is like a bridge to bind data into the viewholder-myrowholder in the recyclerview.
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                Definition currentDefinition = definitionsLocal.get(position);
                holder.searched_term.setText(currentDefinition.getTerm());
                holder.def1.setText(currentDefinition.getDefinition());

//                holder.searched_term.setText(holder.searched_term.getText());
//                holder.def1.setText(holder.def1.getText());

            }
            @Override
            public int getItemCount() {
                return definitionsLocal != null ? definitionsLocal.size() : 0;
            }
        });// end of setAdapter

        //initialize the toolbar. Android will call onCreateOptionsMenu().
        setSupportActionBar(binding.myToolbar);

    } //end of onCreate()

    @Override //this initialized the toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dictionary_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override // this defines when users select a menuItem
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch( item.getItemId() )
        {
            case R.id.item1:
                AlertDialog.Builder builder = new AlertDialog.Builder( DictionaryActivity.this );
                builder.setMessage("search a word to check the definition by clicking 'search'." +
                        "\nsave your searched work and its definitions to device by clicking 'save'." +
                        "\nreview your search history by click 'history'");
                builder.setTitle("Help");
                builder.create().show();
                break;
        }
        return true;
    }

    @Override
    public void onHistoryClick(String word) {
        Log.d("onHistoryClick", word);
        showDefinitionFromDB(word);
        setButtonSaveOrDelete(false);
        hideHistoryFragment();
    }

    private void setButtonSaveOrDelete(boolean saveOrDelete) {
        this.saveOrDelete = saveOrDelete;
        ((Button) findViewById(R.id.save_button)).setText(saveOrDelete ? "Save" : "Delete");
    }

    private void hideHistoryFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        }
    }

    private void showDefinitionFromDB(String word) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Definition> definitions = mDAO.searchDefinitions(word);
            definitionsLocal.clear();
            definitionsLocal.addAll(definitions);
            runOnUiThread(() -> {
                myAdapter.notifyDataSetChanged();
            });
        });
    }

    //myRowHolder
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView searched_term;
        TextView def1;
//        TextView def2;
//        TextView def3;
        //String definition;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            searched_term=itemView.findViewById(R.id.searched_term);
            // 实现你的数据集合以及创建和绑定视图项的逻辑
            def1=itemView.findViewById(R.id.def1);
//            def2=itemView.findViewById(R.id.def2);
//            def3=itemView.findViewById(R.id.def3);

            itemView.setOnClickListener(clk -> {
                int position = getAdapterPosition();
                Definition selectedTerm = definitionsLocal.get(position);
                definitionModel.selectedDefinition.postValue(selectedTerm);
                AlertDialog.Builder builder = new AlertDialog.Builder( DictionaryActivity.this );
                builder.setMessage("Do you want to delete the message:"+ selectedTerm.getTerm());
                builder.setTitle("Question:");
                builder.setNegativeButton("No",(dialog,cl)->{});
                builder.setPositiveButton("Yes",(dialog,cl)->{
                    definitionsLocal.remove(position);
                    myAdapter.notifyItemRemoved(position);
                    Snackbar.make(searched_term,"You deleted message #"+ position, Snackbar.LENGTH_LONG).setAction("Undo", click->{
                        definitionsLocal.add(position, selectedTerm);
                        myAdapter.notifyItemInserted(position);
                    }).show();
                });
                builder.create().show();
            });
        }
    } // end of MyRowHolder

}// end of the class
