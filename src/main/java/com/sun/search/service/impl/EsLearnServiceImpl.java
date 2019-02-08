package com.sun.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sun.search.constants.Constants;
import com.sun.search.service.EsLearnService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @class: EsLearnServiceImpl
 * @description: es各种复杂查询
 * @author: Jay Sun
 * @time: 2019-02-08 15:31
 **/
@Service
@Slf4j
public class EsLearnServiceImpl implements EsLearnService {

    private SearchRequest searchRequest = new SearchRequest(Constants.ES_LEARN_INDEX_NAME);

    @Resource
    private RestHighLevelClient client;

    @Override
    public void accurateQuery(String name) {
        TermQueryBuilder termQuery = QueryBuilders.termQuery("name.keyword", name);
        allResolve(termQuery, null);
    }

    @Override
    public void accurateInQuery(String[] names) {
        TermsQueryBuilder termsQuery = QueryBuilders.termsQuery("name.keyword", names);
        allResolve(termsQuery, null);
    }

    @Override
    public void likeQuery(String name) {
        WildcardQueryBuilder wildcardQuery = QueryBuilders.wildcardQuery("name.keyword", "*" + name + "*");
        allResolve(wildcardQuery, null);
    }

    @Override
    public void rangeQuery(int startId, int endId) {
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("id").from(startId).to(endId).includeLower(true).includeUpper(true);
        allResolve(rangeQuery, null);
    }

    @Override
    public void groupQuery() {
        String groupByField = "prov_code_group";

        SearchSourceBuilder builder = new SearchSourceBuilder();
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(groupByField).field("community_id");
        builder.aggregation(termsAggregationBuilder);

        SearchRequest searchRequest = new SearchRequest("vv_community_ik");
        searchRequest.source(builder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Aggregations aggregations = response.getAggregations();
        Terms terms = aggregations.get(groupByField);
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        System.out.println("buckets=>" + buckets);
        System.out.println("------------------");
        buckets.stream().forEach(bucket -> {
            System.out.print("bucket=>" + bucket.getKeyAsString());
            System.out.println(";" + bucket.getDocCount());
        });
    }

    @Override
    public void sortQuery(Map<String, SortOrder> fieldSort) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        allResolve(boolQuery, fieldSort);
    }

    @Override
    public void andQuery() {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        WildcardQueryBuilder query1 = QueryBuilders.wildcardQuery("name.keyword", "*" + "f" + "*");
        boolQuery.must(query1);
        TermQueryBuilder query2 = QueryBuilders.termQuery("id", 3);
        boolQuery.must(query2);
        allResolve(boolQuery, null);
    }

    @Override
    public void orQuery() {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        TermQueryBuilder query1 = QueryBuilders.termQuery("id", 3);
        boolQuery.should(query1);
        TermQueryBuilder query2 = QueryBuilders.termQuery("id", 4);
        boolQuery.should(query2);
        allResolve(boolQuery, null);
    }

    /**
     * 公共查询方法
     *
     * @param queryBuilder
     * @return
     * @throws IOException
     */
    private SearchResponse exeQuery(QueryBuilder queryBuilder, Map<String, SortOrder> fieldSort) throws IOException {
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
        searchSourceBuilder.query(queryBuilder);
        if (MapUtils.isNotEmpty(fieldSort)) {
            fieldSort.entrySet().forEach(entry -> searchSourceBuilder.sort(new FieldSortBuilder(entry.getKey()).order(entry.getValue())));
        }
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        log.info("es查询出的结果=>{}", response);
        return response;
    }

    /**
     * 处理查询结果
     * @param response
     */
    private void resolveResponse(SearchResponse response) {
        if (Objects.isNull(response)) {
            return;
        }
        if (response.getHits().totalHits > 0) {
            Arrays.stream(response.getHits().getHits()).forEach(hit -> {
                log.info("hit=>" + JSONObject.toJSONString(hit));
                Map<String, Object> map = hit.getSourceAsMap();
                log.info("map=>" + JSONObject.toJSONString(map));
                System.out.println("----------------------------------");
            });
        }
    }

    /**
     * 整体代码处理，为了简洁
     * @param queryBuilder
     */
    public void allResolve(QueryBuilder queryBuilder, Map<String, SortOrder> fieldSort) {
        try {
            SearchResponse response = exeQuery(queryBuilder, fieldSort);
            resolveResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
