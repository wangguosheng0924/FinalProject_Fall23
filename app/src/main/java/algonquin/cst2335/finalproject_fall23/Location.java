package algonquin.cst2335.finalproject_fall23;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity //mapping variables to columns of the database
// Location class to represent individual Location
public class Location {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    long id;

    @ColumnInfo(name="LatitudeColumn")
    String latitude;
    @ColumnInfo(name="LongitudeColumn")
    String longitude;

    public  Location(){}
    // Constructor to initialize Location object
    public Location(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter method for message
    public String getLatitude() {
        return latitude;
    }

    // Getter method for timeSent
    public String getLongitude() {
        return longitude;
    }
}

