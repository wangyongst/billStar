<template>
  <div>
    <el-row>
      <el-col :span="24" style="height: 20px"></el-col>
    </el-row>
    <el-row>
      <el-col :span="6" align="left">
        <el-button size="large" type="primary" plain icon="el-icon-back" @click="backToWork">返回工作台</el-button>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24" style="height: 20px"></el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: 'BackToWork',
    methods: {
      backToWork() {
        const _this = this;
        _this.$router.push({path: "/home"});
      }
    }
  }
</script>
