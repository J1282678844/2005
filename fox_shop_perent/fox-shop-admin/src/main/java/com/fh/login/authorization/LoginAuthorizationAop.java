package com.fh.login.authorization;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fh.commons.result.CommonsReturn;
import com.fh.commons.result.ReturnCode;
import com.fh.login.LoginJwtUtil;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoginAuthorizationAop
 * @Author 阳
 * @Date 2020/12/6 20:45
 * @Version 1.0
 **/
@Aspect
@Component
@Order(3)
public class LoginAuthorizationAop {

    @Resource
    private HttpServletRequest request;
    @Resource
    private LoginJwtUtil loginJwtUtil;
    @Resource
    private RedisTemplate redisTemplate;

    private static final String pre_token = "ACCESS_TOKEN:";

    @Around(value = "execution(* com.fh.*.controller..*.*(..)) && @annotation(loginAuthorization)")
    public Object loginAround(ProceedingJoinPoint joinPoint, LoginAuthorization loginAuthorization) throws Throwable {

        //判断token值是否为空
        String token = request.getHeader("Authorization-token");
        if (StringUtils.isBlank(token)) {
            return CommonsReturn.error(ReturnCode.LOGIN_DISABLED);
        }
        //解析token值
        CommonsReturn commonsReturn = loginJwtUtil.authToken(token);
        if (commonsReturn.getCode() != 200) {
            return commonsReturn;
        }
        //获取用户信息
        Claims claims= (Claims) commonsReturn.getData();
        String userId = String.valueOf(claims.get("userId"));
        String accessKey = pre_token+userId+":"+token;
        if (!redisTemplate.hasKey(accessKey)){
            return commonsReturn.error(ReturnCode.LOGIN_DISABLED);
        }
        //续签（重置缓存的时间）
        Long currentTime = System.currentTimeMillis();
        redisTemplate.opsForValue().set(accessKey, currentTime);
        redisTemplate.expire(accessKey, 2, TimeUnit.MINUTES);

        Object obj = null;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
        return obj;
    }
}
