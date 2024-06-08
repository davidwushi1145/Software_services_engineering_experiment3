package wushi.cn.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wushi.cn.Entity.CommonResult;
import wushi.cn.Feign.ServiceProviderService;
import wushi.cn.entity.User;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private ServiceProviderService serviceProviderService;

    @GetMapping("/getCartByUserId/{userId}")
    @RateLimiter(name = "rateLimiterBackendA", fallbackMethod = "getCartByUserIdFallback")
    public CommonResult<User> getCartByUserId(@PathVariable("userId") Integer userId) {
        System.out.println("该功能已经被调用");
        return serviceProviderService.getUserById(userId);
    }
    public CommonResult<User> getCartByUserIdFallback(Integer userId, Throwable e) {
        e.printStackTrace();
        String message = "异常火爆";
        System.out.println(message);
        CommonResult<User> result = new CommonResult<>(400, message, null);
        return result;
    }
}
