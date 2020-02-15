<template>
  <el-menu class="collapse-menu" :collapse="isCollapse">
    <!--    -->
    <el-menu-item class="" index="home" @click="toPage('home')">
      <i class="el-icon-user"></i>
      <span slot="title">我的信息</span>
    </el-menu-item>

    <!--    工作台 组-->
    <el-submenu index="home">
      <template slot="title">
        <i class="el-icon-setting"></i>
        <span>工作台</span>
      </template>
<!--      <el-menu-item class="normalHide" index="basic-schoolZones" @click="toPage('schoolZones')">-->
<!--        <span slot="title">工作台</span>-->
<!--      </el-menu-item>-->
      <el-menu-item class="normalHide" index="basic-schoolZones" @click="toPage('schoolZones')">
        <span slot="title">老生</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-schoolZones" @click="toPage('schoolZones')">
        <span slot="title">新生</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-semesters" @click="toPage('semesters')">
        <span slot="title">明细</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-subjects" @click="toPage('subjects')">
        <span slot="title">欠费</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-classTypes" @click="toPage('classTypes')">
        <span slot="title">续费</span>
      </el-menu-item>
    </el-submenu>

    <!--    查询 组-->
    <el-submenu index="search">
      <template slot="title">
        <i class="el-icon-setting"></i>
        <span>查询</span>
      </template>
      <el-menu-item class="normalHide" index="basic-schoolZones" @click="toPage('schoolZones')">
        <span slot="title">到期</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-schoolZones" @click="toPage('schoolZones')">
        <span slot="title">超期</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-schoolZones" @click="toPage('schoolZones')">
        <span slot="title">流失</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-semesters" @click="toPage('semesters')">
        <span slot="title">退费</span>
      </el-menu-item>
    </el-submenu>


    <!--    学生管理 组-->
    <el-submenu index="student">
      <template slot="title">
        <i class="el-icon-setting"></i>
        <span>学生管理</span>
      </template>
      <el-menu-item class="normalHide" index="basic-schoolZones" @click="toPage('schoolZones')">
        <span slot="title">转班</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-schoolZones" @click="toPage('schoolZones')">
        <span slot="title">转校</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-semesters" @click="toPage('semesters')">
        <span slot="title">休学办理</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-semesters" @click="toPage('semesters')">
        <span slot="title">复学办理</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-semesters" @click="toPage('semesters')">
        <span slot="title">转期</span>
      </el-menu-item>
    </el-submenu>


    <!--报表中心 组 -->
    <el-submenu index="report">
      <template slot="title">
        <i class="el-icon-s-marketing"></i>
        <span>报表中心</span>
      </template>
      <el-menu-item index="report-payTypeReport" @click="toPage('payTypeReport')">
        <span slot="title">年月报表</span>
      </el-menu-item>
      <el-menu-item index="report-yearReport" @click="toPage('yearReport')">
        <span slot="title">支付类型</span>
      </el-menu-item>
      <el-menu-item index="report-payTypeReport" @click="toPage('payTypeReport')">
        <span slot="title">在校生</span>
      </el-menu-item>
    </el-submenu>

    <!--课程表 -->
    <el-menu-item class="" index="home" @click="toPage('home')">
      <i class="el-icon-user"></i>
      <span slot="title">课程表</span>
    </el-menu-item>

    <!--    基础设置 组-->
    <el-submenu index="basic">
      <template slot="title">
        <i class="el-icon-setting"></i>
        <span>基础设置</span>
      </template>
      <el-menu-item class="normalHide" index="basic-schoolZones" @click="toPage('schoolZones')">
        <span slot="title">校区管理</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-teachers" @click="toPage('teachers')">
        <span slot="title">员工管理</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-semesters" @click="toPage('semesters')">
        <span slot="title">学期信息</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-chargeTypes" @click="toPage('chargeTypes')">
        <span slot="title">收费类型</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-configs" @click="toPage('configs')">
        <span slot="title">功能设置</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-subjects" @click="toPage('subjects')">
        <span slot="title">科目信息</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-classTypes" @click="toPage('classTypes')">
        <span slot="title">学期设置</span>
      </el-menu-item>
      <el-menu-item class="normalHide" index="basic-classTypes" @click="toPage('classTypes')">
        <span slot="title">班级设置</span>
      </el-menu-item>
    </el-submenu>

    <!--    -->
<!--    <el-menu-item index="courses" @click="toPage('courses')">-->
<!--      <i class="el-icon-s-grid"></i>-->
<!--      <span slot="title">所有课程</span>-->
<!--    </el-menu-item>-->
<!--    <el-menu-item index="students" @click="toPage('students')">-->
<!--      <i class="el-icon-s-custom"></i>-->
<!--      <span slot="title">所有学生</span>-->
<!--    </el-menu-item>-->
<!--    <el-menu-item index="bills" @click="toPage('bills')">-->
<!--      <i class="el-icon-notebook-2"></i>-->
<!--      <span slot="title">所有收费</span>-->
<!--    </el-menu-item>-->
<!--    <el-menu-item index="studentCourses" @click="toPage('studentCourses')">-->
<!--      <i class="el-icon-menu"></i>-->
<!--      <span slot="title">学生课程</span>-->
<!--    </el-menu-item>-->
<!--    <el-menu-item index="subjectStudentReport" @click="toPage('subjectStudentReport')">-->
<!--      <i class="el-icon-data-line"></i>-->
<!--      <span slot="title">科目学生</span>-->
<!--    </el-menu-item>-->
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
