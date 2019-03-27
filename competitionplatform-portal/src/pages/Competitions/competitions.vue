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
        <el-card v-for="(data,index) in dataList" :key="index" class="card-item" shadow="hover" @click.native="viewCompetition(data.id)">
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

  .pagination {
    text-align: center;
  }

  .tag-tags {
    margin-right: 10px;
  }

  .p-sponsor {
    font-size: small;
    text-align: left;
  }

  .h-nores {
    text-align: center;
  }
</style>
