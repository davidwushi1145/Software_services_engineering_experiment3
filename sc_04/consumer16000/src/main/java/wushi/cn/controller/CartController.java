package wushi.cn.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
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
    @Bulkhead(name = "bulkhead1", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "getCartByUserIdFallback")
    public CompletableFuture<CommonResult<User>> getCartByUserId(@PathVariable("userId") Integer userId) {
        return CompletableFuture.supplyAsync(() -> serviceProviderService.getUserById(userId));
    }

    public CompletableFuture<CommonResult<User>> getCartByUserIdFallback(Integer userId, Throwable e) {
        e.printStackTrace();
        String message = "服务暂时不可用";
        System.out.println(message);
        return CompletableFuture.supplyAsync(() -> new CommonResult<>(440, message, new User(0, "null", "null")));
    }
}
