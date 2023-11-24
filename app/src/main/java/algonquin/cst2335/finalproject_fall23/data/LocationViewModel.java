package algonquin.cst2335.finalproject_fall23.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.finalproject_fall23.Location;

/*
A ViewModel is a part of the Android Architecture Components and is designed to store and manage UI-related data.
It is responsible for preparing and managing the data for an Activity or a Fragment and
also handles the communication between the UI and the underlying data repository (such as a database).
 */
public class LocationViewModel extends ViewModel {
    // ArrayList to store Location objects
    public ArrayList<Location> theLocation = new ArrayList<>();
    // LiveData to hold the currently selected Location.
    // This LiveData is used to communicate the selected Location between different components,
    // such as the activity and its fragments, and survives configuration changes like screen rotations.
    public MutableLiveData<Location> selectedLocation = new MutableLiveData< >();

}
