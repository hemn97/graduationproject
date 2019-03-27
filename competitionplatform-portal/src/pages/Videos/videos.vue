<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-main class="main">
      <!-- 数据筛选 -->
      <el-row>
        <el-col :span="2" :offset="1">
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
              <el-dropdown-item command="按最热排序">按最热排序</el-dropdown-item>
              <el-dropdown-item command="按最新排序">按最新排序</el-dropdown-item>
              <el-dropdown-item command="按评分排序">按评分排序</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-col>
        <el-col :span="6" :offset="12">
          <el-input placeholder="搜索视频" value="textSearch" v-model="textSearch" size="small">
            <el-button slot="append" icon="el-icon-search" v-on:click="search"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <!-- 数据列表 -->
      <el-row class="row-list">
        <h3 class="h-nores" v-if="maxPage==0">暂时没有视频...</h3>
        <el-card v-for="(data,index) in dataList" :key="index" class="card-item" shadow="hover" @click.native="viewVideo(data.id)">
          <el-row>
            <el-col :span="6">
              <img class="img-item" :src="data.figure" />
            </el-col>
            <el-col :span="18" class="col-item-body">
              <div>
                <span class="span-title">{{ data.title }}</span>
                <small class="span-score">{{ data.score ? parseFloat(data.score).toFixed(1)+"分" : "暂无评分" }}</small>
              </div>
              <p class="p-author">{{ data.author }}</p>
              <p class="p-intros">{{ data.introduction }}</p>
              <p class="p-stat">{{ data.category }} · {{ data.tags }} <i class="el-icon-view">
                {{ data.viewNumber ? data.viewNumber : 0 }}人浏览</i></p>
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
  import { requestVideoList,requestVideoMaxPage } from '@/api/video'
  export default {
    name: "videos",
    components: {
      Header,
      Footer
    },
    methods: {
      handleCategoryCommand(command) {
        this.categorySelected = command;
        this.getMaxPage();
        this.getDataList();
      },
      handleOrderCommand(command) {
        this.orderSelected = command;
        this.getDataList();
      },
      search() {
        this.getMaxPage();
        this.getDataList();
      },
      getMaxPage() {
        this.curPage = 1;
        var that = this;
        let params = {'pageSize':10, 'category':this.categorySelected, 'title':this.textSearch};
        requestVideoMaxPage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.maxPage = successRes.body.maxPage;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getDataList() {
        var that = this;
        let params = {'pageNumber':this.curPage, 'pageSize':10, 'category':this.categorySelected,
          'title':this.textSearch, 'orderByClause': this.orderSelected};
        requestVideoList(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.dataList = successRes.body.list;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      handleCurChange(curPage) {
        this.curPage = curPage;
        this.getDataList();
      },
      viewVideo(id) {
        this.$router.push("/video/"+id);
      }
    },
    data() {
      return {
        categorySelected: '所有类别',
        orderSelected: '按最热排序',
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
    height: 180px;
  }

  .img-item {
    width: 100%;
  }

  .col-item-body {
    padding-left: 10px;
  }

  .span-title {
    font-size: large;;
    font-weight: bold;
  }
  .span-score {
    color: #ff6666;
  }

  .p-author {
    font-size: small;
    color: gray;
  }

  .p-intros {
    font-size: small;
  }

  .p-stat {
    font-size: small;
  }

  .h-nores {
    text-align: center;
  }

  .pagination {
    text-align: center;
  }

</style>
