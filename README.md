![Uploading image.png…]()


✅ 一、请求格式说明（POST）
URL: http://localhost:8080/api/ai/chat
请求方式: POST
Content-Type: application/json
请求体结构：

json
复制编辑
{
  "messages": [
    {"role": "user", "content": "What's the highest mountain in the world?"},
    {"role": "assistant", "content": "The highest mountain in the world is Mount Everest."},
    {"role": "user", "content": "What is the second?"}
  ],
  "model": "deepseek-chat"
}
⚠️ 注意事项：
	1. messages 是一个数组，表示对话的上下文：
		○ 每条消息必须包含 "role"（如 user、assistant）和 "content"。
		○ 最后一条消息通常是用户的问题，模型基于整个上下文生成回复。
	2. model 字段指定要使用的模型（如 deepseek-chat）。必须保证该模型是服务端支持的。
	3. 如果系统支持多个模型或版本，要根据文档明确填写 model 名称，否则可能报错。

✅ 二、返回格式说明

json
复制编辑
{
	"id": "18e7caf7-b09b-4ecc-9401-70755574b205",
	"object": "chat.completion",
	"created": 1745637278,
	"choices": [
		{
			"index": 0,
			"message": {
				"role": "assistant",
				"content": "The **second-highest mountain in the world** is **K2 ..."
			}
		}
	]
}
⚠️ 注意事项：
	1. 返回是一个包含 choices 数组的对象：
		○ 每个 choice 里包含 message，表示一个候选回答（可以多条）。
		○ 一般只用第一个 choices[0] 的内容。
		○ role 一定是 assistant，表示模型的回答。
	2. created 是时间戳（Unix 时间），可用于记录响应时间。
	3. id 可用于日志追踪、对话管理、缓存等。



1. 添加必要的依赖
首先，确保你的Spring项目中包含了HTTP客户端依赖，比如Spring的WebClient。
对于Maven项目，在pom.xml中添加：
xml
<!-- 使用WebClient -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
<!-- JSON处理 -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
2. 配置API密钥和端点
在application.properties或application.yml中配置DeepSeek AI的API密钥和端点：
deepseek:
  api:
    key: sk-c7a536eddc524e07abe41cc4868ed998
    url: https://api.deepseek.com/chat/completions

3. 创建请求和响应DTO
创建用于封装请求和响应的Java类：
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








4. 创建服务类调用API
使用WebClient方式（推荐，异步非阻塞）：
java
复制
下载
@Service
public class DeepSeekService {
    
    @Value("${deepseek.api.key}")
    private String apiKey;
    
    @Value("${deepseek.api.url}")
    private String apiUrl;
    
    private final WebClient webClient;
    
    public DeepSeekService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    
    public Mono<DeepSeekResponse> chatCompletion(DeepSeekRequest request) {
        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(DeepSeekResponse.class);
    }
}



5. 创建控制器暴露API
@RestController
@RequestMapping("/api/ai")
public class AIController {
    
    private final DeepSeekService deepSeekService;
    
    public AIController(DeepSeekService deepSeekService) {
        this.deepSeekService = deepSeekService;
    }
    
    @PostMapping("/chat")
    public Mono<DeepSeekResponse> chat(@RequestBody DeepSeekRequest request) {
        return deepSeekService.chatCompletion(request);
    }
}


6. 使用示例
注意事项
1. API密钥安全：不要将API密钥硬编码在代码中或提交到版本控制系统，使用环境变量或配置中心管理
2. 错误处理：添加适当的错误处理逻辑，处理网络问题和API限制
3. 超时设置：根据需求配置适当的超时时间
4. 异步处理：对于高并发场景，考虑使用异步非阻塞方式（WebClient）
5. API文档：参考DeepSeek AI的最新官方文档，了解支持的模型和参数
以上步骤提供了一个基本的集成框架，你可以根据实际需求和DeepSeek AI的具体API规范进行调整。
![image](https://github.com/user-attachments/assets/93e5fa0e-2529-4995-a35d-92089e52eb30)
