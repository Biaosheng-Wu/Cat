<template>
  <view class="home-wrap">
    <!-- 顶部全屏封面图 -->
    <view class="banner">
      <image 
        class="banner-img" 
        src="/static/title.png" 
        mode="aspectFill"
      ></image>
    </view>

    <!-- 功能按钮区域 -->
    <view class="btn-group">
      <!-- 按钮1：来投喂吧 -->
      <view class="main-btn btn-feed" @click="goCatList" hover-class="btn-hover">
        来投喂吧
      </view>

      <!-- 按钮2：关于投喂者和小猫们 -->
      <view class="main-btn btn-about" @click="goAbout"hover-class="btn-hover">
        关于投喂者和小猫们
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "Index",
  methods: {
    // 跳转到 猫咪图鉴列表页
    goCatList() {
      uni.navigateTo({
        url: "/pages/catList/catList"
      })
    },
    // 跳转到 介绍页面
    goAbout() {
      uni.navigateTo({
        url: "/pages/about/about"
      })
    }
  }
}
</script>

<style scoped>
/* 页面整体 */
.home-wrap {
  width: 100%;
  min-height: 100vh;
  background-color: #fff8f2;
}

.btn-hover {
  opacity: 0.5;
}

/* 顶部横幅容器 */
.banner {
  width: 100%;
}
/* 封面图：全屏宽度 */
.banner-img {
  width: 100%;
  height: 400rpx;
  display: block;
}

/* 按钮整体区域 */
.btn-group {
  padding: 40rpx 30rpx;

}

/* 通用按钮样式：通栏、适配手机宽度 */
.main-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  text-align: center;
  font-size: 32rpx;
  border-radius: 12rpx;
  margin-bottom: 30rpx;
}

/* 第一个按钮 */
.btn-feed {
  background-color: #ff974a;
  color: #ffffff;
  box-shadow: 0 2rpx 10rpx rgba(255, 151, 74, 0.15);
}

/* 第二个按钮*/
.btn-about {
  background-color: #ffffff;
  color: #ff974a;
  border: 1rpx solid #ff974a;
  box-shadow: 0 2rpx 10rpx rgba(255, 151, 74, 0.15);
}
</style>