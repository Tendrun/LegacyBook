package com.example.legacykeep.API;

import com.example.legacykeep.DTO.*;
import com.example.legacykeep.model.PostModel;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Interfejs Retrofit do komunikacji z backendem aplikacji LegacyKeep.
 */
public interface ApiService {

    /**
     * Logowanie użytkownika.
     * @param request dane logowania
     * @return odpowiedź z tokenem autoryzacyjnym
     */
    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    /**
     * Rejestracja nowego użytkownika.
     * @param request dane rejestracyjne
     * @return odpowiedź z informacją o rejestracji
     */
    @POST("/api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    /**
     * Testowy endpoint autoryzacyjny.
     * @param token token JWT
     * @return wiadomość powitalna
     */
    @GET("/api/auth/hello")
    Call<String> getHello(@Header("Authorization") String token);

    /**
     * Tworzenie nowej grupy rodzinnej.
     * @param token token JWT
     * @param request dane grupy
     * @return odpowiedź HTTP
     */
    @POST("/api/auth/CreateFamilyGroup")
    Call<ResponseBody> createFamilyGroup(@Header("Authorization") String token, @Body CreateGroupRequest request);

    /**
     * Dodanie członka do grupy rodzinnej.
     * @param token token JWT
     * @param request dane członka
     * @return odpowiedź tekstowa
     */
    @POST("/api/auth/AddMemberToFamilyGroup")
    Call<String> addMemberToFamilyGroup(@Header("Authorization") String token, @Body AddMemberRequest request);

    /**
     * Usunięcie członka z grupy rodzinnej.
     * @param token token JWT
     * @param request dane członka do usunięcia
     * @return odpowiedź tekstowa
     */
    @POST("/api/auth/DeleteMemberToFamilyGroup")
    Call<String> deleteMemberFromFamilyGroup(@Header("Authorization") String token, @Body DeleteMemberRequest request);

    /**
     * Usunięcie grupy rodzinnej.
     * @param token token JWT
     * @param request dane grupy do usunięcia
     * @return odpowiedź tekstowa
     */
    @POST("/api/auth/DeleteFamily")
    Call<String> deleteFamily(@Header("Authorization") String token, @Body DeleteFamilyRequest request);

    /**
     * Ustawienie roli w rodzinie.
     * @param token token JWT
     * @param request dane roli
     * @return odpowiedź tekstowa
     */
    @POST("/api/auth/SetFamilyRole")
    Call<String> setFamilyRole(@Header("Authorization") String token, @Body SetFamilyRoleRequest request);

    /**
     * Ustawienie roli użytkownika.
     * @param token token JWT
     * @param request dane roli
     * @return odpowiedź tekstowa
     */
    @POST("/api/auth/SetRole")
    Call<String> setRole(@Header("Authorization") String token, @Body SetRoleRequest request);

    /**
     * Pobranie listy grup rodzinnych użytkownika.
     * @param token token JWT
     * @return lista grup rodzinnych
     */
    @GET("/api/auth/GetUserFamilies")
    Call<List<FamilyGroup>> getUserFamilies(@Header("Authorization") String token);

    /**
     * Pobranie szczegółów grupy rodzinnej.
     * @param token token JWT
     * @param groupId identyfikator grupy
     * @return szczegóły grupy rodzinnej
     */
    @GET("/api/auth/GetFamilyGroupDetails")
    Call<FamilyGroup> getFamilyGroupDetails(@Header("Authorization") String token, @Query("groupId") long groupId);

    /**
     * Aktualizacja zdjęcia profilowego użytkownika.
     * @param token token JWT
     * @param profilePicture plik zdjęcia
     * @return odpowiedź tekstowa
     */
    @Multipart
    @POST("/api/auth/updateProfilePicture")
    Call<String> updateProfilePicture(
            @Header("Authorization") String token,
            @Part MultipartBody.Part profilePicture
    );

    /**
     * Utworzenie nowego posta z obrazem i audio.
     * @param token token JWT
     * @param postRequest dane posta
     * @param image plik obrazu
     * @param audio plik audio
     * @return utworzony post
     */
    @Multipart
    @POST("/api/posts")
    Call<PostModel> createPost(
            @Header("Authorization") String token,
            @Part("post") RequestBody postRequest,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part audio
    );

    /**
     * Pobranie wszystkich postów.
     * @param token token JWT
     * @return lista postów
     */
    @GET("/api/posts")
    Call<List<PostModel>> getPosts(@Header("Authorization") String token);

    /**
     * Pobranie profilu użytkownika.
     * @param token token JWT
     * @return dane profilu użytkownika
     */
    @GET("/api/auth/GetUserProfile")
    Call<UserProfileDTO> getUserProfile(@Header("Authorization") String token);

    /**
     * Resetowanie hasła użytkownika.
     * @param request mapa z danymi do resetu hasła (np. email)
     * @return odpowiedź tekstowa
     */
    @POST("/api/auth/resetPassword")
    Call<String> resetPassword(@Body Map<String, String> request);
}