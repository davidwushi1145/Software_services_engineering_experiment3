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
    @Bulkhead(name = "bulkhead1", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "getCartByUserIdFallback")
    public CommonResult<User> getCartByUserId(@PathVariable("userId") Integer userId) {
        CommonResult<User> result = serviceProviderService.getUserById(userId);
        String message = result.getMessage();
        System.out.println(message);
        return result;
    }

    public CommonResult<User> getCartByUserIdFallback(Integer userId, Throwable e) {
        e.printStackTrace();
        CommonResult<User> result = new CommonResult<>(400, "服务暂时不可用", null);
        String message = "服务暂时不可用";
        System.out.println(message);
        return result;
    }
}
