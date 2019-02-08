package com.sun.search;

import com.alibaba.fastjson.JSONObject;
import com.sun.search.model.rsp.TestSlave;
import com.sun.search.service.TestSlaveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @class: TestSlaveTest
 * @description:
 * @author: Jay Sun
 * @time: 2019-02-05 20:43
 **/
@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSlaveTest {

    @Resource
    private TestSlaveService testSlaveService;

    @Test
    public void addIndex() {
        TestSlave testSlave = TestSlave.builder().id(9).age(18).name("李四d1233").build();
        testSlaveService.addIndex(testSlave);
    }

    @Test
    public void deleteIndex() {
        testSlaveService.deleteIndex(2);
    }

    @Test
    public void updateIndex() {
        TestSlave testSlave = TestSlave.builder().id(1).age(26).name("而国际").build();
        testSlaveService.updateIndex(testSlave);
    }

    @Test
    public void queryList() {
        List<TestSlave> list = testSlaveService.queryList();
        System.out.println("list=" + JSONObject.toJSONString(list));
    }

}
