/**
 * API 请求封装 - 校园流浪猫图鉴
 * 所有接口统一收拢在此文件，方便管理和对接
 */

// 后端服务地址（开发时通过 Vite proxy 转发，解决跨域；生产环境改为实际后端地址）
const BASE_URL = ''

/**
 * 通用请求函数
 * @param {string} url    - 接口路径（如 /api/cat/add）
 * @param {string} method - 请求方法 GET/POST/PUT/DELETE
 * @param {object} data   - 请求体（POST/PUT 时传入）
 * @returns {Promise}
 */
function request(url, method = 'GET', data = null) {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method,
      data,
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(res.data)
        } else {
          reject(res)
        }
      },
      fail: (err) => {
        reject(err)
      }
    })
  })
}

/**
 * 文件上传（Form-Data 格式）
 * @param {string} filePath - 本地文件临时路径
 * @returns {Promise<string>} 返回 OSS 图片 URL
 */
export function uploadFile(filePath) {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: BASE_URL + '/api/file/upload',
      filePath,
      name: 'file',
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data)  // 直接就是 OSS 图片 URL 字符串
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

// ══════════════════ 猫咪档案接口 ══════════════════

/** 录入猫咪档案 */
export function addCat(catData) {
  return request('/api/cat/add', 'POST', catData)
}

/** 修改猫咪绝育/TNR 状态 */
export function updateTnrStatus(catId, tnrStatus) {
  return request(`/api/cat/tnr/${catId}?tnrStatus=${tnrStatus}`, 'PUT')
}

/** 删除猫咪档案 */
export function deleteCat(catId) {
  return request(`/api/cat/delete/${catId}`, 'DELETE')
}

// ══════════════════ 健康上报接口 ══════════════════

/** 提交流浪猫健康异常上报 */
export function submitHealthReport(reportData) {
  return request('/api/report/submit', 'POST', reportData)
}

// ═══════════════ 以下接口等待后端补齐 ══════════════
// 当前使用 uni.request 直接调用，后端开发后可立即生效

/** 获取猫咪列表（等待后端 GET /api/cat/list） */
export function getCatList() {
  return request('/api/cat/list', 'GET')
}

/** 获取猫咪详情（等待后端 GET /api/cat/{id}） */
export function getCatDetail(catId) {
  return request(`/api/cat/${catId}`, 'GET')
}

/** 提交投喂打卡（等待后端 POST /api/feed/submit） */
export function submitFeed(feedData) {
  return request('/api/feed/submit', 'POST', feedData)
}

/** 获取某猫咪今日已投喂次数（等待后端 GET /api/feed/todayCount/{catId}） */
export function getTodayFeedCount(catId) {
  return request(`/api/feed/todayCount/${catId}`, 'GET')
}

/** 获取当前用户个人投喂记录（等待后端 GET /api/feed/my） */
export function getMyFeedList() {
  return request('/api/feed/my', 'GET')
}

export default {
  addCat,
  updateTnrStatus,
  deleteCat,
  submitHealthReport,
  uploadFile,
  getCatList,
  getCatDetail,
  submitFeed,
  getTodayFeedCount,
  getMyFeedList
}
