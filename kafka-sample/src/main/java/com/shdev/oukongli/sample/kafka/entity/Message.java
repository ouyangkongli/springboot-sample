package com.shdev.oukongli.sample.kafka.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private Long id;    //id

    private String msg; //消息

    private Date sendTime;
}
