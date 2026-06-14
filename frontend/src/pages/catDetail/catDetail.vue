<template>
  <view class="detail-wrap">
    <!-- 1. 顶部猫咪大图 -->
    <image class="big-img" :src="catInfo.avatar" mode="aspectFill"></image>

    <!-- 2. 基础信息区域 -->
    <view class="info-card">
      <view class="name-row">
        <text class="cat-name">{{ catInfo.name }}</text>
        <view 
          class="tag" 
          :class="catInfo.isSterilized ? 'tag-ok' : 'tag-no'"
        >
          {{ catInfo.isSterilized ? '已绝育' : '未绝育' }}
        </view>
      </view>
      <view class="desc-item">
        <text class="label">出没地点：</text>
        <text class="content">{{ catInfo.location }}</text>
      </view>
      <view class="desc-item">
        <text class="label">常驻位置：</text>
        <text class="content">{{ catInfo.sleepPlace }}</text>
      </view>
      <view class="desc-item">
        <text class="label">喜爱食物：</text>
        <text class="content">{{ catInfo.favoriteFood }}</text>
      </view>
    </view>

    <!-- 3. 历史投喂记录 -->
    <view class="record-card">
      <view class="record-title">📋 历史投喂记录</view>
      <view class="record-list">
        <!-- 循环渲染投喂记录 -->
        <view class="record-item" v-for="(item, index) in catInfo.feedList" :key="index">
          <text class="record-time">{{ item.feedTime }}</text>
          <text class="record-user">{{ item.username }}</text>
          <text class="record-loc">{{ item.feedLocation }}</text>
        </view>
      </view>
      <!-- 暂无记录兜底 -->
      <view class="empty-tip" v-if="catInfo.feedList && catInfo.feedList.length === 0">
        暂时还没有投喂记录哦
      </view>
    </view>

    <!-- 4. 底部固定投喂按钮 -->
    <view class="bottom-btn" @click="goFeed" hover-class="btn-hover">
      立即投喂
    </view>
  </view>
</template>

<script>
import { getCatDetail } from '@/api/index.js'

export default {
  name: "CatDetail",
  data() {
    return {
      catInfo: {},
      // mock 兜底数据（后端接口未就绪时使用）
      mockCatData: [
        {
          id: 1,
          name: "橘猫大胖",
          avatar: "https://picsum.photos/400/300?random=1",
          location: "一食堂门口",
          sleepPlace: "食堂旁黄色三轮车底下",
          favoriteFood: "猫粮、鸡胸肉、火腿肠",
          isSterilized: true,
          feedList: [
            { username: "小明", feedTime: "2026-06-09 08:20", feedLocation: "一食堂门口" },
            { username: "小李", feedTime: "2026-06-09 12:10", feedLocation: "一食堂门口" }
          ]
        },
        {
          id: 2,
          name: "三花小花",
          avatar: "https://picsum.photos/400/300?random=2",
          location: "三号教学楼楼下",
          sleepPlace: "教学楼白色轿车车顶",
          favoriteFood: "罐头、小鱼干",
          isSterilized: false,
          feedList: [
            { username: "小张", feedTime: "2026-06-08 17:30", feedLocation: "三号教学楼楼下" }
          ]
        },
        {
          id: 3,
          name: "黑白警长",
          avatar: "https://picsum.photos/400/300?random=3",
          location: "图书馆后侧草坪",
          sleepPlace: "草坪大树下石凳",
          favoriteFood: "猫粮、面包",
          isSterilized: true,
          feedList: []
        },
        {
          id: 4,
          name: "狸花阿狸",
          avatar: "https://picsum.photos/400/300?random=4",
          location: "操场围栏旁",
          sleepPlace: "操场器材房角落",
          favoriteFood: "小鱼干、猫粮",
          isSterilized: false,
          feedList: [
            { username: "小王", feedTime: "2026-06-09 18:00", feedLocation: "操场围栏旁" },
            { username: "小周", feedTime: "2026-06-09 19:20", feedLocation: "操场围栏旁" }
          ]
        }
      ]
    }
  },
  onLoad(options) {
    const catId = Number(options.catId)
    console.log("当前猫咪ID：", catId)
    this.fetchCatDetail(catId)
  },
  methods: {
    /** 从后端获取猫咪详情，失败则使用 mock 数据 */
    async fetchCatDetail(catId) {
      try {
        const result = await getCatDetail(catId)
        if (result && result.id) {
          this.catInfo = result
          return
        }
      } catch (e) {
        console.log('后端未响应，使用兜底数据', e)
      }
      this.catInfo = this.mockCatData.find(item => item.id === catId) || {}
    },
    // 跳转到投喂打卡页
    goFeed() {
      uni.navigateTo({
        url: "/pages/feed/feed"
      })
    }
  }
}
</script>

<style scoped>
.detail-wrap {
  padding-bottom: 120rpx;
  background-color: #fff8f2;
}
.btn-hover {
  opacity: 0.5;
}
.big-img {
  width: 100%;
  height: 460rpx;
}
.info-card {
  margin: 20rpx;
  padding: 30rpx;
  background: #fff;
  border-radius: 16rpx;
}
.name-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}
.cat-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}
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
.desc-item {
  display: flex;
  margin-top: 16rpx;
  font-size: 28rpx;
}
.label {
  color: #999;
  width: 140rpx;
}
.content {
  flex: 1;
  color: #333;
}
.record-card {
  margin: 0 20rpx;
  padding: 30rpx;
  background: #fff;
  border-radius: 16rpx;
}
.record-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  padding-bottom: 10rpx;
  border-bottom: 1rpx solid #f0e6dc;
}
.record-item {
  display: flex;
  justify-content: space-between;
  font-size: 26rpx;
  color: #666;
  padding: 12rpx 0;
  border-bottom: 1rpx dashed #f0e6dc;
}
.empty-tip {
  text-align: center;
  font-size: 26rpx;
  color: #999;
  padding: 30rpx 0;
}
.bottom-btn {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 100rpx;
  line-height: 100rpx;
  text-align: center;
  background-color: #ff974a;
  color: #fff;
  font-size: 32rpx;
}
</style>
