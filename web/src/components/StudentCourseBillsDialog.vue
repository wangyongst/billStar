<template>
  <el-dialog v-bind:title="title" :visible.sync="visible" @close="doClose" class="report-detail">
    <el-container style="width: 100%">
      <el-table v-loading="loading" :data="list" class="bill-table">
        <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
        <el-table-column label="票据编号" prop="billNo" width="150"></el-table-column>
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
        <el-table-column label="开票时间" prop="billTime" :formatter="baseTableFormatTime" width="100"
                         align="center"></el-table-column>
      </el-table>
    </el-container>

  </el-dialog>
</template>

<script>
  export default {
    name: 'StudentCourseBillsDialog',
    props: {
      dialogVisible: {
        required: false,
        default: false,
      },
      studentCourseId: {
        required: true,
        default: null,
      }
    },
    data() {
      return {
        list: [],
        loading: false,
        studentCourse: null,
        title: '校区/学生 / 课程/老师',
        visible: false,
      }
    },
    mounted: function () {

    },
    watch: {
      studentCourseId(val) {
        if (val && parseInt(val) > 0) {
          this.studentCourseId = val;
          this.getStudentCourseVO();
          this.listBills();
        }
      },
      dialogVisible(val) {
        if (val) {
          this.visible = true;
        }
      }

    },

    methods: {
      generateTitle() {
        this.title = this.studentCourse.deptSchoolName
          + "/" + this.studentCourse.student.name
          + " / " + this.studentCourse.course.dictCourseName
          + this.studentCourse.course.courseIndexName
          + "/" + this.studentCourse.course.teacherName;
      },

      getStudentCourseVO() {
        const _this = this;
        _this.httpUtils.appGet('/studentCourse/get?id=' + _this.studentCourseId).then(function (res) {
          _this.studentCourse = res.data;
          _this.generateTitle();
        }, _this.operateFail);
      },

      listBills() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/bill/listByStudentCourseId?studentCourseId=' + _this.studentCourseId).then(function (res) {
          _this.loading = false;
          _this.list = res.data;
        }, _this.operateFail);
      },
      doClose() {
        this.$emit('dialogClose');//子组件对openStatus修改后向父组件发送事件通知
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
