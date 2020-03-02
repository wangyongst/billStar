<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">

          <SchoolSelect @dataChange="schoolChange"></SchoolSelect>

          <el-form-item label="流失时间：" size="mini">
            <span>最近&nbsp;&nbsp;</span>
            <el-input size="mini" placeholder="月数" style="width: 100px" v-model="query.month"></el-input>
            <span>&nbsp;&nbsp;&nbsp;&nbsp;月</span>
          </el-form-item>

          <TeacherSelect></TeacherSelect>

        </el-form>

      </el-col>
      <!--    数据表格-->
      <el-col :span="24">
        <el-table v-loading="loading" :data="page.list" class="bill-table" size="" style="width: 100%">
          <el-table-column label="" type="index" width="40" align="center">
          </el-table-column>

          <el-table-column label="姓名" width="150" align="left" prop="name">
          </el-table-column>

          <el-table-column label="电话" width="150" align="left" prop="mobile">
          </el-table-column>

          <el-table-column label="流失时间" width="150" align="left" prop="lostTime" :formatter="baseTableFormatDate">
          </el-table-column>

          <el-table-column label="课程" width="300" align="left" prop="name" :formatter="baseFormatCourse">
          </el-table-column>

          <el-table-column label="教师" width="150" align="left" prop="teacherName">
          </el-table-column>

          <el-table-column label="校区" width="150" align="left" prop="schoolName">
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
  import SchoolSelect from "../select/SchoolSelect";
  import BackToWork from "../back/BackToWork";
  import TeacherSelect from "../select/TeacherSelect";

  export default {
    name: 'SearchLose',
    components: {TeacherSelect, BackToWork, SchoolSelect},
    data() {
      return {
        detailDialogVisible: false,
        page: {
          total: 0,
          list: [],
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: [],
          teacherIds: [],
          month: null,
        },
        loading: false
      }

    },

    mounted: function () {
      const _this = this;
      _this.listStudentCourse();
    },

    methods: {
      schoolIdChange(val) {
        this.query.schoolIds = val;
      },
      listStudentCourse() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/course/pageLost', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.list = res.records;
          _this.page.total = res.total;
        }, _this.operateFail);
      },

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
