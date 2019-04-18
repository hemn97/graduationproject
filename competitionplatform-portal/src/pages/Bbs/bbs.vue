<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-main class="main">
      <el-row>
        <el-col :span="5">
          <h5>版块</h5>
          <el-menu :default-active="categorySelected" @select="selectCategory">
            <el-menu-item index="首页">
              <span slot="title">首页</span>
            </el-menu-item>
            <el-submenu index="学科">
              <template slot="title">
                <span>学科</span>
              </template>
              <el-menu-item-group>
                <el-menu-item index="工科">工科</el-menu-item>
                <el-menu-item index="文体">文体</el-menu-item>
                <el-menu-item index="理科">理科</el-menu-item>
                <el-menu-item index="商科">商科</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
            <el-menu-item index="竞赛">
              <span slot="title">竞赛</span>
            </el-menu-item>
            <el-menu-item index="谈天说地">
              <span slot="title">谈天说地</span>
            </el-menu-item>
          </el-menu>
        </el-col>
        <el-col :span="19" class="col-main">
          <div class="div-header">
            <div style="width: 55%">
              <el-button type="text" size="small" @click="selectOrder('按时间排序')">按时间排序</el-button>
              <el-button type="text" size="small" @click="selectOrder('按浏览数排序')">按浏览数排序</el-button>
              <el-button type="text" size="small" @click="selectOrder('按评论数排序')">按评论数排序</el-button>
            </div>
            <div style="width: 32%;display: flex;">
              <el-button type="primary" size="small" @click="releasePost">发帖</el-button>
              <el-input placeholder="搜索帖子" value="textSearch" v-model="textSearch" size="small" style="margin-left: 10px;">
                <el-button slot="append" icon="el-icon-search" v-on:click="search"></el-button>
              </el-input>
            </div>
          </div>
          <h3 v-if="maxPage==0" style="text-align: center;">暂时没有帖子</h3>
          <div class="div-list" v-else>
            <el-row class="row-item" v-for="(data,index) in posts" :key="index">
              <el-col :span="6" style="text-align: center;">
                <div class="div-time">{{ data.releaseTime }}</div>
                <div class="div-stat">{{ data.viewNumber ? data.viewNumber:0 }} 浏览 ·
                  {{ data.commentNumber ? data.commentNumber:0 }} 评论</div>
              </el-col>
              <el-col :span="18">
                <a class="a-title" :href="'#/post/' + data.id ">{{ data.title }}</a>
                <p style="text-align: left">{{ data.abstractContent }}</p>
                <div style="display: flex;">
                  <img src="@/assets/user.png" class="img-user"/>
                  <span class="span-nickname">{{ data.school.split("_")[2] }} - {{ data.nickname }}</span>
                </div>
              </el-col>
            </el-row>
            <el-pagination layout="prev, pager, next" :page-size="10" :total="maxPage*10" @current-change="handleCurChange"
                           :current-page.sync="curPage" class="pagination" v-if="maxPage>1">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-main>
    <el-footer><Footer></Footer></el-footer>
  </el-container>
</template>

<script>
  import Header from '@/components/header'
  import Footer from '@/components/footer'
  import { requestPostList,requestPostMaxPage } from '@/api/post'
  export default {
    name: "bbs",
    components: {
      Header,
      Footer
    },
    methods: {
      selectCategory(index, indexPath) {
        this.categorySelected = index;
        this.textSearch = '';
        this.order = '按时间排序';
        this.getMaxPage();
        this.getPosts();
      },
      getPosts() {
        var that = this;
        let params = {'pageNumber':this.curPage, 'pageSize':10,  'category':this.categorySelected,
          'title':this.textSearch, 'orderByClause': this.order};
        requestPostList(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.posts = successRes.body.list;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getMaxPage() {
        this.curPage = 1;
        var that = this;
        let params = {'pageSize':10,  'category':this.categorySelected,  'title':this.textSearch};
        requestPostMaxPage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.maxPage = successRes.body.maxPage;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      search() {
        this.categorySelected = '首页';
        this.order = '按时间排序';
        this.getMaxPage();
        this.getPosts();
      },
      selectOrder(order) {
        this.order = order;
        this.getMaxPage();
        this.getPosts();
      },
      handleCurChange(page) {
        this.curPage = page;
        this.getPosts();
      },
      releasePost() {
        if (this.$store.state.token == '') {
          this.$message('请先登录');
        } else {
          this.$router.push("/releasepost");
        }
      }
    },
    data() {
      return {
        categorySelected: '首页',
        curPage: 1,
        maxPage: 0,
        textSearch: '',
        order: '按时间排序',
        posts: [],
      };
    },
    created() {
      this.getPosts();
      this.getMaxPage();
    }
  }
</script>

<style scoped>

  .col-main {
    padding: 15px;
  }

  .div-header {
    display: flex;
  }

  .div-list {
    width: 95%;
    padding-top: 15px;
    margin-top: 10px;
    border-top: 1px solid #cccccc;
  }

  .row-item {
    margin-bottom: 20px;
  }

  .div-time {
    font-size: large;
  }

  .div-stat {
    font-size: small;
    margin-top: 5px;
  }

  .a-title {
    text-decoration: none;
    font-weight: bold;
    font-size: large;
    color: #333333;
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

  .pagination {
    text-align: center;
  }

</style>
