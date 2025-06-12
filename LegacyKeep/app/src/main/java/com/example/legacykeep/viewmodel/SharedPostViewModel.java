// Android: SharedPostViewModel.java
package com.example.legacykeep.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.model.PostModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharedPostViewModel extends ViewModel {
    private final MutableLiveData<List<PostModel>> posts = new MutableLiveData<>();

    public LiveData<List<PostModel>> getPosts() {
        return posts;
    }

    public void fetchPosts(String authToken) {
        ApiService apiService = ApiClient.getApiService();
        Call<List<PostModel>> call = apiService.getPosts("Bearer " + authToken);

        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful()) {
                    posts.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private final MutableLiveData<Integer> postCount = new MutableLiveData<>(0);

    public LiveData<Integer> getPostCount() {
        return postCount;
    }

    public void updatePostCount(int count) {
        postCount.setValue(count);
    }
}