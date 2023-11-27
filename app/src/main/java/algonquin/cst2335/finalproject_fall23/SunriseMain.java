package algonquin.cst2335.finalproject_fall23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject_fall23.data.LocationViewModel;
import algonquin.cst2335.finalproject_fall23.databinding.SunriseMainBinding;
import algonquin.cst2335.finalproject_fall23.databinding.SunriseLocationBinding;

public class SunriseMain extends AppCompatActivity {

    SunriseMainBinding binding;
    LocationDAO lDAO;
    Location selectedLocation;
    LocationViewModel locationModel;
    ArrayList<Location> theLocation = null;
    RecyclerView.Adapter myAdapter = null;
    RequestQueue queue = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = SunriseMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //This part goes at the top of the onCreate function:
        // Initialize the RequestQueue using Volley
        queue = Volley.newRequestQueue(this);

        // Get an instance of the LocationViewModel using the ViewModelProvider.
        locationModel = new ViewModelProvider(this).get(LocationViewModel.class);

        // Access the 'theLocation' ArrayList within the LocationViewModel.
        // This ArrayList holds the data associated with the sunrise main.
        theLocation = locationModel.theLocation;

        //load location from the database:
        LocationDatabase db = Room.databaseBuilder(getApplicationContext(), LocationDatabase.class, "finalProject_sunrise_SQL").build();
        //initialize the variable
        lDAO = db.lDAO();//get a DAO object to interact with the database

        // Set up the toolbar(call onCreateOptionsMenu)
        setSupportActionBar(binding.myToolbar);//only one line required to initialize toolbar


        // Set up RecyclerView and Adapter
        binding.recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewResults.setAdapter(
        myAdapter = new RecyclerView.Adapter<MyRowHolder>(){
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SunriseLocationBinding rowBinding = SunriseLocationBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(rowBinding.getRoot());
            }

                 @Override
                 public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                     Location location = theLocation.get(position);
                     holder.latitude.setText(location.getLatitude());
                     holder.longitude.setText(location.getLongitude());
                 }

            @Override
            public int getItemCount() {
                return theLocation.size();
            }
        });

//        // Show a Toast
//        Toast.makeText(this, "Version 1.0, created by Guosheng", Toast.LENGTH_LONG).show();
//
//        // Show a Snackbar
//        Snackbar.make(binding.getRoot(), "You deleted the row", Snackbar.LENGTH_LONG)
//                  .show();
//
//        // Show a AlertDialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(SunriseMain.this);
//        builder.setTitle("Question");
//        builder.setMessage("Do you want to delete the message?");
//        builder.setNegativeButton("No", (btn, obj) -> {/*if no is clicked*/});
//        builder.setPositiveButton("Yes", (p1, p2) -> {
//// code here
//
//        });
//        builder.create().show();


        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        // Click handler for the buttonLookup
        binding.buttonLookup.setOnClickListener(click -> {

            // Save the entered Latitude and Longitude to disk (SharedPreferences)
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Latitude", binding.editTextLatitude.getText().toString());
            editor.putString("Longitude", binding.editTextLongitude.getText().toString());
            editor.apply();//send to disk

            try{
            String newLatitude = URLEncoder.encode(binding.editTextLatitude.getText().toString());
            String newLongitude = URLEncoder.encode(binding.editTextLongitude.getText().toString());

            String url = "http://api.sunrisesunset.io/json?lat=" + newLatitude + "&lng=" + newLongitude + "&timezone=CA&date=today";

            // Create a JsonObjectRequest to fetch weather information
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,

                    // Successful response callback
                    (response) ->{

                        try {
                            // Extract weather information from the JSON response
                            JSONObject mainObject = response.getJSONObject( "results" );

                            String timeSunrise = mainObject.getString("sunrise");
                            String timeSunset = mainObject.getString("sunset");
                            String timeFirstLight = mainObject.getString("first_light");
                            String timeLastLight = mainObject.getString("last_light");
                            String timeDawn = mainObject.getString("dawn");
                            String timeDusk = mainObject.getString("dusk");
                            String timeSolarNoon = mainObject.getString("solar_noon");
                            String timeGoldenHour = mainObject.getString("golden_hour");
                            String dayLength = mainObject.getString("day_length");
                            Log.d("API_RESPONSE", response.toString());

           //                 locationModel.selectedLocation.observe(this,(selectedLocation)-> {
                                ;
                                // Create a new fragment for message details
                                SunriseDetailsFragment sunriseDetailsFragment = new SunriseDetailsFragment();

                                // Pass the sunrise details to the fragment
                                sunriseDetailsFragment.updateSunriseDetails(timeSunrise, timeSunset, timeFirstLight, timeLastLight,
                                        timeDawn, timeDusk, timeSolarNoon, timeGoldenHour, dayLength);

                                //to load fragments:
                                FragmentManager fMgr = getSupportFragmentManager();
                                FragmentTransaction tx = fMgr.beginTransaction();
                                tx.add(R.id.fragmentLocation, sunriseDetailsFragment);
                                tx.commit();
            //                });

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    },
                    (error) ->{  error.printStackTrace();}
            );
                Log.d("SunriseMain", "Before network request");
            queue.add(request); // fetches from the server
                Log.d("SunriseMain", "After network request");

        } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
// code here


        // Load the last entered Latitude and Longitude from SharedPreferences
        String lastEnteredLatitude = prefs.getString("Latitude", "");
        binding.editTextLatitude.setText(lastEnteredLatitude);
        String lastEnteredLongitude = prefs.getString("Longitude", "");
        binding.editTextLongitude.setText(lastEnteredLongitude);

        binding.buttonSaveAsFavouriteLocation.setOnClickListener(click -> {
            String newLatitude = binding.editTextLatitude.getText().toString();
            String newLongitude = binding.editTextLongitude.getText().toString();

            // Create a new Location object
            Location location = new Location(newLatitude, newLongitude);

            // Add the Location to the list
            theLocation.add(location);

            // Tell the recycle view to update
            myAdapter.notifyDataSetChanged();//will redraw

            //add to database on another thread
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {
                //this is on a background thread
                location.id = lDAO.insertLocation(location);//get the id from the database
            });//the body of run()

            Toast.makeText(SunriseMain.this, "You added the location as favourite",Toast.LENGTH_LONG).show();

        });

        binding.buttonViewFavouriteLocation.setOnClickListener(click -> {

            // Fetch favorite locations from the database on another thread
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {

                ArrayList<Location> favoriteLocations = (ArrayList<Location>) lDAO.getAllLocations();

                // Update the UI on the main thread
                runOnUiThread(() -> {
                    // Update the 'theLocation' ArrayList with favorite locations
                    theLocation.addAll(favoriteLocations);

                    // Tell the RecyclerView's adapter to update
                    myAdapter.notifyDataSetChanged();
                });
            });

        });

        // Handle the backToMainActivityButton click
        binding.backToMainActivityButton.setOnClickListener(click -> {
            // to go back:
            finish();
        });

    }
    // Override method for creating the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //inflate a menu into the toolbar
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            int itemId = item.getItemId();

            if(itemId == R.id.detailsLocation) {
                if (selectedLocation != null) {

                    String newLatitude = URLEncoder.encode(selectedLocation.getLatitude().toString());
                    String newLongitude = URLEncoder.encode(selectedLocation.getLongitude().toString());

                    String url = "http://api.sunrisesunset.io/json?lat=" + newLatitude + "&lng=" + newLongitude + "&timezone=CA&date=today";

                    // Create a JsonObjectRequest to fetch weather information
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,

                            // Successful response callback
                            (response) ->{

                                try {
                                    // Extract weather information from the JSON response
                                    JSONObject mainObject = response.getJSONObject( "results" );

                                    String timeSunrise = mainObject.getString("sunrise");
                                    String timeSunset = mainObject.getString("sunset");
                                    String timeFirstLight = mainObject.getString("first_light");
                                    String timeLastLight = mainObject.getString("last_light");
                                    String timeDawn = mainObject.getString("dawn");
                                    String timeDusk = mainObject.getString("dusk");
                                    String timeSolarNoon = mainObject.getString("solar_noon");
                                    String timeGoldenHour = mainObject.getString("golden_hour");
                                    String dayLength = mainObject.getString("day_length");
                                    Log.d("API_RESPONSE", response.toString());

                                    // Create a new fragment for message details
                                    SunriseDetailsFragment sunriseDetailsFragment = new SunriseDetailsFragment();

                                    // Pass the sunrise details to the fragment
                                    sunriseDetailsFragment.updateSunriseDetails(timeSunrise, timeSunset, timeFirstLight, timeLastLight,
                                            timeDawn, timeDusk, timeSolarNoon, timeGoldenHour, dayLength);

                                    //to load fragments:
                                    FragmentManager fMgr = getSupportFragmentManager();
                                    FragmentTransaction tx = fMgr.beginTransaction();
                                    tx.add(R.id.fragmentLocation, sunriseDetailsFragment);
                                    tx.commit();

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            },
                            (error) ->{  error.printStackTrace();}
                    );
                    Log.d("SunriseMain", "Before network request");
                    queue.add(request); // fetches from the server
                    Log.d("SunriseMain", "After network request");

                      }
                else {
                    Toast.makeText(this, "No location selected", Toast.LENGTH_SHORT).show();
                }

            }

            if (itemId == R.id.deleteLocation) {
                // Put your ChatMessage deletion code here.
                // If you select this item, you should show the alert dialog
                // asking if the user wants to delete this message.

                if (selectedLocation != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SunriseMain.this);

                    builder.setTitle("Delete");
                    builder.setMessage("Do you want to delete the message?");

                    builder.setNegativeButton("No", (btn, obj) -> {/*if no is clicked*/});
                    builder.setPositiveButton("Yes", (p1, p2) -> {

                        // Store the index before deleting
                        int deletedMessagePosition = theLocation.indexOf(selectedLocation);

                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            // delete from database
                            lDAO.deleteLocation(selectedLocation);
                        });
                        theLocation.remove(selectedLocation); // remove from the array list
                        myAdapter.notifyDataSetChanged(); // redraw the list

                        // give feedback: anything on the screen
                        Snackbar.make(binding.getRoot(), "You deleted the row", Snackbar.LENGTH_LONG)
                                .setAction("Undo", (btn) -> {
                                    Executor thread1 = Executors.newSingleThreadExecutor();
                                    thread1.execute(() -> {
                                        lDAO.insertLocation(selectedLocation);
                                    });
                                    theLocation.add(deletedMessagePosition, selectedLocation);
                                    myAdapter.notifyDataSetChanged(); // redraw the list
                                })
                                .show();
                                            });

                    builder.create().show(); // this has to be last
                } else {
                    Toast.makeText(this, "No location selected", Toast.LENGTH_SHORT).show();
                }
            }
            if (itemId == R.id.help) {
                // Show a AlertDialog with instructions for how to use the interface
                AlertDialog.Builder builder = new AlertDialog.Builder(SunriseMain.this);
                builder.setTitle("Instructions");
                builder.setMessage("Select a location by clicking on it, then you can choose whether to look up the details information about it or delete it by using the toolbar.");
                 //builder.setNegativeButton("No", (btn, obj) -> {/*if no is clicked*/});
                 //builder.setPositiveButton("Yes", (p1, p2) -> {
                 //                 });
                 builder.create().show();

            }

            return true;
        }

    // Represents a single row on the list
    class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView latitude;
        public TextView longitude;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);


            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();//which row this is
                selectedLocation = theLocation.get(position);

                // Update the selected location in the ViewModel
                locationModel.selectedLocation.postValue(selectedLocation);

            });
        }


    }
}
