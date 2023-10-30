package ru.otus.andrk.config;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class ThreadConfig {

    private final int threadPoolSize;

    public ThreadConfig(
            @Value("${book-app.thread-pool-size:2}") int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory() {
        var eventLoopGroup = new NioEventLoopGroup(threadPoolSize,
                new ThreadFactory() {
                    private final AtomicLong threadIdGenerator = new AtomicLong(0);
                    @Override
                    public Thread newThread(@NonNull Runnable task) {
                        return new Thread(task, "srv-thread-" + threadIdGenerator.incrementAndGet());
                    }
                });

        var factory = new NettyReactiveWebServerFactory();
        factory.addServerCustomizers(builder -> builder.runOn(eventLoopGroup));
        return factory;
    }

    @Bean
    public Scheduler workerPool() {
        return Schedulers.newParallel("worker-thread", threadPoolSize);
    }

}
