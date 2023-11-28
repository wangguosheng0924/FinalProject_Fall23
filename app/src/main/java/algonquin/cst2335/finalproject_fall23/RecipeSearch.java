package algonquin.cst2335.finalproject_fall23;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import algonquin.cst2335.finalproject_fall23.data.RecipeViewModel;
import algonquin.cst2335.finalproject_fall23.databinding.RecipeDetailsBinding;
import algonquin.cst2335.finalproject_fall23.databinding.RecipeSearchBinding;
import algonquin.cst2335.finalproject_fall23.databinding.RecipeTitleBinding;

public class RecipeSearch extends AppCompatActivity {
    RecipeSearchBinding binding;
    RequestQueue queue = null;
    Recipe recipefromAPI;
    RecyclerView.Adapter myAdapter;
    ArrayList<Recipe> recipeList = null;
    RecipeDAO rDAO;
    RecipeViewModel recipeModel ;
    Intent homePage;
    Intent mainPage;
    String searchRecipe;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = RecipeSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);

        recipeModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        RecipeDatabase db = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "recipe-SQL").build();
        rDAO = db.rDAO();

        homePage = new Intent(RecipeSearch.this, MainActivity.class);
        mainPage = new Intent(RecipeSearch.this, RecipeMain.class);

        queue = Volley.newRequestQueue(this);

        recipeList = recipeModel.recipeList.getValue();
        recipeModel.recipeList.setValue(recipeList = new ArrayList<>());

//        if (recipeList == null) {
//            recipeModel.recipeList.setValue(recipeList = new ArrayList<>());
//
//            Executor thread = Executors.newSingleThreadExecutor();
//            thread.execute(() ->
//            {
//                recipeList.addAll(rDAO.getAllRecipes()); //Once you get the data from database
//
//                runOnUiThread(() -> binding.recyclerViewResults.setAdapter(myAdapter)); //You can then load the RecyclerView
//            });
//        }

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        AtomicReference<EditText> searchText = new AtomicReference<>(binding.editTextRecipe);
        Button searchButton = binding.buttonSearch;


        searchButton.setOnClickListener(clk->
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("searchText", searchText.get().getText().toString() );
            editor.apply();

            Toast.makeText(this,"Searching...", Toast.LENGTH_SHORT).show();


            try {
                String searchRecipe = URLEncoder.encode(binding.editTextRecipe.getText().toString(),"UTF-8");
                String url = "https://api.spoonacular.com/recipes/complexSearch?query="
                        + searchRecipe +"&apiKey=00939092e09741dd98d285edda1fc2ec";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,

                    (response) -> {
                        try {
                            JSONArray resultsArray= response.getJSONArray("results");
                            recipeModel.recipeList.setValue(recipeList = new ArrayList<>());

                            for(int i=0;i<resultsArray.length();i++){
                                JSONObject position0 = resultsArray.getJSONObject(i);
                                int id=position0.getInt("id");
                                String title = position0.getString("title");
                                String image = position0.getString("image");


                                recipefromAPI= new Recipe(title,image,id);
                                recipeList.add(recipefromAPI);
                                myAdapter.notifyDataSetChanged();
                                String pathname = getFilesDir() + "/" + id + ".jpg";
                                File file = new File(pathname);
                                if (file.exists()) {

                                } else {
                                    ImageRequest imgReq = new ImageRequest(image, new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {
                                            // Do something with loaded bitmap...

                                            try { bitmap.compress(Bitmap.CompressFormat.PNG, 100, RecipeSearch.this.openFileOutput(id + ".png", Activity.MODE_PRIVATE));
                                            } catch (Exception e) {

                                            }

                                        }
                                    }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                                    });
                                    queue.add(imgReq);
                                }
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (error) -> {
                    });
            queue.add(request);

        } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }});



        searchText.get().setText(prefs.getString("searchText",""));
        recipeModel.selectedRecipe.observe(this, (newValue) -> {
            RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment(newValue);
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();

            tx.replace(R.id.fragmentLocation, recipeDetailsFragment);
            tx.commit();
        });

        // Set up RecyclerView and Adapter
        binding.recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewResults.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                RecipeTitleBinding binding = RecipeTitleBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.recipeTitle.setText(recipeList.get(position).getTitle());
                String obj = recipeList.get(position).getTitle();
                holder.recipeTitle.setText(obj);
            }


            @Override
            public int getItemCount() {
                return recipeList.size();
            }
        });

    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView recipeTitle;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitle=itemView.findViewById(R.id.recipeTitle);
            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                Recipe selected = recipeList.get(position);

                recipeModel.selectedRecipe.postValue(selected);

            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.homePage) {
            Toast.makeText(this, "Back to home page", Toast.LENGTH_SHORT).show();
            startActivity(homePage);
        }

        if (itemId == R.id.mainPage) {
            Toast.makeText(this, "Go to main page", Toast.LENGTH_SHORT).show();
            startActivity(mainPage);
        }

        if (itemId == R.id.help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This recipe search application is created by Jingyi Zhou.").setTitle("About: ")
                    .setNegativeButton("OK", (dialog, cl) -> {
                    }).create().show();
        }

        if (itemId == R.id.saveRecipe) {

            Recipe addRecipe = recipeModel.selectedRecipe.getValue();
            recipeList.add(addRecipe);
            myAdapter.notifyDataSetChanged();
            Executor thread2 = Executors.newSingleThreadExecutor();
            thread2.execute(() -> {
                //this is on a background thread
                addRecipe.id = (int) rDAO.insertRecipe(addRecipe); //get the ID from the database
            }); //the body of run()
            Snackbar.make(this.findViewById(R.id.editTextRecipe), "You added the recipe "
                    + addRecipe.getTitle(), Snackbar.LENGTH_LONG).show();
            getSupportFragmentManager().popBackStack();
                }

        return true;
    }
}