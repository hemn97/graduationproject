import Vue from 'vue'
import VueResource from 'vue-resource'
Vue.use(VueResource)

const baseUrl = 'http://localhost:80/competitionplatform/api/video'

// 分页查询
export const requestVideoList = (params) => {
  return Vue.http.get(`${baseUrl}/list`, {params: params});
}
// 最大页数
export const requestVideoMaxPage = (params) => {
  return Vue.http.get(`${baseUrl}/maxpage`, {params: params});
}
// 详情
export const requestVideoDetails = (params) => {
  return Vue.http.get(`${baseUrl}/find`, {params: params});
}
// 分页查询评价列表
export const requestEvaluationList = (params) => {
  return Vue.http.get(`${baseUrl}/evaluations/list`, {params: params});
}
// 评价列表最大页数
export const requestEvaluationMaxPage = (params) => {
  return Vue.http.get(`${baseUrl}/evaluations/maxpage`, {params: params});
}
// 发表评价
export const requestReleaseEvaluation = (params) => {
  return Vue.http.post(`${baseUrl}/evaluations/release`, params, {emulateJSON:true});
}
// 删除视频
export const requestDeleteVideo = (params) => {
  return Vue.http.post(`${baseUrl}/delete`, params, {emulateJSON:true});
}
// 更新视频
export const requestUpdateVideo = (params) => {
  return Vue.http.post(`${baseUrl}/update`, params, {emulateJSON:true});
}
// 发布视频
export const requestReleaseVideo = (params) => {
  return Vue.http.post(`${baseUrl}/release`, params, {emulateJSON:true});
}
