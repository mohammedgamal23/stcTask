package com.example.stcProject.Controller;

import com.example.stcProject.Model.Entity.File;
import com.example.stcProject.Repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class FileGraphController {

    private FileRepository fileRepository;

    @QueryMapping
    File getFileById(@Argument Long fileId){
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found for ID: " + fileId));
    }



}
