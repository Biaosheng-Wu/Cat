<template>
  <view class="login-wrap">
    <view class="login-card">
      <view class="title">校园流浪猫图鉴</view>
      <view class="subtitle">登录 / 注册</view>

      <view class="form-item">
        <input class="input-box" v-model="username" placeholder="请输入用户名" />
      </view>
      <view class="form-item">
        <input class="input-box" type="password" v-model="password" placeholder="请输入密码" />
      </view>

      <view class="btn-group">
        <view class="login-btn" @click="doLogin" hover-class="btn-hover">登录</view>
        <view class="register-btn" @click="goRegister" hover-class="btn-hover">注册新账号</view>
      </view>
    </view>
  </view>
</template>

<script>
import { login } from '@/api/index.js'

export default {
  name: "Login",
  data() {
    return { username: '', password: '' }
  },
  methods: {
    async doLogin() {
      if (!this.username.trim() || !this.password.trim()) {
        uni.showToast({ title: '请输入用户名和密码', icon: 'none' })
        return
      }
      try {
        const result = await login(this.username.trim(), this.password.trim())
        if (result.code === 200) {
          uni.setStorageSync('token', result.data.token)
          uni.setStorageSync('userInfo', JSON.stringify(result.data))
          uni.showToast({ title: result.message, icon: 'success' })
          setTimeout(() => { uni.switchTab({ url: '/pages/index/index' }) }, 500)
        }
      } catch (e) {
        const msg = (e.data && e.data.message) ? e.data.message : '登录失败'
        uni.showToast({ title: msg, icon: 'none' })
      }
    },
    goRegister() {
      uni.navigateTo({ url: '/pages/register/register' })
    }
  }
}
</script>

<style scoped>
.login-wrap {
  min-height: 100vh;
  background: linear-gradient(135deg, #ff974a 0%, #ff6b35 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40rpx;
}
.btn-hover { opacity: 0.8; }
.login-card {
  width: 100%;
  background: #fff;
  border-radius: 24rpx;
  padding: 50rpx 40rpx;
}
.title {
  font-size: 40rpx;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 10rpx;
}
.subtitle {
  font-size: 28rpx;
  color: #999;
  text-align: center;
  margin-bottom: 50rpx;
}
.form-item { margin-bottom: 25rpx; }
.input-box {
  width: 100%;
  height: 80rpx;
  font-size: 28rpx;
  padding: 0 20rpx;
  border: 1rpx solid #eee;
  border-radius: 12rpx;
}
.btn-group { margin-top: 40rpx; }
.login-btn {
  width: 100%;
  height: 80rpx;
  line-height: 80rpx;
  text-align: center;
  background-color: #ff974a;
  color: #fff;
  font-size: 32rpx;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}
.register-btn {
  width: 100%;
  height: 80rpx;
  line-height: 80rpx;
  text-align: center;
  background-color: #fff;
  color: #ff974a;
  font-size: 32rpx;
  border-radius: 12rpx;
  border: 1rpx solid #ff974a;
}
</style>
