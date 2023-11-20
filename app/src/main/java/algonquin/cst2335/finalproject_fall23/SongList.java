package algonquin.cst2335.finalproject_fall23;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SongList {



    public long Id;
    protected String artist;

    protected ArrayList<String> songs;



    public SongList(){}

    public SongList(String a, ArrayList songlist) {
        artist = a;
        songs = songlist != null ? songlist : new ArrayList<>();
    }


}
