package baseball.baseballDiary.common.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Base Profiles
 */
@Getter
@Slf4j
@Component
public class BaseProfiles {
    private boolean prod;
    private boolean local;
    private boolean dev;
    private final boolean apiProd = false;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @PostConstruct
    public void init() {
        if (StringUtils.hasText(activeProfile)) {
            this.local = activeProfile.contains("local");
            this.dev = activeProfile.contains("dev");
            this.prod = activeProfile.contains("prod");
        }
        log.debug("activeProfile : {}", activeProfile);
        log.info("prod : {}", prod);
        log.info("local : {}", local);
        log.info("dev : {}", dev);
    }
}