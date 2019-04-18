<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-main class="main">
      <h1 style="text-align: center">个人主页</h1>
      <div class="div-body">
        <div class="div-body-left">
          <img src="@/assets/user_edit.png" class="img-user"/>
          <p>{{ userData.nickname }}</p>
          <p>{{ userData.email }}</p>
          <el-form class="form-edit" :model="userData">
            <el-form-item class="item-form">
              <el-input v-if="!editVisible" size="mini" :value="userData.school ? userData.school.split('_')[2] : null" disabled></el-input>
              <el-cascader v-if="editVisible" placeholder="选择就读学校" :options="schoolOptions" filterable
                           size="mini" :show-all-levels=false @change="onSchoolChange"></el-cascader>
            </el-form-item>
            <el-form-item class="item-form">
              <el-input placeholder="请输入联系电话" prefix-icon="el-icon-phone" v-model="userData.telNumber"
                        size="mini" maxlength="15" :disabled="!editVisible">
              </el-input>
            </el-form-item>
            <el-form-item class="item-form">
              <el-input v-if="!editVisible" size="mini" :value="parseSkills(skillsSelected)"  disabled></el-input>
              <el-button size="mini" @click="selectSkillsDialogVisible = true" v-if="editVisible">选择个人擅长技能</el-button>
              <el-dialog title="选择个人擅长技能" :visible.sync="selectSkillsDialogVisible" width="45%" center>
                <el-transfer v-model="skillsSelected" :data="skillOptions" :titles="skillsTitle"></el-transfer>
                <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="selectSkillsDialogVisible = false">确 定</el-button>
              </span>
              </el-dialog>
            </el-form-item>
            <el-form-item class="item-form">
              <el-radio v-model="userData.gender" label="男" :disabled="!editVisible">男</el-radio>
              <el-radio v-model="userData.gender" label="女" :disabled="!editVisible">女</el-radio>
            </el-form-item>
            <el-button type="primary" icon="el-icon-edit" circle size="mini" @click="editVisible = true" v-if="!editVisible"></el-button>
            <div class="div-edit-button" v-else>
              <el-button type="success" icon="el-icon-check" size="mini" @click="updateUserData">保存</el-button>
              <el-button icon="el-icon-close" size="mini" @click="cancelUpdateUserData">取消</el-button>
            </div>
          </el-form>
        </div>
        <div class="div-body-right">
          <el-tabs v-model="activeTabName">
            <el-tab-pane label="概况" name="概况">
              <h5>我的发帖</h5>
              <div class="div-list-post">
                <el-timeline>
                  <el-timeline-item v-for="(post,index) in myPosts" :key="index"
                                    :timestamp="post.releaseTime" placement="top">
                    <p>{{ post.title }}
                      <small style="padding-left: 5px;">{{ post.viewNumber ? post.viewNumber : 0 }}次阅读</small>
                      <small style="padding-left: 5px;">{{ post.commentNumber ? post.commentNumber : 0 }}次评论</small>
                      <el-button type="text" style="padding-left: 10px;" @click="viewMyPost(post.id)">查看原帖</el-button>
                    </p>
                  </el-timeline-item>
                </el-timeline>
                <el-pagination layout="prev, pager, next" :page-size="5" :total="myPostsParams.maxPage*5"
                               @current-change="handleMyPostsPageChange"
                               :current-page.sync="myPostsParams.pageNumber" class="pagination" v-if="myPostsParams.maxPage>1">
                </el-pagination>
              </div>
            </el-tab-pane>
            <el-tab-pane label="信箱" name="信箱">
              <div style="display: flex;">
                <div style="width: 20%;">
                  <el-menu :default-active="messagesParams.mailbox" background-color="#ccffff" text-color="#ff9999"
                           active-text-color="#3399ff" @select="handleChangeMailboxTab">
                    <el-menu-item index="收件箱">
                      <span slot="title">收件箱</span>
                    </el-menu-item>
                    <el-menu-item index="已发送私信">
                      <span slot="title">已发送私信</span>
                    </el-menu-item>
                  </el-menu>
                  <div style="text-align: center;margin-top: 10px;">
                    <el-dropdown @command="handleMessageStatusCommand">
                      <el-button type="primary" size="small">
                        {{ messagesParams.status }}<i class="el-icon-arrow-down el-icon--right"></i>
                      </el-button>
                      <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="所有私信">所有私信</el-dropdown-item>
                        <el-dropdown-item command="未读私信">未读私信</el-dropdown-item>
                      </el-dropdown-menu>
                    </el-dropdown>
                  </div>
                </div>
                <div style="width: 80%; padding: 0 10px;">
                  <p style="text-align: center;">{{ messagesParams.mailbox }}</p>
                  <div v-if="messagesParams.mailbox=='收件箱'">
                    <el-timeline>
                      <el-timeline-item v-for="(message,index) in messages" :key="index"
                                        :timestamp="message.sendTime" placement="top">
                        <p>来自 @<span style="color: blue;">{{ message.senderName}}</span>：{{ message.title }}
                          <el-tag style="margin-left: 10px;" v-if="message.status=='未读'">未读</el-tag>
                          <el-popover placement="bottom" width="200" trigger="click" :content="message.content">
                            <el-button slot="reference" type="text" style="padding-left: 10px;"
                                       @click="viewMessage(index, message.id)">查看内容</el-button>
                          </el-popover>
                          <el-button slot="reference" type="text" style="padding-left: 10px;"
                                     @click="readySendMessage(message.senderId)">回复</el-button>
                        </p>
                      </el-timeline-item>
                    </el-timeline>
                  </div>
                  <div v-if="messagesParams.mailbox=='已发送私信'">
                    <el-timeline>
                      <el-timeline-item v-for="(message,index) in messages" :key="index"
                                        :timestamp="message.sendTime" placement="top">
                        <p>发送给 @<span style="color: blue;">{{ message.recipientName}}</span>：{{ message.title }}
                          <el-tag style="margin-left: 10px;" v-if="message.status=='未读'">未读</el-tag>
                          <el-popover placement="bottom" width="200" trigger="click" :content="message.content">
                            <el-button slot="reference" type="text" style="padding-left: 10px;">查看内容</el-button>
                          </el-popover>
                          <el-button slot="reference" type="text" style="padding-left: 10px;"
                                     @click="readySendMessage(message.recipientId)">再次私信</el-button>
                        </p>
                      </el-timeline-item>
                    </el-timeline>
                  </div>
                  <el-dialog title="发送私信" :visible.sync="messageDialogVisible" center width="40%">
                    <el-form :model="messageForm" :rules="messageFormRules" ref="messageForm" status-icon >
                      <el-form-item label="主题" prop="title" class="item-dialog">
                        <el-input v-model="messageForm.title" autocomplete="off"></el-input>
                      </el-form-item>
                      <el-form-item label="内容" prop="content" class="item-dialog">
                        <el-input type="textarea" :rows="4" v-model="messageForm.content" autocomplete="off"></el-input>
                      </el-form-item>
                    </el-form>
                    <span slot="footer" class="dialog-footer">
                          <el-button type="primary" @click="sendMessageConfirm()">发 送</el-button>
                        </span>
                  </el-dialog>
                  <el-pagination layout="prev, pager, next" :page-size="10" :total="messagesParams.maxPage*10"
                                 @current-change="handleMessagesPageChange"
                                 :current-page.sync="messagesParams.pageNumber" class="pagination"
                                 v-if="messagesParams.maxPage>1">
                  </el-pagination>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="队伍" name="队伍">
              <div style="display: flex">
                <el-dropdown @command="handleTeammateStatusCommand" style="padding-right: 300px;">
                  <el-button type="primary" size="small">
                    {{ myTeamsParams.status }}<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="已加入">已加入</el-dropdown-item>
                    <el-dropdown-item command="审批中">审批中</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
                <el-input placeholder="搜索队伍" value="teamSearch" v-model="myTeamsParams.name" size="small" class="input-search">
                  <el-button slot="append" icon="el-icon-search" v-on:click="searchTeam"></el-button>
                </el-input>
              </div>
              <el-table :data="myTeams" style="width: 100%;margin-top: 15px;">
                <el-table-column prop="name" label="队伍名称" width="160"> </el-table-column>
                <el-table-column prop="comptTitle" label="竞赛名称" width="200"></el-table-column>
                <el-table-column prop="captainName" label="队长"></el-table-column>
                <el-table-column prop="motto" label="公告" width="200"></el-table-column>
                <el-table-column prop="teamStatus" label="队伍状态" width="100"></el-table-column>
                <el-table-column prop="joinTime" label="加入时间" width="160"></el-table-column>
                <el-table-column fixed="right" label="操作" width="100">
                  <template slot-scope="scope">
                    <el-button size="small" @click="viewMyTeam(scope.row.comptId)">查看队伍</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-pagination layout="prev, pager, next" :page-size="5" :total="myTeamsParams.maxPage*5"
                             @current-change="handleMyTeamsPageChange"
                             :current-page.sync="myTeamsParams.pageNumber" class="pagination" v-if="myTeamsParams.maxPage>1">
              </el-pagination>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </el-main>
    <el-footer><Footer></Footer></el-footer>
  </el-container>
</template>

<script>
  import Header from '@/components/header'
  import Footer from '@/components/footer'
  import { requestUserData,requestUpdateData } from "../../api/user";
  import { requestMyPostList,requestMyPostMaxPage } from "../../api/post";
  import { requestFindMyTeams,requestMyTeamsMaxPage } from "../../api/team";
  import { requestMessageList,requestMessageMaxPage,requestReadMessage,requestSendMessage } from "../../api/message";

  export default {
    name: "personal",
    components: {
      Header,
      Footer
    },
    methods: {
      getUserData() {
        var that = this;
        requestUserData({}).then(function(successRes){
          if (successRes.body.code === 1) {
            that.userData = successRes.body.data;
            that.skillsSelected = that.userData.skills.split(",");
          } else {
            that.$router.push("/notfound"); // 找不到用户
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      parseSkills(skills) {
        var res = new Array();
        for(var j = 0; j < skills.length; j++){
          res.push(skills[j].split("_")[1]);
        }
        return res.join(",");
      },
      updateUserData() {
        var that = this;
        this.userData.skills = this.skillsSelected.join(",");
        let params = this.userData;
        console.log(params);
        requestUpdateData(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.$notify.success({
              title: '操作提示',
              message: '修改个人信息成功',
              showClose: true
            });
            that.skillsSelected = that.userData.skills.split(",");
          } else {
            that.$message('修改个人信息失败');
          }
        },function(errRes){
          console.log(errRes.status);
        });
        this.editVisible = false;
      },
      cancelUpdateUserData() {
        this.editVisible = false;
        this.getUserData();
      },
      getMyPosts() {
        var that = this;
        let params = this.myPostsParams;
        requestMyPostList(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.myPosts = successRes.body.list;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getMyPostsMaxPage() {
        this.myPostsParams.pageNumber = 1;
        var that = this;
        let params = this.myPostsParams;
        requestMyPostMaxPage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.myPostsParams.maxPage = successRes.body.maxPage;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      handleMyPostsPageChange(page) {
        this.myPostsParams.pageNumber = page;
        this.getMyPosts();
      },
      handleMyTeamsPageChange(page) {
        this.myTeams.pageNumber = page;
        this.getMyTeams();
      },
      viewMyPost(id) {
        this.$router.push(`/post/${id}`);
      },
      getMyTeams() {
        var that = this;
        let params = this.myTeamsParams;
        requestFindMyTeams(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.myTeams = successRes.body.list;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getMyTeamsMaxPage() {
        this.myTeamsParams.pageNumber = 1;
        var that = this;
        let params = this.myTeamsParams;
        requestMyTeamsMaxPage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.myTeamsParams.maxPage = successRes.body.maxPage;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      handleTeammateStatusCommand(command) {
        this.myTeamsParams.status = command;
        this.getMyTeamsMaxPage();
        this.getMyTeams();
      },
      viewMyTeam(id) {
        this.$router.push(`/competition/${id}`);
      },
      searchTeam() {
        this.getMyTeams();
        this.getMyTeamsMaxPage();
      },
      getMessages() {
        var that = this;
        let params = this.messagesParams;
        requestMessageList(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.messages = successRes.body.list;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getMessagesMaxPage() {
        this.messagesParams.pageNumber = 1;
        var that = this;
        let params = this.messagesParams;
        requestMessageMaxPage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.messagesParams.maxPage = successRes.body.maxPage;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      handleMessagesPageChange(page) {
        var that = this;
        this.messagesParams.pageNumber = page;
        this.getMessages();
      },
      handleChangeMailboxTab(index, indexPath) {
        this.messagesParams.status = '所有私信';
        this.messagesParams.mailbox = index;
        this.getMessagesMaxPage();
        this.getMessages();
      },
      viewMessage(index, id) {
        var that = this;
        if (that.messages[index].status=="未读") {
          let params = {id:id};
          requestReadMessage(params).then(function(successRes){
            if (successRes.body.code==1) {
              that.messages[index].status="已读";
            }
          },function(errRes){
            console.log(errRes.status);
          });
        }
      },
      readySendMessage(recipientId) {
        this.messageForm.recipientId = recipientId;
        this.messageDialogVisible = true;
      },
      sendMessageConfirm() {
        var that = this;
        let params = this.messageForm;
        requestSendMessage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.$notify.success({
              title: '操作提示',
              message: '发送私信成功',
              showClose: true
            });
          } else {
            that.$message('发送私信失败');
          }
          that.messageDialogVisible = false;
        },function(errRes){
          console.log(errRes.status);
        });
      },
      handleMessageStatusCommand(command) {
        this.messagesParams.status = command;
        this.getMessages();
        this.getMessagesMaxPage();
      },
      onSchoolChange(value) {
        this.userData.school = value[2];
      },
    },
    data() {
      return {
        activeTabName: '概况',
        userData: {},
        userEditForm: {},
        myPostsParams: {
          pageNumber: 1,
          pageSize: 5,
          maxPage: 0,
        },
        myPosts: [],
        myTeamsParams: {
          pageNumber: 1,
          pageSize: 10,
          maxPage: 0,
          status: '已加入',
          name: '',
        },
        myTeams: [],
        editVisible: false,
        selectSkillsDialogVisible: false,
        skillsTitle: ['技能可选项','已选择技能'],
        skillsSelected: [],
        skillOptions: [{key: '工科_数学建模_000',label:'数学建模'}, {key: '工科_程序设计_001',key:'程序设计'}, {key: '工科_机器人_002',label:'机器人'},
          {key: '工科_工程机械_003',key:'工程机械'}, {key: '工科_土木建筑_004',key:'土木建筑'},{key: '工科_大数据_005',label:'大数据'},
          {key: '工科_交通车辆_006',label:'交通车辆'}, {key: '工科_航空航天_007',label:'航空航天'}, {key: '工科_船舶海洋_008',label:'船舶海洋'},
          {key: '工科_环境能源_009',label:'环境能源'}, {key: '工科_计算机与信息技术_010',label:'计算机与信息技术'},
          {key: '工科_材料高分子_011',label:'材料高分子'},{key: '工科_电子与自动化_012',label:'电子与自动化'},
          {key: '文体_工业与创意设计_100',label:'工业与创意设计'}, {key: '文体_外语_101',label:'外语'},
          {key: '文体_演讲主持与辩论_102',label:'演讲主持与辩论'}, {key: '文体_模特_103',label:'模特'},
          {key: '文体_歌舞书画与摄影_104',label:'歌舞书画与摄影'},
          {key: '文体_体育_105',label:'体育'}, {key: '文体_UI设计_106',label:'UI设计'}, {key: '文体_服装设计_107',label:'服装设计'},
          {key: '文体_电子竞技_108',label:'电子竞技'},{key: '理科_数学_200',label:'数学'}, {key: '理科_物理_201',label:'物理'},
          {key: '理科_化学化工_202',label:'化学化工'},{key: '理科_健康生命与医学_203',label:'健康生命与医学'}, {key: '理科_力学_204',label:'力学'},
          {key: '商科_创业_300',label:'创业'}, {key: '商科_商业_301',label:'商业'},{key: '综合_职业技能_400',label:'职业技能'},
          {key: '综合_环保公益_401',label:'环保公益'}, {key: '综合_社会综合_402',label:'社会综合'}
        ],
        messagesParams: {
          pageNumber: 1,
          pageSize: 10,
          maxPage: 0,
          mailbox: '收件箱',
          status: '所有私信'
        },
        messages: [],
        messageDialogVisible: false,
        messageForm: {

        },
        messageFormRules: {
          title: [
            {required: true, message: '请输入私信主题', trigger: 'blur'},
            {min: 3, max: 15, message: '长度在 3 个字符到 15 个字符之间', trigger: 'blur'}
          ],
          content: [
            {required: true, message: '请输入私信内容', trigger: 'blur'},
            {min: 6, message: '长度在 6 个字符以上', trigger: 'blur'}
          ],
        },
        schoolOptions: [],
      };
    },
    created() {
      if (this.$store.state.token == '') {
        this.$message('请先登录');
        this.$router.push("/");
      } else {
        this.getUserData();
        this.getMyPosts();
        this.getMyPostsMaxPage();
        this.getMyTeams();
        this.getMyTeamsMaxPage();
        this.getMessages();
        this.getMessagesMaxPage();
        var that = this;
        this.$http.get('static/options/schoolOptions.json').then(res => {
          that.schoolOptions = res.body;
        })
      }
    },
    mounted() {

    }
  }
</script>

<style scoped>
  .div-body {
    display: flex;
    width: 80%;
    margin: 0 auto;
  }
  .img-user {
    width: 120px;
    height: 120px;
  }
  .div-body-left {
    width: 20%;
    text-align: center;
  }
  .div-body-right {
    width: 80%;
    padding: 0 15px;
  }
  .form-edit {
    width: 80%;
    margin: 0 auto;
  }
  .item-form {
    margin: 0;
  }
  .div-edit-button {
    display: flex;
  }
  .i-unread {
    color: #ff6666;
    padding-left: 5px;
  }
  .pagination {
    text-align: center;
    margin-top: 15px;
  }
  .input-search {
    width: 300px;
  }

</style>
