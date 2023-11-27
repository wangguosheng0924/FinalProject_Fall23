package algonquin.cst2335.finalproject_fall23;

import androidx.room.Database;
import androidx.room.RoomDatabase;

    @Database(entities = {Recipe.class}, version = 1)
    public  abstract class RecipeDatabase extends RoomDatabase{
        public abstract RecipeDAO rDAO();
    }


