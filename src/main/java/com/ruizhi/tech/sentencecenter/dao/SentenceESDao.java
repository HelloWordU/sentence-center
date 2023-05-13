package com.ruizhi.tech.sentencecenter.dao;

import com.ruizhi.tech.sentencecenter.es.entity.SentenceEntity;
import com.ruizhi.tech.sentencecenter.pojo.vo.request.QuerySentenceRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author EDY
 * @date 2023/4/12 20:19
 */
@Component
public class SentenceESDao {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    public void insert(SentenceEntity sentenceEntity) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(sentenceEntity.getId().toString())
                .withObject(sentenceEntity)
                .build();
        if (!elasticsearchRestTemplate.indexOps(IndexCoordinates.of("sentence_index")).exists()) {
            elasticsearchRestTemplate.indexOps(IndexCoordinates.of("sentence_index")).create();
        }
        elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of("sentence_index"));
    }

    public List<SentenceEntity> query(QuerySentenceRequest addSentenceRequest) {
        List<SentenceEntity> res = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.minimumShouldMatch(2);
        for (String tag : addSentenceRequest.getTags()) {
            boolQueryBuilder.should(QueryBuilders.termsQuery("tags.keyword", tag));
        }
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder).build();
        SearchHits<SentenceEntity> search = elasticsearchRestTemplate.search(searchQuery, SentenceEntity.class);
        List<SearchHit<SentenceEntity>> searchHits = search.getSearchHits();
        for (SearchHit<SentenceEntity> searchHit : searchHits) {
            res.add(searchHit.getContent());
        }
        return res;
    }
}
