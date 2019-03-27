<template>
  <div id="app">
    <router-view v-if="isRouterAlive"></router-view>
  </div>
</template>

<script>
  import store from './store.js'
  export default {
    name: 'App',
    provide () {
      return {
        reload: this.reload
      }
    },
    data() {
      return {
        isRouterAlive: true
      }
    },
    store: store,
    components: {},
    methods: {
      reload () {
        this.isRouterAlive = false;
        this.$nextTick(function () {
          this.isRouterAlive = true
        })
      }
    }
  }
</script>
<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  width: 85%;
  margin: 0 auto;
}

.img-logo {
  width: 100%;
  padding-top: 5px;
  transform: scale(0.9, 0.9);
}
</style>
