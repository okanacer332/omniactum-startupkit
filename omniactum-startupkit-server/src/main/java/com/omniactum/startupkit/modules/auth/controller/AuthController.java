package com.omniactum.startupkit.modules.auth.controller;

import com.omniactum.startupkit.modules.audit.service.AuditLogService;
import com.omniactum.startupkit.modules.auth.dto.AuthResponse;
import com.omniactum.startupkit.modules.auth.dto.LoginRequest;
import com.omniactum.startupkit.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuditLogService auditLogService;

    // Register endpoint'i kaldırıldı.
    // Kullanıcılar artık sadece admin tarafından IAM UserController üzerinden oluşturulacak.

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            auditLogService.logAction(authentication.getName(), "USER_LOGOUT_SUCCESS", "Kullanıcı başarıyla çıkış yaptı.");
        }
        return ResponseEntity.ok("Çıkış loglandı.");
    }
}