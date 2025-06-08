package com.example.legacykeep.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.legacykeep.model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class SharedPostViewModel extends ViewModel {
    private final MutableLiveData<List<PostModel>> postsLiveData = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<PostModel>> getPosts() {
        return postsLiveData;
    }

    public void addPost(PostModel post) {
        List<PostModel> current = postsLiveData.getValue();
        current.add(0, post);
        postsLiveData.setValue(current);
    }
}
