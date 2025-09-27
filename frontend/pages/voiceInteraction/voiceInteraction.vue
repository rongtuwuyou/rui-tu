<template>
  <view class="container">
    <!-- 语音交互面板 -->
    <view class="voice-panel">
      <view class="transcript-box">
        <scroll-view scroll-y class="dialog-history">
          <view v-for="(item, index) in dialogHistory" :key="index" class="dialog-item">
            <text :class="item.type">{{ item.content }}</text>
          </view>
        </scroll-view>
      </view>

      <!-- 语音控制区 -->
      <view class="control-area">
        <button 
          class="voice-btn" 
          :class="{ 'recording': isRecording }"
          @touchstart="startRecording"
          @touchend="stopRecording">
          <uni-icons type="mic" size="30" color="#fff"></uni-icons>
        </button>
        <view class="status-text">{{ statusMessage }}</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      // 删除地图相关数据
      isRecording: false,
      statusMessage: '点击麦克风开始语音查询',
      dialogHistory: []
    }
  },
  methods: {
    startRecording() {
      this.isRecording = true
      this.statusMessage = '正在聆听...'
      // 调用录音接口
      uni.startRecord({
        success: () => {
          console.log('录音开始')
        }
      })
    },
    stopRecording() {
      this.isRecording = false
      this.statusMessage = '处理中...'
      uni.stopRecord({
        success: (res) => {
          this.sendAudio(res.tempFilePath)
        }
      })
    },
    async sendAudio(filePath) {
      try {
        const response = await uni.uploadFile({
          url: 'https://api.example.com/voice-process',
          filePath: filePath,
          name: 'audio'
        })
        const result = JSON.parse(response.data)
        this.updateDialog(result)
        this.updateTrafficData(result)
      } catch (error) {
        this.statusMessage = '请求失败，请重试'
      }
    },
    updateDialog(result) {
      this.dialogHistory.push(
        { type: 'user', content: result.userText },
        { type: 'ai', content: result.aiResponse }
      )
    },
    updateTrafficData(result) {
      this.mapCenter = result.trafficData.center
      this.markers = result.trafficData.markers
      this.polyline = result.trafficData.routes
      this.statusMessage = '准备就绪'
    }
  }
}
</script>

<style lang="scss">
.container {
  padding: 0;
  height: 100vh;
  background: #1A1A1A;
}

.voice-panel {
  height: 100%;
  padding: 20px;
  background: linear-gradient(180deg, #2B2B2B 0%, #1A1A1A 100%);
  border-radius: 0;
}

.transcript-box {
  height: 200px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 10px;
  margin-bottom: 15px;
}

.dialog-item {
  margin: 8px 0;
  
  .user {
    color: #415058;
    background: #f0f4f8;
    padding: 8px 12px;
    border-radius: 8px;
    float: right;
  }
  
  .ai {
    color: white;
    background: #51A8A9;
    padding: 8px 12px;
    border-radius: 8px;
    float: left;
  }
}

.control-area {
  display: flex;
  flex-direction: column;
  align-items: center;

  .voice-btn {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    background: #51A8A9;
    display: flex;
    justify-content: center;
    align-items: center;
    transition: all 0.3s;

    &.recording {
      transform: scale(1.1);
      box-shadow: 0 0 10px rgba(81,168,169,0.5);
    }
  }

  .status-text {
    margin-top: 10px;
    color: #909399;
    font-size: 14px;
  }
}

<style lang="scss">
.voice-btn {
  width: 80px;
  height: 80px;
  background: #FFAA00;
  box-shadow: 0 0 30px rgba(255,170,0,0.3);
  animation: breath 1.5s ease-in-out infinite;
}

.dialog-item {
  .ai {
    background: #51A8A9;
    border-radius: 15px;
    margin: 10px 0;
    transform: scale(0.95);
    transition: all 0.3s;
  }
}

.voice-panel {
  background: linear-gradient(180deg, #1E1E1E 0%, #2D2D2D 100%);
}
</style>
@keyframes breath {
  0% { transform: scale(1); opacity: 0.8; }
  50% { transform: scale(1.1); opacity: 1; }
  100% { transform: scale(1); opacity: 0.8; }
}
</style>
