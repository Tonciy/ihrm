package cn.zeroeden.salarys.feign;

import cn.zeroeden.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


//声明调用的微服务名称
@FeignClient("ihrm-attendance")
public interface AttendanceFeignClient {
    /**
     * 调用微服务的接口
     */
    @RequestMapping(value = "/attendances/archive/{userId}/{yearMonth}", method = RequestMethod.GET)
    Result atteStatisMonthly(@PathVariable("userId") String userId, @PathVariable("yearMonth") String yearMonth);
}
