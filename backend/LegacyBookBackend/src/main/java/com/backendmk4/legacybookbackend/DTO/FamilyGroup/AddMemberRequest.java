package com.backendmk4.legacybookbackend.DTO.FamilyGroup;

import com.backendmk4.legacybookbackend.Model.UserGroupMembership;

public class AddMemberRequest {
    private String UserEmailToAdd;

    private long groupId;

    public long getGroupId(){ return groupId; }
}
