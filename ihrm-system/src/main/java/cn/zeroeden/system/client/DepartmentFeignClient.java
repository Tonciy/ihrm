package cn.zeroeden.system.client;

import cn.zeroeden.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 【ihrm-company】部门远程调用接口
 */

@FeignClient(value = "ihrm-company")
public interface DepartmentFeignClient {
    //@RequestMapping注解用于对被调用的微服务进行地址映射
    @RequestMapping(value = "/company/department/{id}/", method = RequestMethod.GET)
    Result findById(@PathVariable("id") String id) throws Exception;
}
