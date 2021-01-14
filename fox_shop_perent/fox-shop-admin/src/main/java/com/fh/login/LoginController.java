package com.fh.login;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fh.admin.entity.UmsAdmin;
import com.fh.admin.service.IUmsAdminService;
import com.fh.commons.result.CommonsReturn;
import com.fh.commons.result.ReturnCode;
import com.fh.commons.utils.MD5Util;
import io.swagger.annotations.Api;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoginController
 * @Author 阳
 * @Date 2020/12/6 19:18
 * @Version 1.0
 **/
@RestController
@RequestMapping("/login")
@Api(tags = "LoginController", description = "用户登录")
public class LoginController {

    @Resource
    private IUmsAdminService umsAdminService;
    @Resource
    private LoginJwtUtil loginJwtUtil;
    @Resource
    private RedisTemplate redisTemplate;

    private static final String pre_token = "ACCESS_TOKEN:";

    @PostMapping
    public CommonsReturn doLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        //如果用户名或密码为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return CommonsReturn.error(ReturnCode.USERNAME_PASSWORD_NULL);
        }
        //根据用户名查询用户信息
        UmsAdmin user = umsAdminService.getOne(new QueryWrapper<UmsAdmin>().eq("userName", username));
        //判断用户名是否存在
        if (user == null) {
            return CommonsReturn.error(ReturnCode.USERNAME_NOEXIST);
        }
        //判断密码是否正确
        String md5 = MD5Util.getMD5(password);
        if (!md5.equals(user.getPassword())) {
            return CommonsReturn.error(ReturnCode.PASSWORD_ERROR);
        }
        String token = loginJwtUtil.createToken(user);
        //将token放到redis缓存中
        Long currentTime = System.currentTimeMillis();
        String accessKey = pre_token + user.getId() + ":" + token;
        redisTemplate.opsForValue().set(accessKey, currentTime);
        redisTemplate.expire(accessKey, 2, TimeUnit.MINUTES);
        return CommonsReturn.success(token);
    }
}
