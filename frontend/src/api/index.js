/**
 * API 请求封装 - 校园流浪猫图鉴
 * 所有接口统一收拢在此文件，方便管理和对接
 */

const BASE_URL = ''

/**
 * 从本地存储获取 Token
 */
function getToken() {
  return uni.getStorageSync('token') || ''
}

/**
 * 通用请求函数（自动附带 JWT Token）
 */
function request(url, method = 'GET', data = null) {
  return new Promise((resolve, reject) => {
    const header = { 'Content-Type': 'application/json' }
    const token = getToken()
    if (token) {
      header['Authorization'] = 'Bearer ' + token
    }
    uni.request({
      url: BASE_URL + url,
      method,
      data,
      header,
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(res.data)
        } else if (res.statusCode === 401 || res.statusCode === 403) {
          // Token 过期/无效 或 无权限 — 清除登录状态跳回登录页
          uni.removeStorageSync('token')
          uni.removeStorageSync('userInfo')
          uni.reLaunch({ url: '/pages/login/login' })
          reject(res)
        } else {
          reject(res)
        }
      },
      fail: (err) => { reject(err) }
    })
  })
}

/** 文件上传（Form-Data 格式） */
export function uploadFile(filePath) {
  return new Promise((resolve, reject) => {
    const header = {}
    const token = getToken()
    if (token) {
      header['Authorization'] = 'Bearer ' + token
    }
    uni.uploadFile({
      url: BASE_URL + '/api/file/upload',
      filePath,
      name: 'file',
      header,
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data)
        } else {
          uni.showToast({ title: '上传失败', icon: 'none' })
          reject(res)
        }
      },
      fail: (err) => {
        uni.showToast({ title: '上传异常', icon: 'none' })
        reject(err)
      }
    })
  })
}

// ══════════════════ 认证接口 ══════════════════

/** 获取图形验证码 */
export function getCaptcha() {
  return request('/api/auth/captcha', 'GET')
}

/** 获取当前登录用户信息（含 isAdmin 角色标识） */
export function getCurrentUser() {
  return request('/api/auth/me', 'GET')
}

/** 登录 */
export function login(username, password) {
  return request('/api/auth/login', 'POST', { username, password })
}

/** 注册（需验证码） */
export function register(username, password, captchaKey, captchaCode) {
  return request('/api/auth/register', 'POST', {
    username, password, captchaKey, captchaCode
  })
}

// ══════════════════ 猫咪档案接口 ══════════════════

/** 录入猫咪档案 */
export function addCat(catData) {
  return request('/api/cat/add', 'POST', catData)
}

/** 修改猫咪绝育/TNR 状态（仅管理员） */
export function updateTnrStatus(catId, tnrStatus) {
  return request(`/api/cat/tnr/${catId}?tnrStatus=${tnrStatus}`, 'PUT')
}

/** 删除猫咪档案（仅管理员） */
export function deleteCat(catId) {
  return request(`/api/cat/delete/${catId}`, 'DELETE')
}

// ══════════════════ 健康上报接口 ══════════════════

/** 提交流浪猫健康异常上报 */
export function submitHealthReport(reportData) {
  return request('/api/report/submit', 'POST', reportData)
}

// ══════════════════ 投喂打卡接口 ══════════════════

/** 获取猫咪列表 */
export function getCatList() {
  return request('/api/cat/list', 'GET')
}

/** 获取猫咪详情 */
export function getCatDetail(catId) {
  return request(`/api/cat/${catId}`, 'GET')
}

/** 提交投喂打卡 */
export function submitFeed(feedData) {
  return request('/api/feed/submit', 'POST', feedData)
}

/** 获取某猫咪今日已投喂次数 */
export function getTodayFeedCount(catId) {
  return request(`/api/feed/todayCount/${catId}`, 'GET')
}

/** 获取当前用户个人投喂记录 */
export function getMyFeedList() {
  return request('/api/feed/my', 'GET')
}

export default {
  getCaptcha, getCurrentUser, login, register, addCat, updateTnrStatus, deleteCat,
  submitHealthReport, uploadFile, getCatList, getCatDetail,
  submitFeed, getTodayFeedCount, getMyFeedList
}
