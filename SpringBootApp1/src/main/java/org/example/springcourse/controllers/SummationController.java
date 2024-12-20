package org.example.springcourse.controllers;

import org.example.springcourse.model.ErrorResponse;
import org.example.springcourse.model.SumResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sum")
public class SummationController {

    private static final Semaphore semaphore = new Semaphore(2);

    @GetMapping
    public Object sum(@RequestParam("a") int a, @RequestParam("b") int b) throws InterruptedException {
        if (!semaphore.tryAcquire(0, TimeUnit.SECONDS)) {
            return new ErrorResponse("Too many requests", HttpStatus.TOO_MANY_REQUESTS.value());
        }
        try {
            Thread.sleep(10_000);
            return new SumResponse(a + b);
        } finally {
            semaphore.release();
        }
    }
}
