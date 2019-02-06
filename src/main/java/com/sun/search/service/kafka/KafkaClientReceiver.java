package com.sun.search.service.kafka;

import com.alibaba.fastjson.JSONObject;
import com.sun.search.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @class: KafkaClientReceiver
 * @description:
 * @author: Jay Sun
 * @time: 2019-02-06 10:53
 **/
@Service
@Slf4j
public class KafkaClientReceiver {

    @KafkaListener(topics = {"search-topic"})
    public void consumeMessage(ConsumerRecord record) {
        log.info("record=>" + record);
        Message message = JSONObject.parseObject(record.value().toString(), Message.class);
        log.info("message=>" + message);
    }

}
