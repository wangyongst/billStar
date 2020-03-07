<template>
  <el-container id="body">

    <el-header class="body">教师管理
      <el-button @click="synTeachers"
                 style="float: right;margin-top:13px;"
                 type="primary"
                 plain
                 size="small" class="normalHide">从钉钉同步
      </el-button>
    </el-header>


    <el-form label-width="100px" class="search-form">
      <SchoolSelect @dataChange="schoolChange"></SchoolSelect>
      <el-form-item label="姓名：">
        <el-input size="mini" style="width: 120px" v-model="query.nameLike" placeholder="教师姓名"></el-input>
        <el-button class="btn-search" @click="searchFunc" size="mini">查询</el-button>
      </el-form-item>

    </el-form>
    <el-col :span="24">
      <el-table v-loading="loading" :data="page.records" class="bill-table" style="width: 100%">
        <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" width="120"></el-table-column>
        <el-table-column label="部门" width="300">
          <template slot-scope="scope">{{formatDept(scope.row.schoolList)}}</template>
        </el-table-column>
        <el-table-column label="校区" width="">
          <template slot-scope="scope">{{formatDept(scope.row.schoolList)}}</template>
        </el-table-column>
        <el-table-column label="是否教师" prop="isTeacher" width="100" :formatter="baseYesNo" align="center">
        </el-table-column>
        <el-table-column label="是否管理员" prop="isAppAdmin" width="100" :formatter="baseYesNo" align="center">
        </el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="120">
          <template slot-scope="scope">
            <template v-if="scope.row.isTeacher === true">
              <el-button @click="setAsNotTeacher(scope.row.id)" type="text" size="mini"
                         class="width100">设为非教师
              </el-button>
            </template>
            <template v-if="scope.row.isTeacher == false">
              <el-button @click="setAsTeacher(scope.row.id)" type="text" size="mini"
                         class="width100">设为教师
              </el-button>
            </template>
            <br>
            <template v-if="scope.row.isAppAdmin === true">
              <el-button @click="setAsNotAppAdmin(scope.row.id)" type="text" size="mini"
                         class="width100">设为非管理员
              </el-button>
            </template>
            <template v-if="scope.row.isAppAdmin == false">
              <el-button @click="setAsAppAdmin(scope.row.id)" type="text" size="mini"
                         class="width100">设为管理员
              </el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-col>

    <el-col :span="24">
      <el-pagination
        class="common-page"
        background
        layout="total,prev, pager, next"
        :total="page.total"
        :page-size="query.size"
        :current-page="query.current"
        @current-change="gotoPage"
        @prev-click="gotoPage"
        @next-click="gotoPage"
      ></el-pagination>
    </el-col>

  </el-container>

</template>

<script>

  import SchoolSelect from "../select/SchoolSelect";

  export default {
    name: 'SettingTearcher',
    components: {SchoolSelect},
    data() {
      return {
        page: {
          total: 0,
          records: [],
        },
        query: {
          current: 1,
          size: 10,
          deptIds: [],
          nameLike: null,
        },
        loading: true,
      }
    },
    mounted() {
      const _this = this;
      _this.listTeacher();
    },
    methods: {
      searchFunc() {
        const _this = this;
        this.query.current = 1;
        _this.listTeacher();
      },
      schoolChange(val) {
        this.query.deptIds = val;
      },
      synTeachers() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/ding/synUser').then(function (res) {
          _this.loading = false;
          _this.baseSuccessNotify("操作成功，即将刷新教师列表");
          _this.listTeacher();
        }, _this.operateFail);
      },
      listTeacher() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/teacher/page', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.records = res.records;
          _this.page.total = res.total;
        }, _this.operateFail);
      },
      updateTeacher(cmd) {
        const _this = this;
        _this.httpUtils.appPost('/teacher/update', cmd).then(function (res) {
          _this.listTeacher();
        }, _this.operateFail);
      },

      // 置为教师
      setAsTeacher(tid) {
        const cmd = {id: tid, isTeacher: true};
        this.updateTeacher(cmd);
      },
      // 置为非教师
      setAsNotTeacher(tid) {
        const cmd = {id: tid, isTeacher: false};
        this.updateTeacher(cmd);
      },

      // 置为管理员
      setAsAppAdmin(tid) {
        const cmd = {id: tid, isAppAdmin: true};
        this.updateTeacher(cmd);
      },
      // 置为非教师
      setAsNotAppAdmin(tid) {
        const cmd = {id: tid, isAppAdmin: false};
        this.updateTeacher(cmd);
      },

      // 尝试分页
      gotoPage(page) {
        const _this = this;
        _this.query.current = page;
        _this.listTeacher();
      },
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r);
        _this.loading = false;
      },
    }
  }
</script>

<style scoped>

</style>
