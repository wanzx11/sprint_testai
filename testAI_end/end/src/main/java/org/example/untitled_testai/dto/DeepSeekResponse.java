package org.example.untitled_testai.dto;

import lombok.Data;



import java.util.List;

@Data
public class DeepSeekResponse {
    private String id;
    private String object;
    private long created;
    private List<Choice> choices;

    @Data
    public static class Choice {
        private int index;
        private Message message;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }
}
