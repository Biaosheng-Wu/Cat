<template>
  <view class="about-wrap">
    <view class="content-card">
      <view class="title">关于我们</view>
      <view class="text">
        本小程序是校园流浪猫图鉴与救助打卡平台，旨在记录校园里每一只可爱的流浪猫咪，
        方便同学们有序投喂、科学救助，避免过度投喂伤害猫咪健康。
      </view>

      <view class="sub-title">🐱 关于校园小猫</view>
      <view class="text">
        校园内的流浪猫咪长期陪伴师生，成为校园一道独特的风景线。
        请大家温柔对待小动物，不驱赶、不伤害，文明互动。
      </view>

      <view class="sub-title">🤝 关于投喂志愿者</view>
      <view class="text">
        每一位参与投喂、打卡记录的同学都是爱心志愿者。
        我们统一记录投喂频次，科学管控喂食量，守护猫咪健康。
      </view>

      <view class="sub-title">📢 投喂须知</view>
      <view class="text">
        1. 请勿一天内多次投喂同一只猫咪，避免猫咪积食生病；
        2. 建议投喂专用猫粮、鸡胸肉，不投喂辛辣、高盐人类食物；
        3. 投喂后保持场地整洁，共同维护校园环境。
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "About"
}
</script>

<style scoped>
.about-wrap {
  padding: 20rpx;
  background-color: #fff8f2;
  min-height: 100vh;
}
.content-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
}
.title {
  font-size: 36rpx;
  font-weight: bold;
  text-align: center;
  color: #333;
  margin-bottom: 30rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f0e6dc;
}
/* 子标题 */
.sub-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #ff974a;
  margin: 30rpx 0 15rpx;
}
.text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.8;
  text-align: justify;
}
</style>