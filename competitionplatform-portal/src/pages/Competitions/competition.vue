<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-main class="main body-main">
      <!-- 头部卡片 -->
      <el-card class="card-header">
        <el-row>
          <el-col :span="6">
            <img class="img-header" src="@/assets/comptlogo.jpg" />
          </el-col>
          <el-col :span="12" class="col-header-body">
            <h2>{{ competition.title }}</h2>
            <p class="p-sponsor">{{ competition.sponsor }}</p>
            <div>
              <el-tag size="small" v-for="(tag,index) in competition.tags.split(',')" :key="index" class="tag-tags">{{ tag }}</el-tag>
            </div>
            <p class="p-time">比赛时间：{{ competition.startTime }} 至 {{ competition.endTime }}</p>
          </el-col>
          <el-col :span="6" class="col-header-right">
            <div class="div-stat">
              <div class="div-stat-body">
                <p class="p-stat-key">队伍</p><p class="p-stat-value">{{ competition.teamNumber ? competition.teamNumber : 0 }}</p>
              </div>
              <div class="div-stat-line"></div>
              <div class="div-stat-body">
                <p class="p-stat-key">热度</p><p class="p-stat-value">{{ competition.heat ? competition.heat : 0 }}</p>
              </div>
            </div>
            <div class="div-link">
              <el-badge :value="competition.status" type="primary">
                <a class="a-link" :href="competition.link" target="_blank">访问官网</a>
              </el-badge>
            </div>
          </el-col>
        </el-row>
      </el-card>
      <!-- 主体 -->
      <el-tabs class="tabs-body" type="border-card">
        <el-tab-pane label="竞赛信息">
          <div class="div-content">
            <div v-html='competition.details'></div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="相关视频">
          <el-row class="row-video-list">
            <h3 class="h-nores" v-if="videoParams.maxPage==0">暂时没有相关视频...</h3>
            <el-card v-for="(data,index) in videos" :key="index" class="card-video-item" shadow="hover"
                     @click.native="viewVideo(data.id)">
              <el-row>
                <el-col :span="6">
                  <img class="img-video-item" :src="data.figure" />
                </el-col>
                <el-col :span="18" class="col-video-item-body">
                  <div>
                    <span class="span-video-title">{{ data.title }}</span>
                    <small class="span-video-score">{{ data.score ? data.score+"分" : "暂无评分" }}</small>
                  </div>
                  <p class="p-video-author">{{ data.author }}</p>
                  <p class="p-video-intros">{{ data.introduction }}</p>
                  <p class="p-video-stat">{{ data.category }} · {{ data.tags }} <i class="el-icon-view">
                    {{ data.viewNumber ? data.viewNumber : 0 }}人浏览</i></p>
                </el-col>
              </el-row>
            </el-card>
          </el-row>
          <el-row>
            <el-pagination layout="prev, pager, next" :page-size="10" :total="videoParams.maxPage*10" @current-change="handleVideoCurChange"
                           :current-page.sync="videoParams.pageNumber" class="pagination" v-if="videoParams.maxPage>1">
            </el-pagination>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="讨论区">
          <div class="div-communication-header">
            <el-badge>相关评论</el-badge>
            <el-button type="text" size="small" style="margin-left: 5px"
                       @click="selectCommentOrder">{{ commentParams.orderByClause }}</el-button>
            <el-button type="text" size="small" style="margin-left: 5px"
                       @click="releaseComment(-1,-1)">发表评论</el-button>
            <el-dialog title="评论竞赛" :visible.sync="releaseCommentDialogVisible">
              <el-form :model="releaseCommentForm" :rules="releaseCommentFormRules" ref="releaseCommentForm">
                <el-form-item label="评论内容" prop="content">
                  <el-input v-model="releaseCommentForm.content" autocomplete="off"
                            maxlength="200" :clearable=true type="textarea" rows="5" resize="none"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="releaseCommentDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="confirmReleaseComment">确 定</el-button>
              </div>
            </el-dialog>
          </div>
          <div class="div-comment-list">
            <h3 v-if="commentParams.maxPage==0">暂时没有任何关于该竞赛的评论</h3>
            <div class="div-comment-item" v-for="(comment,index) in comments" :key="index">
              <div class="div-comment-header">
                <img src="@/assets/user.png" />
                <span>{{ comment.nickname }}</span>
              </div>
              <div class="div-comment-body">
                <div v-if="comment.replyId!=-1" class="div-comment-reply">
                  <p style="font-weight: bold;">引用 @<span style="color: #3399cc;">{{ comment.replyNickname }}</span> 发表的：</p>
                  <p>{{ comment.replyContent }}</p>
                </div>
                <div style="margin-top: 5px;">{{ comment.content }}</div>
              </div>
              <div class="div-comment-footer"><span style="color: #808080;">发表于 {{ comment.releaseTime }}</span>
                <el-button type="text" size="small" style="margin-left: 5px"
                           @click="releaseComment(comment.id, comment.fartherId)">回复</el-button>
              </div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="队伍">
          <h2 style="text-align: center;" v-if="!isLogin">登录以使用队伍功能</h2>
          <el-tabs tab-position="left" active-name="myTeam" v-else>
            <el-tab-pane label="我的队伍" name="myTeam">
              <el-card class="card-team" v-if="teamStatus.joinedTeam">
                <div slot="header">
                  <i class="el-icon-star-on"></i>
                  <el-badge :value="myteam.status" type="primary">
                    <span>{{ myteam.name }}</span>
                  </el-badge>
                  <el-button style="float: right; padding: 3px 5px" type="text" v-if="teamStatus.isCaptain"
                             @click="dismissConfirm()">解散队伍</el-button>
                  <el-button style="float: right; padding: 3px 5px" type="text" v-if="teamStatus.isCaptain"
                             @click="updateTeamDialogVisible = true">修改队伍信息</el-button>
                  <el-button style="float: right; padding: 3px 0" type="text" v-if="!teamStatus.isCaptain"
                             @click="leaveConfirm()" >离开队伍</el-button>
                  <el-dialog title="修改队伍信息" :visible.sync="updateTeamDialogVisible" center width="40%">
                    <el-form :model="updateTeamForm" :rules="teamFormRules" ref="updateTeamForm" status-icon >
                      <el-form-item label="队伍名称" prop="name" class="item-dialog">
                        <el-input v-model="updateTeamForm.name" autocomplete="off"></el-input>
                      </el-form-item>
                      <el-form-item label="队伍状态" prop="status" class="item-dialog">
                        <el-radio-group v-model="updateTeamForm.status">
                          <el-radio label="正在招募"></el-radio>
                          <el-radio label="停止招募"></el-radio>
                        </el-radio-group>
                      </el-form-item>
                      <el-form-item label="队伍公告" prop="motto" class="item-dialog">
                        <el-input type="textarea" :rows="4" v-model="updateTeamForm.motto" autocomplete="off"></el-input>
                      </el-form-item>
                    </el-form>
                    <span slot="footer" class="dialog-footer">
                      <el-button type="primary" @click="confirmUpdateTeam()">修 改</el-button>
                    </span>
                  </el-dialog>
                </div>
                <div>
                  <div>队长：{{ myteam.captainName }}</div>
                  <h4>公告</h4>
                  <div>{{ myteam.motto }}</div>
                </div>
              </el-card>
              <el-card class="box-card" v-else>
                <h3>尚未加入任何队伍</h3>
              </el-card>
            </el-tab-pane>
            <el-tab-pane label="所有队伍" name="allTeams" class="pane-allteams">
              <div class="div-allteams-header">
                <div class="dropdown-allteams" >
                  <el-dropdown @command="handleTeamStatusCommand">
                    <el-button type="primary" size="small">
                      {{ teamStatusSelected }}<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item command="所有队伍">所有队伍</el-dropdown-item>
                      <el-dropdown-item command="正在招募">正在招募</el-dropdown-item>
                      <el-dropdown-item command="停止招募">停止招募</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                  <el-button v-if="!teamStatus.joinedTeam" type="primary" size="small" @click="createTeamDialogVisible = true">创建队伍</el-button>
                  <el-dialog title="创建队伍" :visible.sync="createTeamDialogVisible" center width="40%">
                    <el-form :model="createTeamForm" :rules="teamFormRules" ref="createTeamForm" status-icon >
                      <el-form-item label="队伍名称" prop="name" class="item-dialog">
                        <el-input v-model="createTeamForm.name" autocomplete="off"></el-input>
                      </el-form-item>
                      <el-form-item label="队伍公告" prop="motto" class="item-dialog">
                        <el-input type="textarea" :rows="4" v-model="createTeamForm.motto" autocomplete="off"></el-input>
                      </el-form-item>
                    </el-form>
                    <span slot="footer" class="dialog-footer">
                      <el-button type="primary" @click="confirmCreateTeam()">创 建</el-button>
                    </span>
                  </el-dialog>
                </div>
                <el-input placeholder="搜索队伍" value="teamSearch" v-model="teamSearch" size="small" class="input-search">
                  <el-button slot="append" icon="el-icon-search" v-on:click="searchTeam"></el-button>
                </el-input>
              </div>
              <div class="div-allteams-list">
                <h3 v-if="teamsMaxPage==0">暂时没有队伍...</h3>
                <el-card v-for="team in allTeams" :key="team.id" shadow="hover" class="card-team">
                  <div slot="header">
                    <i class="el-icon-star-on"></i>
                    <el-badge :value="team.status" type="primary">
                      <span>{{ team.name }}</span>
                    </el-badge>
                    <el-button style="float: right; padding: 3px 0" type="text"
                               v-if="!teamStatus.joinedTeam && team.status!='停止招募'"
                               @click="joinConfirm(team.id)" >申请加入</el-button>
                  </div>
                  <div>
                    <div>队长：{{ team.captainName }}</div>
                    <h4>公告</h4>
                    <div>{{ team.motto }}</div>
                  </div>
                </el-card>
                <el-pagination layout="prev, pager, next" :page-size="10" :total="teamsMaxPage*10" @current-change="handleTeamsCurChange"
                               :current-page.sync="teamsMaxPage" class="pagination" v-if="teamsMaxPage>1">
                </el-pagination>
              </div>
            </el-tab-pane>
            <el-tab-pane label="我的队员" name="myTeammates" v-if="teamStatus.joinedTeam">
              <div class="div-allteams-header">
                <div class="dropdown-allteams">
                  <el-dropdown @command="handleTeammateStatusCommand">
                    <el-button type="primary" size="small">
                      {{ teammateStatusSelected }}<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item command="已加入">已加入</el-dropdown-item>
                      <el-dropdown-item command="审批中" v-if="teamStatus.isCaptain">审批中</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
                <el-input placeholder="搜索队员" value="teamSearch" v-model="teammateSearch" size="small" class="input-search">
                  <el-button slot="append" icon="el-icon-search" v-on:click="searchTeammate()"></el-button>
                </el-input>
              </div>
              <div class="div-allteams-list">
                <el-table :data="myTeammates" style="width: 100%">
                  <el-table-column prop="nickname" label="昵称" width="100"></el-table-column>
                  <el-table-column prop="school" label="学校" width="140"></el-table-column>
                  <el-table-column prop="skills" label="技能" width="240"></el-table-column>
                  <el-table-column prop="email" label="邮箱"></el-table-column>
                  <el-table-column fixed="right" label="操作" width="180">
                    <template slot-scope="scope">
                      <el-button size="small" v-if="teammateStatusSelected=='已加入'"
                        @click="readySendMessage(scope.row.userId)">私信</el-button>
                      <el-button size="small" type="danger" v-if="teammateStatusSelected=='已加入' && teamStatus.isCaptain
                      && scope.row.userId!=myteam.captainId"
                                 @click="handlerConfirm(scope.row.userId, '移出')">移出队伍</el-button>
                      <el-button size="small" v-if="teammateStatusSelected=='审批中'"
                                 @click="handlerConfirm(scope.row.userId, '同意')">同意</el-button>
                      <el-button size="small" type="danger" v-if="teammateStatusSelected=='审批中'"
                                 @click="handlerConfirm(scope.row.userId, '拒绝')">拒绝</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <el-pagination layout="prev, pager, next" :page-size="10" :total="teammatesMaxPage*10" @current-change="handleTeammatesCurChange"
                               :current-page.sync="teammatesMaxPage" class="pagination" v-if="teammatesMaxPage>1">
                </el-pagination>
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
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-tab-pane>
      </el-tabs>
    </el-main>
    <el-footer><Footer></Footer></el-footer>
  </el-container>
</template>

<script>
  import Header from '@/components/header'
  import Footer from '@/components/footer'
  import { requestCompetitionDetails,requestCommentList,requestCommentMaxPage,requestReleaseComment } from '@/api/competition'
  import { requestFindMyTeam,requestFindAllTeams,requestTeamsMaxPage,requestFindMyTeammates,requestTeammatesMaxPage,
    requestCreateTeam,requestDismissTeam,requestUpdateTeam,requestLeaveTeam,requestJoinTeam,requestHandleJoin } from '@/api/team'
  import { requestSendMessage } from '@/api/message'
  import { requestVideoList,requestVideoMaxPage } from "../../api/video";

  export default {
    name: "Competition",
    components: {
      Header,
      Footer
    },
    methods: {
      getDetails() {
        var that = this;
        let params = {'id':this.comptId};
        requestCompetitionDetails(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.competition = successRes.body.data;
            that.videoParams.tags = that.competition.tags
            that.getVideoList();
            that.getVideoMaxPage();
          } else {
            that.$router.push("/notfound");
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getMyTeam() {
        var that = this;
        let params = {'comptId':this.comptId};
        requestFindMyTeam(params).then(function(successRes){
          if (successRes.body.code === 1) { // 已加入队伍
            that.myteam = successRes.body.data;
            that.updateTeamForm = successRes.body.data;
            that.teamStatus.joinedTeam = true;
            that.teamStatus.isCaptain = successRes.body.isCaptain;
            that.getAllTeammates();
            that.getAllTeammatesMaxPage();
          } else {
            that.myteam = {};
            that.updateTeamForm = {};
            that.teamStatus.joinedTeam = false;
            that.teamStatus.isCaptain = false;
            that.myTeammates = [];
            that.teammatesMaxPage = 0;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      searchTeam() {
        this.getAllTeamsMaxPage();
        this.getAllTeams();
      },
      searchTeammate() {
        this.getAllTeammatesMaxPage();
        this.getAllTeammates();
      },
      getAllTeams() {
        var that = this;
        let params = {'comptId':this.comptId, 'name':this.teamSearch, 'status':this.teamStatusSelected,
          'pageNumber':this.teamsCurPage,'pageSize':10};
        requestFindAllTeams(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.allTeams = successRes.body.list;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getAllTeammates() {
        var that = this;
        let params = {'teamId':this.myteam.id, 'nickname':this.teammateSearch, 'status':this.teammateStatusSelected,
          'pageNumber':this.teammatesCurPage,'pageSize':10};
        requestFindMyTeammates(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.myTeammates = successRes.body.list;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getAllTeamsMaxPage() {
        this.teamsCurPage = 1;
        var that = this;
        let params = {'comptId':this.comptId, 'name':this.teamSearch, 'status':this.teamStatusSelected,'pageSize':10};
        requestTeamsMaxPage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.teamsMaxPage = successRes.body.maxPage;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getAllTeammatesMaxPage() {
        this.teammatesCurPage = 1;
        var that = this;
        let params = {'teamId':this.myteam.id, 'nickname':this.teammateSearch, 'status':this.teammateStatusSelected, 'pageSize':10};
        requestTeammatesMaxPage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.teammatesMaxPage = successRes.body.maxPage;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      handleTeamStatusCommand(command) {
        this.teamStatusSelected = command;
        this.getAllTeamsMaxPage();
        this.getAllTeams();
      },
      handleTeammateStatusCommand(command) {
        this.teammateStatusSelected = command;
        this.getAllTeammatesMaxPage();
        this.getAllTeammates();
      },
      handleTeamsCurChange(curPage) {
        this.teamsCurPage = curPage;
        this.getAllTeams();
      },
      handleTeammatesCurChange(curPage) {
        this.teammatesCurPage = curPage;
        this.getAllTeammates();
      },
      confirmCreateTeam() {
        let params = {'comptId':this.comptId, 'name':this.createTeamForm.name, 'motto':this.createTeamForm.motto};
        var that = this;
        var formName = 'createTeamForm';
        this.$refs[formName].validate((valid) => {
          if (valid) {
            requestCreateTeam(params).then(function(successRes){
              if (successRes.body.code === 1) {
                that.createTeamDialogVisible = false;
                that.$notify.success({
                  title: '操作提示',
                  message: '创建队伍成功',
                  showClose: true
                });
                that.getMyTeam();
                that.getAllTeams();
                that.getAllTeamsMaxPage();
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
      dismissConfirm() {
        var that = this;
        this.$confirm('此操作将永久解散该队伍, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => { // 确认解散
          let params = {'teamId':that.myteam.id};
          requestDismissTeam(params).then(function(successRes){
            if (successRes.body.code === 1) {
              that.getMyTeam();
              that.getAllTeams();
              that.getAllTeamsMaxPage();
              that.$notify.success({
                title: '操作提示',
                message: '解散队伍成功',
                showClose: true
              });
            }
          },function(errRes){
            console.log(errRes.status);
          });
        }).catch(() => {
          that.$message({
            type: 'info',
            message: '已取消解散队伍'
          });
        });
      },
      confirmUpdateTeam() {
        let params = {'teamId':this.myteam.id, 'name':this.updateTeamForm.name, 'motto':this.updateTeamForm.motto,
          'status':this.updateTeamForm.status};
        var that = this;
        var formName = 'updateTeamForm';
        this.$refs[formName].validate((valid) => {
          if (valid) {
            requestUpdateTeam(params).then(function(successRes){
              if (successRes.body.code === 1) {
                that.updateTeamDialogVisible = false;
                that.$notify.success({
                  title: '操作提示',
                  message: '修改队伍信息成功',
                  showClose: true
                });
                that.getMyTeam();
                that.getAllTeams();
                that.getAllTeamsMaxPage();
              }
            },function(errRes){
              console.log(errRes.status);
            });
          } else {
            return false;
          }
        });
      },
      leaveConfirm() {
        var that = this;
        this.$confirm('此操作将永久离开该队伍, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => { // 确认离开
          let params = {'teamId':that.myteam.id};
          requestLeaveTeam(params).then(function(successRes){
            if (successRes.body.code === 1) {
              that.getMyTeam();
              that.$notify.success({
                title: '操作提示',
                message: '离开队伍成功',
                showClose: true
              });
            }
          },function(errRes){
            console.log(errRes.status);
          });
        }).catch(() => {
          that.$message({
            type: 'info',
            message: '已取消离开队伍'
          });
        });
      },
      joinConfirm(id) {
        var that = this;
        this.$confirm('此操作将申请加入该队伍, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => { // 确认加入
          let params = {'teamId':id};
          requestJoinTeam(params).then(function(successRes){
            if (successRes.body.code === 1) {
              that.$notify.success({
                title: '操作提示',
                message: '申请加入队伍成功',
                showClose: true
              });
            }
          },function(errRes){
            console.log(errRes.status);
          });
        }).catch(() => {
          that.$message({
            type: 'info',
            message: '已取消申请加入队伍'
          });
        });
      },
      handlerConfirm(userId, status) {
        var that = this;
        this.$confirm(`此操作将${status}加入该队伍的申请, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let params = {'teamId':that.myteam.id, 'userId':userId, 'status':status};
          requestHandleJoin(params).then(function(successRes){
            if (successRes.body.code === 1) {
              that.$notify.success({
                title: '操作提示',
                message: '操作成功',
                showClose: true
              });
              that.getAllTeammates();
              that.getAllTeammatesMaxPage();
            }
          },function(errRes){
            console.log(errRes.status);
          });
        }).catch(() => {

        });
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
      getComments() {
        var that = this;
        let params = this.commentParams;
        requestCommentList(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.comments = successRes.body.list;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getCommentsMaxPage() {
        var that = this;
        let params = this.commentParams;
        requestCommentMaxPage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.commentParams.maxPage = successRes.body.maxPage;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      selectCommentOrder() {
        this.commentParams.orderByClause == '按时间正序' ?
          this.commentParams.orderByClause = '按时间逆序' : this.commentParams.orderByClause = '按时间正序';
        this.commentParams.pageNumber = 1;
        this.getComments();
      },
      releaseComment(replyId, fartherId) {
        if (this.$store.state.token == '') {
          this.$message('请先登录');
        } else {
          this.releaseCommentForm.replyId = replyId;
          if (replyId != -1) {  // 引用回复
            fartherId==-1 ? this.releaseCommentForm.fartherId = replyId : this.releaseCommentForm.fartherId = fartherId;
          }
          this.releaseCommentDialogVisible = true;
        }
      },
      confirmReleaseComment() {
        let params = this.releaseCommentForm;
        var that = this;
        var formName = 'releaseCommentForm';
        this.$refs[formName].validate((valid) => {
          if (valid) {
            requestReleaseComment(params).then(function(successRes){
              if (successRes.body.code === 1) {
                that.$notify.success({
                  title: '操作提示',
                  message: '发表评论成功',
                  showClose: true
                });
                that.releaseCommentDialogVisible = false;
                that.commentParams.pageNumber = 1;
                that.commentParams.orderByClause = '按时间正序';
                that.getCommentsMaxPage();
                that.getComments();
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
      getVideoMaxPage() {
        this.videoParams.pageNumber = 1;
        var that = this;
        let params = this.videoParams;
        requestVideoMaxPage(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.videoParams.maxPage = successRes.body.maxPage;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      getVideoList() {
        var that = this;
        let params = this.videoParams;
        requestVideoList(params).then(function(successRes){
          if (successRes.body.code === 1) {
            that.videos = successRes.body.list;
          }
        },function(errRes){
          console.log(errRes.status);
        });
      },
      handleVideoCurChange(curPage) {
        this.videoParams.pageNumber = curPage;
        this.getVideoList();
      },
      viewVideo(id) {
        this.$router.push("/video/"+id);
      },
    },
    data() {
      return {
        comptId: 0,
        isLogin: false,
        competition: {
          title: '',
          tags: '',
          details: '{}',
        },
        myteam: {},
        allTeams: [],
        myTeammates: [],
        teamStatus: {
          joinedTeam: false,
          isCaptain: false,
        },
        teamSearch: '',
        teammateSearch: '',
        teamStatusSelected: '所有队伍',
        teammateStatusSelected: '已加入',
        teamsCurPage: 1,
        teamsMaxPage: 1,
        teammatesCurPage: 1,
        teammatesMaxPage: 1,
        createTeamDialogVisible: false,
        createTeamForm: {},
        updateTeamForm: {},
        updateTeamDialogVisible: false,
        teamFormRules: {
          name: [
            {required: true, message: '请输入队伍名称', trigger: 'blur'},
            {min: 3, max: 15, message: '长度在 3 个字符到 15 个字符之间', trigger: 'blur'}
          ],
          motto: [
            {required: true, message: '请输入队伍公告', trigger: 'blur'},
            {min: 6, message: '长度在 6 个字符以上', trigger: 'blur'}
          ],
        },
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
        commentParams: {
          orderByClause: '按时间正序',
          pageSize: 10,
          pageNumber: 1,
          maxPage: 0,
          themeId: 0,
          fartherId: -1,
        },
        comments: [],
        releaseCommentDialogVisible: false,
        releaseCommentForm: {
          content: '',
          replyId: -1,
          fartherId: -1,
          themeId: 0,
        },
        releaseCommentFormRules: {
          content: [
            {required: true, message: '请输入评论内容', trigger: 'blur'},
            {min: 4, max: 200, message: '长度在 4 个字符到 200 个字符之间', trigger: 'blur'}
          ],
        },
        videoParams: {
          pageSize: 10,
          pageNumber: 1,
          maxPage: 0,
          tags: '',
          orderByClause: '按评分排序'
        },
        videos: []
      }
    },
    created() {
      this.comptId = this.$route.path.substring(13);
      this.commentParams.themeId = this.comptId;
      this.releaseCommentForm.themeId = this.comptId;
      this.getDetails();
      this.getComments();
      this.getCommentsMaxPage();
      if (this.$store.state.token == '') { // 未登录
        this.isLogin = false;
      } else {
        this.isLogin = true;
        this.getMyTeam();
        this.getAllTeams();
        this.getAllTeamsMaxPage();
      }
    }
  }
</script>

<style scoped>

  .card-header {
    height: 200px;
  }

  .body-main {
    margin: 0 auto;
    width: 92%;
  }

  .img-header {
    height: 160px;
  }

  .col-header-body {
    position: relative;
    top: -20px;
  }

  .tag-tags {
    margin-right: 10px;
  }

  .tabs-body {
    margin-top: 15px;
  }

  .p-time, .p-sponsor {
    font-size: small;
    text-align: left;
  }

  .div-stat {
    display: flex;
  }

  .div-stat-line {
    width: 1px;
    border: 1px dashed #99cc99;
    height: 80px;
    margin: 15px;
  }

  .div-stat-body {
    text-align: center;
    width: 120px;
  }

  .p-stat-key {
    font-size:x-large;
    font-weight: bold;
  }

  .p-stat-value {
    margin-top: -10px;
  }

  .div-link {
    margin: 0 auto;
    padding-top: 10px;
    width: 80%;
  }

  .a-link {
    text-decoration: none;
    color: black;
    border: 1px solid #66ccff;
    border-radius: 5px;
    padding: 10px 60px;
  }

  .div-allteams-header {
    display: flex;
  }

  .input-search {
    width: 300px;
  }

  .pane-allteams {
    width: 900px;
  }

  .div-allteams-list {
    width: 855px;
    padding-left: 10px;
  }

  .card-team {
    margin: 6px 0;
    border-color: #66cccc;
  }

  .dropdown-allteams {
    padding-left: 10px;
    width: 555px;
  }

  .pagination {
    text-align: center;
    margin-top: 10px;
  }

  .div-communication-header {
    font-weight: bold;
    border-bottom: 1px solid #ccffff;
    padding-bottom: 5px;
  }
  .div-comment-list {
    margin-top: 10px;
    font-size: small;
  }
  .div-comment-item {
    margin-bottom: 10px;
  }
  .div-comment-header {
    display: flex;
  }
  .div-comment-header img{
    width: 32px;
    height: 32px;
  }
  .div-comment-header span{
    line-height: 32px;
    vertical-align: middle;
    margin-left: 10px;
    font-size: small;
  }
  .div-comment-body {
    margin: 8px 0;
  }
  .div-comment-reply {
    margin-top: 10px;
    padding: 5px;
    background-color: #FFF0F5;
    border-radius: 5px;
  }

  .row-video-list {
    margin-top: 10px;
  }

  .card-video-item {
    margin: 0 auto;
    margin-bottom: 10px;
    height: 180px;
  }

  .img-video-item {
    width: 100%;
  }

  .col-video-item-body {
    padding-left: 10px;
  }

  .span-video-title {
    font-size: large;;
    font-weight: bold;
  }
  .span-video-score {
    color: #ff6666;
  }

  .p-video-author {
    font-size: small;
    color: gray;
  }

  .p-video-intros {
    font-size: small;
  }

  .p-video-stat {
    font-size: small;
  }
</style>
