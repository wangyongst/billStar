<template>
  <el-container id="body">

    <el-header class="body">
      学生老师课程
    </el-header>

    <el-container style="width: 100%">
      <el-table v-loading="loading" :data="list" class="bill-table">
        <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
        <el-table-column label="票据编号" prop="billNo" width=""></el-table-column>
        <el-table-column label="开票类型" prop="type" :formatter="commonUtils.appTableBillType" align=""></el-table-column>
        <el-table-column label="支付方式" prop="payTypeName"></el-table-column>
        <el-table-column label="金额" prop="amount"></el-table-column>
        <el-table-column label="欠费" prop="currentArrears"></el-table-column>
        <el-table-column label="课程/教师/过期时间" width="300">
          <template slot-scope="scope">
            {{formatTableClass1(scope.row)}} / {{scope.row.courses[0].teacherName}} /
            {{baseFormatDate(scope.row.courses[0].expireTime)}}
            <template v-if="scope.row.courses.length > 1">
              <br>{{formatTableClass2(scope.row)}} / {{scope.row.courses[1].teacherName}} /
              {{baseFormatDate(scope.row.courses[1].expireTime)}}
            </template>
          </template>
        </el-table-column>
        <el-table-column label="开票人" prop="billCreatorName" align=""></el-table-column>
        <el-table-column label="开票时间" prop="billTime" :formatter="baseTableFormatTime"></el-table-column>
      </el-table>
    </el-container>

  </el-container>

</template>

<script>
  export default {
    name: 'ClassTypes',
    data() {
      return {
        list: [],
        studentCourseId: null,
        loading: false,
      }
    },
    mounted: function () {
      this.studentCourseId = this.$route.query.studentCourseId;
      if (!this.studentCourseId || parseInt(this.studentCourseId) < 0) {
        alert("当前页面无法取到关键参数studentCourseId，请联系管理员");
      } else {
        this.listBills();
      }
    },
    methods: {
      listBills() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/bill/listByStudentCourseId?studentCourseId=' + _this.studentCourseId).then(function (res) {
          _this.loading = false;
          _this.list = res.data;
        }, _this.operateFail);
      },
      // 操作失败的处理
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
