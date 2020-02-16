<template>
  <el-container id="body" v-bind:class="noMenuTag">
    <el-header class="body">
      <template v-if="user !== null">个人信息</template>
      <template v-else>404</template>
    </el-header>
    <!--    用户信息-->
    <el-row id="user-info">

      <template v-if="user !== null">
        <el-col :span="6" style="text-align: right;padding:10px">
          <el-avatar :size="100" :src="user.avatar"></el-avatar>
        </el-col>
        <el-col :span="18" id="info">
          <el-form label-width="120px">
            <el-form-item label="姓名：">
              <span class="textContent">{{user.name}}</span>
            </el-form-item>
            <el-form-item label="是否管理员：">
              <span class="textContent">{{baseYesNoVal(user.isAppAdmin)}}</span>
            </el-form-item>
            <el-form-item label="是否教师：">
              <span class="textContent">{{baseYesNoVal(user.isTeacher)}}</span>
            </el-form-item>
            <el-form-item label="校区：">
              <span class="textContent">{{formatDept(user.deptSchools)}}</span>
            </el-form-item>
            <el-form-item label="部门：">
              <span class="textContent">{{formatDept(user.depts)}}</span>
            </el-form-item>
          </el-form>
        </el-col>
      </template>
      <template v-else>
        <el-col :span="24">
          <p>未能获取用户信息，请确认在钉钉环境，或者联系管理员</p>
        </el-col>
      </template>
      <el-col>
        <p class="lessImportant">客户端信息:{{JSON.stringify(userAgent)}}</p>
      </el-col>
    </el-row>
    <!--浏览器信息-->
  </el-container>
</template>

<script>
  export default {
    name: 'Home2',
    data() {
      return {
        user: {
          "id": 8,
          "userid": "manager6238",
          "name": "曾璐",
          "mobile": null,
          "position": "CTO",
          "email": "zenglu@qq.com",
          "avatar": "https://static-legacy.dingtalk.com/media/lADPACOG820_4rnNAu7NAu4_750_750.jpg",
          "isAdmin": true,
          "isLeader": false,
          "isTeacher": 1,
          "deptSchoolId": 111515409,
          "deptSchoolName": null,
          "isAppAdmin": 1,
        },
        userAgent: null,
        noMenuTag: ""
      }
    },
    mounted() {
      const _this = this;
      const userString = window.localStorage.getItem("user");
      if (_this.baseIsNotBlank(userString)) {
        _this.user = JSON.parse(userString);
      } else {
        _this.user = null;
        _this.noMenuTag = "noMenu";
      }
      _this.userAgent = navigator.userAgent;
    },
    methods: {
    }
  }
</script>

<style scoped>
  #user-info {
    padding: 20px 0 20px 0;
    background-color: #f0f0f0;
  }

  #user-info #info {
    border-left: 1px solid #cccccc;
  }

  #user-info .el-form-item {
    margin-bottom: 0px !important;
  }

  .textContent {
    float: left;
    font-family: "微软雅黑";
  }

  .lessImportant {
    margin-top: 80px;
    font-size: 0.8em;
  }

</style>
