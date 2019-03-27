<template>
  <div>
    <el-row>
      <el-col :span="4">
        <img class="img-logo" src="@/assets/logo.png">
      </el-col>
      <el-col :span="18">
        <h4>后台管理系统</h4>
      </el-col>
      <el-col :span="2" style="margin-top: 16px;">
        <el-button size="small" @click="logoff()" >退出登录</el-button>
      </el-col>
    </el-row>
    <el-menu :default-active="activeIndex" mode="horizontal" @select="handleTabSelect">
      <el-menu-item index="1">竞赛管理</el-menu-item>
      <el-menu-item index="2">视频管理</el-menu-item>
      <el-menu-item index="3">论坛管理</el-menu-item>
      <el-menu-item index="4">用户管理</el-menu-item>
    </el-menu>
    <Competitions v-if="activeIndex == '1'"></Competitions>
    <Videos v-if="activeIndex == '2'"></Videos>
    <Posts v-if="activeIndex == '3'"></Posts>
    <Users v-if="activeIndex == '4'"></Users>
  </div>
</template>

<script>
    import Competitions from '@/pages/admin/competitions'
    import Videos from '@/pages/admin/videos'
    import Posts from '@/pages/admin/posts'
    import Users from '@/pages/admin/users'
    export default {
      name: "admin",
      components: {
        Competitions,
        Videos,
        Posts,
        Users,
      },
      created() {
          if (this.$store.state.token == '' || this.$store.state.roleType != 2) {   // 非管理员登录状态则回到登录主页
            this.$router.push("/admin/login");
          }
      },
      data() {
          return {
            activeIndex: '1',
          }
      },
      methods: {
        handleTabSelect(key, keyPath) {
          this.activeIndex = key;
        },
        logoff() {
          this.$store.commit('clearToken');
          this.$router.push("/admin/login");
        },
      }
    }
</script>

<style scoped>

</style>
