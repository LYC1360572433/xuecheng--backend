package com.xuecheng.learning.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.learning.config.PayNotifyConfig;
import com.xuecheng.learning.service.MyCourseTablesService;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description 接收支付结果 接收消息通知
 */
@Slf4j
@Service
public class ReceivePayNotifyService{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    MqMessageService mqMessageService;

    @Autowired
    MyCourseTablesService myCourseTablesService;


    //监听消息队列接收支付结果通知 指定队列
    @RabbitListener(queues = PayNotifyConfig.PAYNOTIFY_QUEUE)
    //当队列当中有消息了 就能消费 处理
    public void receive(Message message) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //获取消息 从接收的对象中取出body 字节数组转换成MqMessage类
        MqMessage mqMessage = JSON.parseObject(message.getBody(), MqMessage.class);
        log.debug("学习中心服务接收支付结果:{}", mqMessage);
        //解析消息内容
        //消息类型
        String messageType = mqMessage.getMessageType();
        //订单类型,60201表示购买课程
        String businessKey2 = mqMessage.getBusinessKey2();
        //学习中心服务只要购买课程类的支付订单结果  这里只处理支付结果通知
        if (PayNotifyConfig.MESSAGE_TYPE.equals(messageType) && "60201".equals(businessKey2)) {
            //选课记录id
            String choosecourseId = mqMessage.getBusinessKey1();
            //添加选课
            //根据消息内容，更新选课记录、向我的课程表插入记录
            boolean b = myCourseTablesService.saveChooseCourseSuccess(choosecourseId);
            if(!b){
                //添加选课失败，抛出异常，消息重回队列
                XueChengPlusException.cast("保存选课记录状态失败");
            }
        }
    }
}