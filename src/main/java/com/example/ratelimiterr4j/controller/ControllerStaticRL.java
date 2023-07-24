package com.example.ratelimiterr4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ratelimiterr4j.dto.UserDto;
import com.example.ratelimiterr4j.exception.HttpException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/static")
public class ControllerStaticRL {
        // must declare this
        public ResponseEntity<String> fallbackRateLimiter(RequestNotPermitted e) {
                return new ResponseEntity<String>("Rate Limit Exceeded",
                                HttpStatus.TOO_MANY_REQUESTS);
        }

        @RateLimiter(name = "hello1Service", fallbackMethod = "fallbackRateLimiter")
        @GetMapping("/hello1")
        public ResponseEntity<String> hello1() {
                log.info("hello1");
                return ResponseEntity.ok("Hello World #1 !! Rest Controller is working ...");
        }

        @RateLimiter(name = "hello2Service", fallbackMethod = "fallbackRateLimiter")
        @GetMapping("/hello2")
        public ResponseEntity<UserDto> hello2() throws Exception {
                log.info("hello2");
                if (true)
                        throw new HttpException(HttpStatus.UNAUTHORIZED, "User ga ada");
                return ResponseEntity.ok(new UserDto());
        }
        
}
