package com.example.stcProject.Service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Map;

public interface FileService {

    Path createNewFileInFolderInSpace(String spaceName, String folderName, MultipartFile file, String userEmail) throws Exception;

    Map<String, String> getFileMetadata(Long fileId);

    Path createNewFileInSpace(String spaceName, MultipartFile file, String userEmail) throws Exception;
}
