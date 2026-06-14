<template>
  <view class="register-wrap">
    <view class="register-card">
      <view class="title">注册账号</view>
      <view class="subtitle">加入校园流浪猫救助行动</view>

      <!-- 用户名 -->
      <view class="form-item">
        <text class="label">用户名</text>
        <input class="input-box" v-model="username" placeholder="3-20个字符" maxlength="20" />
      </view>

      <!-- 密码 -->
      <view class="form-item">
        <text class="label">密码</text>
        <input class="input-box" type="password" v-model="password" placeholder="至少6位密码" />
      </view>

      <!-- 确认密码 -->
      <view class="form-item">
        <text class="label">确认密码</text>
        <input class="input-box" type="password" v-model="password2" placeholder="再次输入密码" />
      </view>

      <!-- 验证码 -->
      <view class="form-item">
        <text class="label">验证码</text>
        <view class="captcha-row">
          <input class="captcha-input" v-model="captchaCode" placeholder="输入验证码" maxlength="4" />
          <image
            class="captcha-img"
            :src="captchaImage"
            mode="aspectFit"
            @click="refreshCaptcha"
          />
        </view>
      </view>

      <!-- 注册按钮 -->
      <view class="submit-btn" @click="doRegister" hover-class="btn-hover">
        {{ submitting ? '注册中...' : '注册' }}
      </view>

      <!-- 返回登录 -->
      <view class="back-link" @click="goLogin">已有账号？去登录</view>
    </view>
  </view>
</template>

<script>
import { getCaptcha, register } from '@/api/index.js'

export default {
  name: "Register",
  data() {
    return {
      username: '',
      password: '',
      password2: '',
      captchaKey: '',
      captchaImage: '',
      captchaCode: '',
      submitting: false
    }
  },
  onLoad() {
    this.refreshCaptcha()
  },
  methods: {
    /** 刷新验证码 */
    async refreshCaptcha() {
      try {
        const result = await getCaptcha()
        this.captchaKey = result.captchaKey
        this.captchaImage = result.captchaImage
        this.captchaCode = ''
      } catch (e) {
        uni.showToast({ title: '获取验证码失败', icon: 'none' })
      }
    },

    async doRegister() {
      if (this.submitting) return

      const u = this.username.trim()
      const p = this.password.trim()
      const p2 = this.password2.trim()

      if (!u) { uni.showToast({ title: '请输入用户名', icon: 'none' }); return }
      if (u.length < 3 || u.length > 20) { uni.showToast({ title: '用户名需3-20个字符', icon: 'none' }); return }
      if (!p) { uni.showToast({ title: '请输入密码', icon: 'none' }); return }
      if (p.length < 6) { uni.showToast({ title: '密码至少6位', icon: 'none' }); return }
      if (p !== p2) { uni.showToast({ title: '两次密码不一致', icon: 'none' }); return }
      if (!this.captchaCode.trim()) { uni.showToast({ title: '请输入验证码', icon: 'none' }); return }

      this.submitting = true
      try {
        const result = await register(u, p, this.captchaKey, this.captchaCode.trim())
        // 成功: {code:200, message:"注册成功", data:{token, username, nickname}}
        uni.showToast({ title: '注册成功', icon: 'success' })
        setTimeout(() => {
          uni.setStorageSync('token', result.data.token)
          uni.setStorageSync('userInfo', JSON.stringify({
            username: result.data.username,
            nickname: result.data.nickname,
            phone: result.data.phone
          }))
          uni.switchTab({ url: '/pages/index/index' })
        }, 800)
      } catch (e) {
        // 失败: {code:400, message:"验证码错误..."}
        const msg = (e.data && e.data.message) ? e.data.message : '注册失败'
        uni.showToast({ title: msg, icon: 'none' })
        this.refreshCaptcha()
      } finally {
        this.submitting = false
      }
    },

    goLogin() {
      uni.navigateBack()
    }
  }
}
</script>

<style scoped>
.register-wrap {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 40rpx;
}
.btn-hover { opacity: 0.8; }

.register-card {
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
  font-size: 26rpx;
  color: #999;
  text-align: center;
  margin-bottom: 50rpx;
}

.form-item { margin-bottom: 30rpx; }
.label {
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 12rpx;
}
.input-box {
  width: 100%;
  height: 80rpx;
  font-size: 28rpx;
  padding: 0 20rpx;
  border: 1rpx solid #eee;
  border-radius: 12rpx;
  box-sizing: border-box;
}

/* 验证码行 */
.captcha-row {
  display: flex;
  align-items: center;
  gap: 20rpx;
}
.captcha-input {
  flex: 1;
  height: 80rpx;
  font-size: 28rpx;
  padding: 0 20rpx;
  border: 1rpx solid #eee;
  border-radius: 12rpx;
  box-sizing: border-box;
}
.captcha-img {
  width: 200rpx;
  height: 80rpx;
  border-radius: 8rpx;
  border: 1rpx solid #eee;
  flex-shrink: 0;
}

.submit-btn {
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  background-color: #ff974a;
  color: #fff;
  font-size: 32rpx;
  border-radius: 12rpx;
  margin-top: 30rpx;
}
.back-link {
  text-align: center;
  font-size: 26rpx;
  color: #999;
  margin-top: 30rpx;
  padding: 10rpx;
}
</style>
