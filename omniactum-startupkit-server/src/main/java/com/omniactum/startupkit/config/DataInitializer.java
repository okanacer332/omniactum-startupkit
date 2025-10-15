package com.omniactum.startupkit.config;

import com.omniactum.startupkit.modules.iam.model.Role;
import com.omniactum.startupkit.modules.iam.model.User;
import com.omniactum.startupkit.modules.iam.repository.RoleRepository;
import com.omniactum.startupkit.modules.iam.repository.UserRepository;
import com.omniactum.startupkit.modules.iam.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener; // <-- DEĞİŞTİ
import org.springframework.context.event.ContextRefreshedEvent; // <-- DEĞİŞTİ
import org.springframework.core.annotation.Order; // <-- YENİ IMPORT
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(1) // <-- BU ÇOK ÖNEMLİ! İKİNCİ ÇALIŞACAK OLAN BU.
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionService permissionService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // ADMIN rolü yoksa oluştur
        roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role newAdminRole = new Role("ADMIN");
            Set<String> allPermissions = permissionService.getSystemPermissions();

            if (allPermissions.isEmpty()) {
                System.out.println(">>> UYARI: Hiçbir yetki bulunamadı, 'ADMIN' rolü boş oluşturuluyor.");
            } else {
                System.out.println(">>> 'ADMIN' rolüne " + allPermissions.size() + " adet yetki ekleniyor.");
            }

            newAdminRole.setPermissions(allPermissions);
            Role adminRole = roleRepository.save(newAdminRole);
            System.out.println(">>> Varsayılan 'ADMIN' rolü tüm yetkilerle oluşturuldu.");

            // admin kullanıcısı yoksa oluştur
            if (!userRepository.existsByUsername("admin")) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin"));
                adminUser.setFullName("Admin");
                adminUser.setEmail("admin@omniactum.com");
                adminUser.setRoleIds(Set.of(adminRole.getId()));
                adminUser.setActive(true);
                adminUser.setTenantId("SYSTEM");
                userRepository.save(adminUser);
                System.out.println(">>> Varsayılan 'admin' kullanıcısı oluşturuldu.");
            }
            return adminRole;
        });
    }
}