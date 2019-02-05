package com.sun.search.service;

import com.sun.search.model.rsp.TestSlave;

import java.util.List;

/**
 * @class: TestSlaveService
 * @description:
 * @author: Jay Sun
 * @time: 2019-02-05 18:23
 **/
public interface TestSlaveService {

    boolean addIndex(TestSlave testSlave);

    boolean deleteIndex(Integer id);

    boolean updateIndex(TestSlave testSlave);

    TestSlave queryById(Integer id);

    List<TestSlave> queryList();

}
