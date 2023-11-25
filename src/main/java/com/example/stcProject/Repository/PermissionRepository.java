package com.example.stcProject.Repository;

import com.example.stcProject.Model.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
