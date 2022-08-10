package com.foodback.demo.controller;

import com.auth0.jwt.interfaces.Claim;
import com.foodback.demo.Mapper.UserMapper;
import com.foodback.demo.model.*;
import com.foodback.demo.service.UserService;
import com.foodback.demo.utils.Cons;
import com.foodback.demo.utils.JWTUtil;
import com.foodback.demo.utils.Md5Utils;
import com.foodback.demo.utils.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    /**
     * 注册
     * @param username
     * @param password
     * @param account
     * @param email
     * @param worktype
     */
    @PostMapping("/register")
    @ResponseBody
    public String register(String username, String password, String account, String email, String worktype, String location) {
//        String avator = "assets/user/default.jpg";
        User user = new User(username, account, Md5Utils.code(password),worktype,location,email);
        int register = userService.register(user);
        if(register > 0){
            return "success";
        }else {
            return "fail";
        }
    }

    /**
     * 检测用户账号是否重复
     * @param account
     * @return
     */
    @PostMapping("/accountDetection")
    @ResponseBody
    public String accountDetection(String account){
        boolean b = userService.accountDetection(account);
        if(b){
            return "ok";
        }else {
            return "exist";
        }
    }

    /**
     * 登录
     * @param account 账号
     * @param password  密码
     * @param code 验证码
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public HashMap<String,Object> login(String account, String password, String code, HttpServletRequest request){
        String generateCode = (String) request.getSession().getAttribute("code");
        //首先校验验证码，如果验证码错误则返回"验证码错误"，如果验证码正确且密码正确则返回"登录成功",否则返回"账号或者密码错误";
        HashMap<String,Object> result = new HashMap<>();
        HashMap<String,String> payload = new HashMap<>();
        if(generateCode.equals(code)){
            User user = userService.selectByAccount(account);
            if (user == null) {
                result.put(Cons.state,"账号或者密码错误");
                return result;
            }else {
                if(Md5Utils.code(password).equals(user.getPassword())){
                    result.put(Cons.username,user.getUsername());
                    result.put(Cons.account,user.getAccount());
                    result.put(Cons.worktype,user.getWorktype());
                    result.put(Cons.location,user.getLocation());
                    result.put(Cons.avator,user.getAvator());
//                    result.put(Cons.id,user.getId());
                    result.put(Cons.state,"登录成功");
                    payload.put(Cons.account,user.getAccount());
//                    payload.put(Cons.id,user.getId().toString());
                    payload.put(Cons.username,user.getUsername());
                    payload.put(Cons.worktype,user.getWorktype());
                    payload.put(Cons.location,user.getLocation());
                    payload.put(Cons.avator,user.getAvator());
                    String token = JWTUtil.getToken(payload);
                    result.put(Cons.token,token);
                    //将用户登录账号存到session,用于以后登录检测
                    request.getSession().setAttribute(Cons.account,user.getAccount());
                    return result;
                }else {
                    result.put(Cons.state,"账号或者密码错误");
                    return result;
                }
            }
        } else {
            result.put(Cons.state,"验证码错误");
            return result;
        }
    }

    /**
     * 获取登录时的验证码
     * @param session
     * @param response
     * @throws IOException
     */
    @GetMapping(path = "/getImage")
    @ResponseBody
    public void getImage(HttpSession session, HttpServletResponse response) throws IOException {
        //生成验证码
        String securityCode = VerifyCodeUtils.getSecurityCode();
        //将验证码放入session
        session.setAttribute("code",securityCode);
        //生成图片
        BufferedImage image = VerifyCodeUtils.createImage(securityCode);
        //输出图片
        ServletOutputStream outputStream = response.getOutputStream();
        //调用工具类
        ImageIO.write(image,"png",outputStream);
    }

    /**
     * 把用户的上链信息存储起来
     * @param account
     * @param num
     * @return
     */
    @PostMapping("/addUserGoods")
    @ResponseBody
    public String addUserGoods(String account,String num){
        UserGoods userGoods = new UserGoods(account,num);
        int i = userService.addGoods(userGoods);
        if(i > 0){
            return "success";
        }else {
            return "fail";
        }
    }

    /**
     * 存储风险商家信息
     * @param account
     * @param num
     * @param location
     * @return
     */
    @PostMapping("/addDangerUser")
    @ResponseBody
    public String addDangerUser(String account,String num,String location){
        DangerUser dangerUser = new DangerUser(account,num,location);
        int i = userService.addDangerUser(dangerUser);
        if(i > 0){
            return "success";
        }else {
            return "fail";
        }
    }

    /**
     * 把用户的查询历史记录存储起来
     * @param account
     * @param num
     * @param queryTime
     * @return
     */
    @PostMapping("/addQueryHistory")
    @ResponseBody
    public String addQueryHistory(String account,String num,String queryTime,String dState){
        QueryHistory queryHistory = new QueryHistory(account,num,queryTime,dState);
        int i = userService.addHistory(queryHistory);
        if(i > 0){
            return "success";
        }else {
            return "fail";
        }
    }

    /**
     * 获取用户查询的历史记录（分页查询）
     * @param account
     * @param current //当前页数
     * @param size //每页数量
     * @return
     */
    @PostMapping("/getQueryHistory")
    @ResponseBody
    public PageVo<QueryHistory> getQueryHistory(String account, Integer current, Integer size){
        return userService.getQueryHistory(account,current,size);
    }

    /**
     * 获取用户上传的商品记录（分页查询）
     * @param account
     * @param current
     * @param size
     * @return
     */
    @PostMapping("/getUserGoods")
    @ResponseBody
    public PageVo<UserGoods> getUserGoods(String account,Integer current,Integer size){
        return userService.getUserGoods(account,current,size);
    }

//    /**
//     * 获取统计数据
//     * @param
//     */
//    @GetMapping("/getCount")
//    @ResponseBody
//    public HashMap<String, Long> getCount(){
//        List<DangerUser> userList = userMapper.FindAllDangerUsers();
//        System.out.println(userList.size());
//        HashMap<String, Long> result = new HashMap<>();
//        result.put("Usernum",allUser);
//        result.put("Singernum",allSinger);
//        result.put("Songnum",allSong);
//        result.put("SongListnum",allSongList);
//        return result;
//    }

//    /**
//     * 验证token
//     * @param request
//     * @return
//     * /user/verifyToken
//     */
//    @PostMapping("/verifyToken")
//    @ResponseBody
//    public HashMap<String,Object> verifyToken(HttpServletRequest request){
//        String token = request.getHeader("token");
//        Map<String, Claim> payloadFromToken = JWTUtil.getPayloadFromToken(token);
//        HashMap<String, Object> map = new HashMap<>();
//        String account = payloadFromToken.get(Cons.account).asString();
//        User user = userMapper.queryUserByAccount(account);
//        map.put("userId",user.getId());
//        map.put("account",account);
//        map.put("avator",user.getAvator());
//        map.put("username",user.getUsername());
//        map.put("status","success");
//        return map;
//    }
}
