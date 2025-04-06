package com.backendmk4.legacybookbackend.Services;

import com.backendmk4.legacybookbackend.DTO.FamilyGroupRequest;
import com.backendmk4.legacybookbackend.Model.FamilyGroup;
import com.backendmk4.legacybookbackend.Model.FamilyGroupRepository;
import com.backendmk4.legacybookbackend.Model.User;
import com.backendmk4.legacybookbackend.Model.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class FamilyGroupService {

    private final FamilyGroupRepository familyGroupRepository;
    private final UserRepository userRepository;


    public FamilyGroupService(FamilyGroupRepository familyGroupRepository, UserRepository userRepository){
        this.familyGroupRepository = familyGroupRepository;
        this.userRepository = userRepository;
    }

    public List<FamilyGroup> getAllGroupsByUser(Long User_id){
        return familyGroupRepository.findByUsers_Id(User_id);
    }

    public List<FamilyGroup> getAllGroupsByName(String FamilyGroupName){
        return familyGroupRepository.findByFamilyName(FamilyGroupName);
    }

    @Transactional
    public void createFamilyGroup(FamilyGroupRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //TODO
        FamilyGroup group = new FamilyGroup();
        group.setFamilyName(request.getFamilyName());
        group.setUsers(List.of(user)); // Add the user

        familyGroupRepository.save(group);
    }

    public void deleteFamilyGroup(long id){
        familyGroupRepository.deleteFamilyGroup(id);
    }

    public void addMemberToFamilyGroup(long User_id, long FamilyGroupId){
        familyGroupRepository.addMemberToFamily(User_id, FamilyGroupId);
    }

    public void deleteMemberFromFamilyGroup(long User_id, long FamilyGroupId){
        familyGroupRepository.deleteMemberFromFamilyGroup(User_id, FamilyGroupId);
    }

}
