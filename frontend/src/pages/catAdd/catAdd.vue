<template>
  <view class="add-wrap">
    <view class="tip-text">填写猫咪信息录入档案</view>

    <view class="form-item">
      <text class="label">猫咪昵称 *</text>
      <input class="input-box" v-model="form.name" placeholder="如：橘猫大胖" />
    </view>

    <view class="form-item">
      <text class="label">品种</text>
      <input class="input-box" v-model="form.breed" placeholder="如：中华田园猫、暹罗（选填）" />
    </view>

    <view class="form-item">
      <text class="label">性别</text>
      <picker :range="genderList" @change="onGenderChange" class="picker-box">
        <view class="picker-text">{{ form.gender || '请选择' }}</view>
      </picker>
    </view>

    <view class="form-item">
      <text class="label">毛色</text>
      <input class="input-box" v-model="form.color" placeholder="如：橘色、白底黑斑（选填）" />
    </view>

    <view class="form-item">
      <text class="label">预估年龄</text>
      <input class="input-box" v-model="form.ageEstimate" placeholder="如：1岁左右（选填）" />
    </view>

    <view class="form-item">
      <text class="label">外貌特征</text>
      <textarea class="textarea-box" v-model="form.appearanceFeatures" placeholder="如：右耳有缺口，尾巴短（选填）" />
    </view>

    <view class="form-item">
      <text class="label">常出没地点 *</text>
      <input class="input-box" v-model="form.frequentLocations" placeholder="如：一食堂门口" />
    </view>

    <view class="form-item">
      <text class="label">健康状态</text>
      <picker :range="healthList" @change="onHealthChange" class="picker-box">
        <view class="picker-text">{{ form.healthStatus || '请选择' }}</view>
      </picker>
    </view>

    <view class="submit-btn" @click="submitAdd" hover-class="btn-hover">
      提交录入
    </view>
  </view>
</template>

<script>
import { addCat } from '@/api/index.js'

export default {
  name: "CatAdd",
  data() {
    return {
      form: {
        name: '',
        breed: '',
        gender: '',
        color: '',
        ageEstimate: '',
        appearanceFeatures: '',
        frequentLocations: '',
        healthStatus: ''
      },
      genderList: ['不选择', 'MALE-公', 'FEMALE-母', 'UNKNOWN-未知'],
      healthList: ['不选择', 'HEALTHY-健康', 'SICK-生病', 'INJURED-受伤']
    }
  },
  methods: {
    onGenderChange(e) {
      const val = this.genderList[e.detail.value]
      this.form.gender = val === '不选择' ? '' : val
    },
    onHealthChange(e) {
      const val = this.healthList[e.detail.value]
      this.form.healthStatus = val === '不选择' ? '' : val
    },
    async submitAdd() {
      if (!this.form.name.trim()) {
        uni.showToast({ title: '请输入猫咪昵称', icon: 'none' })
        return
      }
      if (!this.form.frequentLocations.trim()) {
        uni.showToast({ title: '请输入常出没地点', icon: 'none' })
        return
      }

      const catData = {
        name: this.form.name.trim(),
        breed: this.form.breed.trim() || null,
        gender: this.form.gender || null,
        color: this.form.color.trim() || null,
        ageEstimate: this.form.ageEstimate.trim() || null,
        appearanceFeatures: this.form.appearanceFeatures.trim() || null,
        frequentLocations: this.form.frequentLocations.trim(),
        healthStatus: this.form.healthStatus || null
      }

      try {
        const result = await addCat(catData)
        uni.showToast({ title: result || '录入成功', icon: 'success' })
        setTimeout(() => { uni.navigateBack() }, 1200)
      } catch (e) {
        uni.showToast({ title: '录入失败，请检查网络', icon: 'none' })
      }
    }
  }
}
</script>

<style scoped>
.add-wrap {
  padding: 20rpx;
  background-color: #fff8f2;
  min-height: 100vh;
  padding-bottom: 120rpx;
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
.label {
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 12rpx;
}
.input-box {
  width: 100%;
  font-size: 28rpx;
  padding: 15rpx;
  border: 1rpx solid #f0e6dc;
  border-radius: 8rpx;
}
.textarea-box {
  width: 100%;
  min-height: 160rpx;
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
  background-color: #ff974a;
  color: #fff;
  font-size: 32rpx;
  border-radius: 12rpx;
  margin-top: 20rpx;
}
</style>
