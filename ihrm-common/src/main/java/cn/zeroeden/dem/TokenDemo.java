package cn.zeroeden.dem;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2022/11/5
 * @description: token测试
 */

public class TokenDemo {
    public static void main(String[] args) {
        // 生成
        JwtBuilder builder = Jwts.builder().setId("999") // 存储id
                .setSubject("小白")                       // 存储
                .setIssuedAt(new Date())         // 发布时间
                .claim("name", "zero")     // 加入自定义内容
                .claim("age", 18)           // 加入自定义内容
                .signWith(SignatureAlgorithm.HS256, "zero");  // 设置加密算法以及私钥
        String token = builder.compact();
        System.out.println(token);

        // 解析
        Claims claims = Jwts.parser().setSigningKey("zero").parseClaimsJws(token).getBody();
        System.out.println("id:" + claims.getId() );
        System.out.println("subject:" + claims.getSubject() );
        System.out.println("issuedAt:" + claims.getIssuedAt() );
        System.out.println("name:" + claims.get("name") );
        System.out.println("age:" + claims.get("age") );
    }
}
