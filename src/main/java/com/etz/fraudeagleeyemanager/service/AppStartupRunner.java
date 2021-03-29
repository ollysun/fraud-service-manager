package com.etz.fraudeagleeyemanager.service;

import com.etz.fraudengine.entity.PocketMoni;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppStartupRunner implements ApplicationRunner {

    public static int counter;

    @Autowired
    private ProducerService producerService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        PocketMoni pocketMoni = new PocketMoni(1L, "NGN", "22000");
        for (int i = 1; i < 100; i++ ){
            pocketMoni.setAmount(String.valueOf(Integer.parseInt(pocketMoni.getAmount()) + i));
            Thread.sleep(500);
            producerService.sendPocketMoni(pocketMoni);
        }
    }
}