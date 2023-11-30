package algonquin.cst2335.finalproject_fall23;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Represents a song entity in the database with various attributes like artist, title, duration, etc.
 *  @author Lei Luo
 *  @version 1.1
 */
@Entity
public class SongList {
    /** Unique identifier for the song. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long Id;
    /** Artist of the song. */
    @ColumnInfo(name = "artist")
    public String artist;
    /** Title of the song. */
    @ColumnInfo(name = "songTitle")
    public String songTitle;

    /** Duration of the song in seconds. */

    @ColumnInfo(name = "duration")
    public int duration;
    /** Name of the album the song belongs to. */
    @ColumnInfo(name = "albumName")
    public String albumName;
    /** Collection to which the song belongs. */
    @ColumnInfo(name = "Collection")
    public String Collection;

    /** URL for the song's image. */
    @ColumnInfo(name = "imageURL")
    public String imageURL;

    /** URL for the song's audio preview. */
    @ColumnInfo(name = "preview")
    public String preview;
    /**
     * Default constructor.
     */
    public SongList() {
    }

    /**
     * Constructor for SongList with default collection name.
     * @param artistN Artist of the song.
     * @param song Title of the song.
     * @param du Duration of the song.
     * @param aN Album name of the song.
     * @param url Image URL of the song.
     * @param pre Preview URL of the song.
     */
    public SongList(String artistN, String song,
                    int du, String aN, String url, String pre) {
        artist = artistN;
        songTitle = song;

        duration = du;
        albumName = aN;
        Collection = "my collection";
        imageURL = url;
        preview = pre;
    }

    /**
     * Constructor for SongList with specified collection name.
     * @param artistN Artist of the song.
     * @param song Title of the song.
     * @param du Duration of the song.
     * @param aN Album name of the song.
     * @param CL Collection name of the song.
     * @param url Image URL of the song.
     * @param pre Preview URL of the song.
     */

    public SongList(String artistN, String song,
                    int du, String aN, String CL, String url, String pre) {
        artist = artistN;
        songTitle = song;
        duration = du;
        albumName = aN;
        Collection = CL;
        imageURL = url;
        preview = pre;
    }


}
