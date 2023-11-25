package com.example.stcProject.Service.implementation;

import com.example.stcProject.Enums.ItemType;
import com.example.stcProject.Enums.PermissionLevel;
import com.example.stcProject.Model.Entity.Item;
import com.example.stcProject.Model.Entity.Permission;
import com.example.stcProject.Model.Entity.PermissionGroup;
import com.example.stcProject.Repository.ItemRepository;
import com.example.stcProject.Repository.PermissionGroupRepository;
import com.example.stcProject.Repository.PermissionRepository;
import com.example.stcProject.Service.SpaceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@AllArgsConstructor
public class SpaceServiceImple implements SpaceService {
    private ItemRepository itemRepository;

    private PermissionGroupRepository permissionGroupRepository;

    private PermissionRepository permissionRepository;

    // absolute path
    public static final String BASE_DIRECTORY = "C:\\Users\\MyPC\\Desktop\\stc\\stcProject\\stcProject\\spaces";

    public void createNewSpaceDirectory(String spaceName) throws Exception {
        // Create the full path for the new space directory
        Path spacePath = Paths.get(BASE_DIRECTORY, spaceName);

        // Create the directory if it doesn't exist
        if (!Files.exists(spacePath)) {
            try {
                Files.createDirectories(spacePath);
                System.out.println("Space directory created: " + spacePath.toString());
            } catch (Exception e) {
                System.err.println("Failed to create space directory: " + e.getMessage());
            }
        } else {
            System.out.println("Space directory already exists: " + spacePath.toString());
            throw new Exception("Failed to create space directory because it already exists");
        }

        var permGroup = new PermissionGroup();
        permGroup.setGroupName("adminGroup");
        permissionGroupRepository.save(permGroup);

        var perm1 = new Permission();
        perm1.setUserEmail("user1");
        perm1.setPermissionLevel(PermissionLevel.VIEW);
        perm1.setGroup(permGroup);
        permissionRepository.save(perm1);


        var perm2 = new Permission();
        perm2.setUserEmail("user2");
        perm2.setPermissionLevel(PermissionLevel.EDIT);
        perm2.setGroup(permGroup);
        permissionRepository.save(perm2);



        var item = new Item();
        item.setName(spaceName);
        item.setType(ItemType.SPACE);
        item.setPermissionGroup(permGroup);
        itemRepository.save(item);
    }


}
