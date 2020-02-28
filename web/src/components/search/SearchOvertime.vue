<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">

          <SchoolSelect @dataChange="schoolChange"></SchoolSelect>
          <SubjectSelect @dataChange="subjectChange"></SubjectSelect>
          <ClassSelect @dataChange="classChange"></ClassSelect>

          <el-row>
            <el-col :span="6" :offset="16">
              <el-button @click="listStudentCharge" type="primary" style="width: 100px;" size="mini" plain round>查询</el-button>
            </el-col>
          </el-row>
        </el-form>

      </el-col>
      <!--    数据表格-->
      <el-col :span="24">
        <el-table v-loading="loading" :data="page.records" class="bill-table" size="" style="width: 100%">
          <el-table-column label="" type="index" width="40" align="center">
          </el-table-column>

          <el-table-column label="姓名" width="150" align="left" prop="studentName">
          </el-table-column>

          <el-table-column label="到期时间" width="150" align="left" prop="expireTime" :formatter="baseTableFormatDate">
          </el-table-column>

          <el-table-column label="已超期天数" width="150" align="left" prop="">
          </el-table-column>

          <el-table-column label="在学课程" width="150" align="left">
          </el-table-column>

          <el-table-column label="教师" width="150" align="left" prop="teacherName">
          </el-table-column>

          <el-table-column label="校区" width="150" align="left" prop="schoolName">
          </el-table-column>

          <el-table-column label="开票人" width="150" align="left" prop="createName">
          </el-table-column>

          <el-table-column label="开票时间" width="150" align="left" prop="createTime">
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

  export default {
    name: 'SearchOvertime',
    components: {BackToWork, ClassSelect, SchoolSelect, SubjectSelect},
    data() {
      return {
        page: {
          total: 0,
          records: [],
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: [],
          subjectIds: [],
          classIds: [],
        },
        loading: false,
      }
    },

    mounted: function () {
      const _this = this;
      _this.listStudentCharge();
    },

    methods: {
      listStudentCharge() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/charge/pageOverTime', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.records = res.records;
          _this.page.total = res.total;
        }, _this.operateFail);
      },
      schoolChange(val) {
        this.query.schoolIds = val;
      },
      subjectChange(val) {
        this.query.subjectIds = val;
      },
      classChange(val) {
        this.query.classIds = val;
      },
      gotoPage(page) {
        const _this = this;
        _this.query.current = page;
        _this.listStudentCourse();
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
