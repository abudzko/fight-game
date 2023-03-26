package budzko.backend.game.fight.app.config;

import budzko.backend.game.fight.app.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CommonBeanConfig {
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonUtils.jsonMapper();
    }
}
