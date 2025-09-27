

<template>
	<view @click="toHome"><< 返回首页</view>
  <div class="declare-container">
    <el-steps :active="activeStep" finish-status="success" align-center class="steps">
      <el-step title="上传图片" />
      <el-step title="生成报告" />
      <el-step title="完善信息" />
      <el-step title="提交申报" />
    </el-steps>

    <el-card class="declare-card" shadow="always">
      <template #header>
        <div class="card-header">
          <span class="title">{{ stepTitles[activeStep] }}</span>
          <div class="step-actions">
            <el-button v-if="activeStep > 0" @click="prevStep">上一步</el-button>
            <el-button 
              type="primary" 
              @click="nextStep"
              :disabled="!canProceed"
              :loading="processing"
            >
              {{ activeStep === 3 ? '提交申报' : '下一步' }}
            </el-button>
          </div>
        </div>
      </template>

      <!-- 步骤1: 上传图片 -->
      <div v-show="activeStep === 0" class="step-content">
        <el-upload
          class="image-uploader"
          action="#"
          list-type="picture-card"
          :auto-upload="false"
          :limit="5"
          :on-change="handleImageChange"
          :on-remove="handleImageRemove"
          multiple
        >
          <el-icon><Plus /></el-icon>
          <template #tip>
            <div class="upload-tip">
              <el-icon><InfoFilled /></el-icon>
              <span>上传道路隐患图片(最多5张)</span>
            </div>
          </template>
        </el-upload>
      </div>

      <!-- 步骤2: 生成报告 -->
      <div v-show="activeStep === 1" class="step-content">
        <div class="report-generation">
          <el-result
            v-if="!reportGenerated"
            icon="el-icon-document-checked"
            title="点击生成智能道路报告"
            sub-title="基于上传的图片分析道路隐患情况"
          >
            <template #extra>
              <el-button 
                type="primary" 
                size="large" 
                @click="generateReport"
                :disabled="uploadedImages.length === 0"
                :loading="generatingReport"
              >
                生成报告
              </el-button>
            </template>
          </el-result>

          <div v-else class="report-preview">
            <el-alert 
              :title="`隐患等级: ${dangerText}`" 
              :type="dangerAlertType" 
              show-icon 
              :closable="false"
            />
            <el-divider />
            <el-scrollbar height="400px">
              <div class="report-details">
                <h4>主要问题:</h4>
                <el-tag 
                  v-for="(issue, index) in detectedIssues" 
                  :key="index" 
                  :type="issueTypes[index % issueTypes.length]"
                  class="issue-tag"
                >
                  {{ issue }}
                </el-tag>
                
                <h4>AI分析建议:</h4>
                <el-card shadow="never" class="suggestion-card">
                  {{ analysisText }}
                </el-card>
              </div>
            </el-scrollbar>
          </div>
        </div>
      </div>

      <!-- 步骤3: 完善信息 -->
      <div v-show="activeStep === 2" class="step-content">
        <el-form ref="form" :model="formData" :rules="rules" label-position="top">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="隐患位置" prop="location">
                <el-input
                  v-model="formData.location"
                  placeholder="请输入隐患位置"
                  clearable
                >
                  <template #prepend>
                    <el-button icon="el-icon-location" @click="selectOnMap" />
                  </template>
                </el-input>
              </el-form-item>
              
              <el-form-item label="补充描述" prop="description">
                <el-input
                  type="textarea"
                  :rows="5"
                  v-model="formData.description"
                  :placeholder="descriptionPlaceholder"
                  clearable
                />
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-card shadow="never" class="report-hints">
                <template #header>
                  <div class="hints-header">
                    <el-icon><Warning /></el-icon>
                    <span>报告提示</span>
                  </div>
                </template>
                <ul class="hints-list">
                  <li v-for="(hint, index) in reportHints" :key="index">
                    {{ hint }}
                  </li>
                </ul>
              </el-card>
            </el-col>
          </el-row>
        </el-form>
      </div>

      <!-- 步骤4: 提交确认 -->
      <div v-show="activeStep === 3" class="step-content">
        <el-result
          icon="success"
          title="确认申报信息"
          sub-title="请检查以下信息是否正确"
        >
          <template #extra>
            <el-descriptions :column="1" border class="declaration-summary">
              <el-descriptions-item label="申报时间">
                {{ new Date().toLocaleString() }}  <!-- 添加时间信息 -->
              </el-descriptions-item>
              <el-descriptions-item label="隐患位置">{{ formData.location }}</el-descriptions-item>
              <el-descriptions-item label="问题描述">
                {{ formData.description || '无补充描述' }}  <!-- 处理空描述情况 -->
              </el-descriptions-item>
              <el-descriptions-item label="隐患等级">
                <el-tag :type="dangerAlertType">{{ dangerText }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="上传图片">
                <div class="image-thumbnails">
                  <el-image
                    v-for="(image, index) in uploadedImages"
                    :key="index"
                    :src="image.url"
                    fit="cover"
                    class="thumbnail"
                  />
                </div>
              </el-descriptions-item>
            </el-descriptions>
          </template>
        </el-result>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Plus, InfoFilled, Warning } from '@element-plus/icons-vue'

const activeStep = ref(0)
const stepTitles = ref(['上传隐患图片', '生成分析报告', '完善申报信息', '提交申报'])
const processing = ref(false)
const generatingReport = ref(false)
const reportGenerated = ref(false)

const formData = ref({
  description: '',
  location: ''
})

const rules = {
  description: [
    { required: false, message: '请补充描述(可选)', trigger: 'blur' },  // 改为非必填
    { min: 10, message: '描述至少需要10个字符', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '请输入位置信息', trigger: 'blur' }
  ]
}

const uploadedImages = ref([])
const dangerLevel = ref(0)
const detectedIssues = ref([])
const reportHints = ref([])
const issueTypes = ['', 'success', 'info', 'warning', 'danger']

const dangerText = computed(() => {
  if (dangerLevel.value < 30) return '低风险'
  if (dangerLevel.value < 70) return '中风险'
  return '高风险'
})

const dangerAlertType = computed(() => {
  if (dangerLevel.value < 30) return 'success'
  if (dangerLevel.value < 70) return 'warning'
  return 'error'
})

const analysisText = ref('')
const descriptionPlaceholder = computed(() => {
  return reportGenerated.value 
    ? '请根据报告内容补充详细描述...' 
    : '请描述道路隐患情况...'
})

const canProceed = computed(() => {
  switch(activeStep.value) {
    case 0: return uploadedImages.value.length > 0
    case 1: return reportGenerated.value
    case 2: return formData.value.description && formData.value.location
    default: return true
  }
})

const handleImageChange = (file) => {
  uploadedImages.value.push(file)
}

const handleImageRemove = (file) => {
  const index = uploadedImages.value.findIndex(f => f.uid === file.uid)
  if (index !== -1) {
    uploadedImages.value.splice(index, 1)
  }
}

const generateReport = () => {
  generatingReport.value = true
  // 模拟AI分析过程
  setTimeout(() => {
    dangerLevel.value = Math.floor(Math.random() * 100)
    detectedIssues.value = [
      '道路裂缝(长度: 1.2m)',
      '路面凹陷(深度: 8cm)',
      '排水不畅'
    ]
    reportHints.value = [
      '建议描述裂缝的具体位置和走向',
      '请说明凹陷是否影响车辆通行',
      '如有积水情况请补充说明'
    ]
    analysisText.value = '根据图像分析，该路段存在多处明显损坏，主要问题为纵向裂缝和局部凹陷，可能由重型车辆碾压和地基沉降导致。建议尽快安排专业人员进行详细勘察和维修，以防止进一步恶化。'
    reportGenerated.value = true
    generatingReport.value = false
  }, 2000)
}

const selectOnMap = () => {
  console.log('打开地图选择位置')
}

const nextStep = () => {
  if (activeStep.value === 3) {
    submitDeclaration()
  } else {
    activeStep.value++
  }
}

const prevStep = () => {
  activeStep.value--
}

const submitDeclaration = () => {
  processing.value = true
  // 模拟提交
  setTimeout(() => {
    processing.value = false
    ElMessage.success('申报提交成功！')
    // 重置表单
    activeStep.value = 0
    formData.value = { description: '', location: '' }
    uploadedImages.value = []
    reportGenerated.value = false
  }, 1500)
}
</script>

<style scoped>
.declare-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.steps {
  margin-bottom: 30px;
}

.declare-card {
  border-radius: 12px;
  border: 1px solid var(--el-border-color-light);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.step-actions {
  display: flex;
  gap: 10px;
}

.step-content {
  padding: 10px;
}

.image-uploader {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.upload-tip {
  margin-top: 20px;
  color: var(--el-text-color-secondary);
  display: flex;
  align-items: center;
  gap: 5px;
}

.report-generation {
  min-height: 400px;
  display: flex;
  flex-direction: column;
}

.report-preview {
  background-color: var(--el-bg-color-page);
  padding: 20px;
  border-radius: 8px;
}

.report-details {
  padding: 10px;
}

.issue-tag {
  margin-right: 8px;
  margin-bottom: 8px;
}

.suggestion-card {
  margin-top: 15px;
  background-color: var(--el-bg-color);
}

.report-hints {
  height: 100%;
}

.hints-header {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--el-color-warning);
}

.hints-list {
  padding-left: 20px;
  line-height: 2;
}

.declaration-summary {
  margin-top: 20px;
}

.image-thumbnails {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.thumbnail {
  width: 80px;
  height: 80px;
  border-radius: 4px;
}
</style>