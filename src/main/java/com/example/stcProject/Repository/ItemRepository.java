package com.example.stcProject.Repository;

import com.example.stcProject.Model.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
