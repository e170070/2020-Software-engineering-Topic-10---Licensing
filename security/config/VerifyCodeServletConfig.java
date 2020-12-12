package com.personal.security.config;

import com.personal.security.utils.VerifyCodeFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 描述：验证码servlet配置项
 *
 * @author
 * @date 2020/06/24 15:43
 **/
//@Configuration
public class VerifyCodeServletConfig {
    @Bean
    public ServletRegistrationBean indexServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new VerifyCodeFactory());
        registration.addUrlMappings("/getVerifyCode");
        return registration;
    }
}
