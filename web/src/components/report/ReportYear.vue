<template>
  <el-container id="body">

    <el-header class="body">
      年月报表
    </el-header>

    <el-form label-width="100px" class="search-form">

      <SchoolSelect @dataChange="schoolChange"></SchoolSelect>

      <el-form-item label="开始月份：">
        <el-date-picker
          v-model="query.begin"
          type="month"
          size="mini"
          placeholder="选择月份">
        </el-date-picker>
        <el-button class="btn-search" @click="listReport" size="mini">查询</el-button>
      </el-form-item>
    </el-form>

    <el-container style="width: 100%">
      <el-table stripe v-loading="loading" :data="page.records" Charge="bill-table">
        <template v-for="(item) in page.header">
          <el-table-column :prop="item.myProp" :label="item.myLabel" :key="item.myProp"></el-table-column>
        </template>
      </el-table>
    </el-container>

  </el-container>

</template>

<script>
  import SchoolSelect from "../select/SchoolSelect";

  export default {
    components: {SchoolSelect},
    name: 'ReportYear',
    data() {
      return {
        page: {
          records: [],
          header: []
        },
        query: {
          schoolIds: [],
          begin: null
        },
        loading: false
      }
    },
    mounted:function(){
      const _this = this;
      _this.listReport();
    },

    methods: {
      listReport() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/report/month', _this.query).then(function (res) {
          _this.page.header = res.header;
          _this.page.records = res.pageRecords;
          _this.loading = false;
        }, _this.operateFail);
      },
      schoolChange(val) {
        console.log(val);
        this.query.schoolIds = val;
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
