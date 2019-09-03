package com.ronhe.romp.schedule.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/getValue")
    public Map<String, String> getValue(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("aa", "bb");
        return map;
    }
}
