package com.hanibalg.yeneservice.patterns.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.patterns.ServiceListRepoitory;

import java.util.ArrayList;

public class ServiceListViewModel extends ViewModel {
    MutableLiveData<ArrayList<ServiceListModel>>liveData;

    public void init(Context context){
        if (liveData != null){
            return;
        }

        liveData = ServiceListRepoitory.getInstance(context).getServicesList();
    }

    public LiveData<ArrayList<ServiceListModel>> getServicesList(){
        return liveData;
    }
}
