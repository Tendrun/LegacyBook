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

/**
 * ViewModel odpowiedzialny za zarządzanie i udostępnianie postów w aplikacji.
 * Pozwala na pobieranie listy postów oraz liczbę postów z wykorzystaniem LiveData.
 */
public class SharedPostViewModel extends ViewModel {
    /**
     * LiveData przechowujące listę postów.
     */
    private final MutableLiveData<List<PostModel>> posts = new MutableLiveData<>();

    /**
     * Zwraca LiveData z listą postów.
     *
     * @return LiveData z listą postów
     */
    public LiveData<List<PostModel>> getPosts() {
        return posts;
    }

    /**
     * Pobiera posty z serwera przy użyciu podanego tokena autoryzacyjnego.
     *
     * @param authToken token autoryzacyjny użytkownika
     */
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

    /**
     * LiveData przechowujące liczbę postów.
     */
    private final MutableLiveData<Integer> postCount = new MutableLiveData<>(0);

    /**
     * Zwraca LiveData z liczbą postów.
     *
     * @return LiveData z liczbą postów
     */
    public LiveData<Integer> getPostCount() {
        return postCount;
    }

    /**
     * Aktualizuje liczbę postów.
     *
     * @param count nowa liczba postów
     */
    public void updatePostCount(int count) {
        postCount.setValue(count);
    }
}