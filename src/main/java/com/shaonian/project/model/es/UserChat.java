package com.shaonian.project.model.es;

import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author 少年
 */
@Document(indexName = "user_chat_records")
public class UserChat {

    private String genResult;

    private String chatData;

    private String id;
}
