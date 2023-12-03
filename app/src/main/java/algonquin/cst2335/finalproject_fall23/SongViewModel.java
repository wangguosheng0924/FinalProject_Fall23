package algonquin.cst2335.finalproject_fall23;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
/**
 * ViewModel for managing and storing song data throughout the lifecycle of the application.
 * This ViewModel holds a list of songs and the currently selected song.
 * *  @author Lei Luo
 *  *  @version 1.1
 */
public class SongViewModel extends ViewModel {

    /** ArrayList to store a list of songs for an artist. */
    public    ArrayList<SongList> artistSongs = new java.util.ArrayList<>();


    /** LiveData to hold and observe changes to the currently selected song. */
    public MutableLiveData<SongList> selectedMessage = new MutableLiveData< >();
}