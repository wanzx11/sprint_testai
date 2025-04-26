package org.example.untitled_testai.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeepSeekRequest {
    private String model;               //模型
    private List<Message> messages;     //消息

    @Data
    public static class Message {
        private String role;            //角色
        private String content;         //内容
    }
}
