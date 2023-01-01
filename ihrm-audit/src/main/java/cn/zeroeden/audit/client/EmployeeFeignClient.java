package cn.zeroeden.audit.client;


import cn.zeroeden.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 声明接口，通过feign调用其他微服务
 */
//声明调用的微服务名称
@FeignClient("ihrm-employee")
public interface EmployeeFeignClient {

    /**
     * 获取个人信息
     * @param uid 用户id
     * @return 个人信息
     */
    @RequestMapping(value = "employees/{id}/personalInfo", method = RequestMethod.GET)
    Result findPersonalInfo(@PathVariable(name = "id") String uid);


    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.GET)
     Result findJobsInfo(@PathVariable(name = "id") String uid);


}
