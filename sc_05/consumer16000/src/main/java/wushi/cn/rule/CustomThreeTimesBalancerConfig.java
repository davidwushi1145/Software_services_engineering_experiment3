package wushi.cn.rule;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

public class CustomThreeTimesBalancerConfig {
    @Bean
    ReactorLoadBalancer<ServiceInstance> threeTimeLoadBalancer(Environment env, LoadBalancerClientFactory lcf) {
        String name = env.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new ThreeTimeLoadBalancer(lcf.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }
}
