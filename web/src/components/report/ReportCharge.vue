<template>
  <el-container id="body">

    <el-header class="body">
      支付方式-校区汇总
    </el-header>

    <el-form label-width="100px" class="search-form">
      <SchoolSelect @dataChange="schoolChange"></SchoolSelect>
      <el-form-item label="时间选择：">
        <el-date-picker
          class="date"
          v-model="query.begin"
          type="date"
          style="width: 180px"
          placeholder="选择日期">
        </el-date-picker>
        &nbsp;&nbsp;至
        <el-date-picker
          class="date"
          v-model="query.end"
          type="date"
          style="width: 180px"
          placeholder="选择日期">
        </el-date-picker>
        <el-button class="btn-search" @click="listReport" size="mini">查询</el-button>
      </el-form-item>
    </el-form>

    <el-container style="width: 100%">
      <el-table stripe v-loading="loading" :data="page.records" Charge="bill-table">
        <el-table-column label="校区" prop="schoolName" width="80" align="center"></el-table-column>
        <el-table-column label="支付方式" prop="chargeType" width="" align="center"></el-table-column>
        <el-table-column label="总计" prop="sum" width="" align="center"></el-table-column>
      </el-table>
    </el-container>

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
    name: 'ReportCharge',
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
          schoolIds: [],
          begin: null,
          end: null
        },
        loading: false
      }
    },
    mounted: function () {
      const _this = this;
      _this.listReport()
    },
    methods: {

      listReport() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/report/charge', _this.query).then(function (res) {
          _this.page.records = res.records;
          _this.page.total = res.total;
          _this.loading = false;
        }, _this.operateFail);
      },
      schoolChange(val) {
        console.log(val);
        this.query.schoolIds = val;
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
      },

    }
  }
</script>

<style scoped>

</style>
