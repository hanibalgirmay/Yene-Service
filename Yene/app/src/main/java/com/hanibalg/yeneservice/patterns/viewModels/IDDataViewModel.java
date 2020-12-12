package com.hanibalg.yeneservice.patterns.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hanibalg.yeneservice.models.IDIdentification;
import com.hanibalg.yeneservice.patterns.IDRepository;

public class IDDataViewModel extends ViewModel {
    MutableLiveData<IDIdentification> liveData;

    public void init(Context context){
        if (liveData != null){
            return;
        }

        liveData = IDRepository.getInstance(context).getIDInfo();
    }
    public LiveData<IDIdentification> getIDInfo(){
        return liveData;
    }
}
