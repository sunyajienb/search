package com.sun.search.controller;

import com.sun.search.model.rsp.TestSlave;
import com.sun.search.service.TestSlaveService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @class: TestSlaveController
 * @description:
 * @author: Jay Sun
 * @time: 2019-02-05 18:39
 **/
@RestController
@RequestMapping("/testSlave")
public class TestSlaveController {

    @Resource
    private TestSlaveService testSlaveService;

    @GetMapping("/list")
    public List<TestSlave> list() {
        return testSlaveService.queryList();
    }

}
