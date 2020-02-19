<template>
  <el-dialog title="报表明细" :visible.sync="visible" @close="doClose" class="report-detail">
    <p class="txt100" style="padding-left:10px;color: rgba(122,122,122,0.96)">{{paramIn.deptSchoolName}} /
      {{paramIn.payTypeName}}
      时间：{{baseFormatDate(paramIn.startDate)}}至{{baseFormatDate(paramIn.endDate)}}</p>
    <el-table v-loading="loading" :data="page.list" class="bill-table" style="width: 100%">
      <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
      <el-table-column prop="deptSchoolName" label="校区" width=""></el-table-column>
      <el-table-column prop="type" label="类型" :formatter="commonUtils.appTableBillType" width="50"></el-table-column>
      <el-table-column prop="payTypeName" label="支付方式" width=""></el-table-column>
      <el-table-column label="金额" width="">
        <template slot-scope="scope">
          {{scope.row.amount}}
        </template>
      </el-table-column>
      <el-table-column prop="billTime" label="开票时间" :formatter="baseTableFormatTime"
                       width="100" align="center"></el-table-column>
      <el-table-column prop="student.name" label="学生" width=""></el-table-column>
      <el-table-column prop="student.mobile" label="学生电话" width="120"></el-table-column>

      <el-table-column prop="" label="班级1" :formatter="formatClass1" width=""></el-table-column>
      <el-table-column prop="courses[0].teacherName" label="教师" width=""></el-table-column>
      <el-table-column prop="courses[0].expireTime" align="center" label="到期时间" :formatter="baseTableFormatTime"
                       width="100"></el-table-column>

      <el-table-column prop="" label="班级2" :formatter="formatClass2" width=""></el-table-column>
      <el-table-column prop="courses[1].teacherName" label="教师" width=""></el-table-column>
      <el-table-column prop="courses[1].expireTime" label="到期时间" :formatter="baseTableFormatTime"
                       width="100" align="center"></el-table-column>
    </el-table>
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

  </el-dialog>

</template>

<script>

  export default {
    name: 'NewPayType',
    props: {
      visible: {
        required: false,
        default: false,
      },
      paramIn: {
        required: false,
        default: function () {
          return {
            deptSchoolId: null,
            startDate: null,
            endDate: null,
            payTypeId: null,
          }
        }
      }
    },
    data() {
      return {
        list: [],
        page: {
          total: 0,
          list: [],
        },
        query: {
          pageNo: 1,
          pageSize: 10,
          orderBy: 'create_date DESC',
          data: {
            deptSchoolId: null,
            deptSchoolName: null,
            startDate: null,
            endDate: null,
            payTypeId: null,
            payTypeName: null,
          }
        },
        loading: false,
      }
    },
    mounted: function () {

    },
    watch: {
      visible(val) {
        const _this = this;
        if (val) {
          _this.searchFunc();
        }
      }
    },
    methods: {
      searchFunc() {
        const _this = this;
        _this.loading = true;
        _this.query.data = _this.paramIn;
        _this.query.data.payTypeId = _this.fetchPayTypeID(_this.paramIn.payTypeId);
        _this.httpUtils.appPost('/bill/listPage', _this.query).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.page.list = res.data.list;
            _this.page.total = res.data.total;
            _this.loading = false;
          } else {
            _this.baseErrorNotify(res.msg);
            _this.loading = false;
          }
        }, _this.operateFail);
      },
      fetchPayTypeID(val) {
        const arr = val.split("_");
        if (arr && arr.length > 1) {
          return arr[1];
        }
        return val;
      },
      //
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r.msg);
        _this.loading = false;
      },
      doClose() {
        this.$emit("detailClose");
      },
      formatClass1(row, col, v) {
        if (row.courses && row.courses.length >= 1) {
          return this.formatClass(row.courses[0]);
        }
      },

      formatClass2(row, col, v) {
        if (row.courses && row.courses.length >= 2) {
          return this.formatClass(row.courses[1]);
        }
      },
      // 尝试分页
      currentPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.searchFunc();
      }
      ,
      prevPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.searchFunc();
      }
      ,
      nextPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.searchFunc();
      }
      ,

    }
  }
</script>

<style scoped>

</style>
