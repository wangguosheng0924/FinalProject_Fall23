package algonquin.cst2335.finalproject_fall23;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.ArrayList;

public class DefinitionViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Definition>> definitionsLocal = new MutableLiveData<>(); // <definition>
    public MutableLiveData<Definition> selectedDefinition= new MutableLiveData<>(); // <definition>ß

}
