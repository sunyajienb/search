package com.sun.search.service;

import org.elasticsearch.search.sort.SortOrder;

import java.util.Map;

/**
 * @interface: EsLearnService
 * @description: elastic search 各种复杂查询
 * @author: Jay Sun
 * @time: 2019-02-08 15:31
 **/
public interface EsLearnService {

    /**
     * 精准匹配
     */
    void accurateQuery(String name);

    /**
     * 精准匹配，类似sql的in条件
     */
    void accurateInQuery(String[] names);

    /**
     * 模糊查询
     */
    void likeQuery(String name);

    /**
     * 范围查询
     */
    void rangeQuery(int startId, int endId);

    /**
     * 分组，类似group by
     */
    void groupQuery();

    /**
     * 排序查询
     */
    void sortQuery(Map<String, SortOrder> fieldSort);

    /**
     * 多条件，类似sql的and
     */
    void andQuery();

    /**
     * 多条件，类似sql的or
     */
    void orQuery();

}
