package algonquin.cst2335.finalproject_fall23;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import algonquin.cst2335.finalproject_fall23.databinding.RecipeSearchBinding;
import algonquin.cst2335.finalproject_fall23.databinding.RecipeTitleBinding;

public class RecipeSearch extends AppCompatActivity {
    private RecipeSearchBinding binding;
    RequestQueue queue = null;
    private RecyclerView.Adapter myAdapter;
    String recipe;
    String url;
    ArrayList<Recipe> recipeList = null;
    RecipeDAO rDAO;
    RecipeViewModel recipeModel;
    Intent homePage;
    Intent favoriteRecipePage;
    Recipe recipefromAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RecipeSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar( binding.recipeToolBar);

        recipeModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        RecipeDatabase db = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "database-recipe").build();
        rDAO = db.rDAO();

        homePage = new Intent( RecipeSearch.this, MainActivity.class);
        favoriteRecipePage = new Intent( RecipeSearch.this, RecipeMain.class);

        queue = Volley.newRequestQueue(this);

        //get data from Database
        recipeList = recipeModel.recipeList.getValue();
        recipeModel.recipeList.setValue(recipeList = new ArrayList<>());

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        AtomicReference<EditText> searchText = new AtomicReference<>(binding.recipeTextView);

        binding.recipeSearchButton.setOnClickListener(clk->
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("searchText", searchText.get().getText().toString() );
            editor.apply();
            CharSequence text = "Searching...";
            Toast.makeText(this,text, Toast.LENGTH_SHORT).show();
            recipe = binding.recipeTextView.getText().toString();

            try {
                url = "https://api.spoonacular.com/recipes/complexSearch?query="
                        + URLEncoder.encode(recipe, "UTF-8")
                        +"&apiKey=00939092e09741dd98d285edda1fc2ec";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            JSONArray resultsArray= response.getJSONArray("results");
                            recipeModel.recipeList.setValue(recipeList = new ArrayList<>());

                            for(int i=0;i<resultsArray.length();i++){
                                JSONObject thisObject = resultsArray.getJSONObject(i);
                                int id=thisObject.getInt("id");
                                String title = thisObject.getString("title");
                                String image = thisObject.getString("image");

                                recipefromAPI= new Recipe(title,image,id);
                                recipeList.add(recipefromAPI);
                                myAdapter.notifyDataSetChanged();

                                String pathname = getFilesDir() + "/" + id + ".jpg";
                                File file = new File(pathname);
                                if (file.exists()) {
                                } else {
                                    ImageRequest imageRequest = new ImageRequest(image, new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {
                                            // Do something with loaded bitmap...

                                            try {

                                                Log.e("ImageRequest", " loading image: " + bitmap);
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, RecipeSearch.this.openFileOutput(id + ".png", Activity.MODE_PRIVATE));
                                            } catch (Exception e) {

                                            }
                                            Log.e("ImageRequest", " loading image: " + image);

                                        }
                                    }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                                        Log.e("ImageRequest", "Error loading image: ");
                                    });
                                    queue.add(imageRequest);
                                }
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (error) -> {
                    });
            queue.add(request);

        });
        searchText.get().setText(prefs.getString("searchText",""));

//        recipeModel.selectedRecipe.observe(this, (newMessageValue) -> {
//            RecipeDetailsFragment chatFragment = new RecipeDetailsFragment( newMessageValue );
//            FragmentManager fMgr = getSupportFragmentManager();
//            FragmentTransaction tx = fMgr.beginTransaction();
//            tx.addToBackStack("");
//            tx.replace(R.id.recipeFragmentLocation,chatFragment);
//            tx.commit();
//        });

        binding.recipeRecycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recipeRecycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                RecipeTitleBinding binding = RecipeTitleBinding.inflate(getLayoutInflater());
                return new MyRowHolder( binding.getRoot() );
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.recipeTitle.setText(recipeList.get(position).getTitle());
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
            if (itemId == R.id.homeMenu){
                CharSequence text = "Back to home page...";
                Toast.makeText(this,text, Toast.LENGTH_SHORT).show();
                startActivity( homePage);

            } else if(itemId == R.id.savedRecipeMenu ){
                CharSequence text2 = "Go to favorite recipe page...";
                Toast.makeText(this,text2, Toast.LENGTH_SHORT).show();
                startActivity(favoriteRecipePage);

            } else if (itemId == R.id.aboutRecipeMenu){
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("Enter the recipe name you want to search, you will get it by clicking on the search button, and you can save the favorite recipe by clicking on it then click the save icon on the toolbar.").setTitle("Instruction: ")
                        .setNegativeButton("OK", (dialog, cl) -> {
                        }).create().show();

            } else if (itemId == R.id.saveTheRecipe) {
                Recipe addRecipe = recipeModel.selectedRecipe.getValue();
                recipeList.add(addRecipe);
                myAdapter.notifyDataSetChanged();
                Executor thread2 = Executors.newSingleThreadExecutor();
                thread2.execute(( ) -> {
                    //this is on a background thread
                    addRecipe.id = (int)rDAO.insertRecipe(addRecipe); //get the ID from the database
                    }); //the body of run()
                Snackbar.make(this.findViewById(R.id.recipeTextView),"You added the recipe "
                        +addRecipe.getTitle(),Snackbar.LENGTH_LONG).show();
                getSupportFragmentManager() .popBackStack();

        }
        return true;
    }
}