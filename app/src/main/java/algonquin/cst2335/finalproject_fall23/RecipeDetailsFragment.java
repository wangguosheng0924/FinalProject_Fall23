package algonquin.cst2335.finalproject_fall23;import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import algonquin.cst2335.finalproject_fall23.databinding.RecipeDetailsBinding;


public class RecipeDetailsFragment extends Fragment {
    Recipe selected;
    public  RecipeDetailsFragment(Recipe recipe){
        selected =recipe;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        RecipeDetailsBinding binding = RecipeDetailsBinding.inflate(inflater);
        binding.recipeDetailsTitle.setText(selected.getTitle());

        String imageUrl=selected.getImage();
        RequestQueue queue = null;
        queue = Volley.newRequestQueue(getActivity());
        ImageRequest imgReq2 = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                // Do something with loaded bitmap...

                try {
                    binding.recipeDetailsImage.setImageBitmap(bitmap);
                    Log.e("ImageRequest", " loading image: " + imageUrl);
                } catch (Exception e) {

                }

            }
        }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
            Log.e("ImageRequest", "Error loading image: " + error.getMessage());
        });
        String stringURL="https://api.spoonacular.com/recipes/"
                +selected.getWebsiteID()
                +"/information?apiKey=00939092e09741dd98d285edda1fc2ec";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                (response) -> {
                    try {
                        String summary=response.getString("summary");
                        Log.d("WeatherResponse", "summary: " + summary);
                        binding.recipeDetailsSummary.setText(summary);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                (error) -> {
                });
        queue.add(request);
        queue.add(imgReq2);

        return binding.getRoot();
    }
}