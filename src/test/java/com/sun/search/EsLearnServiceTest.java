package com.sun.search;

import com.sun.search.service.EsLearnService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @class: EsLearnServiceTest
 * @description:
 * @author: Jay Sun
 * @time: 2019-02-08 16:29
 **/
@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EsLearnServiceTest {

    @Resource
    private EsLearnService esLearnService;

    @Test
    public void testQuery() {
        esLearnService.orQuery();
    }

}
