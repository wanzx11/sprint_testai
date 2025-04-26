<template>
  <div class="chat-wrapper">
    <div class="chat-container">
      <div class="chat-header">AI 聊天机器人</div>

      <div class="chat-body" ref="chatBody">
        <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
          <img
              :src="msg.role === 'user' ? userAvatar : botAvatar"
              class="avatar"
              alt="avatar"
          />
          <div class="bubble" v-html="formatContent(msg.content)" />
        </div>

        <div v-if="loading" class="message assistant">
          <img :src="botAvatar" class="avatar" />
          <div class="bubble typing">AI 正在思考中...</div>
        </div>
      </div>

      <div class="chat-input">
        <el-input
            v-model="input"
            placeholder="请输入你的问题"
            @keydown.enter.native="sendMessage"
            clearable
            class="input-box"
        />
        <el-button type="primary" @click="sendMessage">发送</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { ElMessage } from 'element-plus'

const input = ref('')
const messages = ref([
  { role: 'user', content: '你好，回答时请用中文。' },
  { role: 'assistant', content: '你好，有什么我可以帮你解答的？' },
])
const loading = ref(false)
const chatBody = ref(null)

const userAvatar = 'https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/svg/1f464.svg'
const botAvatar = 'https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/svg/1f916.svg'

const scrollToBottom = () => {
  nextTick(() => {
    if (chatBody.value) {
      chatBody.value.scrollTop = chatBody.value.scrollHeight
    }
  })
}

const sendMessage = async () => {
  if (!input.value.trim()) return

  const userMessage = { role: 'user', content: input.value }
  messages.value.push(userMessage)
  input.value = ''
  loading.value = true
  scrollToBottom()

  const payload = {
    model: 'deepseek-chat',
    messages: messages.value.slice(-5),
  }

  try {
    const res = await fetch('http://localhost:8080/api/ai/chat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })

    const data = await res.json()
    const aiReply = data.choices[0].message
    messages.value.push(aiReply)
  } catch (err) {
    console.error('请求失败', err)
    messages.value.push({
      role: 'assistant',
      content: '😥 请求失败，请稍后再试。',
    })
    ElMessage.error('请求失败，请检查后端服务是否启动')
  } finally {
    loading.value = false
    scrollToBottom()
  }
}

const formatContent = (text) => {
  return text
      .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
      .replace(/\n/g, '<br/>')
}
</script>

<style scoped>
.chat-wrapper {
  min-height: 100vh;
  background: white;
  display: flex;
  justify-content: center;
  padding: 20px;
  box-sizing: border-box;
}

.chat-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 1000px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  flex-grow: 1;
}

.chat-header {
  background-color: #409eff;
  color: white;
  padding: 16px;
  font-size: 20px;
  font-weight: bold;
  text-align: center;
}

.chat-body {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.message {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
}

.message.user {
  flex-direction: row-reverse;
}

.avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin: 0 10px;
}

.bubble {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 12px;
  background-color: #f2f2f2;
  color: #333;
  word-break: break-word;
  line-height: 1.6;
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.05);
}

.message.user .bubble {
  background-color: #409eff;
  color: white;
}

.typing {
  font-style: italic;
  color: #999;
}

.chat-input {
  display: flex;
  padding: 16px;
  border-top: 1px solid #eee;
  gap: 10px;
  background-color: #fff;
}

.input-box {
  flex: 1;
}
</style>
