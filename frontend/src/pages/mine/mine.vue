<template>
  <view class="mine-wrap">
    <!-- 未登录提示 -->
    <view class="login-tip" v-if="!token" @click="goLogin">
      <text class="login-text">点击登录，开始救助之旅</text>
      <text class="arrow">></text>
    </view>

    <!-- 1. 个人信息头部卡片 -->
    <view class="user-card" v-if="token">
      <image class="avatar" src="/static/avatar.png" mode="aspectFill"></image>
      <view class="user-info">
        <text class="nickname">{{ userInfo.nickname || userInfo.username || '校园用户' }}</text>
        <text class="desc">流浪猫救助志愿者</text>
      </view>
      <view class="logout-btn" @click="doLogout" hover-class="btn-hover">退出</view>
    </view>

    <!-- 2. 功能菜单列表 -->
    <view class="menu-card" v-if="token">
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
      token: '',
      userInfo: {},
      showRecord: false,
      myFeedList: []
    }
  },
  onShow() {
    this.token = uni.getStorageSync('token') || ''
    try {
      this.userInfo = JSON.parse(uni.getStorageSync('userInfo') || '{}')
    } catch (e) {
      this.userInfo = {}
    }
  },
  methods: {
    goLogin() {
      uni.navigateTo({ url: '/pages/login/login' })
    },
    doLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            this.token = ''
            this.userInfo = {}
            this.showRecord = false
            this.myFeedList = []
          }
        }
      })
    },
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
          console.log('获取投喂记录失败', e)
        }
      }
    },
    goAbout() {
      uni.navigateTo({ url: "/pages/about/about" })
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
.btn-hover { opacity: 0.8; }

/* 未登录提示 */
.login-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #ff974a, #ff6b35);
  border-radius: 16rpx;
  padding: 40rpx;
  margin-bottom: 20rpx;
}
.login-text {
  font-size: 34rpx;
  color: #fff;
  font-weight: bold;
}
.arrow {
  font-size: 32rpx;
  color: #fff;
  margin-left: 15rpx;
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
  flex: 1;
  margin-left: 30rpx;
}
.nickname {
  font-size: 34rpx;
  font-weight: bold;
  color: #333;
}
.desc {
  display: block;
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
}
.logout-btn {
  font-size: 26rpx;
  color: #999;
  padding: 10rpx 20rpx;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
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
  border-bottom: 1rpx solid #f5f5f5;
}
.menu-text {
  font-size: 30rpx;
  color: #333;
}
.record-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-top: 20rpx;
}
.record-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}
.record-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}
.record-left { flex: 1; }
.cat-name {
  font-size: 28rpx;
  color: #333;
  display: block;
}
.feed-time {
  font-size: 24rpx;
  color: #999;
  margin-top: 5rpx;
}
.feed-loc {
  font-size: 24rpx;
  color: #666;
}
.empty-tip {
  text-align: center;
  font-size: 26rpx;
  color: #999;
  padding: 40rpx 0;
}
</style>
