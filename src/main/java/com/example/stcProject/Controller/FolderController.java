package com.example.stcProject.Controller;

import com.example.stcProject.Service.implementation.FolderServiceImple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/folders")
public class FolderController {
    @Autowired
    private FolderServiceImple folderServiceImple;

    @PostMapping("/{spaceName}/{folderName}")
    public ResponseEntity<String> createNewFolderInSpace(@PathVariable String spaceName,
                                                         @PathVariable String folderName) {
        try {
            folderServiceImple.createNewFolderInSpace(spaceName, folderName);
            return ResponseEntity.ok("folder created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                    .body(e.getMessage());
        }
    }
}