package com.example.legacykeep.API;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Klasa pomocnicza do konfiguracji i udostępniania klienta API opartego o Retrofit.
 * Umożliwia uzyskanie instancji serwisu API oraz bazowego URL.
 */
public class ApiClient {
    /** Bazowy adres URL serwera API */
    private static final String BASE_URL = "http://10.0.2.2:8080";
    /** Instancja Retrofit używana do komunikacji z API */
    private static Retrofit retrofit;

    /**
     * Zwraca instancję serwisu API.
     * Tworzy nową instancję Retrofit, jeśli jeszcze nie została utworzona.
     * @return instancja ApiService do wykonywania zapytań sieciowych
     */
    public static ApiService getApiService() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    /**
     * Zwraca bazowy adres URL serwera API.
     * @return bazowy URL jako String
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }
}