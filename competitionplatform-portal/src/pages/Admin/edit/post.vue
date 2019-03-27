<template>
  <div>
    <el-row>
      <el-col :span="4">
        <img class="img-logo" src="@/assets/logo.png">
      </el-col>
      <el-col :span="16">
        <h4>编辑帖子</h4>
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
        <el-form-item label="分类" prop="category">
          <el-dropdown @command="handleCategoryCommand">
            <el-button size="small" class="button-selector">
              <span>{{ form.category }}<i class="el-icon-arrow-down el-icon--right"></i></span>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="工科">工科</el-dropdown-item>
              <el-dropdown-item command="文体">文体</el-dropdown-item>
              <el-dropdown-item command="理科">理科</el-dropdown-item>
              <el-dropdown-item command="商科">商科</el-dropdown-item>
              <el-dropdown-item command="综合">综合</el-dropdown-item>
              <el-dropdown-item command="竞赛">竞赛</el-dropdown-item>
              <el-dropdown-item command="谈天说地">谈天说地</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <quill-editor v-model="form.content" ref="quillEditor" :options="editorOption"></quill-editor>
        </el-form-item>
        <el-form-item class="item-button">
          <el-button size="mini" type="primary" @click="submit" style="width: 120px;">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-row>
  </div>
</template>

<script>
  import { requestPostDetails,requestUpdatePost } from "../../../api/post";
  import { quillEditor,Quill } from 'vue-quill-editor'

  export default {
    name: "competition",
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
        requestPostDetails(params).then(function(successRes){
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
        this.form.abstractContent = this.editor.getText();
        let params = this.form;
        var that = this;
        var formName = 'form';
        this.$refs[formName].validate((valid) => {
          if (valid) {
            requestUpdatePost(params).then(function(successRes){
              if (successRes.body.code === 1) {
                that.$notify.success({
                  title: '操作提示',
                  message: '修改帖子信息成功',
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
    },
    data() {
      return {
        id: 0,
        form: {
          title: '',
          content: '',
          category: '',
        },
        formRules: {
          title: [
            {required: true, message: '请输入帖子标题', trigger: 'blur'},
            {min: 4, max: 30, message: '长度在 4 个字符到 30 个字符之间', trigger: 'blur'}
          ],
          content: [
            {required: true, message: '请输入帖子内容', trigger: 'blur'},
          ],
          category: [
            {required: true, message: '请选择分类版块', trigger: 'blur'},
          ],
        },
        editorOption: {
          modules:{
            toolbar:[
              ['bold', 'italic', 'underline'],
              ['blockquote', 'code-block']
            ]
          }
        }
      }
    },
    computed: {
      editor() {
        return this.$refs.quillEditor.quill;
      }
    },
    mounted() {
      this.editor.container.style.height = '300px';
    }
  }
</script>

<style scoped>

</style>
