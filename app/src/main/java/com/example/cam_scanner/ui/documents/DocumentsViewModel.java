package com.example.cam_scanner.ui.documents;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DocumentsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DocumentsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is documents fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}