<template>
  <el-container id="body">

    <el-header class="body">
      年月报表
    </el-header>

    <el-form label-width="100px" class="search-form">

      <SchoolSelect></SchoolSelect>

      <el-form-item label="开始月份：">
        <el-date-picker
          v-model="query.startMonth"
          type="month"
          size="mini"
          placeholder="选择月份">
        </el-date-picker>
        <el-button class="btn-search" @click="searchFunc" size="mini">查询</el-button>
      </el-form-item>
    </el-form>

    <el-container style="width: 100%">
      <el-table stripe v-loading="loading" :data="list" Charge="bill-table">
        <el-table-column label="校区" prop="deptSchoolName" width="80" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(0)" prop="sale1" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(1)" prop="sale2" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(2)" prop="sale3" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(3)" prop="sale4" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(4)" prop="sale5" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(5)" prop="sale6" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(6)" prop="sale7" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(7)" prop="sale8" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(8)" prop="sale9" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(9)" prop="sale10" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(10)" prop="sale11" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(11)" prop="sale12" align="center"></el-table-column>
        <el-table-column v-bind:label="formatLabel(12)" prop="sale13" align="center"></el-table-column>
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
        list: [],
        query: {
          deptSchoolIds: [],
          startMonth: null,
        },
        titleArr: [],
        deptSchoolForSelect: [],
        loading: false,
      }
    },
    mounted: function () {
      const _this = this;
      _this.loadStartMonth();
      _this.listDeptSchoolForSelect();
    },
    methods: {
      searchFunc() {
        const _this = this;
        console.log(JSON.stringify(_this.query));
        _this.loading = true;
        _this.httpUtils.appPost('/report/yearReport', _this.query).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.titleArr = res.data.title;
            _this.list = res.data.content;
            _this.loading = false;
          } else {
            _this.baseErrorNotify(res.msg);
            _this.loading = false;
          }
        }, _this.operateFail);
      },
      // 加载起始月份
      loadStartMonth() {
        const _this = this;
        _this.httpUtils.appGet('/config/getDateByName?name=app.reportSchoolYearBeginTime').then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.query.startMonth = res.data;
          } else {
            _this.baseErrorNotify(res.msg);

          }
        }, _this.operateFail);
      },

      listDeptSchoolForSelect() {
        const _this = this;
        _this.httpUtils.appPost('/schoolZone/listSchoolZone').then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.deptSchoolForSelect = res.data;
          } else {
            _this.baseErrorNotify(res.msg);

          }
        }, _this.operateFail);
      },
      deptSchoolIdChange(val) {
        const _this = this;
        this.query.deptSchoolIds = val;
      },
      //
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r.msg);
        _this.loading = false;
      },
      formatLabel(index) {
        const _this = this;
        if (parseInt(index) === 12) {
          return "总计";
        }
        if (_this.titleArr && _this.titleArr.length > 0 && _this.titleArr.length >= index) {
          return _this.baseFormatMonth(_this.titleArr[index]);
        }

        // return _this.baseFormatMonth(new Date().getTime());
        return "-";
      }
    }
  }
</script>
