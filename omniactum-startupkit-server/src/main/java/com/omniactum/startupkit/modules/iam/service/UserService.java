package com.omniactum.startupkit.modules.iam.service;

import com.omniactum.startupkit.modules.audit.service.AuditLogService;
import com.omniactum.startupkit.modules.iam.dto.ChangePasswordRequest;
import com.omniactum.startupkit.modules.iam.dto.CreateUserRequest;
import com.omniactum.startupkit.modules.iam.dto.UpdateProfileRequest;
import com.omniactum.startupkit.modules.iam.dto.UpdateUserRequest;
import com.omniactum.startupkit.modules.iam.model.Role; // YENİ IMPORT
import com.omniactum.startupkit.modules.iam.model.User;
import com.omniactum.startupkit.modules.iam.repository.RoleRepository;
import com.omniactum.startupkit.modules.iam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors; // YENİ IMPORT

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    // ... (diğer metotlar aynı kalacak) ...

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")) {
            return "SYSTEM";
        }
        return authentication.getName();
    }

    public User createUser(CreateUserRequest request) {
        String generatedUsername = generateUsername(request.fullName());
        if (userRepository.existsByUsername(generatedUsername)) {
            throw new IllegalArgumentException("Bu kullanıcı adı zaten mevcut: " + generatedUsername);
        }
        User newUser = new User();
        newUser.setFullName(request.fullName());
        newUser.setUsername(generatedUsername);
        newUser.setEmail(request.email());
        newUser.setTenantId(request.tenantId());
        newUser.setPassword(passwordEncoder.encode("1234"));
        newUser.setRoleIds(request.roleIds());
        newUser.setActive(true);
        User savedUser = userRepository.save(newUser);
        auditLogService.logAction(getCurrentUsername(), "USER_CREATED", "Yeni kullanıcı oluşturuldu: " + savedUser.getUsername());
        return savedUser;
    }

    public User updateUser(String userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + userId));
        long foundRoles = roleRepository.countByIdIn(request.roleIds());
        if (foundRoles != request.roleIds().size()) {
            throw new IllegalArgumentException("Geçersiz veya bulunamayan rol ID'leri gönderildi.");
        }
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setActive(request.active());
        user.setRoleIds(request.roleIds());
        User updatedUser = userRepository.save(user);
        auditLogService.logAction(getCurrentUsername(), "USER_UPDATED", "Kullanıcı güncellendi: " + updatedUser.getUsername());
        return updatedUser;
    }

    public void deleteUser(String userId) {
        Optional<User> userToDelete = userRepository.findById(userId);
        if(userToDelete.isPresent()) {
            String username = userToDelete.get().getUsername();
            userRepository.deleteById(userId);
            auditLogService.logAction(getCurrentUsername(), "USER_DELETED", "Kullanıcı silindi: " + username);
        } else {
            throw new RuntimeException("Silinecek kullanıcı bulunamadı: " + userId);
        }
    }

    private String generateUsername(String fullName) {
        return fullName.trim().toLowerCase()
                .replace("ı", "i").replace("ğ", "g").replace("ü", "u")
                .replace("ş", "s").replace("ö", "o").replace("ç", "c")
                .replaceAll("\\s+", ".");
    }

    public List<User> getAllUsers() { return userRepository.findAll(); }
    public Optional<User> getUserById(String id) { return userRepository.findById(id); }

    public User updateMyProfile(String username, UpdateProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        user.setFullName(request.fullName());
        user.setEmail(request.email());

        User updatedUser = userRepository.save(user);
        auditLogService.logAction(username, "USER_PROFILE_UPDATED", "Kullanıcı kendi profilini güncelledi.");
        return updatedUser;
    }

    public void changeMyPassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mevcut şifre hatalı.");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        auditLogService.logAction(username, "USER_PASSWORD_CHANGED", "Kullanıcı kendi şifresini değiştirdi.");
    }

    // --- BURASI DEĞİŞİYOR ---
    public Optional<User> getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        userOptional.ifPresent(user -> {
            // Kullanıcının sahip olduğu rol ID'leri üzerinden rolleri bul.
            List<Role> roles = roleRepository.findAllById(user.getRoleIds());

            // Bu rollerdeki tüm yetkileri toplayıp, kullanıcının `permissions` set'ine ekle.
            Set<String> permissions = roles.stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .collect(Collectors.toSet());
            user.setPermissions(permissions);
        });

        return userOptional;
    }
    // --- DEĞİŞİKLİK BİTTİ ---

    public void updateAvatarUrl(String username, String avatarUrl) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
        auditLogService.logAction(username, "USER_AVATAR_UPDATED", "Kullanıcı profil fotoğrafını güncelledi.");
    }
}