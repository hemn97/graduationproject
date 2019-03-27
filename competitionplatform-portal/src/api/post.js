import Vue from 'vue'
import VueResource from 'vue-resource'
Vue.use(VueResource)

const baseUrl = 'http://localhost:80/competitionplatform/api/post'

// 分页查询帖子列表
export const requestPostList = (params) => {
  return Vue.http.get(`${baseUrl}/list`, {params: params});
}
// 帖子列表最大页数
export const requestPostMaxPage = (params) => {
  return Vue.http.get(`${baseUrl}/maxpage`, {params: params});
}
// 分页查询我的帖子列表
export const requestMyPostList = (params) => {
  return Vue.http.get(`${baseUrl}/list/my`, {params: params});
}
// 我的帖子列表最大页数
export const requestMyPostMaxPage = (params) => {
  return Vue.http.get(`${baseUrl}/maxpage/my`, {params: params});
}
// 帖子详情
export const requestPostDetails = (params) => {
  return Vue.http.get(`${baseUrl}/find`, {params: params});
}
// 发布帖子
export const requestReleasePost = (params) => {
  return Vue.http.post(`${baseUrl}/release`, params, {emulateJSON:true});
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
// 更新帖子
export const requestUpdatePost = (params) => {
  return Vue.http.post(`${baseUrl}/update`, params, {emulateJSON:true});
}
// 删除帖子
export const requestDeletePost = (params) => {
  return Vue.http.post(`${baseUrl}/delete`, params, {emulateJSON:true});
}
