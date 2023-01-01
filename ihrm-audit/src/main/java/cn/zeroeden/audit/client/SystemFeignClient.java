package cn.zeroeden.audit.client;

import cn.zeroeden.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 声明接口，通过feign调用其他微服务
 */
//声明调用的微服务名称
@FeignClient("ihrm-system")
public interface SystemFeignClient {
    /**
     * 调用微服务的接口
     */
    @RequestMapping(value="/sys/user/{id}",method = RequestMethod.GET)
    Result findById(@PathVariable(value = "id") String id);

    @RequestMapping(value="/sys/city/{cityId}",method = RequestMethod.GET)
    Result findCityById(@PathVariable(value = "cityId") String id);

    @RequestMapping(value = "/sys/user/countByEntry", method = RequestMethod.GET)
    Result countByEntry(@RequestParam(value = "yearMonth") String yearMonth);

    @RequestMapping(value = "/sys/user/findByMobile",method = RequestMethod.GET)
    Result findUserByMobile(@RequestParam(value = "mobile") String mobile);


    @RequestMapping(value = "/sys/city/findByName", method = RequestMethod.GET)
    public Result findCityByName(@RequestParam(value = "cityName") String cityName) ;
}
