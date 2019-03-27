<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-main>
      <h1 style="text-align: center">新用户注册
        <el-button size="mini" type="text" @click="toActivate">激活</el-button>
      </h1>
      <div class="div-form">
        <el-form ref="form" :model="form" :rules="formRules" label-width="80px">
          <el-form-item label="用户名" prop="username">
            <el-input size="mini" v-model="form.username" style="width: 250px;"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="credential">
            <el-input size="mini" type="password" v-model="form.credential" style="width: 250px;"></el-input>
          </el-form-item>
          <el-form-item label="昵称" prop="nickname">
            <el-input size="mini" v-model="form.nickname" style="width: 250px;"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-radio v-model="form.gender" label="男">男</el-radio>
            <el-radio v-model="form.gender" label="女">女</el-radio>
          </el-form-item>
          <el-form-item label="真实姓名" prop="realname">
            <el-input size="mini" v-model="form.realname" style="width: 250px;"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="telNumber">
            <el-input size="mini" v-model="form.telNumber" style="width: 250px;"></el-input>
          </el-form-item>
          <el-form-item label="电子邮箱" prop="email">
            <el-input size="mini" v-model="form.email" style="width: 250px;"></el-input>
          </el-form-item>
          <el-form-item label="个人技能" prop="skills">
            <el-input size="mini" v-model="form.skills" style="width: 250px;" disabled></el-input>
            <el-button size="mini" @click="selectSkillsDialogVisible = true">选择技能</el-button>
            <el-dialog title="选择个人擅长技能" :visible.sync="selectSkillsDialogVisible" width="45%" center>
              <el-transfer v-model="skillsSelected" :data="skillOptions" :titles="skillsTitle"></el-transfer>
              <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="confirmSelectSkills">确 定</el-button>
              </span>
            </el-dialog>
          </el-form-item>
          <el-form-item label="学校" prop="school">
            <el-input size="mini" v-model="form.school" style="width: 250px;"></el-input>
          </el-form-item>
          <el-form-item class="item-button">
            <el-button size="mini" type="primary" @click="submit" style="width: 120px;">注册</el-button>
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
  import { requestRegister } from "../../api/user";

  export default {
    name: "register",
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
            requestRegister(params).then(function(successRes){
              if (successRes.body.code === 1) {
                if (successRes.body.status == 1) {
                  // 注册成功，前往激活
                  that.$notify.success({
                    title: '操作提示',
                    message: '注册成功，激活码已经发至您的电子邮箱',
                    showClose: true
                  });
                  that.$router.push("/activate");
                } else if (successRes.body.status == -1) {
                  that.$message('昵称已被注册');
                } else if (successRes.body.status == -2) {
                  that.$message('用户名已被注册');
                } else if (successRes.body.status == -3) {
                  that.$message('邮箱已被注册');
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
      confirmSelectSkills() {
        this.form.skills = this.skillsSelected.join(",");
        this.selectSkillsDialogVisible = false;
      },
      toActivate() {
        this.$router.push("/activate");
      }
    },
    data() {
      let emailReg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
      var validateEmail = (rule, value, callback) => {
        if(!emailReg.test(value))  {
          callback(new Error('请输入正确的邮箱格式'));
        } else {
          callback();
        }
      };
      return {
        form: {},
        selectSkillsDialogVisible: false,
        skillsTitle: ['技能可选项','已选择技能'],
        skillsSelected: [],
        skillOptions: [{key: '数学建模',label:'数学建模'}, {key: '程序设计',key:'程序设计'}, {key: '机器人',label:'机器人'},
          {key: '工程机械',key:'工程机械'}, {key: '土木建筑',key:'土木建筑'},{key: '大数据',label:'大数据'},
          {key: '交通车辆',label:'交通车辆'}, {key: '航空航天',label:'航空航天'}, {key: '船舶海洋',label:'船舶海洋'},
          {key: '环境能源',label:'环境能源'}, {key: '计算机与信息技术',label:'计算机与信息技术'},
          {key: '材料高分子',label:'材料高分子'},{key: '电子与自动化',label:'电子与自动化'},
          {key: '工业与创意设计',label:'工业与创意设计'}, {key: '外语',label:'外语'},
          {key: '演讲主持与辩论',label:'演讲主持与辩论'}, {key: '模特',label:'模特'},
          {key: '歌舞书画与摄影',label:'歌舞书画与摄影'},
          {key: '体育',label:'体育'}, {key: 'UI设计',label:'UI设计'}, {key: '服装设计',label:'服装设计'},
          {key: '电子竞技',label:'电子竞技'},{key: '数学',label:'数学'}, {key: '物理',label:'物理'},
          {key: '化学化工',label:'化学化工'},{key: '健康生命与医学',label:'健康生命与医学'}, {key: '力学',label:'力学'},
          {key: '创业',label:'创业'}, {key: '商业',label:'商业'},{key: '职业技能',label:'职业技能'},
          {key: '环保公益',label:'环保公益'}, {key: '社会综合',label:'社会综合'}
        ],
        formRules: {
          username: [
            {required: true, message: '请输入用户名', trigger: 'blur'},
            {min: 4, max: 200, message: '长度在 4 个字符到 12 个字符之间', trigger: 'blur'}
          ],
          credential: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 4, max: 200, message: '长度在 6 个字符到 20 个字符之间', trigger: 'blur'}
          ],
          nickname: [
            {required: true, message: '请输入昵称', trigger: 'blur'},
            {min: 4, max: 200, message: '长度在 4 个字符到 12 个字符之间', trigger: 'blur'}
          ],
          realname: [
            {required: true, message: '请输入真实姓名', trigger: 'blur'},
            {min: 4, max: 200, message: '长度在 2 个字符到 12 个字符之间', trigger: 'blur'}
          ],
          gender: [
            {required: true, message: '请选择性别', trigger: 'blur'}
          ],
          email: [
            {required: true, message: '请输入电子邮箱', trigger: 'blur'},
            {validator: validateEmail, trigger: 'blur' }
          ],
          school: [
            {required: true, message: '请输入学校名称', trigger: 'blur'},
            {min: 4, max: 200, message: '长度在 4 个字符到 20 个字符之间', trigger: 'blur'}
          ],
        }
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
    width: 40%;
    margin: 0 auto;
  }
  .item-button {
    padding-left: 70px;
  }
</style>
