<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-main>
      <h1 style="text-align: center">发帖</h1>
      <div class="div-body">
        <div style="display: flex;">
          <el-input v-model="title" placeholder="请输入标题"></el-input>
          <el-select v-model="category" placeholder="请选择发布版块" style="padding-left: 10px;">
            <el-option  v-for="item in options" :key="item" :label="item" :value="item">
            </el-option>
          </el-select>
        </div>
        <div class="div-content">
          <quill-editor v-model="content" ref="quillEditor" :options="editorOption"></quill-editor>
        </div>
        <div class="div-confirm"><el-button type="primary" size="small" @click="confirmRelease">发布</el-button></div>
      </div>
    </el-main>
    <el-footer><Footer></Footer></el-footer>
  </el-container>
</template>

<script>
  import Header from '@/components/header'
  import Footer from '@/components/footer'
  import { quillEditor,Quill } from 'vue-quill-editor'
  import { requestReleasePost } from '@/api/post'
  export default {
    name: "releasePost",
    components: {
      Header,
      Footer
    },
    methods: {
      confirmRelease() {
        if (this.title.length < 6 || this.title.length > 20) {
          this.$message({
            message: '标题长度在 6 个字符到 20 个字符之间',
            type: 'warning'
          });
          return;
        }
        if (this.category=='') {
          this.$message({
            message: '请选择要发布到的版块',
            type: 'warning'
          });
          return;
        }
        if (this.content.length < 20 || this.content.length > 200) {
          this.$message({
            message: '内容长度在 20 个字符到 200 个字符之间',
            type: 'warning'
          });
          return;
        }
        var that = this;
        let params = {'title':this.title, 'content':this.content, 'abstractContent':this.editor.getText(),
          'category':this.category };
        requestReleasePost(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.$notify.success({
              title: '操作提示',
              message: '发帖成功',
              showClose: true
            });
            that.$router.push(`/post/${successRes.body.id}`);
          }
        },function(errRes){
          console.log(errRes.status);
        });
      }
    },
    data() {
      return {
        title: '',
        content: '',
        category: '',
        options: ['工科', '文体', '理科', '商科', '综合', '竞赛', '谈天说地'],
        editorOption: {
          modules:{
            toolbar:[
              ['bold', 'italic', 'underline'],
              ['blockquote', 'code-block']
            ]
          }
        }
      };
    },
    created() {
      if (this.$store.state.token == '') {
        this.$message('请先登录');
        this.$router.push("/");
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
  .div-body {
    width: 50%;
    margin: 0 auto;
  }
  .div-content {
    margin-top: 10px;
  }
  .div-confirm {
    text-align: center;
    margin-top: 10px;
  }
</style>
