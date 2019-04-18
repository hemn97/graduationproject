<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-main class="main">
      <!-- 数据筛选 -->
      <el-row>
        <el-col :span="2" :offset="1">
          <el-dropdown @command="handleStatusCommand">
            <el-button size="small" class="button-selector">
              <span>{{ statusSelected }}<i class="el-icon-arrow-down el-icon--right"></i></span>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="所有状态">所有状态</el-dropdown-item>
              <el-dropdown-item command="可报名">可报名</el-dropdown-item>
              <el-dropdown-item command="进行中">进行中</el-dropdown-item>
              <el-dropdown-item command="已结束">已结束</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-col>
        <el-col :span="2">
          <el-dropdown @command="handleCategoryCommand">
            <el-button size="small" class="button-selector">
              <span>{{ categorySelected }}<i class="el-icon-arrow-down el-icon--right"></i></span>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="所有类别">所有类别</el-dropdown-item>
              <el-dropdown-item command="工科">工科</el-dropdown-item>
              <el-dropdown-item command="文体">文体</el-dropdown-item>
              <el-dropdown-item command="理科">理科</el-dropdown-item>
              <el-dropdown-item command="商科">商科</el-dropdown-item>
              <el-dropdown-item command="综合">综合</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-col>
        <el-col :span="2">
          <el-dropdown @command="handleOrderCommand">
            <el-button size="small">
              <span>{{ orderSelected }}<i class="el-icon-arrow-down el-icon--right"></i></span>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="默认排序">默认排序</el-dropdown-item>
              <el-dropdown-item command="按最热排序">按最热排序</el-dropdown-item>
              <el-dropdown-item command="按最新排序">按最新排序</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-col>
        <el-col :span="6" :offset="10">
          <el-input placeholder="搜索赛题" value="textSearch" v-model="textSearch" size="small">
            <el-button slot="append" icon="el-icon-search" v-on:click="search"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <!-- 数据列表 -->
      <el-row class="row-list">
        <h3 class="h-nores" v-if="maxPage==0">暂时没有竞赛...</h3>
        <div v-for="(data,index) in dataList" :key="index" class="list-box" @click="viewCompetition(data.id)">
          <el-col :span="3" class="list-pic">
            <img :src="data.logo" />
          </el-col>
          <div :span="6" class="list-main">
            <span>{{ data.title }}</span>
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
            </div>
          </div>
        </div>
      </el-row>
      <el-row>
        <el-pagination layout="prev, pager, next" :page-size="10" :total="maxPage*10" @current-change="handleCurChange"
                       :current-page.sync="curPage" class="pagination" v-if="maxPage>1">
        </el-pagination>
      </el-row>
    </el-main>
    <el-footer><Footer></Footer></el-footer>
  </el-container>
</template>

<script>
  import Header from '@/components/header'
  import Footer from '@/components/footer'
  import { requestCompetitionList,requestCompetitionMaxPage } from '@/api/competition'
  export default {
    name: "competitions",
    components: {
      Header,
      Footer
    },
    methods: {
      handleStatusCommand(command) {
        this.statusSelected = command;
        this.getMaxPage();
        this.getDataList();
      },
      handleCategoryCommand(command) {
        this.categorySelected = command;
        this.getMaxPage();
        this.getDataList();
      },
      handleOrderCommand(command) {
        this.orderSelected = command;
        this.getMaxPage();
        this.getDataList();
      },
      search() {
        this.getMaxPage();
        this.getDataList();
      },
      getMaxPage() {
        this.curPage = 1;
        var that = this;
        let params = {'pageSize':10, 'status':this.statusSelected, 'category':this.categorySelected, 'title':this.textSearch};
        requestCompetitionMaxPage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.maxPage = successRes.body.maxPage;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getDataList() {
        var that = this;
        let params = {'pageNumber':this.curPage, 'pageSize':10, 'status':this.statusSelected,
          'category':this.categorySelected, 'title':this.textSearch, 'orderByClause': this.orderSelected};
        requestCompetitionList(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.dataList = successRes.body.list;
          } else {
            alert(successRes.body.message);
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      handleCurChange(curPage) {
        this.curPage = curPage;
        this.getDataList();
      },
      viewCompetition(id) {
        this.$router.push("/competition/"+id);
      }
    },
    data() {
      return {
        statusSelected: '所有状态',
        categorySelected: '所有类别',
        orderSelected: '默认排序',
        textSearch: '',
        maxPage: 0,
        curPage: 1,
        dataList: [],
      };
    },
    created() {
      this.getMaxPage();
      this.getDataList();
    }
  }
</script>

<style scoped>
  .button-selector {
    width: 92px;
  }

  .row-list {
    margin-top: 15px;
  }

  .pagination {
    text-align: center;
  }

  .h-nores {
    text-align: center;
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
  .list-box:hover {
    box-shadow: 4px 4px 10px #EED5D2;
    cursor: pointer;
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
  .row-box-bottom > div {
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
</style>
