package com.omniactum.startupkit.modules.iam.service;

import com.omniactum.startupkit.config.security.HasPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order; // <-- YENİ IMPORT
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Order(0) // <-- BU ÇOK ÖNEMLİ! İLK ÇALIŞACAK OLAN BU.
@RequiredArgsConstructor
public class PermissionService implements ApplicationListener<ContextRefreshedEvent> {

    private final ApplicationContext context;
    private static final Set<String> SYSTEM_PERMISSIONS = ConcurrentHashMap.newKeySet();
    private boolean permissionsLoaded = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (permissionsLoaded) {
            return;
        }
        Map<String, Object> controllers = context.getBeansWithAnnotation(RestController.class);
        for (Object controller : controllers.values()) {
            Class<?> targetClass = controller.getClass();
            if (targetClass.getName().contains("$$SpringCGLIB$$") || targetClass.getName().contains("$$EnhancerBySpringCGLIB$$")) {
                targetClass = targetClass.getSuperclass();
            }
            if (!targetClass.getPackageName().startsWith("com.omniactum.startupkit.modules")) {
                continue;
            }
            for (Method method : targetClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(HasPermission.class)) {
                    SYSTEM_PERMISSIONS.add(method.getAnnotation(HasPermission.class).value());
                }
            }
        }
        System.out.println(">>> Sistemdeki dinamik yetkiler ApplicationListener ile yüklendi: " + SYSTEM_PERMISSIONS.size() + " adet.");
        permissionsLoaded = true;
    }

    public Set<String> getSystemPermissions() {
        return SYSTEM_PERMISSIONS;
    }

    // hasPermission metodu aynı kalıyor...
}