import Vue from 'vue'
import VueResource from 'vue-resource'
Vue.use(VueResource)

const baseUrl = 'http://localhost:80/competitionplatform/api/message'

// 分页查询消息
export const requestMessageList = (params) => {
  return Vue.http.get(`${baseUrl}/list`, {params: params});
}
// 消息最大页数
export const requestMessageMaxPage = (params) => {
  return Vue.http.get(`${baseUrl}/maxpage`, {params: params});
}
// 消息已读
export const requestReadMessage = (params) => {
  return Vue.http.post(`${baseUrl}/read`, params, {emulateJSON:true});
}
// 消息未读
export const requestUnreadMessage = (params) => {
  return Vue.http.post(`${baseUrl}/unread`, params, {emulateJSON:true});
}
// 发送消息
export const requestSendMessage = (params) => {
  return Vue.http.post(`${baseUrl}/send`, params, {emulateJSON:true});
}
