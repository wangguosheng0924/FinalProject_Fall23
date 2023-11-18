package algonquin.cst2335.finalproject_fall23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.finalproject_fall23.databinding.ActivitySunriseMainBinding;

public class SunriseMain extends AppCompatActivity {

    ActivitySunriseMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySunriseMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up RecyclerView and Adapter
        binding.recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewResults.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });

        // Show a Toast
        Toast.makeText(this, "Version 1.0, created by Guosheng", Toast.LENGTH_LONG).show();

        // Show a Snackbar
        Snackbar.make(binding.getRoot(), "You deleted the row", Snackbar.LENGTH_LONG)
                  .show();

        // Show a AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(SunriseMain.this);
        builder.setTitle("Question");
        builder.setMessage("Do you want to delete the message?");
        builder.setNegativeButton("No", (btn, obj) -> {/*if no is clicked*/});
        builder.setPositiveButton("Yes", (p1, p2) -> {
// code here

        });
        builder.create().show();


        // Click handler for the buttonLookup
        binding.buttonLookup.setOnClickListener(click ->{
            String newLatitude = binding.editTextLatitude.getText().toString();
            String newLongitude = binding.editTextLongitude.getText().toString();
// code here
        });
    };
}