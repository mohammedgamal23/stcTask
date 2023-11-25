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
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
    public Path createNewFileInFolderInSpace(String spaceName, String folderName, MultipartFile file, String userEmail) throws Exception {
        Permission userPermission = permissionRepository.findOneByUserEmail(userEmail);

        if (userPermission.getPermissionLevel().equals(PermissionLevel.VIEW)){
            throw new Exception("Permission Denied");
        }

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


        var item = new Item();
        item.setName(file.getOriginalFilename());
        item.setType(ItemType.FILE);
        item.setPermissionGroup(userPermission.getGroup());
        itemRepository.save(item);

        File file1 = new File();
        file1.setBinary(file.getBytes());
        file1.setItem(item);
        fileRepository.save(file1);

        Files.copy(file.getInputStream(), spacePath.resolve(folderName).resolve(file.getOriginalFilename()), REPLACE_EXISTING);

        return spacePath.resolve(folderName).resolve(file.getOriginalFilename());
    }

    @Override
    @Transactional
    public Map<String, String> getFileMetadata(Long fileId) {
        String nativeQuery = "SELECT * FROM file WHERE id = :fileId";

        Query query = entityManager.createNativeQuery(nativeQuery, File.class).setParameter("fileId", fileId);

        List<File> resultList = query.getResultList();

        return TikaFileMetadataExtractor.inferContentType(resultList.get(0).getBinary());
    }

    @Override
    public Path createNewFileInSpace(String spaceName, MultipartFile file, String userEmail) throws Exception {
        Permission userPermission = permissionRepository.findOneByUserEmail(userEmail);

        if (userPermission.getPermissionLevel().equals(PermissionLevel.VIEW)){
            throw new Exception("Permission Denied");
        }

        // Create the full path for the space directory
        Path spacePath = Paths.get(BASE_DIRECTORY, spaceName);

        // Check if the space directory exists
        if (!Files.exists(spacePath) && !Files.isDirectory(spacePath)) {
            System.out.println("Space directory not found: " + spacePath.toString());
            throw new Exception("Space directory not found");
        }

        var item = new Item();
        item.setName(file.getOriginalFilename());
        item.setType(ItemType.FILE);
        item.setPermissionGroup(userPermission.getGroup());
        itemRepository.save(item);

        File file1 = new File();
        file1.setBinary(file.getBytes());
        file1.setItem(item);
        fileRepository.save(file1);

        Files.copy(file.getInputStream(), spacePath.resolve(file.getOriginalFilename()), REPLACE_EXISTING);

        return spacePath.resolve(file.getOriginalFilename());

    }

    @Override
    public File getFileById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    public File getFileContentById(Long fileId) {
        Optional<File> file=  fileRepository.findById(fileId);
        return file.orElse(null);
    }

}
