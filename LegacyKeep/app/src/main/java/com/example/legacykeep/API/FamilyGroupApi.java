package com.example.legacykeep.API;

import com.example.legacykeep.DTO.AddMemberRequest;
import com.example.legacykeep.DTO.CreateGroupRequest;
import com.example.legacykeep.DTO.DeleteFamilyRequest;
import com.example.legacykeep.DTO.DeleteMemberRequest;
import com.example.legacykeep.DTO.SetFamilyRoleRequest;
import com.example.legacykeep.DTO.SetRoleRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FamilyGroupApi {

    @POST("/api/auth/CreateFamilyGroup")
    Call<Void> createFamilyGroup(@Body CreateGroupRequest request);

    @POST("/api/auth/AddMemberToFamilyGroup")
    Call<Void> addMemberToFamilyGroup(@Body AddMemberRequest request);

    @POST("/api/auth/DeleteFamily")
    Call<Void> deleteFamily(@Body DeleteFamilyRequest request);

    @POST("/api/auth/SetFamilyRole")
    Call<Void> setFamilyRole(@Body SetFamilyRoleRequest request);

    @POST("/api/auth/SetRole")
    Call<Void> setRole(@Body SetRoleRequest request);

    @POST("/api/auth/DeleteMember")
    Call<Void> deleteMember(@Body DeleteMemberRequest request);
}