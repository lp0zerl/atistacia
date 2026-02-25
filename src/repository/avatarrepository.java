package com.example.demo.repository;

import com.example.demo.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    // Метод findAll(Pageable pageable) уже есть в JpaRepository
}