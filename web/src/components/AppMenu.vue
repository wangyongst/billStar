<template>
  <el-menu class="collapse-menu">

    <el-menu-item class="" index="home" @click="toPage('home')">
      <i class="el-icon-monitor"></i>
      <span slot="title">工作台</span>
    </el-menu-item>


    <el-submenu index="home">
      <template slot="title">
        <i class="el-icon-monitor"></i>
        <span slot="title">收费</span>
      </template>
      <el-menu-item class="normalHide" @click="toPage('work/details')">
        <span slot="title">收费明细</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('work/arrears')">
        <span slot="title">欠费</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('work/xu')">
        <span slot="title">续费</span>
      </el-menu-item>
    </el-submenu>

    <!--    查询 组-->
    <el-submenu index="search">
      <template slot="title">
        <i class="el-icon-search"></i>
        <span slot="title">查询</span>
      </template>
      <el-menu-item class="normalHide" @click="toPage('search/expire')">
        <span slot="title">到期</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('search/overtime')">
        <span slot="title">超期</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('search/lose')">
        <span slot="title">流失</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('search/backfee')">
        <span slot="title">退费</span>
      </el-menu-item>
    </el-submenu>


    <!--    学生管理 组-->
    <el-submenu index="student">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">学生管理</span>
      </template>
      <el-menu-item class="normalHide" @click="toPage('student/change')">
        <span slot="title">转班</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('student/xiu')">
        <span slot="title">休学办理</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('student/fu')">
        <span slot="title">复学办理</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('student/transfer')">
        <span slot="title">转期</span>
      </el-menu-item>
    </el-submenu>


    <!--报表中心 组 -->
    <el-submenu index="report">
      <template slot="title">
        <i class="el-icon-s-marketing"></i>
        <span slot="title">报表中心</span>
      </template>
      <el-menu-item class="normalHide" @click="toPage('report/year')">
        <span slot="title">年月报表</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('report/charge')">
        <span slot="title">支付类型</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('report/student')">
        <span slot="title">在校生</span>
      </el-menu-item>
    </el-submenu>

    <!--课程表 -->
    <el-submenu index="course">
      <template slot="title">
        <i class="el-icon-setting"></i>
        <span slot="title">课程表</span>
      </template>
      <el-menu-item class="normalHide" @click="toPage('course/table')">
        <span slot="title">课程表</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('course/manager')">
        <span slot="title">课程管理</span>
      </el-menu-item>
    </el-submenu>

    <!--    基础设置 组-->
    <el-submenu index="basic">
      <template slot="title">
        <i class="el-icon-setting"></i>
        <span slot="title">基础设置</span>
      </template>
      <el-menu-item class="normalHide" @click="toPage('setting/school')">
        <span slot="title">校区管理</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('setting/teacher')">
        <span slot="title">员工管理</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('setting/charge')">
        <span slot="title">收费类型</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('setting/config')">
        <span slot="title">功能设置</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('setting/subject')">
        <span slot="title">科目设置</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('setting/semester')">
        <span slot="title">学期设置</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('setting/class')">
        <span slot="title">班级设置</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('setting/class/no')">
        <span slot="title">班别设置</span>
      </el-menu-item>
      <el-menu-item class="normalHide" @click="toPage('setting/class/room')">
        <span slot="title">教室设置</span>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
  export default {
    name: 'AppMenu',
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
