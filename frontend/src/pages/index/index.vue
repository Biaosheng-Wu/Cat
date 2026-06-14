<template>
  <view class="home-wrap">
    <!-- 顶部全屏封面图 -->
    <view class="banner">
      <image class="banner-img" src="/static/title.png" mode="aspectFill"></image>
    </view>

    <!-- 功能按钮区域 -->
    <view class="btn-group">
      <view class="main-btn btn-feed" @click="goCatList" hover-class="btn-hover">
        来投喂吧
      </view>
      <view class="main-btn btn-about" @click="goReport" hover-class="btn-hover">
        猫咪异常上报
      </view>
      <view class="main-btn btn-about" @click="goAbout" hover-class="btn-hover">
        关于投喂者和小猫们
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "Index",
  methods: {
    goCatList() {
      uni.navigateTo({ url: "/pages/catList/catList" })
    },
    goReport() {
      uni.navigateTo({ url: "/pages/report/report" })
    },
    goAbout() {
      uni.navigateTo({ url: "/pages/about/about" })
    }
  }
}
</script>

<style scoped>
.home-wrap { width: 100%; min-height: 100vh; background-color: #fff8f2; }
.btn-hover { opacity: 0.5; }
.banner { width: 100%; }
.banner-img { width: 100%; height: 400rpx; display: block; }
.btn-group { padding: 40rpx 30rpx; }
.main-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  text-align: center;
  font-size: 32rpx;
  border-radius: 12rpx;
  margin-bottom: 30rpx;
}
.btn-feed {
  background-color: #ff974a;
  color: #ffffff;
  box-shadow: 0 2rpx 10rpx rgba(255, 151, 74, 0.15);
}
.btn-about {
  background-color: #ffffff;
  color: #ff974a;
  border: 1rpx solid #ff974a;
  box-shadow: 0 2rpx 10rpx rgba(255, 151, 74, 0.15);
}
</style>
