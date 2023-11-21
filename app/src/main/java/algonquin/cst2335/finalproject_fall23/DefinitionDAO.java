package algonquin.cst2335.finalproject_fall23;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DefinitionDAO {
    @Query("SELECT * FROM Definition WHERE term = :searchTerm")
    List<Definition> searchDefinitions(String searchTerm);

    @Insert
    void insertDefinition(Definition definition);

    @Delete
    void deleteDefinition(Definition definition);

    @Query("SELECT * FROM Definition")
    List<Definition> getAllDefinitions();
}
