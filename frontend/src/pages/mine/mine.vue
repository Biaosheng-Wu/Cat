<template>
  <view class="mine-wrap">
    <!-- 1. 个人信息头部卡片 -->
    <view class="user-card">
      <image class="avatar" src="/static/avatar.png" mode="aspectFill"></image>
      <view class="user-info">
        <text class="nickname">校园用户</text>
        <text class="desc">流浪猫救助志愿者</text>
      </view>
    </view>

    <!-- 2. 功能菜单列表 -->
    <view class="menu-card">
      <view class="menu-item" @click="toggleRecord">
        <text class="menu-text">我的投喂记录</text>
        <text class="arrow">{{ showRecord ? '▲' : '▼' }}</text>
      </view>
      <view class="menu-item" @click="goAbout">
        <text class="menu-text">关于本小程序</text>
        <text class="arrow">></text>
      </view>
    </view>

    <!-- 3. 我的投喂记录区域 -->
    <view class="record-card" v-if="showRecord">
      <view class="record-title">投喂历史</view>
      <view class="record-list">
        <view class="record-item" v-for="(item, idx) in myFeedList" :key="idx">
          <view class="record-left">
            <text class="cat-name">{{ item.catName }}</text>
            <text class="feed-time">{{ item.feedTime }}</text>
          </view>
          <view class="record-right">
            <text class="feed-loc">{{ item.location }}</text>
          </view>
        </view>
      </view>
      <view class="empty-tip" v-if="myFeedList.length === 0">
        你还没有投喂记录，快去帮助校园流浪猫吧~
      </view>
    </view>
  </view>
</template>

<script>
import { getMyFeedList } from '@/api/index.js'

export default {
  name: "Mine",
  data() {
    return {
      showRecord: false,
      myFeedList: [],
      // mock 兜底数据
      mockFeedList: [
        {
          catName: "橘猫大胖",
          feedTime: "2026-06-09 08:20",
          location: "一食堂门口"
        },
        {
          catName: "狸花阿狸",
          feedTime: "2026-06-09 18:00",
          location: "操场围栏旁"
        }
      ]
    }
  },
  methods: {
    /** 展开/收起投喂记录，首次展开时从后端获取数据 */
    async toggleRecord() {
      this.showRecord = !this.showRecord
      if (this.showRecord && this.myFeedList.length === 0) {
        try {
          const result = await getMyFeedList()
          const list = Array.isArray(result) ? result : (result.data || [])
          if (list.length > 0) {
            this.myFeedList = list
            return
          }
        } catch (e) {
          console.log('后端未响应，使用兜底数据', e)
        }
        this.myFeedList = this.mockFeedList
      }
    },
    goAbout() {
      uni.navigateTo({
        url: "/pages/about/about"
      })
    }
  }
}
</script>

<style scoped>
.mine-wrap {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}
.user-card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}
.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background-color: #eee;
}
.user-info {
  margin-left: 30rpx;
}
.nickname {
  font-size: 34rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 10rpx;
}
.desc {
  font-size: 26rpx;
  color: #999;
}
.menu-card {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}
.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}
.menu-item:last-child {
  border-bottom: none;
}
.menu-text {
  font-size: 28rpx;
  color: #333;
}
.arrow {
  font-size: 28rpx;
  color: #999;
}
.record-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-top: 20rpx;
}
.record-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  padding-bottom: 10rpx;
  border-bottom: 1rpx solid #eee;
}
.record-item {
  display: flex;
  justify-content: space-between;
  padding: 20rpx 0;
  border-bottom: 1rpx dashed #f0f0f0;
}
.record-item:last-child {
  border-bottom: none;
}
.cat-name {
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
}
.feed-time {
  font-size: 24rpx;
  color: #999;
}
.feed-loc {
  font-size: 26rpx;
  color: #666;
}
.empty-tip {
  text-align: center;
  font-size: 26rpx;
  color: #999;
  padding: 40rpx 0;
}
</style>
