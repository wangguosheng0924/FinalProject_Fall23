package algonquin.cst2335.finalproject_fall23;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
@Dao
public interface RecipeDAO {
    @Insert
    long insertRecipe(Recipe r);
    @Query("Select * from Recipe;")
    List<Recipe> getAllRecipes();
    @Delete
    int deleteRecipe(Recipe r);


}
