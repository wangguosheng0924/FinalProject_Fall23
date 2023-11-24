package algonquin.cst2335.finalproject_fall23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject_fall23.data.LocationViewModel;
import algonquin.cst2335.finalproject_fall23.databinding.ActivitySunriseMainBinding;
import algonquin.cst2335.finalproject_fall23.databinding.LocationBinding;

public class SunriseMain extends AppCompatActivity {

    ActivitySunriseMainBinding binding;
    LocationDAO lDAO;
    Location selectedLocation;
    LocationViewModel locationModel;
    ArrayList<Location> theLocation = null;
    RecyclerView.Adapter myAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySunriseMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get an instance of the LocationViewModel using the ViewModelProvider.
        locationModel = new ViewModelProvider(this).get(LocationViewModel.class);

        // Access the 'theLocation' ArrayList within the LocationViewModel.
        // This ArrayList holds the data associated with the sunrise main.
        theLocation = locationModel.theLocation;

        //load location from the database:
        LocationDatabase db = Room.databaseBuilder(getApplicationContext(), LocationDatabase.class, "finalProject_sunrise_SQL").build();
        //initialize the variable
        lDAO = db.lDAO();//get a DAO object to interact with the database


        // Set up RecyclerView and Adapter
        binding.recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewResults.setAdapter(
             myAdapter = new RecyclerView.Adapter<MyRowHolder>(){
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LocationBinding rowBinding = LocationBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(rowBinding.getRoot());
            }

                 @Override
                 public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                     MyRowHolder myRowHolder = (MyRowHolder) holder;
                     Location location = theLocation.get(position);
                     myRowHolder.latitude.setText(location.getLatitude());
                     myRowHolder.longitude.setText(location.getLongitude());
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

            String newLatitude = binding.editTextLatitude.getText().toString();
            String newLongitude = binding.editTextLongitude.getText().toString();
// code here
        });

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

        });

        binding.buttonViewFavouriteLocation.setOnClickListener(click -> {

            // Fetch favorite locations from the database on another thread
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {
                // This is on a background thread
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

    ;

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
