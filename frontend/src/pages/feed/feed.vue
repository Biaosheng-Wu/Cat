<template>
  <view class="feed-wrap">
    <!-- 页面提示 -->
    <view class="tip-text">请选择猫咪并标记投喂位置</view>

    <!-- 1. 选择猫咪 下拉选择器 -->
    <view class="form-item">
      <text class="label">选择猫咪</text>
      <picker 
        :range="catList" 
        range-key="name"
        @change="onCatChange"
        class="picker-box"
      >
        <view class="picker-text">
          {{ selectCat.name || '请选择要投喂的猫咪' }}
        </view>
      </picker>
    </view>

    <!-- 2. 地图区域 + 打点位置 -->
    <view class="form-item">
      <text class="label">投喂位置</text>
      <!-- 微信小程序原生地图组件 -->
      <map
        class="map-box"
        :longitude="location.longitude"
        :latitude="location.latitude"
        :markers="markers"
        @click="onMapClick"
      ></map>
      <view class="loc-tip" v-if="location.address">
        已标记位置：{{ location.address }}
      </view>
      <view class="loc-tip empty" v-else>
        点击地图任意位置完成打点
      </view>
    </view>

    <!-- 3. 投喂备注（选填） -->
    <view class="form-item">
      <text class="label">投喂备注</text>
      <input 
        class="input-box" 
        v-model="remark" 
        placeholder="例：投喂猫粮、小鱼干（选填）"
      />
    </view>

    <!-- 4. 提交打卡按钮 -->
    <view class="submit-btn" @click="submitFeed" hover-class="btn-hover">
        确认投喂
    </view>
  </view>
</template>

<script>
export default {
  name: "Feed",
  data() {
    return {
      // 猫咪列表（和首页/详情页模拟数据保持一致）
      catList: [
        { id: 1, name: "橘猫大胖" },
        { id: 2, name: "三花小花" },
        { id: 3, name: "黑白警长" },
        { id: 4, name: "狸花阿狸" }
      ],
      // 选中的猫咪
      selectCat: {
        id: "",
        name: ""
      },
      // 地图经纬度、位置信息
      location: {
        longitude: 116.397428, // 默认北京经纬度，可根据校园自行修改
        latitude: 39.90923,
        address: ""
      },
      // 地图标记点
      markers: [],
      // 投喂备注
      remark: "",
      // 模拟：每只猫咪今日已投喂次数（模拟后端统计数据，实现防过量投喂）
      todayFeedCount: {
        1: 2,
        2: 1,
        3: 0,
        4: 2
      },
      // 限制：单只猫咪每日最大投喂次数
      maxFeed: 3
    }
  },
  methods: {
    // 下拉选择猫咪
    onCatChange(e) {
      const index = e.detail.value
      const cat = this.catList[index]
      this.selectCat.id = cat.id
      this.selectCat.name = cat.name
    },

    // 点击地图 打点
    onMapClick(e) {
      const { longitude, latitude } = e.detail
      // 更新地图中心
      this.location.longitude = longitude
      this.location.latitude = latitude
      this.location.address = `经纬度：${longitude.toFixed(4)} , ${latitude.toFixed(4)}`

      // 添加标记点（地图小红点）
      this.markers = [
        {
          id: 1,
          longitude,
          latitude,
          iconPath: "/static/marker.png", // 地图标记图标
          width: 30,
          height: 30
        }
      ]
    },

    // 提交投喂打卡
    submitFeed() {
      // 1. 表单校验：必须选择猫咪
      if (!this.selectCat.id) {
        uni.showToast({
          title: "请先选择猫咪",
          icon: "none"
        })
        return
      }
      // 2. 表单校验：必须地图打点
      if (!this.location.address) {
        uni.showToast({
          title: "请在地图标记位置",
          icon: "none"
        })
        return
      }

      // 3. 防重复/过量投喂判断
      const currentCount = this.todayFeedCount[this.selectCat.id] || 0
      if (currentCount >= this.maxFeed) {
        uni.showModal({
          title: "提示",
          content: `${this.selectCat.name} 今日投喂次数已达上限(${this.maxFeed}次)，请勿过度投喂哦~`,
          showCancel: false
        })
        return
      }

      // 4. 模拟提交数据给后端
      uni.showModal({
        title: "提交成功",
        content: `已成功打卡：${this.selectCat.name}`,
        confirmText: "返回",
        success: () => {
          // 打卡成功，次数+1
          this.todayFeedCount[this.selectCat.id] += 1
          // 返回上一页
          uni.navigateBack()
        }
      })
    }
  }
}
</script>

<style scoped>
.feed-wrap {
  padding: 20rpx;
  background-color: #fff8f2;
  min-height: 100vh;
}
.btn-hover {
  opacity: 0.8;
}
.tip-text {
  text-align: center;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 30rpx;
}

/* 表单项通用样式 */
.form-item {
  background: #fff;
  border-radius: 16rpx;
  padding: 25rpx;
  margin-bottom: 20rpx;
}

.label {
  font-size: 30rpx;
  color: #333;
  display: block;
  margin-bottom: 15rpx;
}

/* 下拉选择器 */
.picker-box {
  width: 100%;
}
.picker-text {
  font-size: 28rpx;
  color: #666;
  padding: 15rpx;
  border: 1rpx solid #f0e6dc;
  border-radius: 8rpx;
}

/* 地图容器 */
.map-box {
  width: 100%;
  height: 400rpx;
  border-radius: 12rpx;
}

/* 位置提示文字 */
.loc-tip {
  font-size: 26rpx;
  color: #ff974a;
  margin-top: 15rpx;
}
.loc-tip.empty {
  color: #999;
}

/* 输入框 */
.input-box {
  width: 100%;
  font-size: 28rpx;
  padding: 15rpx;
  border: 1rpx solid #f0e6dc;
  border-radius: 8rpx;
}

/* 提交按钮 */
.submit-btn {
  width: 100%;
  height: 80rpx;
  line-height: 80rpx;
  text-align: center;
  background-color: #ff974a;
  color: #fff;
  font-size: 32rpx;
  border-radius: 12rpx;
  margin-top: 40rpx;
}
</style>