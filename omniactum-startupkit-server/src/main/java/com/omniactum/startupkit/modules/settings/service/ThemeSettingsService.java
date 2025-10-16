// omniactum-startupkit-server/src/main/java/com/omniactum/startupkit/modules/settings/service/ThemeSettingsService.java
package com.omniactum.startupkit.modules.settings.service;

import com.omniactum.startupkit.modules.settings.dto.ThemeSettingDTO;
import com.omniactum.startupkit.modules.settings.model.ThemeSetting;
import com.omniactum.startupkit.modules.settings.repository.ThemeSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThemeSettingsService {

    private final ThemeSettingRepository themeSettingRepository;

    // Mevcut ayarları getir veya varsayılanları döndür
    public ThemeSettingDTO getThemeSettings() {
        return themeSettingRepository.findFirstBy()
                .map(this::toDTO)
                .orElse(getDefaults());
    }

    // Ayarları güncelle (yoksa oluştur)
    public ThemeSettingDTO updateThemeSettings(ThemeSettingDTO dto) {
        ThemeSetting settings = themeSettingRepository.findFirstBy().orElse(new ThemeSetting());
        settings.setDashboardBackground(dto.dashboardBackground());
        settings.setPrimaryButton(dto.primaryButton());
        settings.setDestructiveButton(dto.destructiveButton());
        settings.setSidebarBackground(dto.sidebarBackground());

        ThemeSetting savedSettings = themeSettingRepository.save(settings);
        return toDTO(savedSettings);
    }

    private ThemeSettingDTO toDTO(ThemeSetting model) {
        return new ThemeSettingDTO(
                model.getDashboardBackground(),
                model.getPrimaryButton(),
                model.getDestructiveButton(),
                model.getSidebarBackground()
        );
    }

    // Frontend'deki varsayılan renkler
    private ThemeSettingDTO getDefaults() {
        return new ThemeSettingDTO("#f4f4f5", "#18181b", "#ef4444", "#ffffff");
    }
}