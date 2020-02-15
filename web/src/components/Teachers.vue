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
      <DeptSchoolSelect @dataChange="deptSchoolIdChange"
                        @deptSchoolIdInitFinish="deptSchoolIdInitFinish"
                        dept-query-url="/schoolZone/listAllDeptEntity"
      ></DeptSchoolSelect>
      <el-form-item label="姓名：">
        <el-input size="mini" style="width: 120px" v-model="query.data.nameLike" placeholder="教师姓名"></el-input>
        <el-button class="btn-search" @click="searchFunc" size="mini">查询</el-button>
      </el-form-item>

    </el-form>
    <el-container style="width: 100%">
      <el-table v-loading="loading" :data="page.list" class="bill-table" style="width: 100%">
        <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" width="120"></el-table-column>
        <el-table-column label="部门" width="300">
          <template slot-scope="scope">{{formatDept(scope.row.depts)}}</template>
        </el-table-column>
        <el-table-column label="校区" width="">
          <template slot-scope="scope">{{formatDept(scope.row.deptSchools)}}</template>
        </el-table-column>
        <el-table-column label="是否教师" prop="isTeacher" width="100" :formatter="baseYesNo" align="center">
        </el-table-column>
        <el-table-column label="是否管理员" prop="isAppAdmin" width="100" :formatter="baseYesNo" align="center">
        </el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="120">
          <template slot-scope="scope">
            <template v-if="scope.row.isTeacher === 1">
              <el-button @click="setAsNotTeacher(scope.row.id)" type="text" size="mini"
                         class="width100">设为非教师
              </el-button>
            </template>
            <template v-if="scope.row.isTeacher !== 1">
              <el-button @click="setAsTeacher(scope.row.id)" type="text" size="mini"
                         class="width100">设为教师
              </el-button>
            </template>
            <br>
            <template v-if="scope.row.isAppAdmin === 1">
              <el-button @click="setAsNotAppAdmin(scope.row.id)" type="text" size="mini"
                         class="width100">设为非管理员</el-button>
            </template>
            <template v-if="scope.row.isAppAdmin !== 1">
              <el-button @click="setAsAppAdmin(scope.row.id)" type="text" size="mini"
                         class="width100">设为管理员
              </el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-container>

    <el-pagination
      class="common-page"
      background
      layout="total,prev, pager, next"
      :total="page.total"
      :page-size="query.pageSize"
      :current-page="query.pageNo"
      @current-change="currentPage"
      @prev-click="prevPage"
      @next-click="nextPage"
    ></el-pagination>

  </el-container>

</template>

<script>
  import DeptSchoolSelect from "./forSelect/DeptSchoolSelect";

  export default {
    name: 'Teachers',
    components: {DeptSchoolSelect},
    data() {
      return {
        page: {
          total: 0,
          list: [],
        },
        query: {
          pageNo: 1,
          pageSize: 10,
          orderBy: 'create_date DESC',
          data: {
            deptIds: [],
            nameLike: null,
          }

        },
        loading: true,
      }
    },
    mounted: function () {
      const _this = this;
    },
    methods: {
      deptSchoolIdInitFinish() {
        this.listTeacher();
      },
      searchFunc() {
        const _this = this;
        _this.listTeacher();
      },
      deptSchoolIdChange(val) {
        this.query.data.deptIds = val;
      },
      //
      synTeachers() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/ding/synOrganization').then(function (res) {
          _this.loading = false;
          _this.baseSuccessNotify("操作成功，即将刷新教师列表");
          _this.listTeacher();
        }, _this.operateFail);
      },
      listTeacher() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/teacher/listUser', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.list = res.data.list;
          _this.page.total = res.data.total;
        }, _this.operateFail);
      },
      formatterYesNo(row, column, v) {
        return parseInt(v) === 1 ? "是" : "否";
      },
      // 置为教师
      setAsTeacher(tid) {
        const _this = this;
        _this.httpUtils.appPost('/teacher/setAsTeacher?id=' + tid).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
            _this.listTeacher();
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },
      // 置为非教师
      setAsNotTeacher(tid) {
        const _this = this;
        _this.httpUtils.appPost('/teacher/setAsNotTeacher?id=' + tid).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
            _this.listTeacher();
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },

      // 置为管理员
      setAsAppAdmin(tid) {
        const _this = this;
        _this.httpUtils.appPost('/teacher/setAsAppAdmin?id=' + tid).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
            _this.listTeacher();
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },
      // 置为非教师
      setAsNotAppAdmin(tid) {
        const _this = this;
        _this.httpUtils.appPost('/teacher/setAsNotAppAdmin?id=' + tid).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
            _this.listTeacher();
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },

      // 尝试分页
      currentPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listTeacher();
      },
      prevPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listTeacher();
      },
      nextPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listTeacher();
      },
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(JSON.stringify(r));
        _this.loading = false;
      },
    }
  }
</script>

<style scoped>

</style>
