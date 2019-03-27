import Vue from 'vue'
import VueResource from 'vue-resource'
Vue.use(VueResource)

const baseUrl = 'http://localhost:80/competitionplatform/api/competition'

// 分页查询
export const requestCompetitionList = (params) => {
  return Vue.http.get(`${baseUrl}/list`, {params: params});
}
// 最大页数
export const requestCompetitionMaxPage = (params) => {
  return Vue.http.get(`${baseUrl}/maxpage`, {params: params});
}
// 详情
export const requestCompetitionDetails = (params) => {
  return Vue.http.get(`${baseUrl}/find`, {params: params});
}
// 分页查询评论列表
export const requestCommentList = (params) => {
  return Vue.http.get(`${baseUrl}/comments/list`, {params: params});
}
// 评论列表最大页数
export const requestCommentMaxPage = (params) => {
  return Vue.http.get(`${baseUrl}/comments/maxpage`, {params: params});
}
// 发表评论
export const requestReleaseComment = (params) => {
  return Vue.http.post(`${baseUrl}/comments/release`, params, {emulateJSON:true});
}
// 删除竞赛
export const requestDeleteCompetition = (params) => {
  return Vue.http.post(`${baseUrl}/delete`, params, {emulateJSON:true});
}
// 修改竞赛信息
export const requestUpdateCompetition = (params) => {
  return Vue.http.post(`${baseUrl}/update`, params, {emulateJSON:true});
}
// 发布竞赛
export const requestReleaseCompetition = (params) => {
  return Vue.http.post(`${baseUrl}/release`, params, {emulateJSON:true});
}
