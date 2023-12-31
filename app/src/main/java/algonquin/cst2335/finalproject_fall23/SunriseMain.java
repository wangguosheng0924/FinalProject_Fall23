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
import android.content.Intent;
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

/**
 * SunriseMain is an activity class that displays sunrise and sunset information for a specific location.
 * It allows users to search for location data, view details of selected locations, and manage their favorite locations.
 * @author Guosheng Wang
 */
public class SunriseMain extends AppCompatActivity {

    /** Binding instance for the activity. */
    private SunriseMainBinding binding;

    /** DAO for accessing location data in the database. */
    LocationDAO lDAO;

    /** The currently selected location. */
    Location selectedLocation;

    /** ViewModel for managing location data. */
    LocationViewModel locationModel;

    /** ArrayList to store a list of locations. */
    ArrayList<Location> theLocation = null;

    /** Adapter for the RecyclerView displaying locations. */
    RecyclerView.Adapter myAdapter = null;

    /** Volley RequestQueue for network requests. */
    RequestQueue queue = null;

    /**
     * Called when the activity is starting.
     * Initializes the user interface and sets up interactions with the location list and API requests.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
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

            String url = "https://api.sunrisesunset.io/json?lat=" + newLatitude + "&lng=" + newLongitude + "&timezone=CA&date=today";

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

                            //locationModel.selectedLocation.observe(this,(selectedLocation)-> {

                                // Create a new fragment for message details
                                SunriseDetailsFragment sunriseDetailsFragment = new SunriseDetailsFragment(timeSunrise, timeSunset, timeFirstLight, timeLastLight,
                                        timeDawn, timeDusk, timeSolarNoon, timeGoldenHour, dayLength);

                            // Show the fragment container
                            binding.fragmentLocation.setVisibility(View.VISIBLE);
                            binding.recyclerViewResults.setVisibility(View.GONE);

                           //to load fragments:
                            FragmentManager fMgr = getSupportFragmentManager();
                            FragmentTransaction tx = fMgr.beginTransaction();
                            tx.replace(R.id.fragmentLocation, sunriseDetailsFragment);
                            tx.commit();


                           //});

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
            //myAdapter.notifyDataSetChanged();//will redraw

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
                    theLocation.clear();
                    // Update the 'theLocation' ArrayList with favorite locations
                    theLocation.addAll(0,favoriteLocations);

                    // Tell the RecyclerView's adapter to update
                    myAdapter.notifyDataSetChanged();
                    binding.fragmentLocation.setVisibility(View.GONE);
                    binding.recyclerViewResults.setVisibility(View.VISIBLE);
                });
            });

        });

//        // Handle the backToMainActivityButton click
//        binding.backToMainActivityButton.setOnClickListener(click -> {
//            // to go back:
//            finish();
//        });

    }/**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which items are placed.
     * @return true for the menu to be displayed; if false, it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //inflate a menu into the toolbar
        getMenuInflater().inflate(R.menu.sunrise_menu, menu);
        return true;
    }

    /**
     * Called whenever an item in the options menu is selected.
     * Handles navigation and interaction with menu items.
     * @param item The menu item that was selected.
     * @return true to consume the menu selection here; false to allow normal menu processing to proceed.
     */
    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            int itemId = item.getItemId();

            if(itemId == R.id.homepage) {
                Toast.makeText(this, "Back to home page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SunriseMain.this, MainActivity.class));
            }

        if (itemId == R.id.detailsLocation) {
            if (selectedLocation != null) {
                String newLatitude = URLEncoder.encode(selectedLocation.getLatitude().toString());
                String newLongitude = URLEncoder.encode(selectedLocation.getLongitude().toString());

                String url = "https://api.sunrisesunset.io/json?lat=" + newLatitude + "&lng=" + newLongitude + "&timezone=CA&date=today";

                // Create a JsonObjectRequest to fetch sunrise details
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        // Successful response callback
                        (response) -> {
                            try {
                                // Extract sunrise details from the JSON response
                                JSONObject mainObject = response.getJSONObject("results");

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

                                // Create a new fragment for sunrise details
                                SunriseDetailsFragment sunriseDetailsFragment = new SunriseDetailsFragment(
                                        timeSunrise, timeSunset, timeFirstLight, timeLastLight,
                                        timeDawn, timeDusk, timeSolarNoon, timeGoldenHour, dayLength);

                                // Show the fragment container
                                binding.fragmentLocation.setVisibility(View.VISIBLE);
                                binding.recyclerViewResults.setVisibility(View.GONE);

                                // Load the sunrise details fragment
                                FragmentManager fMgr = getSupportFragmentManager();
                                FragmentTransaction tx = fMgr.beginTransaction();
                                tx.replace(R.id.fragmentLocation, sunriseDetailsFragment);
                                tx.addToBackStack(null);  // Add to back stack to enable going back
                                tx.commit();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(this, "Error parsing sunrise details", Toast.LENGTH_SHORT).show();
                            }
                        },
                        // Error callback
                        (error) -> {
                            error.printStackTrace();
                            Toast.makeText(this, "Error fetching sunrise details", Toast.LENGTH_SHORT).show();
                        }
                );

                // Add the request to the Volley queue
                Log.d("SunriseMain", "Before network request");
                queue.add(request);
                Log.d("SunriseMain", "After network request");
            } else {
                Toast.makeText(this, "No favourite location selected", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "No favourite location selected", Toast.LENGTH_SHORT).show();
                }
            }

            if (itemId == R.id.help) {
                // Show a AlertDialog with instructions for how to use the interface
                AlertDialog.Builder builder = new AlertDialog.Builder(SunriseMain.this);
                builder.setTitle("Instructions");
                builder.setMessage("Select a favourite location by clicking on it, then you can choose whether to look up the details information about it or delete it by using the toolbar.");
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
