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

    @ColumnInfo(name = "albumCover")
    String albumCover;

    @ColumnInfo(name = "duration")
    String duration;

    @ColumnInfo(name = "albumName")
    String albumName;


    public SongList() {
    }



    public SongList(String artistN, String song,String albumCoverPath, String du, String aN) {
        artist = artistN;
        songTitle=song;
        albumCover=albumCoverPath;
        duration=du;
        albumName=aN;
    }


}
