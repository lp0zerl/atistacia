package com.example.demo.controller;

import com.example.demo.entity.Avatar;
import com.example.demo.service.AvatarService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/avatars")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping
    public ResponseEntity<Page<Avatar>> getAllAvatars(
            @RequestParam int page,
            @RequestParam int size) {
        return ResponseEntity.ok(avatarService.getAvatars(page, size));
    }
}