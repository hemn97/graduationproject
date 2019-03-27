<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-main class="main">
      <h1 style="text-align: center">找不到页面</h1>
    </el-main>
    <el-footer><Footer></Footer></el-footer>
  </el-container>
</template>

<script>
  import Header from '@/components/header'
  import Footer from '@/components/footer'
  export default {
    name: "notfound",
    components: {
      Header,
      Footer
    },
    methods: {

    },
    data() {
      return {

      };
    },
  }
</script>

<style scoped>

</style>
