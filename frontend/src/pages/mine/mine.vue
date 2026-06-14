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
      <view class="menu-item" @click="showRecord = !showRecord">
        <text class="menu-text">我的投喂记录</text>
        <text class="arrow">{{ showRecord ? '▲' : '▼' }}</text>
      </view>
      <view class="menu-item">
        <text class="menu-text">关于本小程序</text>
        <text class="arrow">></text>
      </view>
    </view>

    <!-- 3. 我的投喂记录区域 -->
    <view class="record-card" v-if="showRecord">
      <view class="record-title">投喂历史</view>
      <!-- 记录列表 -->
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
      <!-- 暂无记录兜底 -->
      <view class="empty-tip" v-if="myFeedList.length === 0">
        你还没有投喂记录，快去帮助校园流浪猫吧~
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "Mine",
  data() {
    return {
      // 控制投喂记录展开/收起
      showRecord: false,
      // 模拟当前用户的投喂记录（对接后端后替换为接口数据）
      myFeedList: [
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
  }
}
</script>

<style scoped>
/* 页面整体 */
.mine-wrap {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 个人信息卡片 */
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

/* 功能菜单卡片 */
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
/* 最后一项取消下边框 */
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

/* 投喂记录卡片 */
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

/* 单条记录 */
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

/* 空记录提示 */
.empty-tip {
  text-align: center;
  font-size: 26rpx;
  color: #999;
  padding: 40rpx 0;
}
</style>