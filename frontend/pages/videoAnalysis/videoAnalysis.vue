<template>
  <div class="video-analysis-container">
    <!-- 标题区域 -->
    <div class="header">
      <h2>交通事故视频分析</h2>
      <p class="subtitle">智能识别视频中的交通事故情况</p>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 视频播放区域 -->
      <div class="video-section">
        <video 
          ref="videoPlayer" 
          controls 
          class="video-player">
          <!-- 移除poster属性 -->
          <source src="@/static/sample-accident.mp4" type="video/mp4">
          您的浏览器不支持视频播放
        </video>
        
        <!-- 视频控制按钮 -->
        <div class="video-controls">
          <button @click="analyzeVideo" class="analyze-btn">
            <i class="el-icon-search"></i> 开始分析
          </button>
          <button @click="resetAnalysis" class="reset-btn">
            <i class="el-icon-refresh"></i> 重置
          </button>
        </div>
      </div>

      <!-- 分析结果区域 -->
      <div class="analysis-section">
        <div class="result-card" v-if="analysisResult">
          <h3 class="result-title">分析结果</h3>
          
          <div class="result-item">
            <div class="label">事故类型:</div>
            <div class="value">{{ analysisResult.type }}</div>
          </div>
          
          <div class="result-item">
            <div class="label">严重程度:</div>
            <div class="value">
              <el-rate
                v-model="analysisResult.severity"
                disabled
                :colors="['#20B2AA', '#20B2AA', '#20B2AA']">
              </el-rate>
            </div>
          </div>
          
          <div class="result-item">
            <div class="label">关键帧:</div>
            <div class="key-frames">
              <img 
                v-for="(frame, index) in analysisResult.keyFrames" 
                :key="index" 
                :src="frame" 
                class="key-frame"
                @click="showFrameDetail(frame)">
            </div>
          </div>
        </div>
        
        <div class="empty-state" v-else>
          <i class="el-icon-video-camera"></i>
          <p>请上传或选择视频进行分析</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'VideoAnalysis',
  data() {
    return {
      analysisResult: null,
      sampleResult: {
        type: '追尾碰撞',
        severity: 3,
        keyFrames: [
          require('@/assets/frame1.jpg'),
          require('@/assets/frame2.jpg'),
          require('@/assets/frame3.jpg')
        ]
      }
    }
  },
  methods: {
    analyzeVideo() {
      // 模拟分析过程
      this.$message.info('正在分析视频...');
      
      setTimeout(() => {
        this.analysisResult = this.sampleResult;
        this.$message.success('分析完成');
      }, 2000);
    },
    resetAnalysis() {
      this.analysisResult = null;
      this.$refs.videoPlayer.currentTime = 0;
      this.$refs.videoPlayer.pause();
    },
    showFrameDetail(frame) {
      this.$alert(`<img src="${frame}" style="width:100%">`, '关键帧详情', {
        dangerouslyUseHTMLString: true,
        customClass: 'frame-detail-dialog'
      });
    }
  }
}
</script>

<style scoped>
.video-analysis-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.header {
  text-align: center;
  margin-bottom: 30px;
}

.header h2 {
  color: #333;
  font-size: 28px;
}

.subtitle {
  color: #20B2AA; /* 青绿色点缀 */
  font-size: 16px;
}

.main-content {
  display: flex;
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.video-section {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.video-player {
  width: 100%;
  border-radius: 4px;
  margin-bottom: 15px;
}

.video-controls {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.analyze-btn, .reset-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.analyze-btn {
  background-color: #20B2AA; /* 青绿色 */
  color: white;
}

.analyze-btn:hover {
  background-color: #1E9C96;
}

.reset-btn {
  background-color: #f5f5f5;
  color: #666;
}

.reset-btn:hover {
  background-color: #e8e8e8;
}

.analysis-section {
  flex: 1;
}

.result-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.result-title {
  color: #333;
  font-size: 20px;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #20B2AA; /* 青绿色点缀 */
}

.result-item {
  margin-bottom: 15px;
}

.label {
  font-weight: bold;
  color: #666;
  margin-bottom: 5px;
}

.value {
  color: #333;
}

.key-frames {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.key-frame {
  width: 80px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  cursor: pointer;
  transition: transform 0.3s;
}

.key-frame:hover {
  transform: scale(1.05);
}

.empty-state {
  text-align: center;
  padding: 50px 0;
  color: #999;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.empty-state i {
  font-size: 50px;
  color: #20B2AA; /* 青绿色点缀 */
  margin-bottom: 15px;
}

/* 对话框样式 */
.frame-detail-dialog {
  width: 70%;
}
</style>