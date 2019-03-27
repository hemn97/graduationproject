<template>
  <div>
    <!-- 数据筛选 -->
    <el-row style="margin: 10px;">
      <el-col :span="2">
        <el-dropdown @command="handleRoleCommand">
          <el-button size="small" class="button-selector">
            <span>{{ params.roleType }}<i class="el-icon-arrow-down el-icon--right"></i></span>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="所有用户">所有用户</el-dropdown-item>
            <el-dropdown-item command="未激活">未激活</el-dropdown-item>
            <el-dropdown-item command="普通用户">普通用户</el-dropdown-item>
            <el-dropdown-item command="管理员">管理员</el-dropdown-item>
            <el-dropdown-item command="封禁中">封禁中</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <el-col :span="2">
        <el-dropdown @command="handleOrderCommand">
          <el-button size="small">
            <span>{{ params.orderByClause }}<i class="el-icon-arrow-down el-icon--right"></i></span>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="id asc">按ID排序</el-dropdown-item>
            <el-dropdown-item command="register time desc">按注册时间排序</el-dropdown-item>
            <el-dropdown-item command="school asc">按学校排序</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <el-col :span="8" :offset="12" style="display: flex;">
        <el-input placeholder="搜索用户" value="textSearch" v-model="params.searchKey" size="small" style="width: 100%;">
          <el-button slot="append" icon="el-icon-search" v-on:click="search"></el-button>
        </el-input>
      </el-col>
    </el-row>
    <el-table :data="list">
      <el-table-column prop="id" label="ID" width="100"></el-table-column>
      <el-table-column prop="nickname" label="昵称" width="160"></el-table-column>
      <el-table-column prop="realname" label="真实姓名" width="150"></el-table-column>
      <el-table-column prop="gender" label="性别" width="100"></el-table-column>
      <el-table-column prop="username" label="用户名" width="150"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
      <el-table-column prop="telNumber" label="联系电话" width="180"></el-table-column>
      <el-table-column prop="school" label="学校" width="100"></el-table-column>
      <el-table-column prop="roleType" label="角色" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.roleType==0">未激活</span>
          <span v-if="scope.row.roleType==1">普通用户</span>
          <span v-if="scope.row.roleType==2">管理员</span>
          <span v-if="scope.row.roleType==3">封禁中</span>
        </template>
      </el-table-column>
      <el-table-column prop="registerTime" label="注册时间" width="160"></el-table-column>
      <el-table-column fixed="right" label="操作" width="120">
        <template slot-scope="scope">
          <el-button @click="editRole(scope.row.id, scope.row.roleType)" type="text" size="small">修改角色</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="修改用户角色" :visible.sync="editDialogVisible" width="30%">
      <span>请选择用户角色</span>
      <el-radio-group v-model="editParams.roleType" size="mini" style="margin-top: 10px;">
        <el-radio-button label="0">未激活</el-radio-button>
        <el-radio-button label="1">普通用户</el-radio-button>
        <el-radio-button label="2">管理员</el-radio-button>
        <el-radio-button label="3">封禁中</el-radio-button>
      </el-radio-group>
      <span slot="footer">
        <el-button size="mini" @click="editDialogVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="confirmEditRole">确 定</el-button>
      </span>
    </el-dialog>
    <el-pagination layout="prev, pager, next" :page-size="10" :total="params.maxPage*10" @current-change="handleCurChange"
                   :current-page.sync="params.pageNumber" class="pagination" v-if="params.maxPage>1">
    </el-pagination>
  </div>
</template>

<script>
  import { requestUserList,requestUserMaxPage,requestUpdateUserRole } from "../../api/user";
  export default {
    name: "posts",
    data() {
      return {
        list: [],
        params: {
          pageSize: 10,
          pageNumber: 1,
          maxPage: 0,
          searchKey: '',
          roleType: '所有用户',
          orderByClause: 'id asc',
        },
        editDialogVisible: false,
        editParams: {
          id: 0,
          roleType: '',
        }
      }
    },
    methods: {
      handleRoleCommand(command) {
        this.params.roleType = command;
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
        requestUserMaxPage(params).then(function(successRes){
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
        requestUserList(params).then(function(successRes){
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
      editRole(id, roleType) {
        this.editParams.id = id;
        this.editParams.roleType = roleType;
        this.editDialogVisible = true;
      },
      confirmEditRole() {
        let params = this.editParams;
        var that = this;
        requestUpdateUserRole(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.$notify.success({
              title: '操作提示',
              message: '修改用户角色成功',
              showClose: true
            });
            that.editDialogVisible = false;
            that.getMaxPage();
            that.getDataList();
          }
        },function(errRes){
          console.log(errRes.status);
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
