package com.turkmen.rest.concurrency.controller;


import com.turkmen.rest.concurrency.kotlin.KotlinBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
public class MainController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    KotlinBean kotlinBean;


    @RequestMapping(value = "/main")
    public String getSuccess() {

        long start = 0, end = 0;

        System.out.println("Rest Call started .........." + String.valueOf(start = System.currentTimeMillis()));

        for (int i = 0; i < 10000; i++) {
            System.out.println(restTemplate.getForObject("http://localhost:8080/callKotlin", String.class));
        }

        System.out.println("Rest Call finished .........." + String.valueOf(end = System.currentTimeMillis()));

        System.out.println("Total time spent in seconds " + String.valueOf((end - start) / 1000));

        return "success";
    }

    @RequestMapping(value = "/callKotlin")
    public String callKotlin() {

        return kotlinBean.helloKotlin("Hello Kotlin");
    }


    @RequestMapping(value = "/callJavaCallable")
    public String callJavaCallable() throws Exception {
        long start = 0, end = 0;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<String>> futures = new ArrayList<>();
        System.out.println("Java Callable Rest Call started .........." + String.valueOf(start = System.currentTimeMillis()));
        for (int i = 0; i < 10000; i++) {

            Future<String> future = executorService.submit(() -> restTemplate.getForObject("http://localhost:8080/callKotlin", String.class));
            futures.add(future);
            //final CompletableFuture<String> uCompletableFuture = CompletableFuture
            //      .supplyAsync(() -> restTemplate.getForObject("https://api.coinmarketcap.com/v2/listings", String.class));

            //   System.out.println(uCompletableFuture.get());
        }

        for (Future<String> future : futures) {
            System.out.println(future.get());
        }

        executorService.shutdown();
        System.out.println("Java Callable Rest Call finished .........." + String.valueOf(end = System.currentTimeMillis()));

        System.out.println("Java Callable Total time spent in seconds " + String.valueOf((end - start) / 1000));

        return "success callable Java";
    }


    @RequestMapping(value = "/callKotlinCallable")
    public String callKotlinCallable() throws Exception {

        long start = 0, end = 0;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<String>> futures = new ArrayList<>();
        System.out.println("Kotlin Callable Rest Call started .........." + String.valueOf(start = System.currentTimeMillis()));
        for (int i = 0; i < 10000; i++) {
            Future<String> future = executorService.submit(
                    new KotlinBean().getCallable(() -> restTemplate.getForObject("http://localhost:8080/callKotlin", String.class)));
            futures.add(future);

        }

        for (Future<String> future : futures) {
            System.out.println(future.get());
        }

        executorService.shutdown();
        System.out.println("Kotlin Callable Rest Call finished .........." + String.valueOf(end = System.currentTimeMillis()));

        System.out.println("Kotlin Callable Total time spent in seconds " + String.valueOf((end - start) / 1000));


        return "success callable Kotlin";
    }


}
