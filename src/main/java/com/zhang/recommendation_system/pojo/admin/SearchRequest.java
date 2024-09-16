package com.zhang.recommendation_system.pojo.admin;

import lombok.Data;

@Data
public class SearchRequest {

    private  Integer group;

    private String keyword;

    private String type;
}
