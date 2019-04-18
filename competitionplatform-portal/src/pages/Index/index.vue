<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-main class="main">
      <div><img class="img-bg" src="@/assets/indexbg.jpg"></div>
      <div class="div-recommendation">
        <div class="div-recommendation-header">
          <h4>竞赛推荐</h4>
          <div class="recommendation-header-left"></div>
          <img src="@/assets/compt.png"></img>
          <div class="recommendation-header-right"></div>
        </div>
        <div class="div-recommendation-list">
          <div v-for="(data,index) in recommendCompetitions" :key="index" class="list-box">
            <el-col :span="3" class="list-pic">
              <img :src="data.logo" />
            </el-col>
            <div :span="6" class="list-main">
              <span class="list-title" @click="viewCompetition(data.id)">{{ data.title }}</span>
              <span>{{ data.sponsor }}</span>
              <div class="row-box-tag">
                <ul>
                  <li v-for="(tag,index) in data.tags.split(',')" :key="index">{{ tag }}</li>
                </ul>
              </div>
              <div class="row-box-bottom">
                <div>时间：{{ data.startTime }} 至 {{ data.endTime }}</div>
                <div>
                  <span>参赛队伍：</span>
                  <i>{{ data.teamNumber==undefined ? 0 : data.teamNumber }}</i>
                </div>
                <div>
                  <span>热度值：</span>
                  <i>{{ data.heat==undefined ? 0 : data.heat }}</i>
                </div>
                <div class="link-btn">
                  <a :href="data.link" target="_blank">官网报名</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-main>
    <el-footer><Footer></Footer></el-footer>
  </el-container>
</template>

<script>
  import Header from '@/components/header'
  import Footer from '@/components/footer'
  import { requestCompetitionList } from "../../api/competition";

  export default {
    name: "index",
    components: {
      Header,
      Footer
    },
    data() {
      return {
        recommendCompetitions: []
      }
    },
    methods: {
      getRecommendCompetitionList() {
        var that = this;
        let params = {'pageNumber':1, 'pageSize':5, 'orderByClause': '按最热排序'};
        requestCompetitionList(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.recommendCompetitions = successRes.body.list;
          } else {
            alert(successRes.body.message);
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      viewCompetition(id) {
        this.$router.push("/competition/"+id);
      }
    },
    created() {
      this.getRecommendCompetitionList();
    }
  }
</script>

<style scoped>
  .img-bg {
    width: 100%;
  }

  .recommendation-header-left, .recommendation-header-right {
    display: inline-block;
    height: 1px;
    width: 45%;
    background: #1296db;
    overflow: hidden;
    vertical-align: middle;
    position: relative;
    top: -10px;
  }

  .div-recommendation-header {
    text-align: center;
  }

  .div-recommendation-list {
    margin-top: 10px;
  }

  .list-box {
    background: #F2F2F2;
    display: table;
    width: 1000px;
    min-height: 180px;
    transition: box-shadow 0.25s ease-in;
    -moz-transition: box-shadow 0.25s ease-in;
    -webkit-transition: box-shadow 0.25s ease-in;
    -o-transition: box-shadow 0.25s ease-in;
    margin: 10px auto;
    border-radius: 8px;
  }
  .list-pic {
    width: 260px;
    height: 146px;
  }
  .list-pic img {
    width: 220px;
    height: 146px;
    margin-left: 20px;
    margin-top: 16px;
    -webkit-border-radius: 6px;
    -moz-border-radius: 6px;
    border-radius: 6px;
  }
  .list-main {
    width: 100%;
    min-height: 164px;
    margin-left: -25px;
    margin-top: 16px;
  }
  .list-main span:nth-child(1) {
    font-size: 20px;
    font-weight: bold;
    color: #34383d;
    word-break: break-all;
  }
  .list-main span:nth-child(2) {
    display: block;
    font-size: 16px;
    color: #4a596d;
    word-break: break-all;
    padding-top: 10px;
    font-weight: normal;
  }
  .row-box-tag, .row-box-bottom {
    width: 100%;
    height: 22px;
    display: table-row;
  }
  .row-box-tag ul {
    min-width: 204px;
    display: inline-block;
    float: left;
    height: 20px;
    list-style: none;
    margin-left: -40px;
  }
  .row-box-tag li {
    float: left;
    line-height: 20px;
    padding: 0 13px;
    border: 1px solid #A0AAD2;
    border-radius: 2px;
    font-size: 12px;
    color: #A1A7C0;
    margin-right: 14px;
  }
  .row-box-bottom > div:nth-child(-n+3) {
    float: left;
    line-height: 20px;
    font-size: 14px;
    color: #A0AAD2;
    margin-right: 49px;
  }
  .row-box-bottom span:nth-child(1), .row-box-bottom > div span {
    font-size: 12px;
    color: #A0AAD2;
    float: left;
  }
  .row-box-bottom > div i {
    font-style: normal;
    font-size: 19px;
    color: #3AA3FF;
  }

  .link-btn {
    width: 100px;
    height: 20px;
    border-radius: 20px;
    overflow: hidden;
    outline: none;
    border: 0;
    color: #FFFFFF;
    font-size: 14px;
    background: linear-gradient(90deg,#4de3ff,#2d9dff);
    box-shadow: 6px 6px 20px rgba(63,191,255,.3);
    padding: 2px;
    line-height: 20px;
    text-align: center;
    display: inline-block;
  }
  .link-btn a {
    font-style: inherit;
    width: 100%;
    height: 100%;
    border-radius: 20px;
    display: inline-block;
    transition: color .25s,background .25s;
    color: #fff;
    font-size: 14px;
  }
  .list-title:hover {
    cursor: pointer;
  }
</style>
