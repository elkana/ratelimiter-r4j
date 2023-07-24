package com.example.ratelimiterr4j.controller;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ratelimiterr4j.config.LimiterManager;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// based on https://bootcamptoprod.com/resilience4j-rate-limiter/
// see RateLimiterConfiguration
// cocok dipakai utk pricing plan
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dynamic")
public class ControllerDynamicRL {
    private final LimiterManager mgr;

    // must declare this
    public ResponseEntity<String> fallbackRateLimiter(RequestNotPermitted e) {
        return new ResponseEntity<String>("Access Limit Exceeded", HttpStatus.TOO_MANY_REQUESTS);
    }

    public String doSomething() {
        log.info("doing someting");
        return "do something man";
    }

    // @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "customRateLimiter",
    // fallbackMethod = "fallbackRateLimiter")
    @GetMapping("/hello1")
    public ResponseEntity<String> hello1() {
        log.info("hello1");
        // final RateLimiter limiter = mgr.getLimiter("1233");
        // RateLimiterConfig config = RateLimiterConfig.custom()
        // .limitForPeriod(2)
        // .limitRefreshPeriod(Duration.ofSeconds(10))
        // .timeoutDuration(Duration.ofSeconds(5))
        // .build();
        // RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        // RateLimiter limiter = registry.rateLimiter("customRateLimiter1");
        // RateLimiter.
        // RateLimiter limiter = RateLimiter.ofDefaults("customRateLimiterB");
        RateLimiter limiter = mgr.getRate().get();
        // baru tau penggunaan Supplier itu generic function
        Supplier<String> result = RateLimiter.decorateSupplier(limiter, () -> doSomething());
        // Supplier<String> result = RateLimiter.of("customRateLimiterB", () -> doSomething());

        return ResponseEntity.ok(result.get());
        // return ResponseEntity.ok("Hello World #1 !! Rest Controller is working ...");
    }
}


@Configuration
class RateLimiterConfiguration {
    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    // sayangnya ini cuma di eksekusi pertama kali saja
    @Bean
    public RateLimiter rateLimitWithCustomConfig() {
        RateLimiterConfig customConfig = RateLimiterConfig.custom()
                // only allow 2 requests within period
                .limitForPeriod(8)
                // rate limit will reset every 10s
                .limitRefreshPeriod(Duration.of(10, ChronoUnit.SECONDS))
                // if a request exceeds 5s, trigger fallback. request will fail after 5s if it didnt
                // get a chance to execute.
                // mengontrol brp lama harus menunggu
                .timeoutDuration(Duration.of(5, ChronoUnit.SECONDS)).build();

        return rateLimiterRegistry.rateLimiter("customRateLimiterA", customConfig);
    }

    @Bean
    public RateLimiter rateLimitWithCustomConfigB() {
        RateLimiterConfig customConfig = RateLimiterConfig.custom()
                // only allow 2 requests within period
                .limitForPeriod(1)
                // rate limit will reset every 10s
                .limitRefreshPeriod(Duration.of(10, ChronoUnit.SECONDS))
                // if a request exceeds 5s, trigger fallback. request will fail after 5s if it didnt
                // get a chance to execute.
                // mengontrol brp lama harus menunggu
                .timeoutDuration(Duration.of(5, ChronoUnit.SECONDS)).build();

        return rateLimiterRegistry.rateLimiter("customRateLimiterB", customConfig);
    }
}
