package cn.zeroeden.controller;

import cn.zeroeden.domain.company.response.ProfileResult;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: Zero
 * @time: 2022/10/30
 * @description:
 */

public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String companyName;
    protected Claims claims;


    /**
     * jwt方式获取
     * @param request
     * @param response
     */
//    @ModelAttribute
//    public void setResAndReq(HttpServletRequest request, HttpServletResponse response) {
//        this.request = request;
//        this.response = response;
//        Object obj = request.getAttribute("user_claims");
//        if (obj != null) {
//            this.claims = (Claims) obj;
//            this.companyId = (String) claims.get("companyId");
//            this.companyName = (String) claims.get("companyName");
//        }
//    }


    /**
     * shiro方式获取
     * @param request
     * @param response
     */
    @ModelAttribute
    public void setResAndReq(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principals = subject.getPrincipals();
        if(principals != null && !principals.isEmpty()){
            // 获取安全数据
            ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
            this.companyId = result.getCompanyId();
            this.companyName = result.getCompany();
        }
    }
}
