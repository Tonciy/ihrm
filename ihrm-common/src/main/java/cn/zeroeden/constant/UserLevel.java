package cn.zeroeden.constant;

/**
 * @author: Zero
 * @time: 2022/11/6
 * @description: 用户身份类别-大概分
 */


public enum UserLevel {


    /**
     * saas管理员
     */
    SAAS_ADMIN("saasAdmin"),
    /**
     * 企业管理员
     */
    COMPANY_ADMIN("coAdmin"),
    /**
     * 普通用户
     */
    USER("user");

    String level;

    UserLevel(String level) {
        this.level = level;
    }
}
