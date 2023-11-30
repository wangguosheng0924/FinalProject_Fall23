package algonquin.cst2335.finalproject_fall23;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Entity
public class SongList {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long Id;

    @ColumnInfo(name = "artist")
    public String artist;

    @ColumnInfo(name = "songTitle")
    public String songTitle;

    @ColumnInfo(name = "duration")
    public int duration;

    @ColumnInfo(name = "albumName")
    public String albumName;

    @ColumnInfo(name = "Collection")
    public String Collection;

    @ColumnInfo(name = "imageURL")
    public String imageURL;

    @ColumnInfo(name = "preview")
    public String preview;

    public SongList() {
    }

    public SongList(String artistN, String song,
                    int du, String aN,String url,String pre ) {
        artist = artistN;
        songTitle=song;

        duration=du;
        albumName=aN;
        Collection="my collection";
        imageURL=url;
        preview=pre;
    }

    public SongList(String artistN, String song,
                    int du, String aN,String CL,String url,String pre) {
        artist = artistN;
        songTitle=song;
        duration=du;
        albumName=aN;
        Collection=CL;
        imageURL=url;
        preview=pre;
    }


}
