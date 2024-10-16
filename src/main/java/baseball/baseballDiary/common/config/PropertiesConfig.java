package baseball.baseballDiary.common.config;

import baseball.baseballDiary.common.property.BaseProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {

    @Bean(name = "baseballDiaryProperty")
    @ConfigurationProperties(prefix = "baseball.jwt")
    public BaseProperty getGroupwareProperty() {
        return new BaseProperty();
    }

}
