<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">

          <SchoolSelect @dataChange="schoolChange"></SchoolSelect>

          <el-row>
            <el-col :span="6" :offset="16">
              <el-button @click="listStudent" type="primary" style="width: 100px;" size="mini" plain round>查询</el-button>
            </el-col>
          </el-row>

        </el-form>

      </el-col>
      <!--    数据表格-->
      <el-col :span="24">
        <el-table v-loading="loading" :data="page.records" class="bill-table" size="" style="width: 100%">
          <el-table-column label="" type="index" width="40" align="center">
          </el-table-column>

          <el-table-column label="姓名" width="150" align="left" prop="name">
          </el-table-column>

          <el-table-column label="校区" width="150" align="left" prop="schoolName">
          </el-table-column>

          <el-table-column label="电话" width="150" align="left" prop="mobile">
          </el-table-column>

          <el-table-column label="预计复学时间" width="200" align="left" prop="fuTime" :formatter="baseTableFormatTime">
          </el-table-column>

          <el-table-column fixed="right" label="操作" align="rigth" width="180">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="updateItem(scope.row.id)">复学</el-button>
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
    </el-row>
  </div>
</template>

<script>
  import ClassSelect from "../select/ClassSelect";
  import SchoolSelect from "../select/SchoolSelect";
  import SubjectSelect from "../select/SubjectSelect";
  import BackToWork from "../back/BackToWork";
  import TeacherSelect from "../select/TeacherSelect";

  export default {
    name: 'StudentFu',
    components: {BackToWork, SchoolSelect},
    data() {
      return {
        page: {
          total: 0,
          records: [],
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: []
        },
        fu: {
          studentId: null
        },
        loading: false
      }
    },

    mounted: function () {
      const _this = this;
      _this.listStudent();
    }
    ,

    methods: {
      updateItem(item) {
        const _this = this;
        _this.fu.studentId = item;
        _this.chargePost();
      }
      ,
      listStudent() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/main/pageFu', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.records = res.records;
          _this.page.total = res.total;
        }, _this.operateFail);
      }
      ,
      schoolChange(val) {
        this.query.schoolIds = val;
      }
      ,
      dialogCancel() {
        this.createDialogVisible = false;
      }
      ,
      chargePost() {
        const _this = this;
        _this.httpUtils.appPost('/student/main/fu', _this.fu).then(function (res) {
          _this.listStudent();
          _this.createDialogVisible = false;
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      }
      ,
      gotoPage(page) {
        const _this = this;
        _this.query.current = page;
        _this.listStudentCourse();
      }
      ,
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r);
        _this.loading = false;
      }
    }
  }
</script>

<style scoped>

</style>
