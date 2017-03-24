package cn.partytime.config;

import com.sun.org.apache.regexp.internal.RE;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by lENOVO on 2016/10/9.
 */

@Configuration
public class ThreadPoolConfig {


    @Bean(name = "clientBootstrap")
    public Bootstrap initClientBootstrap(){
        return  new Bootstrap();
    }

    @Bean(name = "clientEventLoopGroup")
    public EventLoopGroup intClientEventLoopGroup(){
        return new NioEventLoopGroup();
    }

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor initThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        //线程池所使用的缓冲队列
        poolTaskExecutor.setQueueCapacity(200);
        //线程池维护线程的最少数量
        poolTaskExecutor.setCorePoolSize(5);
        //线程池维护线程的最大数量
        poolTaskExecutor.setMaxPoolSize(1000);
        //线程池维护线程所允许的空闲时间
        poolTaskExecutor.setKeepAliveSeconds(30000);
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }


}
