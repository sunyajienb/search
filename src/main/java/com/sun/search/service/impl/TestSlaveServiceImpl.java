package com.sun.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sun.search.constants.Constants;
import com.sun.search.model.rsp.TestSlave;
import com.sun.search.service.TestSlaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @class: TestSlaveServiceImpl
 * @description:
 * @author: Jay Sun
 * @time: 2019-02-05 18:24
 **/
@Service
@Slf4j
public class TestSlaveServiceImpl implements TestSlaveService {

    @Value("${search.index.testSlave}")
    private String index;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean addIndex(TestSlave testSlave) {
        if (Objects.isNull(testSlave)) {
            return false;
        }
        IndexRequest indexRequest = new IndexRequest(index, Constants.TYPE, testSlave.getId().toString()).source(JSONObject.toJSONString(testSlave), XContentType.JSON);
        try {
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            log.error("插入出错", e);
        }
        return false;
    }

    @Override
    public boolean deleteIndex(Integer id) {
        if (Objects.isNull(id)) {
            return false;
        }
        DeleteRequest request = new DeleteRequest(index, Constants.TYPE, id.toString());
        try {
            restHighLevelClient.delete(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            log.error("删除出错", e);
        }
        return false;
    }

    @Override
    public boolean updateIndex(TestSlave testSlave) {
        if (Objects.isNull(testSlave)) {
            return false;
        }
        UpdateRequest request = new UpdateRequest(index, Constants.TYPE, testSlave.getId().toString());
        request.doc(JSONObject.toJSONString(testSlave), XContentType.JSON);
        try {
            restHighLevelClient.update(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            log.error("更新出错", e);
        }
        return false;
    }

    @Override
    public List<TestSlave> queryList() {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        builder.query(boolQueryBuilder);
        builder.sort(new FieldSortBuilder("age").order(SortOrder.DESC));

        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(builder);
        SearchResponse response;
        List<TestSlave> result = new ArrayList<>();
        try {
            log.info("searchRequest=>" + searchRequest);
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            log.info("response=>" + response);
            result = createResult(response);
        } catch (Exception e) {
            log.error("查询出错", e);
        }
        return result;
    }

    private List<TestSlave> createResult(SearchResponse response) {
        List<TestSlave> list = new ArrayList<>();
        Arrays.stream(response.getHits().getHits()).forEach(hit -> {
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    TestSlave testSlave = new TestSlave();
                    testSlave.setId((Integer) sourceAsMap.get("id"));
                    testSlave.setAge((Integer) sourceAsMap.get("age"));
                    testSlave.setName((String) sourceAsMap.get("name"));
                    list.add(testSlave);
                }
        );
        return list;
    }

}
