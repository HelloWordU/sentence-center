package com.ruizhi.tech.sentencecenter.dao;

import com.ruizhi.tech.sentencecenter.es.entity.SentenceEntity;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
        if(!elasticsearchRestTemplate.indexOps(IndexCoordinates.of("sentence_index")).exists())
        {
            elasticsearchRestTemplate.indexOps(IndexCoordinates.of("sentence_index")).create();
        }
        elasticsearchRestTemplate.index(indexQuery,IndexCoordinates.of("sentence_index"));
    }
}
