package com.personal.controller;
import com.personal.entity.*;
import com.personal.security.utils.EncryUtil;
import com.personal.service.UserService;
import com.personal.utils.Response;
import com.personal.utils.StringUtil;
import com.personal.utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description
 * @author
 * @date 2020/11/24 18:55
 **/
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    /**
     * 登录失败
     */
    @RequestMapping("/login_error")
    public Response loginError(){
        Response response = new Response();
        response.buildSuccessResponse("登录失败");
        return response;
    }

    /**
     * 登录成功，跳转首页
     */
    @RequestMapping("/login_success")
    public String loginSuccess(){
        return "index";
    }


    /**
     * 首次登录，跳转
     */
    @RequestMapping("/firstlogin")
    public String firstlogin(){
        return "firstlogin";
    }


    /**
     * 登录失败或者退出，跳转到登陆首页
     */
    @RequestMapping("/login_page")
    public ModelAndView root(HttpServletRequest request){
        Object msg=request.getAttribute("msg");
        Map m = new HashMap();
        if(null!=msg){
            m.put("msg",msg.toString());
        }
        return new ModelAndView("login",m);
    }

    /**
     * 系统入口，进入登陆页
     */
    @RequestMapping("/")
    public String first(){
        return "login";
    }

    /**
     * 系统首页跳转
     */
    @RequestMapping("/index")
    public ModelAndView index(Map<String,Object> model, Principal principal, HttpServletRequest httpServletRequest){
        if(null==principal.getName()){
            return new ModelAndView("login");
        }else{
            model.put("user", userService.findByName(principal.getName()));
            return new ModelAndView("index",model);
        }

    }

    /**
     * 欢迎页面
     */
    @RequestMapping("/welcome")
    public ModelAndView welcome(){
        return new ModelAndView("welcome");
    }

    /**
     * 获取验证码
     */
    @RequestMapping("/getVerifyCode")
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> map = VerifyCodeUtil.getVerifyCode();
        HttpSession session = request.getSession();
        session.setAttribute(VerifyCodeUtil.SESSION_KEY, map.get(VerifyCodeUtil.SESSION_KEY));
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。
        try {
            ServletOutputStream sos = response.getOutputStream();
            ImageIO.write((RenderedImage) map.get(VerifyCodeUtil.BUFFIMG_KEY), "jpeg", sos);
            sos.close();
            //设置验证码过期时间
            VerifyCodeUtil.removeAttrbute(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 人员管理
     */
    @RequestMapping("/peoplemanage")
    public ModelAndView peoplemanage(Map<String,Object> model, Principal principal, HttpServletRequest httpServletRequest){
        if(null==principal.getName()){
            return new ModelAndView("login");
        }else {
            model.put("users", userService.queryUsers());
            return new ModelAndView("people", model);
        }
    }


    /**
     * 人员新增页面
     */
    @RequestMapping("/newuser")
    public ModelAndView newuser(Map<String,Object> model){
        User user = new User();
        user.setCode(String.valueOf(new Random().nextInt(10000000)));
        model.put("user",user);
        model.put("msg","");
        return new ModelAndView("peoplenew",model);
    }
    /**
     * 人员编辑前获取人员信息
     */
    @RequestMapping("/getuser/{username}")
    public ModelAndView getuser(Map<String,Object> model, Principal principal, HttpServletRequest httpServletRequest
            , @PathVariable String username){
        model.put("user",userService.findByName(username));
        return new ModelAndView("peopleedit",model);
    }
    /**
     * 删除人员
     */
    @RequestMapping("/deluser/{username}")
    public ModelAndView deluser(Map<String,Object> model, Principal principal, HttpServletRequest httpServletRequest
            , @PathVariable String username){
        int i = userService.deleteUser(username);
        model.put("msg",i==1?"删除（"+username+")成功！":"");
        return new ModelAndView("welcome",model);
    }
    /**
     * 人员修改
     */
    @RequestMapping("/edituser")
    public ModelAndView edituser(Map<String,Object> model, Principal principal, HttpServletRequest httpServletRequest,
                                 @RequestParam(value = "id") int id,
                                 @RequestParam(value = "realname") String realname,
                                 @RequestParam(value = "password") String password,
                                 @RequestParam(value = "phone") String phone,
                                 @RequestParam(value = "email") String email,
                                 @RequestParam(value = "license") String license,
                                 @RequestParam(value = "deadline",required = false) String deadline) throws ParseException {

        User u = new User();
        u.setId(id);
        u.setRealname(realname);
        u.setPassword(password);
        u.setPhone(phone);
        u.setEmail(email);
        u.setLicense(license);
        if(StringUtil.isNotEmpty(deadline)){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            u.setDeadline(format.parse(deadline));
        }
        int i = userService.updateUser(u);
        model.put("msg",i==1?"("+realname+")The personal information has been modified successfully!":"");
        return new ModelAndView("welcome",model);
    }

    /**
     * 增加人员
     */
    @RequestMapping("/adduser")
    public ModelAndView newuser(Map<String,Object> model, Principal principal, HttpServletRequest httpServletRequest,
                                @RequestParam(value = "code") String code,
                                @RequestParam(value = "username") String username,
                                @RequestParam(value = "realname") String realname,
                                @RequestParam(value = "password") String password,
                                @RequestParam(value = "phone") String phone,
                                @RequestParam(value = "email") String email,
                                @RequestParam(value = "address") String address){
        User t =userService.findByName(username);
        if(null!=t){
            model.put("user",t);
            model.put("msg",t.getUsername()+"has existed.");
            return new ModelAndView("peoplenew",model);
        }else if(StringUtil.isNullOrEmpty(username)||StringUtil.isNullOrEmpty(password)||StringUtil.isNullOrEmpty(realname)){
            model.put("msg","Incomplete information.");
            return new ModelAndView("peoplenew",model);
        }else {
            Date licenseDate = new Date();
            licenseDate.setMonth(licenseDate.getMonth()+1);//默认30天有效期
            int i = userService.insert(new User(code, username, password, phone, email, realname,address,licenseDate));
            model.put("msg", i == 1 ? "(" + realname + ")Personal information added successfully!" : "");
            return new ModelAndView("welcome", model);
        }
    }


    /**
     * 个人信息获取
     */
    @RequestMapping("/person")
    public ModelAndView person(Map<String,Object> model, Principal principal, HttpServletRequest httpServletRequest){
        User t = userService.findByName(principal.getName());
        model.put("user",t);
        return new ModelAndView("peopleedit",model);
    }
    /**
     * 注册页面
     */
    @RequestMapping("/register")
    public ModelAndView register(Map<String, Object> model) {
        return new ModelAndView("register", model);
    }
    /**
     * 注册提交
     * 验证license是否有效,无效则默认多久,有效则更新为有效期
     */
    @RequestMapping("/registeruser")
    public ModelAndView registeruser(Map<String, Object> model,
                                     @RequestParam(value = "username") String username,
                                     @RequestParam(value = "realname") String realname,
                                     @RequestParam(value = "password") String password,
                                     @RequestParam(value = "phone") String phone,
                                     @RequestParam(value = "email") String email,
                                     @RequestParam(value = "license") String license) {
        String id = UUID.randomUUID().toString();
        User t = userService.findByName(username);
        if (null != t) {
            model.put("msg", t.getUsername() + "has existent.");
            return new ModelAndView("register", model);
        } else if (StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password) || StringUtil.isNullOrEmpty(realname)) {
            model.put("msg", "信息不完整");
            return new ModelAndView("register", model);
        } else {
            String licenseDateStr = EncryUtil.decrypt(license,EncryUtil.publicKey);
            Date licenseDate = new Date();
            licenseDate.setMonth(licenseDate.getMonth()+1);//默认30天有效期
            if(!licenseDateStr.isEmpty()){
                try{
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    licenseDate =format.parse(licenseDateStr);
                }catch (Exception e){
                }
            }
            int i = userService.insert(new User(id, username, password, phone, email, realname, !licenseDateStr.isEmpty()?license:"",licenseDate));
            model.put("msg", i == 1 ? "(" + realname + ")User registration is successful!" : "");
            return new ModelAndView("login", model);
        }
    }
}
