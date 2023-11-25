package com.example.stcProject.Service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileService {

    void createNewFileInFolderInSpace(String spaceName, String folderName, MultipartFile file) throws Exception;

    Map<String, String> getFileMetadata(Long fileId);
}
