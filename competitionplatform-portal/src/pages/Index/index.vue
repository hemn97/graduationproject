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
          <el-card v-for="(data,index) in recommendCompetitions" :key="index" class="card-item" shadow="hover"
                   @click.native="viewCompetition(data.id)">
            <el-row>
              <el-col :span="6">
                <img class="img-item" src="@/assets/comptlogo.jpg" />
              </el-col>
              <el-col :span="12" class="col-item-body">
                <h3>{{ data.title }}</h3>
                <p class="p-sponsor">{{ data.sponsor }}</p>
                <div>
                  <el-tag size="small" v-for="(tag,index) in data.tags.split(',')" :key="index" class="tag-tags">{{ tag }}</el-tag>
                </div>
                <div style="margin-top: 20px;">
                  <el-badge :value="data.teamNumber==undefined ? 0 : data.teamNumber" class="badge-item-stat">队伍</el-badge>
                  <el-badge :value="data.heat==undefined ? 0 : data.heat" class="badge-item-stat">热度</el-badge>
                </div>
              </el-col>
              <el-col :span="6" class="col-item-right">
                <el-badge :value="data.status" class="badge-item-status" type="primary">比赛时间</el-badge>
                <p>{{ data.startTime }}</p>
                <p>~</p>
                <p>{{ data.endTime }}</p>
              </el-col>
            </el-row>
          </el-card>
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

  .card-item {
    margin: 0 auto;
    width: 92%;
    margin-bottom: 10px;
    height: 200px;
  }

  .img-item {
    height: 160px;
  }

  .col-item-body {
    position: relative;
    top: -20px;
  }

  .badge-item-stat {
    margin-right: 30px;
  }

  .col-item-right {
    position: relative;
    top: 20px;
    text-align: center;
  }

  .badge-item-status {
    font-weight: bold;
  }
</style>
