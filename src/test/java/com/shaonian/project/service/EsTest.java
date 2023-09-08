package com.shaonian.project.service;

import com.shaonian.project.model.es.UserChat;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class EsTest {


    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    void test(){
        TermsAggregationBuilder chatDataAgg = AggregationBuilders.terms("chatDataAgg")
                .field("chatData.keyword")
                .size(5000);
        NativeSearchQuery nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withAggregations(chatDataAgg).build();
        SearchHits<UserChat> search = elasticsearchRestTemplate.search(nativeSearchQueryBuilder, UserChat.class);

        Aggregations aggregations = (Aggregations) search.getAggregations();
        Terms chatData = aggregations.get("chatDataAgg");
        List<? extends Terms.Bucket> buckets = chatData.getBuckets();

    }
}
