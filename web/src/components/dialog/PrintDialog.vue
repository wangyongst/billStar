<template>
  <el-dialog :close-on-click-modal="false"
             class="bill-dialog bill-cmd-dialog"
             :visible.sync="dialogVisible"
             v-loading="loading"
             size="mini">
    <el-form class="demo-form-inline" :inline="true" label-width="70px" size="mini">

      <el-form-item class="inlineFormItem  " size="mini" align="center">
        <!--    数据表格-->
        <el-col :span="24">
          <el-table v-loading="loading" :data="list" class="bill-table" size="" style="width: 100%">
            <el-table-column label="课程" width="300" align="left" prop="courseId" :formatter="baseFormatCourse">
            </el-table-column>

            <el-table-column label="到期时间" width="150" align="left" prop="expireTime" :formatter="baseTableFormatDate">
            </el-table-column>

            <el-table-column label="教师" width="150" align="left" prop="teacherName">
            </el-table-column>
          </el-table>
        </el-col>
      </el-form-item>

    </el-form>

  </el-dialog>
</template>

<script>
  export default {
    name: 'PrintDialog',
    data() {
      return {
        courseLoading: false,
        dialogVisible: false,
        loading: false,
        dialogTitle: "学习课程",
        labelWidth: '80px',
        list: []
      }
    },

    mounted: function () {
      const _this = this;
      eventBus.$on('studentCourse', function (val) {
        _this.listStudentCourse(val);
        _this.dialogVisible = true;
      });
    },

    methods: {
      listStudentCourse(val) {
        const _this = this;
        _this.httpUtils.appGet('/student/course/list/' + val).then(function (res) {
          _this.list = res;
          _this.createDialogVisible = false;
        }, _this.operateFail);
      },
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r);
        _this.loading = false;
      }
    }
  }
</script>
