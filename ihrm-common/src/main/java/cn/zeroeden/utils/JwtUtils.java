package cn.zeroeden.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;
import java.util.Map;

/**
 * @author: Zero
 * @time: 2022/11/5
 * @description: jwt工具类
 */
@Data
@ConfigurationProperties("jwt.config")
public class JwtUtils {
    /**
     * 加密私钥
     */
    private String key;
    /**
     * 有限时间，单位-秒
     */
    private Long ttl;

    /**
     * 生成token
     * @param id 存储的用户id
     * @param name 存储的用户名
     * @param map  存储的额外数据
     * @return token
     */
    public String createJwt(String id, String name, Map<String, Object> map) {
        // 计算失效时间
        long now = System.currentTimeMillis();
        long exp = now + ((ttl != null && ttl.longValue() > 0) ? ttl * 1000 : 0);
        // 创建
        JwtBuilder builder = Jwts.builder().setId(id) // 存储id
                .setSubject(name)                       // 存储
                .setIssuedAt(new Date())         // 发布时间
                .signWith(SignatureAlgorithm.HS256, key);  // 设置加密算法以及私钥
        // builder.setClaims(map); 这样好像会直接取代原本已经赋值的id，subject那些值（也就是在上面赋值的全没有了）
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.claim(entry.getKey(),entry.getValue());
        }

        builder.setExpiration(new Date(exp));
        return builder.compact();
    }

    /**
     * 解析token
     * @param token token
     * @return 装载数据的实体
     */
    public Claims parseJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }

}
