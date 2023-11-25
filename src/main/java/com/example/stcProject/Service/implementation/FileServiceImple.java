package com.example.stcProject.Service.implementation;

import com.example.stcProject.Enums.ItemType;
import com.example.stcProject.Enums.PermissionLevel;
import com.example.stcProject.Model.Entity.File;
import com.example.stcProject.Model.Entity.Item;
import com.example.stcProject.Model.Entity.Permission;
import com.example.stcProject.Model.Entity.PermissionGroup;
import com.example.stcProject.Repository.FileRepository;
import com.example.stcProject.Repository.ItemRepository;
import com.example.stcProject.Repository.PermissionGroupRepository;
import com.example.stcProject.Repository.PermissionRepository;
import com.example.stcProject.Service.FileService;
import com.example.stcProject.util.TikaFileMetadataExtractor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.stcProject.Service.implementation.SpaceServiceImple.BASE_DIRECTORY;

@Service
@AllArgsConstructor
public class FileServiceImple implements FileService {
    private FileRepository fileRepository;
    private ItemRepository itemRepository;
    private PermissionRepository permissionRepository;
    private PermissionGroupRepository permissionGroupRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void createNewFileInFolderInSpace(String spaceName, String folderName, MultipartFile file) throws Exception {
        // Create the full path for the space directory
        Path spacePath = Paths.get(BASE_DIRECTORY, spaceName);

        // Check if the space directory exists
        if (!Files.exists(spacePath) && !Files.isDirectory(spacePath)) {
            System.out.println("Space directory not found: " + spacePath.toString());
            throw new Exception("Space directory not found");
        }

        if(!Files.exists(spacePath.resolve(folderName))){
            throw new Exception("folder not found");
        }

        var permGroup = new PermissionGroup();
        permGroup.setGroupName("notSoAdminGroup");
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
        item.setName(file.getOriginalFilename());
        item.setType(ItemType.FILE);
        item.setPermissionGroup(permGroup);
        itemRepository.save(item);

        File file1 = new File();
        file1.setBinary(file.getBytes());
        file1.setItem(item);
        fileRepository.save(file1);

    }

    @Override
    @Transactional
    public Map<String, String> getFileMetadata(Long fileId) {
        String nativeQuery = "SELECT * FROM file WHERE id = :fileId";

        Query query = entityManager.createNativeQuery(nativeQuery, File.class).setParameter("fileId", fileId);

        List<File> resultList = query.getResultList();

        return TikaFileMetadataExtractor.inferContentType(resultList.get(0).getBinary());
    }

    public byte[] getFileContentById(Long fileId) {
        Optional<File> file=  fileRepository.findById(fileId);
        return file.map(File::getBinary).orElse(null);
    }

}
