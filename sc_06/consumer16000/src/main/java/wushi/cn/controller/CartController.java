package wushi.cn.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wushi.cn.Entity.CommonResult;
import wushi.cn.Feign.ServiceProviderService;
import wushi.cn.Entity.User;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private ServiceProviderService serviceProviderService;

    @GetMapping("/getCartByUserId/{userId}")
    @TimeLimiter(name = "backendA", fallbackMethod = "getCartByUserIdFallback")
    public CompletableFuture<User> getCartByUserId(@PathVariable("userId") Integer userId) {
        return CompletableFuture.completedFuture(serviceProviderService.getUserById(userId).getData());
    }

    public CompletableFuture<User> getCartByUserIdFallback(Integer userId, Throwable e) {
        e.printStackTrace();
        String message = "服务暂时不可用";
        System.out.println(message);
        return CompletableFuture.completedFuture(new CommonResult<>(400, message, new User()).getData());
    }
}
