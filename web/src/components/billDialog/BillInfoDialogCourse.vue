<!-- 设计做显示 -->
<template>
  <el-row>
    <el-col v-bind:span="cols.col1" >
      <div class="label">班级班别</div>
    </el-col>
    <el-col v-bind:span="cols.col2" class="pure-text txt100">
      {{this.formatClass(billCourse)}}
    </el-col>
    <el-col v-bind:span="cols.col3">
      <div class="label">上课时间</div>
    </el-col>
    <el-col v-bind:span="cols.col4" class="pure-text txt100">
      {{this.billCourse.regularTime}}
    </el-col>
    <el-col v-bind:span="cols.col5">
      <div class="label">教室号</div>
    </el-col>
    <el-col v-bind:span="cols.col6" class="pure-text txt100">
      {{this.billCourse.classroomNo}}
    </el-col>
    <el-col v-bind:span="cols.col7">
      <div class="label">过期时间</div>
    </el-col>
    <el-col v-bind:span="cols.col8" class="pure-text txt100">
      {{this.baseFormatDate(billCourse.expireTime)}}
    </el-col>
  </el-row>

</template>

<script>
  export default {
    name: 'DialogBill',
    props: {
      billCourse: {
        required: true,
        default: function () {
          return {}
        }
      },
      cols: {
        required: true,
      }
    },

    data() {
    },

    mounted: function () {
    },

  }
</script>

<style scoped>

</style>
