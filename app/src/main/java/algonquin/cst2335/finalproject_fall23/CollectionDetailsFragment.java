package algonquin.cst2335.finalproject_fall23;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject_fall23.databinding.DetailsLayoutBinding;

public class CollectionDetailsFragment  extends Fragment {

    SongList mySong;

    public CollectionDetailsFragment(SongList toDisplay) {
        mySong = toDisplay;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding =
                DetailsLayoutBinding.inflate(getLayoutInflater());

        binding.albumName.setText(mySong.albumName);

        binding.duration.setText(String.valueOf(mySong.duration));

        binding.collection.setText(mySong.Collection);
        return binding.getRoot();
    }
}