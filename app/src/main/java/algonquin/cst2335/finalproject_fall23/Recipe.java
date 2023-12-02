package algonquin.cst2335.finalproject_fall23;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
//test
@Entity
public class Recipe {
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    public int id;
    @ColumnInfo(name="Title")
    protected String title;
    @ColumnInfo(name="Image")
    protected String image;
    @ColumnInfo(name="websiteID")
    protected int websiteID;

    public Recipe(String title, String image, int websiteID)
    {
        this.title = title;
        this.image = image;
        this.websiteID = websiteID;
    }

    public String getTitle() {
        return title;
    }
    public String getImage(){
        return image;
    }
    public int getWebsiteID(){
        return websiteID;
    }
}