// omniactum-startupkit-server/src/main/java/com/omniactum/startupkit/modules/settings/model/ThemeSetting.java
package com.omniactum.startupkit.modules.settings.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "theme_settings")
public class ThemeSetting {
    @Id
    private String id; // Genelde tek bir ayar dokümanı olacak
    private String dashboardBackground;
    private String primaryButton;
    private String destructiveButton;
    private String sidebarBackground;
}