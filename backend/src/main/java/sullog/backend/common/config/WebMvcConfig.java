package sullog.backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sullog.backend.common.interceptor.MemberIdInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberIdInterceptor memberIdInterceptor;

    public WebMvcConfig(MemberIdInterceptor memberIdInterceptor) {
        this.memberIdInterceptor = memberIdInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberIdInterceptor);
    }
}
