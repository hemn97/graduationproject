<template>
  <el-row>
    <el-col :span="4">
      <img class="img-logo" src="@/assets/logo.png">
    </el-col>
    <el-col :span="16">
      <el-menu :default-active="activeIndex" mode="horizontal" :router=true>
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/competitions">全部竞赛</el-menu-item>
        <el-menu-item index="/videos">视频中心</el-menu-item>
        <el-menu-item index="/bbs">论坛</el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="4" class="col-button">
      <el-menu mode="horizontal" v-if="loginStatus == false">
        <el-menu-item><el-button type="text" size="small" @click="loginDialogVisible = true" >登录</el-button></el-menu-item>
        <el-menu-item><el-button type="text" size="small" @click="toRegister()">注册</el-button></el-menu-item>
        <el-dialog title="登录" :visible.sync="loginDialogVisible" center width="40%">
          <el-form :model="loginForm" class="form-login" :rules="loginRules" ref="loginForm" status-icon >
            <el-form-item label="用户名 / 邮箱" :label-width="formLabelWidth" prop="identifier" class="item-dialog">
              <el-input v-model="loginForm.identifier" autocomplete="off" class="input-dialog"></el-input>
            </el-form-item>
            <el-form-item label="密码" :label-width="formLabelWidth" prop="credential" class="item-dialog">
              <el-input type="password" v-model="loginForm.credential" autocomplete="off" class="input-dialog"></el-input>
            </el-form-item>
            <div><a>忘记密码</a></div>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="loginConfirm()">确 定</el-button>
          </span>
        </el-dialog>
      </el-menu>
      <el-menu mode="horizontal" v-else>
        <el-menu-item><el-button type="text" size="small" @click="viewPersonal()">个人中心</el-button></el-menu-item>
        <el-menu-item><el-button type="text" size="small" @click="logoff()" >退出登录</el-button></el-menu-item>
      </el-menu>
    </el-col>
  </el-row>

</template>

<script>
  import { requestUserLogin } from '@/api/user'
  export default {
    data() {
      var path = this.$route.path;
      return {
        activeIndex: path,
        loginStatus: this.getLoginStatus(),
        loginDialogVisible: false,
        loginForm: {
          identifier: '',
          credential: ''
        },
        formLabelWidth: '120px',
        loginRules: {
          identifier: [
            { required: true, message: '请输入用户名 / 邮箱', trigger: 'blur' },
            { min: 3, max: 50, message: '长度在 3 个字符以上', trigger: 'blur' }
          ],
          credential: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 6, max: 50, message: '长度在 6 个字符以上', trigger: 'blur' }
          ],
        }
      };
    },
    inject: ['reload'],
    methods: {
      getLoginStatus() {
        return this.$store.state.token === '' ? false : true;
      },
      loginConfirm() {
        var that = this;
        var formName = 'loginForm';
        this.$refs[formName].validate((valid) => {
          if (valid) {
            // 发送登录请求
            const {identifier, credential} = this.loginForm;
            let params = {identifier, credential};
            requestUserLogin(params).then(function(successRes){
              if (successRes.body.code === 1) {
                that.$store.commit('setToken', successRes.body.token, successRes.body.roleType);
                that.$store.commit('setRoleType', successRes.body.roleType);
                that.loginStatus = true;
                that.$refs[formName].resetFields();
                that.loginDialogVisible = false;
                that.reload();
              } else {
                that.$message(successRes.body.message);
              }
            },function(errRes){
              console.log(errRes.status);
            });
          } else {
            return false;
          }
        });
      },
      logoff() {
        this.$store.commit('clearToken');
        this.loginStatus = false;
        this.reload();
      },
      viewPersonal() {
        this.$router.push("/personal");
      },
      toRegister() {
        this.$router.push("/register");
      }
    }
  }
</script>

<style>
  a {
    text-decoration: none;
  }

  .img-logo {
    width: 100%;
    padding-top: 5px;
    transform: scale(0.9, 0.9);
  }

  .input-dialog {
    width: 80%;
    float: left;
  }

  .form-login {
    text-align: center;
  }

  .item-dialog {
    padding-left: 10%;
  }
</style>
