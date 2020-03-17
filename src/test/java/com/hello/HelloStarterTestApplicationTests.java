package com.hello;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

//@SpringBootTest
class HelloStarterTestApplicationTests {

    @Test
    public void test() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        now.plusMinutes(5);
        System.out.println(now);
    }

}
