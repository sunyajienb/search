package com.sun.search.service.kafka;

import com.alibaba.fastjson.JSONObject;
import com.sun.search.constants.Constants;
import com.sun.search.model.Message;
import com.sun.search.model.rsp.TestSlave;
import com.sun.search.service.TestSlaveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @class: KafkaClientReceiver
 * @description:
 * @author: Jay Sun
 * @time: 2019-02-06 10:53
 **/
@Service
@Slf4j
public class KafkaClientReceiver {

    @Resource
    private TestSlaveService testSlaveService;

    @KafkaListener(topics = {"search-topic"})
    public void consumeMessage(ConsumerRecord record) {
        log.info("record=>" + record);
        Message message = JSONObject.parseObject(record.value().toString(), Message.class);
        log.info("message=>" + message);
        switch (message.getAction().intValue()) {
            case Constants.add_index:
                addIndex(message);
                break;
            case Constants.delete_index:
                break;
            case Constants.update_index:
                break;
            default:
                break;
        }
    }

    private void addIndex(Message message) {
        testSlaveService.addIndex(JSONObject.parseObject(message.getBody().toString(), TestSlave.class));
    }

}
