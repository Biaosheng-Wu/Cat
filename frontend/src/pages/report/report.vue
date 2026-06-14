<template>
  <view class="report-wrap">
    <view class="tip-text">发现猫咪健康异常？请提交上报</view>

    <view class="form-item">
      <text class="label">选择猫咪 *</text>
      <picker :range="catList" range-key="name" @change="onCatChange" class="picker-box">
        <view class="picker-text">{{ selectCat.name || '请选择' }}</view>
      </picker>
    </view>

    <view class="form-item">
      <text class="label">异常类型 *</text>
      <picker :range="healthTypes" range-key="label" @change="onTypeChange" class="picker-box">
        <view class="picker-text">{{ selectedType.label || '请选择' }}</view>
      </picker>
    </view>

    <view class="form-item">
      <text class="label">异常描述 *</text>
      <textarea class="textarea-box" v-model="description" placeholder="请详细描述猫咪的异常状况..." />
    </view>

    <view class="form-item">
      <text class="label">发现位置</text>
      <text class="desc">（点击按钮获取实时位置，也可手动填写经纬度）</text>

      <!-- 一键获取实时位置 -->
      <view class="locate-btn" @click="getCurrentLocation" hover-class="btn-hover">
        {{ gettingLocation ? '定位中...' : (locationObtained ? '重新定位' : '获取当前位置') }}
      </view>

      <!-- 定位结果显示 -->
      <view class="loc-result" v-if="latitude && longitude">
        <text class="loc-text">经度: {{ longitude.toFixed ? longitude.toFixed(6) : longitude }}</text>
        <text class="loc-text">纬度: {{ latitude.toFixed ? latitude.toFixed(6) : latitude }}</text>
      </view>

      <!-- 手动输入经纬度 -->
      <view style="display:flex;margin-top:15rpx;gap:15rpx">
        <input class="input-box" style="flex:1" v-model="latitudeInput" placeholder="手动输入纬度" />
        <input class="input-box" style="flex:1" v-model="longitudeInput" placeholder="手动输入经度" />
      </view>
    </view>

    <view class="submit-btn" @click="submitReport" hover-class="btn-hover">
      提交上报
    </view>
  </view>
</template>

<script>
import { submitHealthReport, getCatList } from '@/api/index.js'

export default {
  name: "Report",
  data() {
    return {
      catList: [
        { id: 1, name: "橘猫大胖" },
        { id: 2, name: "三花小花" },
        { id: 3, name: "黑白警长" },
        { id: 4, name: "狸花阿狸" }
      ],
      selectCat: { id: '', name: '' },
      healthTypes: [
        { label: '-- 请选择 --', value: '' },
        { label: '生病', value: 'SICK' },
        { label: '受伤', value: 'INJURED' },
        { label: '消瘦', value: 'THIN' },
        { label: '其他异常', value: 'OTHER' }
      ],
      selectedType: { label: '-- 请选择 --', value: '' },
      description: '',
      latitude: '',
      longitude: '',
      latitudeInput: '',
      longitudeInput: '',
      gettingLocation: false,
      locationObtained: false
    }
  },
  onLoad() {
    this.fetchCatList()
  },
  methods: {
    async fetchCatList() {
      try {
        const result = await getCatList()
        const list = Array.isArray(result) ? result : (result.data || [])
        if (list.length > 0) {
          this.catList = list.map(c => ({ id: c.id, name: c.name }))
        }
      } catch (e) {
        console.log('后端未响应，使用内置列表')
      }
    },
    onCatChange(e) {
      const cat = this.catList[e.detail.value]
      this.selectCat = { id: cat.id, name: cat.name }
    },
    onTypeChange(e) {
      this.selectedType = this.healthTypes[e.detail.value]
    },

    /** 获取用户当前实时位置 */
    getCurrentLocation() {
      this.gettingLocation = true

      const onSuccess = (coords) => {
        this.latitude = coords.latitude
        this.longitude = coords.longitude
        this.locationObtained = true
        this.gettingLocation = false
        uni.showToast({ title: '定位成功', icon: 'success' })
      }

      const onFail = (err) => {
        this.gettingLocation = false
        console.log('定位失败:', err)
        const msg = typeof err === 'string' ? err : (err.message || '定位失败')
        uni.showToast({ title: msg + '，请手动填写', icon: 'none' })
      }

      // #ifdef H5
      // H5 环境：浏览器 geolocation 需要 HTTPS 才能工作
      if (!navigator.geolocation) {
        onFail('浏览器不支持定位')
        return
      }
      navigator.geolocation.getCurrentPosition(
        (pos) => {
          onSuccess({ latitude: pos.coords.latitude, longitude: pos.coords.longitude })
        },
        (err) => {
          const tips = { 1: '请允许定位权限', 2: '无法获取位置', 3: '定位超时' }
          onFail(tips[err.code] || err.message)
        },
        { enableHighAccuracy: true, timeout: 10000, maximumAge: 60000 }
      )
      // #endif

      // #ifndef H5
      // 小程序 / App 环境：使用 uni.getLocation
      uni.getLocation({
        type: 'gcj02',
        success: (res) => onSuccess(res),
        fail: (err) => onFail(err)
      })
      // #endif
    },
    async submitReport() {
      if (!this.selectCat.id) {
        uni.showToast({ title: '请选择猫咪', icon: 'none' }); return
      }
      if (!this.selectedType.value) {
        uni.showToast({ title: '请选择异常类型', icon: 'none' }); return
      }
      if (!this.description.trim()) {
        uni.showToast({ title: '请填写异常描述', icon: 'none' }); return
      }
      try {
        // 优先使用实时定位获取的经纬度，否则使用手动输入
        const lat = parseFloat(this.latitude) || parseFloat(this.latitudeInput) || null
        const lng = parseFloat(this.longitude) || parseFloat(this.longitudeInput) || null
        const result = await submitHealthReport({
          catId: this.selectCat.id,
          reportType: this.selectedType.value,
          description: this.description.trim(),
          latitude: lat,
          longitude: lng
        })
        uni.showToast({ title: result || '上报成功', icon: 'success' })
        setTimeout(() => { uni.navigateBack() }, 1500)
      } catch (e) {
        uni.showToast({ title: '上报失败，请检查网络', icon: 'none' })
      }
    }
  }
}
</script>

<style scoped>
.report-wrap {
  padding: 20rpx;
  background-color: #fff8f2;
  min-height: 100vh;
}
.btn-hover { opacity: 0.8; }
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
.label { font-size: 28rpx; color: #333; display: block; margin-bottom: 12rpx; }
.desc { font-size: 24rpx; color: #999; display: block; margin-top: 4rpx; }
.input-box {
  width: 100%;
  font-size: 28rpx;
  padding: 15rpx;
  border: 1rpx solid #f0e6dc;
  border-radius: 8rpx;
}
.textarea-box {
  width: 100%;
  min-height: 200rpx;
  font-size: 28rpx;
  padding: 15rpx;
  border: 1rpx solid #f0e6dc;
  border-radius: 8rpx;
}
.picker-box { width: 100%; }
.picker-text {
  font-size: 28rpx;
  color: #666;
  padding: 15rpx;
  border: 1rpx solid #f0e6dc;
  border-radius: 8rpx;
}
.submit-btn {
  width: 100%;
  height: 80rpx;
  line-height: 80rpx;
  text-align: center;
  background-color: #4CAF50;
  color: #fff;
  font-size: 32rpx;
  border-radius: 12rpx;
  margin-top: 40rpx;
}

/* 获取位置按钮 */
.locate-btn {
  width: 100%;
  height: 70rpx;
  line-height: 70rpx;
  text-align: center;
  background-color: #2196F3;
  color: #fff;
  font-size: 28rpx;
  border-radius: 12rpx;
  margin-top: 15rpx;
}

/* 定位结果展示 */
.loc-result {
  display: flex;
  gap: 20rpx;
  margin-top: 12rpx;
  padding: 10rpx 0;
}
.loc-text {
  font-size: 24rpx;
  color: #2196F3;
  background-color: #e3f2fd;
  padding: 6rpx 16rpx;
  border-radius: 6rpx;
}
</style>
