package algonquin.cst2335.finalproject_fall23;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;
    @ColumnInfo(name="apiId")
    int apiId;
    @ColumnInfo(name="title")
    String title;
    @ColumnInfo(name="image")
    String image;

public Recipe(String title,String image,int apiId){
    this.apiId=apiId;
    this.title=title;
    this.image=image;
}
public int getApiId(){
    return apiId;
}

public String getTitle(){
    return title;
}

public String getImage(){
    return image;
}

}

