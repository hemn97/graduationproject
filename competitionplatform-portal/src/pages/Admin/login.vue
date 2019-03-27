<template>
  <div>
    <h1 style="text-align: center;margin-top: 160px;">竞赛平台管理系统</h1>
    <div class="div-form">
      <el-form ref="form" :model="form" :rules="formRules" label-width="120px">
        <el-form-item label="用户名 / 邮箱" prop="identifier">
          <el-input size="mini" v-model="form.identifier" style="width: 250px;"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="credential">
          <el-input size="mini" type="password" v-model="form.credential" style="width: 250px;"></el-input>
        </el-form-item>
        <el-form-item class="item-button">
          <el-button size="mini" type="primary" @click="submit" style="width: 120px;">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  import { requestUserLogin} from "../../api/user";

  export default {
    name: "adminLogin",
    methods: {
      submit() {
        var that = this;
        var formName = 'form';
        this.$refs[formName].validate((valid) => {
          if (valid) {
            // 发送登录请求
            let params = this.form;
            requestUserLogin(params).then(function(successRes){
              if (successRes.body.code === 1) {
                if (successRes.body.roleType == 2) {
                  that.$store.commit('setToken', successRes.body.token);
                  that.$store.commit('setRoleType', successRes.body.roleType);
                  that.$router.push("/admin");
                } else {
                  that.$message('非管理员权限');
                }
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
      }
    },
    data() {
      return {
        form: {},
        formRules: {
          identifier: [
            {required: true, message: '请输入用户名 / 邮箱', trigger: 'blur'},
            {min: 4, max: 200, message: '长度在 4 个字符到 12 个字符之间', trigger: 'blur'}
          ],
          credential: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 4, max: 200, message: '长度在 6 个字符到 20 个字符之间', trigger: 'blur'}
          ]
        }
      }
    }
  }
</script>

<style scoped>
  .div-form {
    width: 40%;
    margin: 0 auto;
  }
  .item-button {
    padding-left: 50px;
  }
</style>
