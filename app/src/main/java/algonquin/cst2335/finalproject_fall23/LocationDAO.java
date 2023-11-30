package algonquin.cst2335.finalproject_fall23;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDAO {
    @Insert //id              //@Entity
    public long insertLocation(Location l);//for inserting, long is the new id

    @Query("Select * from Location;")//the SQL search
    public List<Location> getAllLocations();//for query

    @Delete //number of rows deleted
    public int deleteLocation(Location l);//delete the location by id(Primary Key)
}
