// omniactum-startupkit-server/src/main/java/com/omniactum/startupkit/modules/settings/controller/ThemeSettingsController.java
package com.omniactum.startupkit.modules.settings.controller;

import com.omniactum.startupkit.config.security.HasPermission;
import com.omniactum.startupkit.modules.settings.dto.ThemeSettingDTO;
import com.omniactum.startupkit.modules.settings.service.ThemeSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings/theme")
@RequiredArgsConstructor
public class ThemeSettingsController {

    private final ThemeSettingsService themeSettingsService;

    // Herkesin okuyabilmesi için @PreAuthorize yok
    @GetMapping
    public ResponseEntity<ThemeSettingDTO> getThemeSettings() {
        return ResponseEntity.ok(themeSettingsService.getThemeSettings());
    }

    // Sadece yazma yetkisi olanlar güncelleyebilir
    @PutMapping
    @HasPermission("PAGE_THEME_SETTINGS:WRITE")
    @PreAuthorize("hasAuthority('PAGE_THEME_SETTINGS:WRITE')")
    public ResponseEntity<ThemeSettingDTO> updateThemeSettings(@Valid @RequestBody ThemeSettingDTO dto) {
        return ResponseEntity.ok(themeSettingsService.updateThemeSettings(dto));
    }
}