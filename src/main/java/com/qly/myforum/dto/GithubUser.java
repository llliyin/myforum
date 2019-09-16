package com.qly.myforum.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private long id;
    private String avatarUrl;
    private String bio;
}
