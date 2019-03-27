<template>
  <div>
    <!-- 数据筛选 -->
    <el-row style="margin: 10px;">
      <el-col :span="2">
        <el-dropdown @command="handleCategoryCommand">
          <el-button size="small" class="button-selector">
            <span>{{ params.category }}<i class="el-icon-arrow-down el-icon--right"></i></span>
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
            <span>{{ params.orderByClause }}<i class="el-icon-arrow-down el-icon--right"></i></span>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="按最热排序">按最热排序</el-dropdown-item>
            <el-dropdown-item command="按最新排序">按最新排序</el-dropdown-item>
            <el-dropdown-item command="按评分排序">按评分排序</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <el-col :span="8" :offset="12" style="display: flex;">
        <el-button type="primary" size="small" @click="toRelease" style="margin-right: 5px;">发布视频</el-button>
        <el-input placeholder="搜索视频" value="textSearch" v-model="params.title" size="small" style="width: 80%;">
          <el-button slot="append" icon="el-icon-search" v-on:click="search"></el-button>
        </el-input>
      </el-col>
    </el-row>
    <el-table :data="list">
      <el-table-column prop="title" label="标题" width="200"></el-table-column>
      <el-table-column prop="author" label="主讲人" width="120"></el-table-column>
      <el-table-column prop="introduction" label="简介" width="300"></el-table-column>
      <el-table-column prop="category" label="分类" width="100"></el-table-column>
      <el-table-column prop="tags" label="标签" width="150"></el-table-column>
      <el-table-column prop="releaseTime" label="发布时间" width="160"></el-table-column>
      <el-table-column fixed="right" label="操作" width="100">
        <template slot-scope="scope">
          <el-button @click="toEdit(scope.row.id)" type="text" size="small">编辑</el-button>
          <el-button @click="deleteItem(scope.row.id)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination layout="prev, pager, next" :page-size="10" :total="params.maxPage*10" @current-change="handleCurChange"
                   :current-page.sync="params.pageNumber" class="pagination" v-if="params.maxPage>1">
    </el-pagination>
  </div>
</template>

<script>
  import { requestVideoList,requestVideoMaxPage,requestDeleteVideo } from "../../api/video";
  export default {
    name: "videos",
    data() {
      return {
        list: [],
        params: {
          pageSize: 10,
          pageNumber: 1,
          maxPage: 0,
          title: '',
          category: '所有类别',
          orderByClause: '按最新排序',
        }
      }
    },
    methods: {
      handleCategoryCommand(command) {
        this.params.category = command;
        this.getMaxPage();
        this.getDataList();
      },
      handleOrderCommand(command) {
        this.params.orderByClause = command;
        this.getDataList();
      },
      search() {
        this.getMaxPage();
        this.getDataList();
      },
      getMaxPage() {
        this.params.pageNumber = 1;
        let params = this.params;
        var that = this;
        requestVideoMaxPage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.params.maxPage = successRes.body.maxPage;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getDataList() {
        let params = this.params;
        var that = this;
        requestVideoList(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.list = successRes.body.list;
          } else {
            alert(successRes.body.message);
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      handleCurChange(curPage) {
        this.params.pageNumber = curPage;
        this.getDataList();
      },
      toEdit(id) {
        this.$router.push("/admin/edit/video/"+id);
      },
      toRelease() {
        this.$router.push("/admin/release/video");
      },
      deleteItem(id) {
        var that = this;
        this.$confirm('此操作将删除该视频, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let params = {'id':id };
          requestDeleteVideo(params).then(function(successRes){
            if (successRes.body.code === 1) {
              that.$notify.success({
                title: '操作提示',
                message: '删除视频成功',
                showClose: true
              });
              that.getMaxPage();
              that.getDataList();
            }
          },function(errRes){
            console.log(errRes.status);
          });
        }).catch(() => {

        });
      }
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
  .pagination {
    text-align: center;
    margin: 15px 0;
  }
</style>
