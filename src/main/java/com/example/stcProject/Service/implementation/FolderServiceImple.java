package com.example.stcProject.Service.implementation;

import com.example.stcProject.Enums.ItemType;
import com.example.stcProject.Enums.PermissionLevel;
import com.example.stcProject.Model.Entity.Item;
import com.example.stcProject.Model.Entity.Permission;
import com.example.stcProject.Model.Entity.PermissionGroup;
import com.example.stcProject.Repository.ItemRepository;
import com.example.stcProject.Repository.PermissionGroupRepository;
import com.example.stcProject.Repository.PermissionRepository;
import com.example.stcProject.Service.FolderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.example.stcProject.Service.implementation.SpaceServiceImple.BASE_DIRECTORY;

@Service
@AllArgsConstructor
public class FolderServiceImple implements FolderService {
    private ItemRepository itemRepository;
    private PermissionGroupRepository permissionGroupRepository;
    private PermissionRepository permissionRepository;

    @Override
    public void createNewFolderInSpace(String spaceName, String folderName) throws Exception {
        // Create the full path for the space directory
        Path spacePath = Paths.get(BASE_DIRECTORY, spaceName);

        // Check if the space directory exists
        if (Files.exists(spacePath) && Files.isDirectory(spacePath)) {
            // Create the full path for the new folder within the space
            Path folderPath = spacePath.resolve(folderName);

            // Create the folder if it doesn't exist
            if (!Files.exists(folderPath)) {
                try {
                    Files.createDirectories(folderPath);
                    System.out.println("Folder created in space: " + folderPath.toString());
                } catch (Exception e) {
                    System.err.println("Failed to create folder: " + e.getMessage());
                    throw new Exception("Failed to create folder");
                }
            } else {
                System.out.println("Folder already exists: " + folderPath.toString());
                throw new Exception("Folder already exists");

            }
        } else {
            System.out.println("Space directory not found: " + spacePath.toString());
            throw new Exception("Space directory not found");
        }

        var permGroup = new PermissionGroup();
        permGroup.setGroupName("notAdminGroup");
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
        item.setType(ItemType.FOLDER);
        item.setPermissionGroup(permGroup);
        itemRepository.save(item);

    }
}
