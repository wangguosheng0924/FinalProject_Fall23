package algonquin.cst2335.finalproject_fall23;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities =  {Location.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {

    //only one function:return the DAO object
    public abstract LocationDAO lDAO();//to access database
}
