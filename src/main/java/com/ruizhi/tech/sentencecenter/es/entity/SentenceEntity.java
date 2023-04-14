package com.ruizhi.tech.sentencecenter.es.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author EDY
 * @date 2023/4/12 16:33
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "sentence_index")
@Setting(shards = 3)
@Builder
public class SentenceEntity  implements Serializable {
    @Id
    @Field(type = FieldType.Text)
    private String id;
    @Field(type = FieldType.Text,analyzer="ik_max_word")
    private String content;

    @Field(type = FieldType.Keyword,analyzer="ik_max_word")
    private List<String> tags;


}
