<template>
  <el-dialog v-bind:title="title" :visible.sync="visible" @close="doClose" class="report-detail">
    <el-container style="width: 100%">
      <el-table v-loading="loading" :data="list" class="bill-table">
        <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
        <el-table-column label="课程" width="">
          <template slot-scope="scope">
            {{scope.row.course.dictCourseName}}{{scope.row.course.courseIndexName}}
          </template>
        </el-table-column>
        <el-table-column label="老师" prop="course.teacherName" width=""></el-table-column>
        <el-table-column label="开始时间" prop="beginTime" :formatter="baseTableFormatDate" width=""></el-table-column>
        <el-table-column label="过期时间" prop="expireTime" :formatter="baseTableFormatDate" width=""></el-table-column>
        <el-table-column label="升班时间" prop="riseClassTime" :formatter="baseTableFormatDate" width=""></el-table-column>
      </el-table>
    </el-container>

  </el-dialog>
</template>

<script>
  export default {
    name: 'StudentCourseBillsDialogForSC',
    props: {
      dialogVisible: {
        required: false,
        default: false,
      },
      studentCourses: {
        required: true,
      },
      dialogTitle: {
        required: true,
        default: null,
      }
    },
    data() {
      return {
        list: [],
        loading: false,
        studentCourse: null,
        title: '学生课程',
        visible: false,
      }
    },
    mounted: function () {

    },
    watch: {
      dialogVisible(val) {
        if (val) {
          this.visible = true;
          this.list = this.studentCourses;
          this.title = this.dialogTitle;
        }
      }
    },

    methods: {
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
