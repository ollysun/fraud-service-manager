package com.etz.fraudeagleeyemanager.controller;

import com.etz.fraudengine.entity.PocketMoni;
import com.etz.fraudengine.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testkafka")
public class FraudEngineController {
    @Autowired
    private ProducerService producerService;

    @GetMapping
    public String producePocketMoni() {

        PocketMoni pocketMoni = new PocketMoni(1L, "NGN", "22000");
        producerService.sendPocketMoni(pocketMoni);
        return "publish to queue";
    }
}
