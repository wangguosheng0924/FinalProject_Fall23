package algonquin.cst2335.finalproject_fall23;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject_fall23.databinding.DetailsLayoutBinding;


/**
 * CollectionDetailsFragment is used to display the details of a selected song.
 *  * @author Lei Luo
 *  * @version 1.1
 */
public class CollectionDetailsFragment  extends Fragment {

    /** SongList object that holds the details of the song. */
    private SongList mySong;

    /**
     * Constructor for CollectionDetailsFragment.
     * @param toDisplay The SongList object containing the song details to be displayed.
     */
    public CollectionDetailsFragment(SongList toDisplay) {
        mySong = toDisplay;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
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