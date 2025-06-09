package com.example.legacykeep.API;

import com.example.legacykeep.DTO.AddMemberRequest;
import com.example.legacykeep.DTO.AuthResponse;
import com.example.legacykeep.DTO.CreateGroupRequest;
import com.example.legacykeep.DTO.DeleteFamilyRequest;
import com.example.legacykeep.DTO.DeleteMemberRequest;
import com.example.legacykeep.DTO.FamilyGroup;
import com.example.legacykeep.DTO.LoginRequest;
import com.example.legacykeep.DTO.RegisterRequest;
import com.example.legacykeep.DTO.RegisterResponse;
import com.example.legacykeep.DTO.SetFamilyRoleRequest;
import com.example.legacykeep.DTO.SetRoleRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("/api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("/api/auth/hello")
    Call<String> getHello(@Header("Authorization") String token);

    @POST("/api/auth/CreateFamilyGroup")
    Call<ResponseBody> createFamilyGroup(@Header("Authorization") String token, @Body CreateGroupRequest request);

    @POST("/api/auth/AddMemberToFamilyGroup")
    Call<String> addMemberToFamilyGroup(@Header("Authorization") String token, @Body AddMemberRequest request);

    @POST("/api/auth/DeleteMemberToFamilyGroup")
    Call<String> deleteMemberFromFamilyGroup(@Header("Authorization") String token, @Body DeleteMemberRequest request);

    @POST("/api/auth/DeleteFamily")
    Call<String> deleteFamily(@Header("Authorization") String token, @Body DeleteFamilyRequest request);

    @POST("/api/auth/SetFamilyRole")
    Call<String> setFamilyRole(@Header("Authorization") String token, @Body SetFamilyRoleRequest request);

    @POST("/api/auth/SetRole")
    Call<String> setRole(@Header("Authorization") String token, @Body SetRoleRequest request);

    @GET("/api/auth/GetUserFamilies")
    Call<List<FamilyGroup>> getUserFamilies(@Header("Authorization") String token);

    @GET("/api/auth/GetFamilyGroupDetails")
    Call<FamilyGroup> getFamilyGroupDetails(@Header("Authorization") String token, @Query("groupId") long groupId);


}
