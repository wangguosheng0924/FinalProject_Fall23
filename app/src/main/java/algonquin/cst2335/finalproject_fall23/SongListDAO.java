package algonquin.cst2335.finalproject_fall23;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface SongListDAO {

    @Insert
    public long insertMessage(SongList m);

    @Query( "Select * from SongList;")
    public List<SongList> getAllMessages();

    //number of rows deleted
    @Delete
    void deleteMessage(SongList m);
}
