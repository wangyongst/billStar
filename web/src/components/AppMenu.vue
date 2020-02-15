<template>
  <el-menu class="collapse-menu" :collapse="isCollapse">
    <!--    -->
    <el-menu-item class="" index="home" @click="toPage('home')">
      <i class="el-icon-user"></i>
      <span slot="title">我的信息</span>
    </el-menu-item>
    <!--    基础配置 组-->
    <el-submenu index="basic">
      <template slot="title">
        <i class="el-icon-setting"></i>
        <span>基础配置</span>
      </template>
      <el-menu-item class="normalHide" index="basic-schoolZones" @click="toPage('schoolZones')">
        <span slot="title">校区管理</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-semesters" @click="toPage('semesters')">
        <span slot="title">学期信息</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-subjects" @click="toPage('subjects')">
        <span slot="title">科目信息</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-classTypes" @click="toPage('classTypes')">
        <span slot="title">班级管理</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-teachers" @click="toPage('teachers')">
        <span slot="title">员工管理</span>
      </el-menu-item>

      <el-menu-item class="normalHide" index="basic-chargeTypes" @click="toPage('chargeTypes')">
        <span slot="title">收费类型</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-configs" @click="toPage('configs')">
        <span slot="title">配置信息</span>
      </el-menu-item>
    </el-submenu>

    <!--营业报表 -->
    <el-submenu index="report">
      <template slot="title">
        <i class="el-icon-s-marketing"></i>
        <span>营业报表</span>
      </template>
      <el-menu-item index="report-yearReport" @click="toPage('yearReport')">
        <span slot="title">年月报表</span>
      </el-menu-item>
      <el-menu-item index="report-payTypeReport" @click="toPage('payTypeReport')">
        <span slot="title">支付类型报表</span>
      </el-menu-item>
    </el-submenu>

    <!--    -->
    <el-menu-item index="courses" @click="toPage('courses')">
      <i class="el-icon-s-grid"></i>
      <span slot="title">所有课程</span>
    </el-menu-item>
    <el-menu-item index="students" @click="toPage('students')">
      <i class="el-icon-s-custom"></i>
      <span slot="title">所有学生</span>
    </el-menu-item>
    <el-menu-item index="bills" @click="toPage('bills')">
      <i class="el-icon-notebook-2"></i>
      <span slot="title">所有收费</span>
    </el-menu-item>
    <el-menu-item index="studentCourses" @click="toPage('studentCourses')">
      <i class="el-icon-menu"></i>
      <span slot="title">学生课程</span>
    </el-menu-item>
    <el-menu-item index="subjectStudentReport" @click="toPage('subjectStudentReport')">
      <i class="el-icon-data-line"></i>
      <span slot="title">科目学生</span>
    </el-menu-item>
    <!--    -->
  </el-menu>
</template>

<script>
  export default {
    name: 'AppMenu',
    props: {
      isCollapse: {
        required: true,
        default: false,
      }
    },
    data() {
      return {}
    },
    methods: {
      toPage(url) {
        const _this = this;
        url = "/" + url;
        console.log("url=" + url);
        _this.$router.push({path: url});
      }
    }
  }
</script>

<style scoped>

</style>
