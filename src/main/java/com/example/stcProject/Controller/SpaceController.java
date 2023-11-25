package com.example.stcProject.Controller;

import com.example.stcProject.Service.implementation.SpaceServiceImple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spaces")
public class SpaceController {
    @Autowired
    private SpaceServiceImple spaceServiceImple;

    @PostMapping("/{spaceName}")
    public ResponseEntity<String> createSpace(@PathVariable String spaceName) {
        try {
            spaceServiceImple.createNewSpaceDirectory(spaceName);
            return ResponseEntity.ok("Space directory created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                    .body(e.getMessage());
        }
    }

}
