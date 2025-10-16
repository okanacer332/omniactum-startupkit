// omniactum-startupkit-server/src/main/java/com/omniactum/startupkit/modules/settings/dto/ThemeSettingDTO.java
package com.omniactum.startupkit.modules.settings.dto;

import jakarta.validation.constraints.Pattern;

public record ThemeSettingDTO(
        @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Geçersiz hex renk kodu")
        String dashboardBackground,

        @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Geçersiz hex renk kodu")
        String primaryButton,

        @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Geçersiz hex renk kodu")
        String destructiveButton,

        @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Geçersiz hex renk kodu")
        String sidebarBackground
) {}