package algonquin.cst2335.finalproject_fall23;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SongViewModel extends ViewModel {
    public    ArrayList<SongInfo> artistSongs = new java.util.ArrayList<>();
    public MutableLiveData<SongInfo> selectedMessage = new MutableLiveData< >();
}