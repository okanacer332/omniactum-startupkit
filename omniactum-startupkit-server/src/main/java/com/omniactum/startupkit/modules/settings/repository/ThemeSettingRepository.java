// omniactum-startupkit-server/src/main/java/com/omniactum/startupkit/modules/settings/repository/ThemeSettingRepository.java
package com.omniactum.startupkit.modules.settings.repository;

import com.omniactum.startupkit.modules.settings.model.ThemeSetting;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ThemeSettingRepository extends MongoRepository<ThemeSetting, String> {
    // Genelde tek bir ayar dokümanı olacağı için ilkini bulmak yeterli olacaktır.
    Optional<ThemeSetting> findFirstBy();
}