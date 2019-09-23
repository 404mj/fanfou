package com.chinamobile.sd.controller;

import com.chinamobile.sd.model.User;
import com.chinamobile.sd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/try")
public class TryController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public Map<String,String> index() {
        Map<String,String> a = new HashMap<>();
        a.put("1","hello");
        a.put("2", "world");
        return a;
    }

    @RequestMapping("/array")
    public List<String> array() {
        List<String> b = new ArrayList<>();
        b.add("I");
        b.add("love");
        b.add("java");
        return b;
    }


    @RequestMapping("/alluser")
    public List<User> allUser() {
        return userService.findAllUsers();
    }

    @RequestMapping("/testparam")
    public String testparam(@RequestParam(value = "userName") String userName) {
        return userName;
    }

    @RequestMapping("/jsonparam")
    public String jsonParam(@RequestBody String jsonData) {
        return jsonData;
    }

}
