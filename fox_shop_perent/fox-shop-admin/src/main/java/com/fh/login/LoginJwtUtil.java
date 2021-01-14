package com.fh.login;

import com.fh.commons.result.CommonsReturn;
import com.fh.commons.result.ReturnCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LoginJwtUtil
 * @Author 阳
 * @Date 2020/12/6 19:49
 * @Version 1.0
 **/
@Component
public class LoginJwtUtil {

    @Value("${user.jwtset.signature}")
    private String signature;
    @Value("${user.jwtset.exptime}")
    private Long exptime;
    @Value("${user.jwtset.type}")
    private String type;
    @Value("${user.jwtset.alg}")
    private String alg;


    public String createToken(com.fh.admin.entity.UmsAdmin user) {
        //JWTtoken有三部分组成
        //1.头信息
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", alg);
        headerMap.put("type", type);
        //有效负载
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("userId", user.getId());
        payloadMap.put("userName", user.getUsername());

        //设置失效时间
        long startTime = System.currentTimeMillis();
        long expTime = startTime + exptime;

        //生成jwt
        String token = Jwts.builder()
                .setHeader(headerMap)
                .setClaims(payloadMap)
                .setExpiration(new Date(expTime))
                .signWith(SignatureAlgorithm.HS256, signature)
                .compact();
        return token;
    }

    //解析token
    public CommonsReturn authToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(signature)
                    .parseClaimsJws(token)
                    .getBody();
            return CommonsReturn.success(claims);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonsReturn.error(ReturnCode.LOGIN_DISABLED);
        }
    }

}
