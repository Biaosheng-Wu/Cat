<template>
  <!-- 页面外层容器 -->
  <view class="page-wrap">
    <!-- 猫咪列表容器 -->
    <view class="cat-list">
      <!-- 循环渲染每一只猫咪卡片 -->
      <view 
        class="cat-item" 
        v-for="cat in catList" 
        :key="cat.id"
        @click="goDetail(cat.id)"
      >
        <!-- 猫咪照片 -->
        <image class="cat-img" :src="cat.avatar" mode="aspectFill"></image>

        <!-- 猫咪信息区域 -->
        <view class="cat-info">
          <view class="cat-name">{{ cat.name }}</view>
          <view class="cat-location">出没地点：{{ cat.location }}</view>
          
          <!-- 绝育状态标签 -->
          <view 
            class="tag" 
            :class="cat.isSterilized ? 'tag-ok' : 'tag-no'"
          >
            {{ cat.isSterilized ? '已绝育' : '未绝育' }}
          </view>
        </view>

        <!-- 投喂按钮 -->
        <view class="feed-btn" @click.stop="goFeed" hover-class="btn-hover">去投喂</view>
      </view>
    </view>

    <!-- 加载中提示 -->
    <view class="loading-tip" v-if="loading">加载中...</view>

    <!-- 底部"录入猫咪"悬浮按钮 -->
    <view class="float-btn" @click="goAdd" hover-class="btn-hover">
      + 录入猫咪
    </view>
  </view>
</template>

<script>
import { getCatList } from '@/api/index.js'

export default {
  name: "CatList",
  data() {
    return {
      catList: [],
      loading: false,
      // mock 兜底数据（后端接口未就绪时使用）
      mockCatList: [
        {
          id: 1,
          name: "橘猫大胖",
          avatar: "https://picsum.photos/400/300?random=1",
          location: "一食堂门口",
          isSterilized: true
        },
        {
          id: 2,
          name: "三花小花",
          avatar: "https://picsum.photos/400/300?random=2",
          location: "三号教学楼楼下",
          isSterilized: false
        },
        {
          id: 3,
          name: "黑白警长",
          avatar: "https://picsum.photos/400/300?random=3",
          location: "图书馆后侧草坪",
          isSterilized: true
        },
        {
          id: 4,
          name: "狸花阿狸",
          avatar: "https://picsum.photos/400/300?random=4",
          location: "操场围栏旁",
          isSterilized: false
        }
      ]
    }
  },
  onLoad() {
    this.fetchCatList()
  },
  methods: {
    /** 从后端获取猫咪列表，失败则使用 mock 数据 */
    async fetchCatList() {
      this.loading = true
      try {
        const result = await getCatList()
        // 后端返回的可能是数组，也可能是 { data: [...] }
        const list = Array.isArray(result) ? result : (result.data || [])
        if (list.length > 0) {
          this.catList = list
          this.loading = false
          return
        }
      } catch (e) {
        console.log('后端未响应，使用兜底数据', e)
      }
      this.catList = this.mockCatList
      this.loading = false
    },
    // 跳转到猫咪详情页
    goDetail(catId) {
      uni.navigateTo({
        url: `/pages/catDetail/catDetail?catId=${catId}`
      })
    },
    // 跳转到投喂打卡页
    goFeed() {
      uni.navigateTo({
        url: "/pages/feed/feed"
      })
    },
    // 跳转到录入猫咪页
    goAdd() {
      uni.navigateTo({
        url: "/pages/catAdd/catAdd"
      })
    }
  }
}
</script>

<style scoped>
/* 页面整体容器 */
.page-wrap {
  padding: 20rpx;
  box-sizing: border-box;
}

.btn-hover {
  opacity: 0.8;
}

/* 猫咪列表：纵向列表布局 */
.cat-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

/* 单只猫咪卡片 */
.cat-item {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.08);
  transition: all 250ms ease;
}

.cat-item:active {
  transform: translateY(-4rpx);
  box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.12);
}

/* 猫咪图片 */
.cat-img {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  flex-shrink: 0;
}

/* 猫咪信息区域 */
.cat-info {
  flex: 1;
  margin: 0 20rpx;
}

.cat-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 10rpx;
}

.cat-location {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 12rpx;
}

/* 状态标签通用样式 */
.tag {
  display: inline-block;
  font-size: 24rpx;
  padding: 4rpx 12rpx;
  border-radius: 20rpx;
}
.tag-ok {
  background-color: #e8f5e9;
  color: #2e7d32;
}
.tag-no {
  background-color: #ffebee;
  color: #c62828;
}

/* 投喂按钮 */
.feed-btn {
  flex-shrink: 0;
  font-size: 28rpx;
  color: #fff;
  background-color: #ff974a;
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
}

.loading-tip {
  text-align: center;
  padding: 40rpx;
  color: #999;
  font-size: 28rpx;
}
.float-btn {
  position: fixed;
  right: 30rpx;
  bottom: 80rpx;
  background-color: #ff974a;
  color: #fff;
  font-size: 28rpx;
  padding: 18rpx 36rpx;
  border-radius: 40rpx;
  box-shadow: 0 4rpx 16rpx rgba(255, 151, 74, 0.35);
  z-index: 100;
}
</style>
