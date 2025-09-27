<template>
	<view><< 返回首页</view>
  <div class="history-container">
    <el-card shadow="never" class="history-card">
      <template #header>
        <div class="card-header">
          <span class="title">道路申报历史</span>
          <div class="header-actions">
            <el-input
              v-model="searchQuery"
              placeholder="搜索申报记录"
              clearable
              style="width: 300px"
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button icon="el-icon-search" @click="handleSearch" />
              </template>
            </el-input>
            <el-button type="primary" icon="el-icon-refresh" @click="refreshData">
              刷新
            </el-button>
          </div>
        </div>
      </template>

      <el-table
        :data="filteredRecords"
        style="width: 100%"
        v-loading="loading"
        :default-sort="{ prop: 'reportTime', order: 'descending' }"
      >
        <el-table-column prop="reportTime" label="申报时间" sortable width="180">
          <template #default="{ row }">
            {{ formatDate(row.reportTime) }}
          </template>
        </el-table-column>
        
        <el-table-column prop="location" label="隐患位置" width="200" />
        
        <el-table-column prop="description" label="问题描述" show-overflow-tooltip />
        
        <el-table-column prop="status" label="处理状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="dangerLevel" label="危险等级" width="120">
          <template #default="{ row }">
            <el-progress 
              :percentage="row.dangerLevel" 
              :color="getDangerColor(row.dangerLevel)"
              :show-text="false"
              :stroke-width="15"
            />
            <span style="margin-left: 8px">{{ getDangerText(row.dangerLevel) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="viewDetail(row)">详情</el-button>
            <el-button 
              size="small" 
              type="primary" 
              v-if="row.status === 0"
              @click="followUp(row)"
            >
              跟进
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="totalRecords"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 申报详情对话框 -->
    <el-dialog v-model="detailVisible" title="申报详情" width="50%">
      <div v-if="currentRecord" class="detail-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="申报时间">
            {{ formatDate(currentRecord.reportTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="隐患位置">
            {{ currentRecord.location }}
          </el-descriptions-item>
          <el-descriptions-item label="问题描述">
            {{ currentRecord.description || '无补充描述' }}
          </el-descriptions-item>
          <el-descriptions-item label="危险等级">
            <el-tag :type="getDangerTagType(currentRecord.dangerLevel)">
              {{ getDangerText(currentRecord.dangerLevel) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="处理状态">
            <el-tag :type="getStatusType(currentRecord.status)">
              {{ getStatusText(currentRecord.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="处理进展" v-if="currentRecord.process">
            <el-timeline>
              <el-timeline-item
                v-for="(item, index) in currentRecord.process"
                :key="index"
                :timestamp="formatDate(item.time)"
              >
                {{ item.action }}
              </el-timeline-item>
            </el-timeline>
          </el-descriptions-item>
          <el-descriptions-item label="现场图片">
            <div class="image-gallery">
              <el-image
                v-for="(img, index) in currentRecord.images"
                :key="index"
                :src="img"
                :preview-src-list="currentRecord.images"
                fit="cover"
                class="gallery-image"
              />
            </div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

// 模拟数据
const mockRecords = [
  {
    id: 1,
    reportTime: new Date('2025-2-15 09:30:00'),
    location: '环球中心停车场附近',
    description: '路面有长约2米的裂缝，存在安全隐患',
    dangerLevel: 75,
    status: 2, // 0: 待处理, 1: 处理中, 2: 已解决
    images: [
      'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg',
      'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg'
    ],
    process: [
      { time: new Date('2023-12-15 10:00:00'), action: '已受理申报' },
      { time: new Date('2023-12-16 14:30:00'), action: '现场勘察完成' },
      { time: new Date('2023-12-18 09:00:00'), action: '道路修复施工完成' }
    ]
  },
  {
    id: 2,
    reportTime: new Date('2025-3-24 17:54:23'),
    location: '春熙路银石广场北3门门口',
    description: '路面有明显坑洼，存在安全隐患',
    dangerLevel: 70,
    status: 2, // 0: 待处理, 1: 处理中, 2: 已解决
    images: [
      'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg',
      'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg'
    ],
    process: [
      { time: new Date('2023-12-15 10:00:00'), action: '已受理申报' },
      { time: new Date('2023-12-16 14:30:00'), action: '现场勘察完成' },
      { time: new Date('2023-12-18 09:00:00'), action: '道路修复施工完成' }
    ]
  },
  {
    id: 3,
    reportTime: new Date('2025-3-30 14:21:34'),
    location: '中和地铁站C出口',
    description: '路面抛洒物',
    dangerLevel: 35,
    status: 2, // 0: 待处理, 1: 处理中, 2: 已解决
    images: [
      'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg',
      'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg'
    ],
    process: [
      { time: new Date('2023-12-15 10:00:00'), action: '已受理申报' },
      { time: new Date('2023-12-16 14:30:00'), action: '现场勘察完成' },
      { time: new Date('2023-12-18 09:00:00'), action: '道路修复施工完成' }
    ]
  },
  {
    id: 4,
    reportTime: new Date('2025-4-14 18:58:15'),
    location: '成华区二环路王府井门口',
    description: '道路坑洼',
    dangerLevel: 15,
    status: 0, // 0: 待处理, 1: 处理中, 2: 已解决
    images: [
      'static/pic1.png',
      'static/pic2.png'
    ],
    process: [
      { time: new Date('2025-4-14 18:58:15'), action: '用户申报' },
      { time: new Date('2024-4-14 18:58:20'), action: '待受理' },
    ]
  },
  // 更多模拟数据...
]

const loading = ref(false)
const records = ref([])
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalRecords = ref(0)
const detailVisible = ref(false)
const currentRecord = ref(null)

const filteredRecords = computed(() => {
  return records.value.filter(item => 
    item.location.includes(searchQuery.value) || 
    (item.description && item.description.includes(searchQuery.value))
  )
})

// 状态类型和文本映射
const statusMap = [
  { type: 'danger', text: '待处理' },
  { type: 'warning', text: '处理中' },
  { type: 'success', text: '已解决' }
]

// 危险等级颜色和文本映射
const dangerLevelMap = [
  { min: 0, max: 30, color: '#67C23A', tag: 'success', text: '低风险' },
  { min: 30, max: 70, color: '#E6A23C', tag: 'warning', text: '中风险' },
  { min: 70, max: 100, color: '#F56C6C', tag: 'danger', text: '高风险' }
]

onMounted(() => {
  fetchData()
})

const fetchData = () => {
  loading.value = true
  // 模拟API请求
  setTimeout(() => {
    records.value = mockRecords
    totalRecords.value = mockRecords.length
    loading.value = false
  }, 800)
}

const refreshData = () => {
  fetchData()
}

const handleSearch = () => {
  currentPage.value = 1
}

const handleSizeChange = (val) => {
  pageSize.value = val
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

const formatDate = (date) => {
  return new Date(date).toLocaleString()
}

const getStatusType = (status) => {
  return statusMap[status]?.type || ''
}

const getStatusText = (status) => {
  return statusMap[status]?.text || ''
}

const getDangerColor = (level) => {
  const item = dangerLevelMap.find(d => level >= d.min && level < d.max)
  return item?.color || '#E6A23C'
}

const getDangerTagType = (level) => {
  const item = dangerLevelMap.find(d => level >= d.min && level < d.max)
  return item?.tag || 'warning'
}

const getDangerText = (level) => {
  const item = dangerLevelMap.find(d => level >= d.min && level < d.max)
  return item?.text || '中风险'
}

const viewDetail = (record) => {
  currentRecord.value = record
  detailVisible.value = true
}

const followUp = (record) => {
  ElMessage.info(`已跟进申报ID: ${record.id}`)
}
</script>

<style scoped>
.history-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.history-card {
  border-radius: 8px;
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

.header-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.detail-content {
  padding: 10px;
}

.image-gallery {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.gallery-image {
  width: 120px;
  height: 120px;
  border-radius: 4px;
  cursor: pointer;
}

:deep(.el-progress-bar) {
  margin-right: 0;
}
</style>
