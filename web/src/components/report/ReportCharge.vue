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
        <el-radio-group v-model="query.before">
          <el-radio size="mini" label="1">昨天</el-radio>
          <el-radio size="mini" label="2">今天</el-radio>
          <el-radio size="mini" label="3">本月</el-radio>
          <el-radio size="mini" label="4">上月</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <el-container style="width: 100%">
      <el-table stripe v-loading="loading" :data="page.records" Charge="bill-table">
        <template v-for="(item) in page.header">
          <el-table-column :prop="item.myProp" :label="item.myLabel" :key="item.myProp" :formatter="setZero" style="width: 80px"></el-table-column>
        </template>
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
          records: [],
          header: []
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: [],
          begin: null,
          end: null,
          before:null
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
          _this.page.header = res.header;
          _this.page.records = res.pageRecords;
          _this.loading = false;
        }, _this.operateFail);
      },
      schoolChange(val) {
        console.log(val);
        this.query.schoolIds = val;
      },
      setZero(row, col, val) {
        if (!val) return 0;
        else return val;
      },
      gotoPage(page) {
        const _this = this;
        _this.query.current = page;
        _this.listReport();
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
