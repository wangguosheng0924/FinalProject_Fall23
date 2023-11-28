package algonquin.cst2335.finalproject_fall23;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {SongList.class}, version = 2)
public abstract class PersonalSongListData extends RoomDatabase {



    public abstract SongListDAO cmDAO();
}




