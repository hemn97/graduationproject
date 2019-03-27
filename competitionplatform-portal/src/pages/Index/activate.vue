<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-main class="main">
      <h1 style="text-align: center">账号激活</h1>
      <div class="div-form">
        <el-form ref="form" :model="form" :rules="formRules" label-width="120px">
          <el-form-item label="用户名 / 邮箱" prop="identifier">
            <el-input size="mini" v-model="form.identifier" style="width: 250px;"></el-input>
          </el-form-item>
          <el-form-item label="激活码" prop="activateCode">
            <el-input size="mini" v-model="form.activateCode" style="width: 250px;"></el-input>
            <el-button size="mini" type="text" @click="sendCode" style="margin-left: 5px;"
                       :disabled="sendCodeDisable">{{ sendCodeText }}</el-button>
          </el-form-item>
          <el-form-item class="item-button">
            <el-button size="mini" type="primary" @click="submit" style="width: 120px;">激活</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-main>
    <el-footer><Footer></Footer></el-footer>
  </el-container>
</template>

<script>
  import Header from '@/components/header'
  import Footer from '@/components/footer'
  import { requestActivate,requestActivateResend } from "../../api/user";

  export default {
    name: "activate",
    components: {
      Header,
      Footer
    },
    methods: {
      submit() {
        let params = this.form;
        var that = this;
        var formName = 'form';
        this.$refs[formName].validate((valid) => {
          if (valid) {
            requestActivate(params).then(function(successRes){
              if (successRes.body.code === 1) {
                if (successRes.body.status == 1) {
                  // 激活成功
                  that.$notify.success({
                    title: '操作提示',
                    message: '激活成功，自动登录',
                    showClose: true
                  });
                  that.$store.commit('setToken', successRes.body.token);
                  that.$router.push("/");
                } else if (successRes.body.status == -1) {
                  that.$message('用户不存在');
                } else if (successRes.body.status == -2) {
                  that.$message('该用户已经激活使用');
                } else if (successRes.body.status == -3) {
                  that.$message('激活码错误');
                }
              }
            },function(errRes){
              console.log(errRes.status);
            });
          } else {
            return false;
          }
        });
      },
      sendCode() {
        if (this.form.identifier.length < 4 || this.form.identifier.length > 20) {
          this.$message('用户名 / 邮箱长度在 4 个字符到 20 个字符之间');
          return;
        }
        let params = this.form;
        var that = this;
        this.sendCodeCount();
        requestActivateResend(params).then(function(successRes){
          if (successRes.body.code === 1) {
            if (successRes.body.status == 1) {  // 发送成功
              that.$notify.success({
                title: '操作提示',
                message: '激活码已经发至您的电子邮箱',
                showClose: true
              });
            } else if (successRes.body.status == -1){
              that.$message('用户不存在');
            } else if (successRes.body.status == -2){
              that.$message('用户已经激活');
            } else {
              that.$message('操作失败');
            }
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      sendCodeCount() {
        var that = this;
        if (this.codeCountDown == 0) {
          this.sendCodeDisable = false;
          this.sendCodeText = "重新发送";
          this.codeCountDown = 60;
          return;
        } else {
          this.sendCodeDisable = true;
          this.sendCodeText = this.codeCountDown+"s后重新发送";
          this.codeCountDown = this.codeCountDown-1;
        }
        setTimeout(function () {
          that.sendCodeCount();
        }, 1000)
      }
    },
    data() {
      return {
        form: {
          identifier: '',
          activateCode: '',
        },
        formRules: {
          identifier: [
            {required: true, message: '请输入用户名 / 邮箱', trigger: 'blur'},
            {min: 4, max: 200, message: '长度在 4 个字符到 20 个字符之间', trigger: 'blur'}
          ],
          activateCode: [
            {required: true, message: '请输入激活码', trigger: 'blur'},
            {min: 6, max: 6, message: '长度为 6 个字符', trigger: 'blur'}
          ],
        },
        codeCountDown: 60,
        sendCodeDisable: false,
        sendCodeText: "重新发送"
      };
    },
    created() {
      if (this.$store.state.token != '') {  // 已登录则默认回到主页
        this.$router.push("/");
      }
    }
  }
</script>

<style scoped>
  .div-form {
    width: 45%;
    margin: 0 auto;
  }
  .item-button {
    padding-left: 65px;
  }
</style>
