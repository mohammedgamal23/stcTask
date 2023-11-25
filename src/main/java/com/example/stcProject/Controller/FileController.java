package com.example.stcProject.Controller;

import com.example.stcProject.Model.Entity.File;
import com.example.stcProject.Service.implementation.FileServiceImple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileServiceImple fileServiceImple;

    @PostMapping("/{spaceName}/{folderName}/upload")
    public ResponseEntity<String> upload(@PathVariable String spaceName,
                                         @PathVariable String folderName,
                                         @RequestParam("file") MultipartFile file,
                                         @RequestParam String userEmail) throws Exception {
        try {
            Path path = fileServiceImple.createNewFileInFolderInSpace(spaceName, folderName, file, userEmail);
            return ResponseEntity.ok(path.toString());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                    .body(e.getMessage());

        }
    }

    @PostMapping("/{spaceName}/upload")
    public ResponseEntity<String> upload(@PathVariable String spaceName,
                                         @RequestParam("file") MultipartFile file,
                                         @RequestParam String userEmail) throws Exception {
        try {
            Path path = fileServiceImple.createNewFileInSpace(spaceName, file, userEmail);
            return ResponseEntity.ok(path.toString());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                    .body(e.getMessage());

        }
    }

    @GetMapping("/{fileId}/metadata")
    public ResponseEntity<Map<String, String>> getFileMetadata(@PathVariable Long fileId) {
        Map<String, String> fileMetadata = fileServiceImple.getFileMetadata(fileId);
        if (fileMetadata != null) {
            return ResponseEntity.ok(fileMetadata);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<?> downloadFile(@PathVariable Long fileId) {

        File file = fileServiceImple.getFileContentById(fileId);
        byte[] fileContent =file.getBinary();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileServiceImple.getFileMetadata(fileId).get("Content-Type")))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getItem().getName() + "\"")
                .body(fileContent);
    }

}
