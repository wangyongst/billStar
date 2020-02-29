<template>
  <el-container id="body">

    <el-header class="body">
      支付方式-校区汇总
    </el-header>

    <el-form label-width="100px" class="search-form">
      <SchoolSelect></SchoolSelect>
      <el-form-item label="时间选择：">
      <el-date-picker
        size="mini"
        v-model="dateValue"
        type="daterange"
        align="right"
        unlink-panels
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :picker-options="pickerOptions">
      </el-date-picker>
      <el-button class="btn-search" @click="searchFunc" size="mini">查询</el-button>
    </el-form-item>
    </el-form>

    <el-container style="width: 100%">
      <el-table stripe v-loading="loading" :data="list" Charge="bill-table">
        <el-table-column label="校区" prop="deptSchoolName" width="80" align="center"></el-table-column>
        <!--        动态列-->
        <template v-for="item in dynamicColumn">
          <el-table-column v-bind:label="item.label"
                           v-bind:prop="item.prop" width="" align="center">
            <template slot-scope="scope">
              <template v-if="!scope.row.isTotal">
                <el-button @click="showDetails(scope.row,item.prop,item.label)" type="text">
                  {{scope.row.payTypeSaleMap[item.prop]}}
                </el-button>
              </template>
              <template v-else>
                {{scope.row.payTypeSaleMap[item.prop]}}
              </template>
            </template>
            {{list[item.prop]}}
          </el-table-column>
        </template>
        <el-table-column label="总计" prop="deptTotal" width="" align="center"></el-table-column>
      </el-table>
    </el-container>
  </el-container>

</template>

<script>

  import SchoolSelect from "../select/SchoolSelect";

  export default {
    name: 'ReportCharge',
    components: {SchoolSelect},
    data() {
      return {
        list: [],
        query: {
          deptSchoolIds: [],
          beginDate: null,
          endDate: null
        },
        detailParam: {
          deptSchoolIds: [],
          startDate: null,
          endDate: null,
          payTypeId: null,
        },
        dynamicColumn: [],
        deptSchoolForSelect: [],
        loading: false,
        dateValue: [],
        detailVisible: false,
        // 时间选择

        pickerOptions: {
          shortcuts: [
            {
              text: '今天',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                picker.$emit('pick', [start, end]);
              }
            }, {
              text: '昨天',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24);
                end.setTime(end.getTime() - 3600 * 1000 * 24);
                picker.$emit('pick', [start, end]);
              }
            },
            {
              text: '最近一周',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                picker.$emit('pick', [start, end]);
              }
            }, {
              text: '最近一个月',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                picker.$emit('pick', [start, end]);
              }
            }, {
              text: '最近三个月',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                picker.$emit('pick', [start, end]);
              }
            }]
        },
      }


    },
    mounted: function () {

    },
    methods: {
      showDetails(row, payTypeId, payTypeName) {
        const _this = this;
        _this.detailParam.startDate = _this.query.beginDate;
        _this.detailParam.endDate = _this.query.endDate;
        _this.detailParam.deptSchoolIds = [row.deptSchoolId];
        _this.detailParam.payTypeId = payTypeId;
        _this.detailParam.payTypeName = payTypeName;
        _this.detailParam.deptSchoolName = row.deptSchoolName;
        _this.detailVisible = true;
      },
      detailClose() {
        this.detailVisible = false;
      },

      searchFunc() {
        const _this = this;
        if (!_this.dateValue || _this.dateValue.length < 2) {
          _this.baseErrorNotify("请先选择时间");
          return;
        }
        _this.query.beginDate = _this.dateValue[0];
        _this.query.endDate = _this.dateValue[1];
        console.log(JSON.stringify(_this.query));
        _this.loading = true;
        _this.httpUtils.appPost('/report/payTypeReport', _this.query).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.dynamicColumn = res.data.dynamicTitle;
            _this.list = res.data.content;
            _this.loading = false;
          } else {
            _this.baseErrorNotify(res.msg);
            _this.loading = false;
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

    }
  }
</script>

<style scoped>

</style>
