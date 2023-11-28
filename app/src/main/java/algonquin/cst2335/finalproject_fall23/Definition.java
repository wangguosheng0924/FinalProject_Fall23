package algonquin.cst2335.finalproject_fall23;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
// the variables declared depend on the data extracted form the url. continued to be changed.
public class Definition {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    @ColumnInfo(name="term")
    protected String term;

    @ColumnInfo(name="definition")
    protected String definition;

    public Definition(){}

    Definition(int id,String term,String definition){
        this.id=id;
        this.term=term;
        this.definition=definition;
    }

    public int getId(){return id;}

    public String getTerm(){return term;}

    public void setTerm(String term){this.term=term;}

    public String getDefinition(){return definition;}

    public void setDefinition(String df){this.definition=df;}



}
