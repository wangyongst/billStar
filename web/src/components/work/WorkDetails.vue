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

          <el-form-item label="开票区间：" size="mini">
            <el-radio-group v-model="query.time">
              <el-radio size="mini" label="今天"></el-radio>
              <el-radio size="mini" label="本周"></el-radio>
              <el-radio size="mini" label="本月"></el-radio>
              <el-radio size="mini" label="上周"></el-radio>
              <el-radio size="mini" label="上月"></el-radio>
              <el-radio size="mini" label="区间"></el-radio>
            </el-radio-group>
          </el-form-item>


          <el-form-item label="选择类型：" size="mini">
            <el-checkbox-group v-model="query.types">
              <el-checkbox size="mini" :label="全选"></el-checkbox>
              <el-checkbox size="mini">补费</el-checkbox>
              <el-checkbox size="mini">新生</el-checkbox>
              <el-checkbox size="mini">续费</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>


      </el-col>
      <!--    数据表格-->
      <el-col :span="24">
        <el-table v-loading="loading" :data="page.records" class="bill-table" size="" style="width: 100%">
          <el-table-column label="" type="index" width="40" align="center">
          </el-table-column>

          <el-table-column label="类型" width="150" align="left">
          </el-table-column>

          <el-table-column label="收费方式" width="150" align="left">
          </el-table-column>

          <el-table-column label="校区" width="150" align="left">
          </el-table-column>

          <el-table-column label="姓名" width="150" align="left">
          </el-table-column>

          <el-table-column label="金额" width="150" align="left">
          </el-table-column>

          <el-table-column label="开票人" width="150" align="left">
          </el-table-column>

          <el-table-column label="开票时间" width="150" align="left">
          </el-table-column>

          <el-table-column label="课程" width="150" align="left">
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
    name: 'WorkDetails',
    components: {BackToWork, ClassSelect, SchoolSelect, SubjectSelect},
    data() {
      return {
        detailDialogVisible: false,
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
          teacherIds: [],
          time: null,
          type: null
        },
        loading: false,
      }

    },

    mounted: function () {
      const _this = this;
    },

    methods: {
      listStudentCharge() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/charge/pagev', _this.query).then(function (res) {
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
