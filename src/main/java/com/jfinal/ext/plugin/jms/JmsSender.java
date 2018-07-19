/*
 * Copyright 2018 Jobsz (zcq@zhucongqi.cn)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
*/
package com.jfinal.ext.plugin.jms;

import java.io.Serializable;
import java.util.Map;

import com.jfinal.log.Log;


public class JmsSender {
	
    private final Log LOG = Log.getLog(JmsSender.class);

    Map<String, QueueProducer> queueProducers;
    Map<String, TopicPublisher> topicPublishers;

    public boolean queueSend(String queueName, Serializable message, int msgType) {
        if (queueProducers == null) {
        	LOG.error("JmsPlugin not start");
            return false;
        }
        QueueProducer queueProducer = queueProducers.get(queueName);
        LOG.info("send msg " + message + "to queue " + queueName + " ,msgType " + msgType);
        return queueProducer.sendMessage(message, msgType);

    }

    public boolean topicSend(String topicName, Serializable message, int msgType) {
        if (topicPublishers == null) {
        	LOG.error("JmsPlugin not start");
            return false;
        }

        TopicPublisher topicPublisher = topicPublishers.get(topicName);
        LOG.info("send msg " + message + "to topic" + topicName);
        return topicPublisher.publishMessage(message, msgType);

    }
}
