package com.sun.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @class: Message
 * @description: 消息对象
 * @author: Jay Sun
 * @time: 2019-02-06 10:41
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message<T> {
    /**
     * 消息编号
     */
    private String id;

    /**
     * 消费者接受到消息进行什么操作
     */
    private Integer action;

    /**
     * 消息体
     */
    private T body;

    /**
     * 发送时间
     */
    private Date sendTime;

}
