package com.example.yeneservice.FirebaseRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yeneservice.Models.LocationModel;

import java.util.List;

public class LocationRepModelView extends ViewModel implements LocationsRepository.OnFirestoreTaskComplete {

    private MutableLiveData<List<LocationModel>> locationListModelData = new MutableLiveData<>();

    public LiveData<List<LocationModel>> getLocationListModelData() {
        return locationListModelData;
    }

    private LocationsRepository firebaseRepository = new LocationsRepository(this);

    public LocationRepModelView() {
        firebaseRepository.getLocationData();
    }

    @Override
    public void quizListDataAdded(List<LocationModel> locationListModelsList) {
        locationListModelData.setValue(locationListModelsList);
    }

    @Override
    public void onError(Exception e) {

    }
}
