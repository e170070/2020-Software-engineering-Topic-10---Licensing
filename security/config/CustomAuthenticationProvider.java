package com.personal.security.config;

import com.personal.utils.VerifyCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 自定义SpringSecurity的认证器
 * @author
 * @date 2020/06/24 15:43
 **/
@Component
@Slf4j
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {//implements AuthenticationProvider {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //用户输入的用户名
        String username = authentication.getName();
        //用户输入的密码
        String password = authentication.getCredentials().toString();
        //通过CustomWebAuthenticationDetails获取用户输入的验证码信息
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
        String verifyCode = details.getVerifyCode();
        if(null == verifyCode || verifyCode.isEmpty()){
            log.warn("No verification code entered");
            throw new NullPointerException("No verification code entered");
        }
        //校验验证码
        if(!validateVerifyCode(verifyCode)){
            log.warn("Incorrect verification code");
            throw new DisabledException("Incorrect verification code");
        }
        //通过自定义的CustomUserDetailsService，以用户输入的用户名查询用户信息
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        //校验用户密码
        if(!userDetails.getPassword().equals(password)){
            log.warn("Wrong password");
            throw new BadCredentialsException("Wrong password");
        }
        //通过license对应的deadline对比当前时间来确认是否还在有效期
        if(new Date().compareTo(userDetails.getDeadline())>0){
            log.warn("用戶license过期");
            throw new DisabledException("用戶license过期");
        }
        //
        Object principalToReturn = userDetails;
        //将用户信息塞到SecurityContext中，方便获取当前用户信息
        return this.createSuccessAuthentication(principalToReturn, authentication, userDetails);
    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        return null;
    }

    /**
     * 验证用户输入的验证码
     * @param inputVerifyCode
     * @return
     */
    public boolean validateVerifyCode(String inputVerifyCode){
        //获取当前线程绑定的request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 这个VerifyCodeFactory.SESSION_KEY是在servlet中存入session的名字
        HttpSession session = request.getSession();
        String verifyCode = (String)session.getAttribute(VerifyCodeUtil.SESSION_KEY);
        if(null == verifyCode || verifyCode.isEmpty()){
            log.warn("The verification code has expired, please verify again");
            throw new DisabledException("The verification code has expired, please verify again");
        }
        // 不分区大小写
        verifyCode = verifyCode.toLowerCase();
        inputVerifyCode = inputVerifyCode.toLowerCase();

        log.info("验证码：{}, 用户输入：{}", verifyCode, inputVerifyCode);

        return verifyCode.equals(inputVerifyCode);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
