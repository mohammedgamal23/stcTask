package com.example.stcProject.Controller;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
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
                                     @RequestParam("file") MultipartFile file) throws Exception {
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path(BASE_DIRECTORY)
//                .path(spaceName).path(folderName)
//                .toUriString();
        try {
            Path path = fileServiceImple.createNewFileInFolderInSpace(spaceName, folderName, file);
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
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        // Get file content as a byte array
        byte[] fileContent = fileServiceImple.getFileContentById(fileId);

        // Create a ByteArrayResource with file content
        ByteArrayResource resource = new ByteArrayResource(fileContent);

        // Set content disposition header for download
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file_" + fileId + ".bin");

        // Set content type
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // Return ResponseEntity with ByteArrayResource and headers
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileContent.length)
                .body(resource);
    }

}
