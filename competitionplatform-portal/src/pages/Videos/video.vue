<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-main class="main">
      <el-row>
        <el-col :span="12">
          <video :src="videoDetails.src" controls="controls" class="video">
            您的浏览器不支持 video 标签。
          </video>
        </el-col>
        <el-col :span="12">
          <p class="p-title">{{ videoDetails.title }}</p>
          <p class="p-intros">{{ videoDetails.introduction }}</p>
          <p class="p-small">主讲人：{{ videoDetails.author }}</p>
          <p class="p-small">分类：{{ videoDetails.category }}</p>
          <p class="p-small">标签：{{ videoDetails.tags }}</p>
          <p class="p-small p-viewstat">已有{{ videoDetails.viewNumber ? videoDetails.viewNumber : 0 }}人看过</p>
        </el-col>
      </el-row>
      <el-row class="row-middle">
        <el-col :span="18" class="col-middle-left">
          <el-row class="row-comment-header">
            <el-badge>视频评价</el-badge>
          </el-row>
          <el-row class="row-score">
            <div class="div-score-left">{{ videoDetails.score ? videoDetails.score : "0.0" }}</div>
            <div class="div-score-right">
              <el-rate v-model="score" disabled text-color="#ff9900" score-template="{value}">
              </el-rate>
              <div class="div-socre-times">共{{ videoDetails.evaluateNumber ? videoDetails.evaluateNumber : 0 }}条评价</div>
            </div>
          </el-row>
          <el-row class="row-order">
            <el-button type="text" size="mini" style="padding-right: 10px;" @click="releaseEva">评价</el-button>
            <el-radio-group v-model="evaluationParams.orderByClause" size="mini" fill="#CD8500" @change="handleEvaOrderChange">
              <el-radio-button label="按时间正序"></el-radio-button>
              <el-radio-button label="按时间逆序"></el-radio-button>
            </el-radio-group>
          </el-row>
          <el-dialog title="评价" :visible.sync="releaseEvaDialogVisible">
            <el-form :model="releaseEvaForm" :rules="releaseEvaFormRules" ref="releaseEvaForm">
              <el-form-item label="视频评分" prop="score">
                <el-radio-group v-model="releaseEvaForm.score" size="small">
                  <el-radio-button label="0"></el-radio-button>
                  <el-radio-button label="1"></el-radio-button>
                  <el-radio-button label="2"></el-radio-button>
                  <el-radio-button label="3"></el-radio-button>
                  <el-radio-button label="4"></el-radio-button>
                  <el-radio-button label="5"></el-radio-button>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="评价内容" prop="comment">
                <el-input v-model="releaseEvaForm.comment" autocomplete="off"
                          maxlength="200" :clearable=true type="textarea" rows="5" resize="none"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="releaseEvaDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="confirmReleaseEva">确 定</el-button>
            </div>
          </el-dialog>
          <el-row class="div-eva-list">
            <h3 v-if="evaluationParams.maxPage==0">暂时没有任何视频评价</h3>
            <div class="div-eva-item" v-for="(evaluation,index) in evaluations" :key="index">
              <div class="div-eva-header">
                <img src="@/assets/user.png" />
                <span>{{ evaluation.nickname }}</span>
              </div>
              <div class="div-eva-body">{{ evaluation.comment }}</div>
              <div class="div-eva-footer">
                <span style="color: #808080;">评价于 {{ evaluation.evaluateTime }}</span>
                <span style="margin-left: 10px;">{{ evaluation.score }}分</span>
              </div>
            </div>
          </el-row>
        </el-col>
        <el-col :span="6">
          <el-row class="row-recommend-header">
            <div>相似推荐</div>
          </el-row>
          <el-row>
            <h5 v-if="recommendList.length==0">暂时没有类似视频</h5>
            <div v-for="(item,index) in recommendList" :key="index" style="display: flex;margin: 5px 0;">
              <a href="javascript:void(0);" @click="toVideo(item.id)" style="color: #CD8500;">{{ item.title }}</a>
              <div style="margin-left: 10px;color: #808080;">{{ item.score }}分</div>
            </div>
          </el-row>
        </el-col>
      </el-row>
    </el-main>
    <el-footer><Footer></Footer></el-footer>
  </el-container>
</template>

<script>
  import Header from '@/components/header'
  import Footer from '@/components/footer'
  import { requestVideoDetails,requestEvaluationList,requestEvaluationMaxPage,
    requestReleaseEvaluation,requestVideoList,requestVideoMaxPage } from '@/api/video'
    export default {
      name: "videoplayer",
      components: {
        Header,
        Footer
      },
      data() {
        return {
          videoDetails: {
            id: 0
          },
          score: 0,
          evaluationParams: {
            orderByClause: '按时间正序',
            pageNumber: 1,
            maxPage: 0,
            pageSize: 10,
            videoId: 0,
          },
          evaluations: [],
          releaseEvaDialogVisible: false,
          releaseEvaForm: {
            score: '',
            comment: '',
            videoId: '',
          },
          releaseEvaFormRules: {
            score: [
              {required: true, message: '请给视频打分', trigger: 'blur'},
            ],
            comment: [
              {required: true, message: '请输入评价内容', trigger: 'blur'},
              {min: 4, max: 200, message: '长度在 4 个字符到 200 个字符之间', trigger: 'blur'}
            ],
          },
          recommendList: []
        }
      },
      methods: {
        getVideoDetails() {
          var that = this;
          let params = {'id':this.videoDetails.id};
          requestVideoDetails(params).then(function(successRes){
            if (successRes.body.code === 1) {
              that.videoDetails = successRes.body.data;
              that.score = parseFloat(that.videoDetails.score);
              that.getRecommendList();
            } else {
              that.$router.push("/notfound");
            }
          },function(errRes){
            console.log(errRes.status);
          });
        },
        getEvaluationList() {
          var that = this;
          let params = this.evaluationParams;
          requestEvaluationList(params).then(function(successRes){
            if (successRes.body.code === 1) {
              that.evaluations = successRes.body.list;
            }
          },function(errRes){
            console.log(errRes.status);
          });
        },
        getEvaluationMaxPage() {
          var that = this;
          let params = this.evaluationParams;
          requestEvaluationMaxPage(params).then(function(successRes){
            if (successRes.body.code === 1) {
              that.evaluationParams.maxPage = successRes.body.maxPage;
            }
          },function(errRes){
            console.log(errRes.status);
          });
        },
        handleEvaOrderChange() {
          this.evaluationParams.pageNumber = 1;
          this.getEvaluationList();
        },
        releaseEva() {
          if (this.$store.state.token == '') {
            this.$message('请先登录');
          } else {
            this.releaseEvaDialogVisible = true;
          }
        },
        confirmReleaseEva() {
          let params = this.releaseEvaForm;
          var that = this;
          var formName = 'releaseEvaForm';
          this.$refs[formName].validate((valid) => {
            if (valid) {
              requestReleaseEvaluation(params).then(function(successRes){
                if (successRes.body.code === 1) {
                  if (successRes.body.status == 1) {
                    that.$notify.success({
                      title: '操作提示',
                      message: '发表评价成功',
                      showClose: true
                    });
                    that.getVideoDetails();
                    that.evaluationParams.curPage = 1;
                    that.evaluationParams.orderByClause = '按时间正序';
                    that.getEvaluationMaxPage();
                    that.getEvaluationList();
                  } else {
                    that.$notify({
                      title: '操作提示',
                      message: '你已经评价过该视频了，请勿重复评价',
                      type: 'warning'
                    });
                  }
                  that.releaseEvaDialogVisible = false;
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
        getRecommendList() {
          var that = this;
          let params = {'pageNumber':1, 'pageSize':5, 'tags':this.videoDetails.tags, 'id':this.videoDetails.id,
            'orderByClause': '按评分排序'};
          requestVideoList(params).then(function(successRes){
            if (successRes.body.code === 1) {
              that.recommendList = successRes.body.list;
            }
          },function(errRes){
            console.log(errRes.status);
          });
        },
        toVideo(id) {
          this.$router.push("/video/"+id);
        },
        fetchData() {
          this.videoDetails.id = this.$route.path.substring(7);
          this.evaluationParams.videoId = this.videoDetails.id;
          this.releaseEvaForm.videoId = this.videoDetails.id;
          this.getVideoDetails();
          this.getEvaluationMaxPage();
          this.getEvaluationList();
        }
      },
      created() {
        this.fetchData();
      },
      watch: {
        // 如果路由有变化，会再次执行该方法
        "$route": "fetchData"
      }
    }
</script>

<style scoped>
  .video {
    width: 95%;
  }
  .p-title {
    font-size: x-large;
    font-weight: bold;
    margin-top: 0;
  }
  .p-small {
    font-size: small;
  }
  .p-intros {
    font-size: small;
    line-height: 22px;
    background-color: #FFF0F5;
    padding: 10px;
    border-radius: 5px;
  }
  .p-viewstat {
    color: #53868B;
  }

  .row-middle {
    padding-top: 25px;
  }
  .col-middle-left {
    padding-right: 20px;
  }
  .row-comment-header {
    font-weight: bold;
    border-bottom: 1px solid #ccffff;
    padding-bottom: 5px;
  }
  .row-recommend-header {
    font-weight: bold;
    border-bottom: 1px solid #ccffff;
    padding-bottom: 5px;
  }
  .row-score {
    display: flex;
    margin: 10px 0;
  }
  .div-score-left {
    font-size: 48px;
    font-weight: bold;
    color: #CD8500;
  }
  .div-score-right {
    margin-left: 15px;
    margin-top: 6px;
  }
  .div-socre-times {;
    font-size: 14px;
    color: #CD919E;
  }
  .row-order {
    width: 100%;
    text-align: right;
  }
  .div-eva-list {
    margin-top: 10px;
    font-size: small;
  }
  .div-eva-item {
    margin-bottom: 10px;
  }
  .div-eva-header {
    display: flex;
  }
  .div-eva-header img{
    width: 32px;
    height: 32px;
  }
  .div-eva-header span{
    line-height: 32px;
    vertical-align: middle;
    margin-left: 10px;
    font-size: small;
  }
  .div-eva-body {
    margin: 8px 0;
  }
  .div-eva-footer {
    display: flex;
  }
</style>
