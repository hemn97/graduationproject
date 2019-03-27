import Vue from 'vue'
import VueResource from 'vue-resource'
Vue.use(VueResource)

const baseUrl = 'http://localhost:80/competitionplatform/api/user'

// 登陆
export const requestUserLogin = (params) => {
  return Vue.http.post(`${baseUrl}/login`, params, {emulateJSON:true});
}
// 获取用户信息
export const requestUserData = (params) => {
  return Vue.http.get(`${baseUrl}/get`, {params: params});
}
// 修改用户信息
export const requestUpdateData = (params) => {
  return Vue.http.post(`${baseUrl}/update`, params, {emulateJSON:true});
}
// 注册
export const requestRegister = (params) => {
  return Vue.http.post(`${baseUrl}/register`, params, {emulateJSON:true});
}
// 激活
export const requestActivate = (params) => {
  return Vue.http.post(`${baseUrl}/activate`, params, {emulateJSON:true});
}
// 重新发送激活码
export const requestActivateResend = (params) => {
  return Vue.http.post(`${baseUrl}/activate/resend`, params, {emulateJSON:true});
}
// 获取用户列表
export const requestUserList = (params) => {
  return Vue.http.get(`${baseUrl}/list`, {params: params});
}
// 获取用户最大页数
export const requestUserMaxPage = (params) => {
  return Vue.http.get(`${baseUrl}/maxpage`, {params: params});
}
// 修改用户角色
export const requestUpdateUserRole = (params) => {
  return Vue.http.post(`${baseUrl}/role/update`, params, {emulateJSON:true});
}
