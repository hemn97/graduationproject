<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-main class="main">
      <div class="div-body">
        <div><span>{{ post.releaseTime }}</span><span class="span-stat">{{ post.viewNumber ? post.viewNumber : 0 }} 次浏览</span></div>
        <h2>{{ post.title }}</h2>
        <div style="display: flex;">
          <img src="@/assets/user.png" class="img-user"/>
          <span class="span-nickname">{{ post.school.split("_")[2] }} - {{ post.nickname }}</span>
        </div>
        <div class="div-content">
          <div v-html='post.content'></div>
        </div>
        <div class="div-comment-header">
          <el-badge :value="post.commentNumber ? post.commentNumber : 0">评论</el-badge>
          <el-button type="text" size="small" style="margin-left: 25px" @click="selectOrder">{{ orderByClause }}</el-button>
          <div style="float: right;padding-right: 15px;">
            <el-button size="small" @click="releaseComment(-1, -1)" icon="el-icon-edit">评论</el-button>
            <el-dialog title="评论" :visible.sync="releaseCommentDialogVisible">
              <el-form :model="releaseCommentForm" :rules="releaseCommentFormRules" ref="releaseCommentForm">
                <el-form-item label="评论内容" prop="content">
                  <el-input v-model="releaseCommentForm.content" autocomplete="off"
                            maxlength="200" :clearable=true type="textarea" rows="5" resize="none"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="releaseCommentDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="confirmReleaseComment">确 定</el-button>
              </div>
            </el-dialog>
          </div>
        </div>
        <div class="div-comment-list">
          <h3 style="text-align: center;" v-if="maxPage==0">暂时没有评论</h3>
          <el-timeline v-else>
            <el-timeline-item v-for="(data,index) in comments" :key="index" :timestamp="data.releaseTime" placement="top">
              <el-card>
                <div style="display: flex;">
                  <img src="@/assets/user.png" class="img-user"/>
                  <span class="span-nickname">{{ data.nickname }}</span>
                </div>
                <div v-if="data.replyId!=-1" class="div-comment-reply">
                  <p style="font-weight: bold;">引用 @<span style="color: #3399cc;">{{ data.replyNickname }}</span> 发表的：</p>
                  <p>{{ data.replyContent }}</p>
                </div>
                <p class="p-comment">{{ data.content }}</p>
                <div style="float: right;padding-bottom: 5px;">
                  <el-button type="text" size="mini" @click="releaseComment(data.id, data.fartherId)">回复</el-button>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
          <el-pagination layout="prev, pager, next" :page-size="10" :total="maxPage*10" @current-change="handleCurChange"
                         :current-page.sync="curPage" class="pagination" v-if="maxPage>1">
          </el-pagination>
        </div>
      </div>
    </el-main>
    <el-footer><Footer></Footer></el-footer>
  </el-container>
</template>

<script>
  import Header from '@/components/header'
  import Footer from '@/components/footer'
  import { requestPostDetails,requestCommentList,requestCommentMaxPage,requestReleaseComment } from '@/api/post'
  export default {
    name: "post",
    components: {
      Header,
      Footer
    },
    methods: {
      handleCurChange(page) {
        this.curPage = page;
        this.getComments();
      },
      getPostDetails() {
        var that = this;
        let params = {'id':this.postId};
        requestPostDetails(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.post = successRes.body.data;
          } else {
            that.$router.push("/notfound");
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getComments() {
        var that = this;
        let params = {'pageNumber':this.curPage, 'pageSize':10, 'themeId':this.postId, 'fartherId':-1,
          'orderByClause':this.orderByClause};
        requestCommentList(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.comments = successRes.body.list;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getMaxPage() {
        var that = this;
        let params = {'pageSize':10, 'themeId':this.postId, 'fartherId':-1};
        requestCommentMaxPage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.maxPage = successRes.body.maxPage;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      selectOrder() {
        this.orderByClause == '按时间正序' ? this.orderByClause = '按时间逆序' : this.orderByClause = '按时间正序';
        this.curPage = 1;
        this.getComments();
      },
      releaseComment(replyId, fartherId) {
        if (this.$store.state.token == '') {
          this.$message('请先登录');
        } else {
          this.releaseCommentForm.replyId = replyId;
          if (replyId != -1) {  // 引用回复
            fartherId==-1 ? this.releaseCommentForm.fartherId = replyId : this.releaseCommentForm.fartherId = fartherId;
          }
          this.releaseCommentDialogVisible = true;
        }
      },
      confirmReleaseComment() {
        let params = this.releaseCommentForm;
        var that = this;
        var formName = 'releaseCommentForm';
        this.$refs[formName].validate((valid) => {
          if (valid) {
            requestReleaseComment(params).then(function(successRes){
              if (successRes.body.code === 1) {
                that.$notify.success({
                  title: '操作提示',
                  message: '发表评论成功',
                  showClose: true
                });
                that.releaseCommentDialogVisible = false;
                that.getPostDetails();
                that.curPage = 1;
                that.orderByClause = '按时间正序';
                that.getMaxPage();
                that.getComments();
                that.$refs[formName].resetFields();
              }
            },function(errRes){
              console.log(errRes.status);
            });
          } else {
            return false;
          }
        });
      },
    },
    data() {
      return {
        postId: 0,
        curPage: 1,
        maxPage: 0,
        comments: [],
        post: {},
        orderByClause: '按时间正序',
        releaseCommentDialogVisible: false,
        releaseCommentForm: {
          content: '',
          replyId: -1,
          fartherId: -1,
          themeId: this.$route.path.substring(6),
        },
        releaseCommentFormRules: {
          content: [
            {required: true, message: '请输入评论内容', trigger: 'blur'},
            {min: 4, max: 200, message: '长度在 4 个字符到 200 个字符之间', trigger: 'blur'}
          ],
        }
      };
    },
    created() {
      this.postId = this.$route.path.substring(6);
      this.getPostDetails();
      this.getComments();
      this.getMaxPage();
    }
  }
</script>

<style scoped>
  .div-body {
    width: 60%;
    margin: 0 auto;
  }

  .img-user {
    width: 32px;
    height: 32px;
  }

  .span-nickname {
    line-height: 32px;
    vertical-align: middle;
    margin-left: 10px;
    font-size: small;
  }

  .div-content {
    margin-top: 15px;
  }

  .div-content p {
    text-align: left;
  }

  .div-comment-header {
    padding-top: 15px;
    font-weight: bold;
    border-bottom: 1px solid #ccffff;
    padding-bottom: 5px;
  }
  .span-stat {
    font-size: small;
    margin-left: 15px;
  }

  .div-comment-list {
    margin-top: 15px;
  }

  .p-comment {
    text-align: left;
  }

  .pagination {
    text-align: center;
  }

  .div-comment-reply {
    margin-top: 10px;
    padding: 5px;
    background-color: #FFF0F5;
    border-radius: 5px;
  }

  .div-comment-reply p {
    text-align: left;
    padding: 0 5px;
  }

  .div-content >>> .ql-syntax {
    background-color: #23241f;
    color: #f8f8f2;
    overflow: visible;
    padding: 5px;
    border-radius: 5px;
  }

  .div-content >>> blockquote {
    border-left: 4px solid #ccc;
    margin-bottom: 5px;
    margin-top: 5px;
    padding-left: 16px;
  }
</style>
