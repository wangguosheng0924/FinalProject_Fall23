package algonquin.cst2335.finalproject_fall23;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject_fall23.data.RecipeViewModel;
import algonquin.cst2335.finalproject_fall23.databinding.RecipeMainBinding;
import algonquin.cst2335.finalproject_fall23.databinding.RecipeTitleBinding;

public class RecipeMain extends AppCompatActivity {
    private RecipeMainBinding binding;
    RequestQueue queue = null;
    private RecyclerView.Adapter myAdapter;
    ArrayList<Recipe> recipeList = null;
    RecipeDAO rDAO;
    RecipeViewModel recipeModel;
    Intent homePage;
    Intent searchPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RecipeMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar( binding.recipeToolBar);

        RecipeDatabase db = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "database-recipe").build();
        rDAO = db.rDAO();

        homePage = new Intent( RecipeMain.this, MainActivity.class);
        searchPage = new Intent( RecipeMain.this, RecipeSearch.class);

        queue = Volley.newRequestQueue(this);

        recipeModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipeList = recipeModel.recipeList.getValue();
        //get data from Database
        if(recipeList == null)
        {
            recipeModel.recipeList.setValue(recipeList = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                recipeList.addAll( rDAO.getAllRecipes() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recipeRecycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        recipeModel.selectedRecipe.observe(this, (newMessageValue) -> {
            RecipeDetailsFragment chatFragment = new RecipeDetailsFragment( newMessageValue );
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.addToBackStack("");
            tx.replace(R.id.recipeFragmentLocation,chatFragment);
            tx.commit();
        });

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
                holder.recipeTitle.setText("");
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
        getMenuInflater().inflate(R.menu.recipe_main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.homeMenu){
                Toast.makeText(this,"Back to home page...", Toast.LENGTH_SHORT).show();
                startActivity( homePage);

        } else if(itemId == R.id.searchRecipeMenu){
                Toast.makeText(this,"Go to searching page...", Toast.LENGTH_SHORT).show();
                startActivity( searchPage);

        } else if(itemId == R.id.aboutRecipeMenu){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Select a favourite recipe by clicking on it, then you can choose whether to look up the details information about it or delete it by using the toolbar").setTitle("Instruction")
                        .setNegativeButton("OK", (dialog, cl) -> {
                        }).create().show();

        } else if(itemId == R.id.deleteRecipe){
                Recipe removedRecipe = recipeModel.selectedRecipe.getValue();
                int position = recipeList.indexOf(removedRecipe);
                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeMain.this);

                builder.setMessage("Do you want to delete the recipe:"
                                + removedRecipe.getTitle()).setTitle("Question: ")
                        .setNegativeButton("No", (dialog, cl) -> {
                        })
                        .setPositiveButton("Yes", (dialog, cl) -> {

                            recipeList.remove(position);
                            myAdapter.notifyDataSetChanged();
                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(( ) -> {
                                //this is on a background thread
                                rDAO.deleteRecipe(removedRecipe); //get the ID from the database

                            }); //the body of run()
                            Snackbar.make(this.findViewById(R.id.recipeTextView),"You deleted recipe #"
                                            + position,Snackbar.LENGTH_LONG)
                                    .setAction("Undo", click -> {
                                        recipeList.add(position,removedRecipe);
                                        myAdapter.notifyDataSetChanged();
                                        Executor thread1 = Executors.newSingleThreadExecutor();
                                        thread1.execute(( ) -> {
                                            //this is on a background thread
                                            removedRecipe.id = (int) rDAO.insertRecipe(removedRecipe); //get the ID from the database
                                        }); //the body of run()
                                    }).show();
                        }).create().show();
                getSupportFragmentManager() .popBackStack();

        }

        return true;
    }
}