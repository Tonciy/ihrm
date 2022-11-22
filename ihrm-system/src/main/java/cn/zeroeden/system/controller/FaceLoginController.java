package cn.zeroeden.system.controller;

import cn.zeroeden.domain.company.response.FaceLoginResult;
import cn.zeroeden.domain.company.response.QRCode;
import cn.zeroeden.entity.Result;
import cn.zeroeden.system.service.impl.FaceLoginService;
import cn.zeroeden.system.utils.BaiduAiUtil;
import com.baidu.aip.util.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 人脸识别
 */
@RestController
@RequestMapping("/sys/faceLogin")
public class FaceLoginController {


    @Autowired
    private FaceLoginService faceLoginService;

    @Autowired
    private BaiduAiUtil baiduAiUtil;
    /**
     * 获取刷脸登录二维码
     */
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    public Result qrcode() throws Exception {
        QRCode qrCode = faceLoginService.getQRCode();
        return Result.SUCCESS(qrCode);
    }

    /**
     * 检查二维码：登录页面轮询调用此方法，根据唯一标识code判断用户登录情况
     */
    @RequestMapping(value = "/qrcode/{code}", method = RequestMethod.GET)
    public Result qrcodeCeck(@PathVariable(name = "code") String code) throws Exception {
        FaceLoginResult result = faceLoginService.checkQRCode(code);
        return Result.SUCCESS(result);
    }

    /**
     * 人脸登录：根据落地页随机拍摄的面部头像进行登录
     *          根据拍摄的图片调用百度云AI进行检索查找
     */
    @RequestMapping(value = "/{code}", method = RequestMethod.POST)
    public Result loginByFace(@PathVariable(name = "code") String code, @RequestParam(name = "file") MultipartFile attachment) throws Exception {
        String userId = faceLoginService.loginByFace(code, attachment);
        if(userId == null){
            return Result.FAIL();
        }
        return Result.SUCCESS();
    }


    /**
     * 图像检测，判断图片中是否存在面部头像
     */
    @RequestMapping(value = "/checkFace", method = RequestMethod.POST)
    public Result checkFace(@RequestParam(name = "file") MultipartFile attachment) throws Exception {
        String image = Base64Util.encode(attachment.getBytes());
        Boolean flag = baiduAiUtil.faceCheck(image);
        if(flag){
            return Result.SUCCESS();
        }else{
            return Result.FAIL();
        }

    }

}
