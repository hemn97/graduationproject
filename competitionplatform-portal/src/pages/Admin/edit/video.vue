<template>
  <div>
    <el-row>
      <el-col :span="4">
        <img class="img-logo" src="@/assets/logo.png">
      </el-col>
      <el-col :span="16">
        <h4>编辑视频</h4>
      </el-col>
      <el-col :span="4" style="margin-top: 16px;">
        <el-button size="small" @click="backPage()" >返回上一页</el-button>
        <el-button size="small" @click="logoff()" >退出登录</el-button>
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px;">
      <el-form ref="form" :model="form" :rules="formRules" label-width="120px">
        <el-form-item label="标题" prop="title">
          <el-input size="mini" v-model="form.title" style="width: 300px;"></el-input>
        </el-form-item>
        <el-form-item label="主讲人" prop="author">
          <el-input size="mini" v-model="form.author" style="width: 300px;"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="introduction">
          <el-input type="textarea" size="mini" v-model="form.introduction" style="width: 500px;" resize="none" rows=4></el-input>
        </el-form-item>
        <el-form-item label="封面链接地址" prop="figure">
          <el-input size="mini" v-model="form.figure" style="width: 500px;"></el-input>
        </el-form-item>
        <el-form-item label="视频源链接地址" prop="src">
          <el-input size="mini" v-model="form.src" style="width: 500px;"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-dropdown @command="handleCategoryCommand">
            <el-button size="small" class="button-selector">
              <span>{{ form.category }}<i class="el-icon-arrow-down el-icon--right"></i></span>
            </el-button>
            <el-dropdown-menu slot="dropdown" style="width:120px;">
              <el-dropdown-item command="工科">工科</el-dropdown-item>
              <el-dropdown-item command="文体">文体</el-dropdown-item>
              <el-dropdown-item command="理科">理科</el-dropdown-item>
              <el-dropdown-item command="商科">商科</el-dropdown-item>
              <el-dropdown-item command="综合">综合</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input size="mini" v-model="form.tags" style="width: 250px;" disabled></el-input>
          <el-button size="mini" @click="selectTagsDialogVisible = true">选择标签</el-button>
          <el-dialog title="选择视频标签" :visible.sync="selectTagsDialogVisible" width="45%" center>
            <el-transfer v-model="tagsSelected" :data="tagOptions" :titles="tagsTitle"></el-transfer>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="confirmSelectTags">确 定</el-button>
              </span>
          </el-dialog>
        </el-form-item>
        <el-form-item class="item-button">
          <el-button size="mini" type="primary" @click="submit" style="width: 120px;">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-row>
  </div>
</template>

<script>
  import { requestVideoDetails,requestUpdateVideo } from "../../../api/video";

  export default {
    name: "video",
    created() {
      if (this.$store.state.token == '' || this.$store.state.roleType != 2) {   // 非管理员登录状态则回到登录主页
        this.$router.push("/admin/login");
      }
      this.form.id = this.$route.path.split("/")[4];
      this.getDetails();
    },
    methods: {
      backPage() {
        this.$router.go(-1);
      },
      logoff() {
        this.$store.commit('clearToken');
        this.$router.push("/admin/login");
      },
      getDetails() {
        var that = this;
        let params = {'id':this.form.id};
        requestVideoDetails(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.form = successRes.body.data;
          } else {
            that.$router.push("/admin");
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      submit() {
        let params = this.form;
        var that = this;
        var formName = 'form';
        this.$refs[formName].validate((valid) => {
          if (valid) {
            requestUpdateVideo(params).then(function(successRes){
              if (successRes.body.code === 1) {
                that.$notify.success({
                  title: '操作提示',
                  message: '修改视频信息成功',
                  showClose: true
                });
              }
            },function(errRes){
              console.log(errRes.status);
            });
          } else {
            return false;
          }
        });
      },
      handleCategoryCommand(command) {
        this.form.category = command;
      },
      confirmSelectTags() {
        this.form.tags = this.tagsSelected.join(",");
        this.selectTagsDialogVisible = false;
      }
    },
    data() {
      return {
        id: 0,
        form: {
          title: '',
          author: '',
          introduction: '',
          figure: '',
          category: '工科',
          tags: '',
          src: '',
        },
        formRules: {
          title: [
            {required: true, message: '请输入视频标题', trigger: 'blur'},
            {min: 4, max: 30, message: '长度在 4 个字符到 30 个字符之间', trigger: 'blur'}
          ],
          author: [
            {required: true, message: '请输入视频主讲人', trigger: 'blur'},
            {min: 2, max: 30, message: '长度在 2 个字符到 30 个字符之间', trigger: 'blur'}
          ],
          introduction: [
            {required: true, message: '请输入视频简介', trigger: 'blur'},
            {min: 10, max: 120, message: '长度在 10 个字符到 120 个字符之间', trigger: 'blur'}
          ],
          figure: [
            {required: true, message: '请输入视频封面的链接地址', trigger: 'blur'},
          ],
          category: [
            {required: true, message: '请选择视频分类', trigger: 'blur'},
          ],
          tags: [
            {required: true, message: '请选择视频标签', trigger: 'blur'},
          ],
          src: [
            {required: true, message: '请输入视频源的链接地址', trigger: 'blur'},
          ],
        },
        selectTagsDialogVisible: false,
        tagsTitle: ['标签可选项','已选择标签'],
        tagsSelected: [],
        tagOptions: [{key: '数学建模',label:'数学建模'}, {key: '程序设计',key:'程序设计'}, {key: '机器人',label:'机器人'},
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
      }
    },
  }
</script>

<style scoped>

</style>
