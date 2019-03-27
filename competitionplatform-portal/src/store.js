import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    // 存储token
    token: localStorage.getItem('token') ? localStorage.getItem('token') : '',
    roleType: localStorage.getItem('roleType') ? localStorage.getItem('roleType') : ''
  },

  mutations: {
    setToken (state, token) {
      state.token = token;
      localStorage.setItem('token', token);
    },
    clearToken (state) {
      state.token = '';
      state.roleType = '';
      localStorage.removeItem('token');
      localStorage.removeItem('roleType');
    },
    setRoleType(state, roleType) {
      state.roleType = roleType;
      localStorage.setItem('roleType', roleType);
    }
  }
});
