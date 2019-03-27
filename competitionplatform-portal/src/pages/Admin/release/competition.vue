<template>
  <div>
    <el-row>
      <el-col :span="4">
        <img class="img-logo" src="@/assets/logo.png">
      </el-col>
      <el-col :span="16">
        <h4>发布新竞赛</h4>
      </el-col>
      <el-col :span="4" style="margin-top: 16px;">
        <el-button size="small" @click="backPage()" >返回上一页</el-button>
        <el-button size="small" @click="logoff()" >退出登录</el-button>
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px;">
      <el-form ref="form" :model="form" :rules="formRules" label-width="120px">
        <el-form-item label="标题" prop="title">
          <el-input size="mini" v-model="form.title" style="width: 300px;"></el-input>
        </el-form-item>
        <el-form-item label="主办方" prop="sponsor">
          <el-input size="mini" v-model="form.sponsor" style="width: 300px;"></el-input>
        </el-form-item>
        <el-form-item label="LOGO" prop="logo">
          <el-input size="mini" v-model="form.logo" style="width: 300px;"></el-input>
        </el-form-item>
        <el-form-item label="官网链接" prop="link">
          <el-input size="mini" v-model="form.link" style="width: 300px;"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-dropdown @command="handleCategoryCommand">
            <el-button size="small" class="button-selector">
              <span>{{ form.category }}<i class="el-icon-arrow-down el-icon--right"></i></span>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="工科">工科</el-dropdown-item>
              <el-dropdown-item command="文体">文体</el-dropdown-item>
              <el-dropdown-item command="理科">理科</el-dropdown-item>
              <el-dropdown-item command="商科">商科</el-dropdown-item>
              <el-dropdown-item command="综合">综合</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-dropdown @command="handleStatusCommand">
            <el-button size="small" class="button-selector">
              <span>{{ form.status }}<i class="el-icon-arrow-down el-icon--right"></i></span>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="可报名">可报名</el-dropdown-item>
              <el-dropdown-item command="进行中">进行中</el-dropdown-item>
              <el-dropdown-item command="已结束">已结束</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input size="mini" v-model="form.tags" style="width: 250px;" disabled></el-input>
          <el-button size="mini" @click="selectTagsDialogVisible = true">选择标签</el-button>
          <el-dialog title="选择竞赛标签" :visible.sync="selectTagsDialogVisible" width="45%" center>
            <el-transfer v-model="tagsSelected" :data="tagOptions" :titles="tagsTitle"></el-transfer>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="confirmSelectTags">确 定</el-button>
              </span>
          </el-dialog>
        </el-form-item>
        <el-form-item label="竞赛时间" prop="timeSelected">
          <el-date-picker size="mini" v-model="form.timeSelected" type="datetimerange" range-separator="至"
                          start-placeholder="开始时间" end-placeholder="结束时间" value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="竞赛信息" label-position="top" prop="details">
          <quill-editor v-model="form.details" ref="quillEditor" :options="editorOption"></quill-editor>
        </el-form-item>
        <el-form-item class="item-button">
          <el-button size="mini" type="primary" @click="submit" style="width: 120px;">确认发布</el-button>
        </el-form-item>
      </el-form>
    </el-row>
  </div>
</template>

<script>
  import { requestReleaseCompetition } from "../../../api/competition";
  import { quillEditor,Quill } from 'vue-quill-editor'

  export default {
    name: "competition",
    created() {
      if (this.$store.state.token == '' || this.$store.state.roleType != 2) {   // 非管理员登录状态则回到登录主页
        this.$router.push("/admin/login");
      }
    },
    methods: {
      backPage() {
        this.$router.go(-1);
      },
      logoff() {
        this.$store.commit('clearToken');
        this.$router.push("/admin/login");
      },
      submit() {
        this.form.startTime = this.form.timeSelected[0];
        this.form.endTime = this.form.timeSelected[1];
        let params = this.form;
        var that = this;
        var formName = 'form';
        this.$refs[formName].validate((valid) => {
          if (valid) {
            requestReleaseCompetition(params).then(function(successRes){
              if (successRes.body.code === 1) {
                that.$notify.success({
                  title: '操作提示',
                  message: '发布新竞赛成功',
                  showClose: true
                });
                that.$router.go(-1);
              }
            },function(errRes){
              console.log(errRes.status);
            });
          } else {
            return false;
          }
        });
      },
      handleCategoryCommand(command) {
        this.form.category = command;
      },
      handleStatusCommand(command) {
        this.form.status = command;
      },
      confirmSelectTags() {
        this.form.tags = this.tagsSelected.join(",");
        this.selectTagsDialogVisible = false;
      }
    },
    data() {
      return {
        id: 0,
        form: {
          title: '',
          sponsor: '',
          category: '工科',
          tags: '',
          status: '可报名',
          startTime: '',
          endTime: '',
          link: '',
          logo: '',
          details: '',
          timeSelected: [],
        },
        formRules: {
          title: [
            {required: true, message: '请输入竞赛标题', trigger: 'blur'},
            {min: 4, max: 30, message: '长度在 4 个字符到 30 个字符之间', trigger: 'blur'}
          ],
          sponsor: [
            {required: true, message: '请输入竞赛主办方', trigger: 'blur'},
            {min: 4, max: 30, message: '长度在 4 个字符到 30 个字符之间', trigger: 'blur'}
          ],
          tags: [
            {required: true, message: '请选择竞赛标签', trigger: 'blur'},
          ],
          timeSelected: [
            { type: 'array', required: true, message: '请选择竞赛开始 / 结束时间', trigger: 'blur' }
          ],
          link: [
            {required: true, message: '请输入竞赛官网链接地址', trigger: 'blur'},
          ],
          logo: [
            {required: true, message: '请输入竞赛LOGO图片地址', trigger: 'blur'},
          ],
          details: [
            {required: true, message: '请输入竞赛信息', trigger: 'blur'},
          ],
        },
        selectTagsDialogVisible: false,
        tagsTitle: ['标签可选项','已选择标签'],
        tagsSelected: [],
        tagOptions: [{key: '数学建模',label:'数学建模'}, {key: '程序设计',key:'程序设计'}, {key: '机器人',label:'机器人'},
          {key: '工程机械',key:'工程机械'}, {key: '土木建筑',key:'土木建筑'},{key: '大数据',label:'大数据'},
          {key: '交通车辆',label:'交通车辆'}, {key: '航空航天',label:'航空航天'}, {key: '船舶海洋',label:'船舶海洋'},
          {key: '环境能源',label:'环境能源'}, {key: '计算机与信息技术',label:'计算机与信息技术'},
          {key: '材料高分子',label:'材料高分子'},{key: '电子与自动化',label:'电子与自动化'},
          {key: '工业与创意设计',label:'工业与创意设计'}, {key: '外语',label:'外语'},
          {key: '演讲主持与辩论',label:'演讲主持与辩论'}, {key: '模特',label:'模特'},
          {key: '歌舞书画与摄影',label:'歌舞书画与摄影'},
          {key: '体育',label:'体育'}, {key: 'UI设计',label:'UI设计'}, {key: '服装设计',label:'服装设计'},
          {key: '电子竞技',label:'电子竞技'},{key: '数学',label:'数学'}, {key: '物理',label:'物理'},
          {key: '化学化工',label:'化学化工'},{key: '健康生命与医学',label:'健康生命与医学'}, {key: '力学',label:'力学'},
          {key: '创业',label:'创业'}, {key: '商业',label:'商业'},{key: '职业技能',label:'职业技能'},
          {key: '环保公益',label:'环保公益'}, {key: '社会综合',label:'社会综合'}
        ],
        editorOption: {
          modules:{
            toolbar:[
              ['bold', 'italic', 'underline'],
            ]
          }
        }
      }
    },
    computed: {
      editor() {
        return this.$refs.quillEditor.quill;
      }
    },
    mounted() {
      this.editor.container.style.height = '300px';
    }
  }
</script>

<style scoped>

</style>
