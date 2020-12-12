package com.personal.controller;

import com.personal.entity.User;
import com.personal.security.utils.EncryUtil;
import com.personal.service.UserService;
import com.personal.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @Description
 * @author
 * @date 2020/11/24 18:55
 **/
@Controller
public class LicenseController {
    @Autowired
    private UserService userService;


    /**
     * 登录认证license，跳转去认证
     */
    @RequestMapping("/license")
    public ModelAndView license(Map<String,Object> model, Principal principal){
        model.put("msg", "");
        return new ModelAndView("license",model);
    }
    /**
     * 跳转去sale
     */
    @RequestMapping("/sale")
    public String license(){
        return "sale";
    }

    @RequestMapping("/goshop/{money}")
    public ModelAndView goshop(Map<String,Object> model, Principal principal, @PathVariable(value = "money")Integer money){
        model.put("money", String.valueOf(money));
        return new ModelAndView("shop",model);
    }


    /**
     * 去商店，跳转去shop
     */
    @RequestMapping("/shop")
    public String shop(){
        return "shop";
    }
    @RequestMapping("/buylicense/{money}")
    public ResponseEntity buylicense(@PathVariable(value = "money") String money){
        int addMonth =0;
        if("30".equals(money)){
            addMonth = 3;
        }else if("60".equals(money)){
            addMonth = 6;
        }else if("120".equals(money)){
            addMonth = 12;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date deadline = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());   //设置当前时间
        cal.add(Calendar.MONTH, addMonth);  //购买一次有效期3个月 6个月 12个月
        String cc =format.format(cal.getTime());
        return ResponseEntity.ok(EncryUtil.encrypt(cc,EncryUtil.publicKey));
    }
    /**
     * 提价license认证
     */
    @RequestMapping("/updatelicense")
    public ModelAndView updatelicense(Map<String,Object> model, Principal principal, HttpServletRequest httpServletRequest,
                                      @RequestParam(value = "username") String username,
                                      @RequestParam(value = "password") String password,
                                      @RequestParam(value = "license") String license) throws ParseException {
        if(StringUtil.isNullOrEmpty(username)||StringUtil.isNullOrEmpty(password)||StringUtil.isNullOrEmpty(license)){
            model.put("msg","Information cannot be empty!");
            return new ModelAndView("license",model);
        }
        User user  =userService.findByName(username);
        if(!password.equals(user.getPassword())){
            model.put("msg","Incorrect username or password!");
            return new ModelAndView("license",model);
        }
        String deadline = EncryUtil.decrypt(license,EncryUtil.publicKey);
        if(StringUtil.isNullOrEmpty(deadline)){
            model.put("msg","Invalid license!");
            return new ModelAndView("license",model);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        user.setLicense(license);
        user.setDeadline(df.parse(deadline));
        userService.updateUser(user);
        model.put("msg","The license update was successful! Please log in again.");
        return new ModelAndView("login",model);
    }

}
