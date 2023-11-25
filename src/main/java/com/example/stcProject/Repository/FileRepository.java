package com.example.stcProject.Repository;

import com.example.stcProject.Model.Entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
