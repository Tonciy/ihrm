package cn.zeroeden.system.service.impl;


import cn.zeroeden.domain.company.response.FaceLoginResult;
import cn.zeroeden.domain.company.response.QRCode;
import cn.zeroeden.domain.system.User;
import cn.zeroeden.system.dao.UserDao;
import cn.zeroeden.system.utils.BaiduAiUtil;
import cn.zeroeden.system.utils.QRCodeUtil;
import cn.zeroeden.utils.IdWorker;
import com.baidu.aip.util.Base64Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Service
public class FaceLoginService {

    @Value("${qr.url}")
    private String url;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private QRCodeUtil qrCodeUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BaiduAiUtil baiduAiUtil;

    @Autowired
    private UserDao userDao;

    //创建二维码
    public QRCode getQRCode() throws Exception {
        // 1. 创建唯一标识
        String code = idWorker.nextId() + "";
        // 2. 创建二维码存放的url地址
        String content = url + "?code=" + code;
        // 3. 创建DataUrl形式的二维码
        String file = qrCodeUtil.createQRCode(content);
        // 4. 存入当前二维码状态进Redis
        FaceLoginResult result = new FaceLoginResult("-1");
        redisTemplate.boundValueOps(getCacheKey(code)).set(result, 10, TimeUnit.MINUTES);
        return null;
    }

    //根据唯一标识，查询用户是否登录成功
    public FaceLoginResult checkQRCode(String code) {
        String key = getCacheKey(code);
        return (FaceLoginResult) redisTemplate.opsForValue().get(key);
    }

    //扫描二维码之后，使用拍摄照片进行登录
    public String loginByFace(String code, MultipartFile attachment) throws Exception {
        // 1. 通过百度云Ai查询当前用户
        String userid = baiduAiUtil.faceSearch(Base64Util.encode(attachment.getBytes()));
        FaceLoginResult result = new FaceLoginResult("0");
        if (userid != null) {
            // 模拟登录过程
            User user = userDao.selectById(userid);
            if(user != null){
                Subject subject = SecurityUtils.getSubject();
                subject.login(new UsernamePasswordToken(user.getMobile(), user.getPassword()));
                // 获取token
                String token = subject.getSession().getId() + "";
                result = new FaceLoginResult("1", token, userid);
                // 3. 修改二维码状态
                redisTemplate.boundValueOps(getCacheKey(code)).set(result, 10, TimeUnit.MINUTES);
            }
        }

        return userid;
    }

    //构造缓存key
    private String getCacheKey(String code) {
        return "qrcode_" + code;
    }
}
