// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
/*引入资源请求插件*/
import VueResource from 'vue-resource'
import store from './store.js'
import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(VueResource)
Vue.use(VueQuillEditor)

Vue.http.interceptors.push((request, next) => {

  // modify request
  const jwtToken = store.state.token;
  if (jwtToken) {
    Vue.http.headers.common.token = store.state.token;
  } else {
    delete Vue.http.headers.common.token;
  }
  // continue to next interceptor
  next((response) => {
    //在响应之后传给then之前对response进行修改和逻辑判断。对于token时候已过期的判断，就添加在此处，页面中任何一次http请求都会先调用此处方法

    return response;

  });
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
