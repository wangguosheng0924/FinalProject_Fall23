package algonquin.cst2335.finalproject_fall23;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject_fall23.databinding.SunriseDetailsBinding;

public class SunriseDetailsFragment extends Fragment {

    private SunriseDetailsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = SunriseDetailsBinding.inflate(inflater, container, false);
                return binding.getRoot();
    }

    // Method to update UI elements with sunrise details
    public void updateSunriseDetails(String sunrise, String sunset, String firstLight, String lastLight,
                                     String dawn, String dusk, String solarNoon, String goldenHour, String dayLength) {
        binding.timeSunrise.setText("The sunrise time is " + sunrise);
        binding.timeSunrise.setVisibility(View.VISIBLE);

        binding.timeSunset.setText("The sunset time is " + sunset);
        binding.timeSunset.setVisibility(View.VISIBLE);

        binding.timeFirstLight.setText("The first_light time is " + firstLight);
        binding.timeFirstLight.setVisibility(View.VISIBLE);

        binding.timeLastLight.setText("The last_light time is " + lastLight);
        binding.timeLastLight.setVisibility(View.VISIBLE);

        binding.timeDawn.setText("The dawn time is " + dawn);
        binding.timeDawn.setVisibility(View.VISIBLE);

        binding.timeDusk.setText("The dusk time is " + dusk);
        binding.timeDusk.setVisibility(View.VISIBLE);

        binding.timeSolarNoon.setText("The solar_noon time is " + solarNoon);
        binding.timeSolarNoon.setVisibility(View.VISIBLE);

        binding.timeGoldenHour.setText("The golden_hour time is " + goldenHour);
        binding.timeGoldenHour.setVisibility(View.VISIBLE);

        binding.dayLength.setText("The day_length is " + dayLength);
        binding.dayLength.setVisibility(View.VISIBLE);
    }

}