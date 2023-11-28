package algonquin.cst2335.finalproject_fall23;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Entity //mapping variables to columns
public class SongList {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long Id;

    @ColumnInfo(name = "artist")
    protected String artist;

    @ColumnInfo(name = "songTitle")
    String songTitle;



    @ColumnInfo(name = "duration")
    int duration;

    @ColumnInfo(name = "albumName")
    String albumName;

    @ColumnInfo(name = "Collection")
    protected String Collection;


    public SongList() {
    }

    public SongList(String artistN, String song,
                    int du, String aN) {
        artist = artistN;
        songTitle=song;

        duration=du;
        albumName=aN;
        Collection="my collection";
    }

    public SongList(String artistN, String song,
                    int du, String aN,String CL) {
        artist = artistN;
        songTitle=song;
        duration=du;
        albumName=aN;
        Collection=CL;
    }


}
