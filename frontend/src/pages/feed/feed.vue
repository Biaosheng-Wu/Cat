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

      <!-- #ifdef H5 -->
      <view id="leaflet-map" class="map-box"></view>
      <!-- #endif -->

      <!-- #ifndef H5 -->
      <map
        class="map-box"
        :longitude="location.longitude"
        :latitude="location.latitude"
        :markers="markers"
        @click="onMapClick"
      ></map>
      <!-- #endif -->

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

    <!-- 4. 健康状态上报（选填） -->
    <view class="form-item">
      <text class="label">猫咪健康状态（选填）</text>
      <picker
        :range="healthTypes"
        range-key="label"
        @change="onHealthChange"
        class="picker-box"
      >
        <view class="picker-text">
          {{ selectedHealth.label || '如发现异常请选择' }}
        </view>
      </picker>
      <input
        v-if="selectedHealth.value"
        class="input-box"
        style="margin-top:15rpx"
        v-model="healthDesc"
        placeholder="请描述猫咪异常情况..."
      />
    </view>

    <!-- 5. 提交打卡按钮 -->
    <view class="submit-btn" @click="submitFeed" hover-class="btn-hover">
      确认投喂
    </view>
  </view>
</template>

<script>
import { submitHealthReport, submitFeed, getTodayFeedCount, getCatList } from '@/api/index.js'

export default {
  name: "Feed",
  data() {
    return {
      catList: [
        { id: 1, name: "橘猫大胖" },
        { id: 2, name: "三花小花" },
        { id: 3, name: "黑白警长" },
        { id: 4, name: "狸花阿狸" }
      ],
      selectCat: { id: "", name: "" },
      location: {
        longitude: 116.397428,
        latitude: 39.90923,
        address: ""
      },
      markers: [],
      remark: "",
      healthTypes: [
        { label: '-- 请选择 --', value: '' },
        { label: '生病', value: 'SICK' },
        { label: '受伤', value: 'INJURED' },
        { label: '消瘦', value: 'THIN' },
        { label: '其他异常', value: 'OTHER' }
      ],
      selectedHealth: { label: '-- 请选择 --', value: '' },
      healthDesc: '',
      todayFeedCount: { 1: 0, 2: 0, 3: 0, 4: 0 },
      maxFeed: 3,
      // H5 Leaflet 地图实例
      leafletMap: null,
      leafletMarker: null
    }
  },
  mounted() {
    // #ifdef H5
    this.$nextTick(() => {
      this.initLeafletMap()
    })
    // #endif
  },
  beforeUnmount() {
    // #ifdef H5
    if (this.leafletMap) {
      this.leafletMap.remove()
      this.leafletMap = null
    }
    // #endif
  },
  onLoad() {
    this.fetchCatList()
  },
  methods: {
    // #ifdef H5
    /** 动态加载 Leaflet JS 并初始化地图 */
    initLeafletMap() {
      if (window.L) {
        this.createLeafletMap()
        return
      }
      const script = document.createElement('script')
      script.src = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'
      script.onload = () => {
        this.$nextTick(() => {
          this.createLeafletMap()
        })
      }
      document.head.appendChild(script)
    },

    createLeafletMap() {
      const mapEl = document.getElementById('leaflet-map')
      if (!mapEl || this.leafletMap) return

      // 初始化 Leaflet 地图
      const map = window.L.map(mapEl, {
        center: [this.location.latitude, this.location.longitude],
        zoom: 15,
        attributionControl: false
      })

      // OpenStreetMap 瓦片（免费，无需 API Key）
      window.L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19
      }).addTo(map)

      // 点击地图打点
      map.on('click', (e) => {
        const { lat, lng } = e.latlng
        this.setMapMarker(map, lat, lng)
      })

      // 延迟刷新避免初始布局异常
      setTimeout(() => { map.invalidateSize() }, 200)

      this.leafletMap = map
    },

    setMapMarker(map, lat, lng) {
      // 移除旧标记
      if (this.leafletMarker) {
        map.removeLayer(this.leafletMarker)
      }

      // 创建红色圆形标记
      this.leafletMarker = window.L.circleMarker([lat, lng], {
        radius: 10,
        fillColor: '#ff974a',
        color: '#fff',
        weight: 2,
        fillOpacity: 0.9
      }).addTo(map)

      this.location.longitude = lng
      this.location.latitude = lat
      this.location.address = `经纬度：${lng.toFixed(4)}, ${lat.toFixed(4)}`
    },
    // #endif

    async fetchCatList() {
      try {
        const result = await getCatList()
        const list = Array.isArray(result) ? result : (result.data || [])
        if (list.length > 0) {
          this.catList = list.map(c => ({ id: c.id, name: c.name }))
          return
        }
      } catch (e) {
        console.log('后端未响应，使用内置猫咪列表')
      }
    },

    onCatChange(e) {
      const index = e.detail.value
      const cat = this.catList[index]
      this.selectCat.id = cat.id
      this.selectCat.name = cat.name
      this.checkTodayCount(cat.id)
    },

    onHealthChange(e) {
      const index = e.detail.value
      this.selectedHealth = this.healthTypes[index]
    },

    /** 原生 map 点击事件（仅小程序/App） */
    onMapClick(e) {
      const { longitude, latitude } = e.detail
      this.location.longitude = longitude
      this.location.latitude = latitude
      this.location.address = `经纬度：${longitude.toFixed(4)} , ${latitude.toFixed(4)}`
      this.markers = [
        {
          id: 1,
          longitude,
          latitude,
          iconPath: "/static/marker.png",
          width: 30,
          height: 30
        }
      ]
    },

    async checkTodayCount(catId) {
      try {
        const count = await getTodayFeedCount(catId)
        if (typeof count === 'number') {
          this.todayFeedCount[catId] = count
        }
      } catch (e) {
        // 后端未实现，使用本地计数
      }
    },

    async submitFeed() {
      if (!this.selectCat.id) {
        uni.showToast({ title: "请先选择猫咪", icon: "none" })
        return
      }
      if (!this.location.address) {
        uni.showToast({ title: "请在地图标记位置", icon: "none" })
        return
      }

      const currentCount = this.todayFeedCount[this.selectCat.id] || 0
      if (currentCount >= this.maxFeed) {
        uni.showModal({
          title: "提示",
          content: `${this.selectCat.name} 今日投喂次数已达上限(${this.maxFeed}次)，请勿过度投喂哦~`,
          showCancel: false
        })
        return
      }

      // 1. 提交投喂打卡记录到后端
      try {
        const feedData = {
          catId: this.selectCat.id,
          remark: this.remark || '',
          latitude: this.location.latitude,
          longitude: this.location.longitude,
          locationName: this.location.address
        }
        const feedResult = await submitFeed(feedData)
        console.log('投喂提交结果:', feedResult)
      } catch (e) {
        console.log('投喂打卡接口调用失败', e)
        uni.showToast({ title: '网络异常，打卡暂未同步到服务器', icon: 'none' })
      }

      // 2. 如果选了健康异常，同时调用健康上报接口
      if (this.selectedHealth.value && this.healthDesc) {
        try {
          const reportData = {
            catId: this.selectCat.id,
            reportType: this.selectedHealth.value,
            description: this.healthDesc,
            latitude: this.location.latitude,
            longitude: this.location.longitude
          }
          const result = await submitHealthReport(reportData)
          console.log('健康上报结果:', result)
        } catch (e) {
          console.log('健康上报接口调用失败', e)
        }
      }

      this.todayFeedCount[this.selectCat.id] += 1

      uni.showModal({
        title: "提交成功",
        content: `已成功打卡：${this.selectCat.name}`,
        confirmText: "返回",
        success: () => {
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
.map-box {
  width: 100%;
  height: 400rpx;
  border-radius: 12rpx;
  overflow: hidden;
}
.loc-tip {
  font-size: 26rpx;
  color: #ff974a;
  margin-top: 15rpx;
}
.loc-tip.empty {
  color: #999;
}
.input-box {
  width: 100%;
  font-size: 28rpx;
  padding: 15rpx;
  border: 1rpx solid #f0e6dc;
  border-radius: 8rpx;
}
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
