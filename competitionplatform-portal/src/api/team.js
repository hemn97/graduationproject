import Vue from 'vue'
import VueResource from 'vue-resource'
Vue.use(VueResource)

const baseUrl = 'http://localhost:80/competitionplatform/api/team'

// 我的竞赛队伍
export const requestFindMyTeam = (params) => {
  return Vue.http.get(`${baseUrl}/findteam/my`, {params: params});
}
// 我的所有队伍
export const requestFindMyTeams = (params) => {
  return Vue.http.get(`${baseUrl}/findteams/my`, {params: params});
}
// 竞赛所有队伍
export const requestFindAllTeams = (params) => {
  return Vue.http.get(`${baseUrl}/findteams/all`, {params: params});
}
// 所有队伍最大页数
export const requestTeamsMaxPage = (params) => {
  return Vue.http.get(`${baseUrl}/maxpage/teams`, {params: params});
}
// 我的队伍最大页数
export const requestMyTeamsMaxPage = (params) => {
  return Vue.http.get(`${baseUrl}/maxpage/myteams`, {params: params});
}
// 我的所有队员
export const requestFindMyTeammates = (params) => {
  return Vue.http.get(`${baseUrl}/findteammates/list`, {params: params});
}
// 所有队伍最大页数
export const requestTeammatesMaxPage = (params) => {
  return Vue.http.get(`${baseUrl}/maxpage/teammates`, {params: params});
}
// 创建队伍
export const requestCreateTeam = (params) => {
  return Vue.http.post(`${baseUrl}/create`, params, {emulateJSON:true});
}
// 解散队伍
export const requestDismissTeam = (params) => {
  return Vue.http.post(`${baseUrl}/dismiss`, params, {emulateJSON:true});
}
// 更新队伍信息
export const requestUpdateTeam = (params) => {
  return Vue.http.post(`${baseUrl}/update`, params, {emulateJSON:true});
}
// 离开队伍
export const requestLeaveTeam = (params) => {
  return Vue.http.post(`${baseUrl}/leave`, params, {emulateJSON:true});
}
// 申请加入队伍
export const requestJoinTeam = (params) => {
  return Vue.http.post(`${baseUrl}/join`, params, {emulateJSON:true});
}
// 处理队伍申请
export const requestHandleJoin = (params) => {
  return Vue.http.post(`${baseUrl}/join/handle`, params, {emulateJSON:true});
}
