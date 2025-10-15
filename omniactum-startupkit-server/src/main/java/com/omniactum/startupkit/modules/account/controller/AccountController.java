package com.omniactum.startupkit.modules.account.controller;

import com.omniactum.startupkit.modules.iam.dto.ChangePasswordRequest;
import com.omniactum.startupkit.modules.iam.dto.UpdateProfileRequest;
import com.omniactum.startupkit.modules.iam.model.User;
import com.omniactum.startupkit.modules.iam.service.UserService;
import com.omniactum.startupkit.modules.storage.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    // Giriş yapmış kullanıcının bilgilerini getirir
    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile(Authentication authentication) {
        return userService.getUserByUsername(authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Giriş yapmış kullanıcının profilini günceller
    @PutMapping("/me")
    public ResponseEntity<User> updateMyProfile(Authentication authentication, @Valid @RequestBody UpdateProfileRequest request) {
        User updatedUser = userService.updateMyProfile(authentication.getName(), request);
        return ResponseEntity.ok(updatedUser);
    }

    // Giriş yapmış kullanıcının şifresini değiştirir
    @PostMapping("/change-password")
    public ResponseEntity<Void> changeMyPassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest request) {
        userService.changeMyPassword(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(Authentication authentication, @RequestParam("file") MultipartFile file) {
        String username = authentication.getName();
        String filePath = fileStorageService.storeFile(file, "avatars");
        userService.updateAvatarUrl(username, filePath);
        return ResponseEntity.ok(filePath);
    }
}