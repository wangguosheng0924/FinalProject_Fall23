package algonquin.cst2335.finalproject_fall23;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface SongListDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public long insertMessage(SongList m);

    @Query("SELECT DISTINCT Id, songTitle, artist,duration,albumName," +
            "Collection," +
            "imageURL " +
            "FROM " +
            "SongList WHERE " +
            "songTitle " +
            "= :songTitle AND artist = :artist;")
    List<SongList> getMessagesBySongAndArtist(String songTitle, String artist);


    @Query( "Select * from SongList")
    public List<SongList> getAllMessages();

    //number of rows deleted
    @Delete
    void deleteMessage(SongList m);
}
