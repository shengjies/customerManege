package com.ruoyi.framework.jwt;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.system.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

public static final String CLAIM_KEY_USER="sub";
    public static final String CLAIM_KEY_SECRET="secret";

    /**
     * 进行信息加密
     * @param claims
     * @return
     */
    public static String getToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(getExpirationDate())
                .signWith(SignatureAlgorithm.HS256,CLAIM_KEY_SECRET)
                .compact();
    }

    /**
     * 获取存活时间
     * @return
     */
    public static Date getExpirationDate(){
        return new Date(System.currentTimeMillis()+60*60*1000);
    }

    /**
     * token 解析
     * @param token
     * @return
     */
    public static Claims getClaimsFromToken(String token){
        Claims claims =null;
        try {
             claims  = Jwts.parser()
                    .setSigningKey(CLAIM_KEY_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            //e.printStackTrace();
        }
        return  claims;
    }

    public static User getUserByToken(String token){
        User userInfo =null;
        try {
            //判断解析结果是否为空
            Claims claims = getClaimsFromToken(token);
            if(claims !=null){
                String subject = claims.getSubject();
                userInfo = JSON.parseObject(subject,User.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  userInfo;
    }

    public static User getTokenUser(HttpServletRequest request){
       return getTokenCookie(request.getCookies());
    }

    public static User getTokenCookie(Cookie[] cookies){
        String token = null;
        if(cookies == null)return null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("token")){
                token = cookie.getValue();
            }
        }
        if(StringUtils.isEmpty(token)){
            return null;
        }
        return getUserByToken(token);
    }

    public static String getToken(HttpServletRequest request){
        String token = null;
        Cookie[] cookies = request.getCookies();
        if(cookies == null)return token;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("token")){
                token = cookie.getValue();
            }
        }
        return token;
    }
}
