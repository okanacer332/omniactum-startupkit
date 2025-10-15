package com.omniactum.startupkit.modules.iam.controller;

import com.omniactum.startupkit.config.security.HasPermission;
import com.omniactum.startupkit.modules.iam.dto.CreateUserRequest;
import com.omniactum.startupkit.modules.iam.dto.UpdateUserRequest;
import com.omniactum.startupkit.modules.iam.model.User;
import com.omniactum.startupkit.modules.iam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // YENİ IMPORT
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/iam/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @HasPermission("PAGE_USERS:WRITE")
    @PreAuthorize("hasAuthority('PAGE_USERS:WRITE')")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User createdUser = userService.createUser(request);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping
    @HasPermission("PAGE_USERS:READ")
    @PreAuthorize("hasAuthority('PAGE_USERS:READ')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @HasPermission("PAGE_USERS:READ")
    @PreAuthorize("hasAuthority('PAGE_USERS:READ')")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @HasPermission("PAGE_USERS:WRITE")
    @PreAuthorize("hasAuthority('PAGE_USERS:WRITE')")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @HasPermission("PAGE_USERS:WRITE")
    @PreAuthorize("hasAuthority('PAGE_USERS:WRITE')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}