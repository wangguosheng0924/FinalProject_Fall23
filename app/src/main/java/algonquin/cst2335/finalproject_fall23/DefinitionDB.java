package algonquin.cst2335.finalproject_fall23;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities={Definition.class},version=1)
public abstract class DefinitionDB extends RoomDatabase{
    public abstract DefinitionDAO cmDAO();
}
