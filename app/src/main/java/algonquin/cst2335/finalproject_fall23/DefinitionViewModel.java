package algonquin.cst2335.finalproject_fall23;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DefinitionViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Definition>> definitions = new MutableLiveData<>();

}
