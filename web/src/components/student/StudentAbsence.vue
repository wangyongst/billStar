<template>
  <div>

    <BackToWork></BackToWork>

    <el-row>

      <el-form class="demo-form-inline" :inline="true" label-width="70px" size="mini">
        <!--      -->
        <el-divider content-position="left">学生信息</el-divider>
        <el-form-item label="学生姓名" size="mini">
          <el-input class="bill-cmd-input"
                    placeholder="学生姓名"></el-input>
        </el-form-item>

        <el-form-item label="电话">
          <el-input class="bill-cmd-input"
                    placeholder="电话"></el-input>
        </el-form-item>

        <el-form-item class="inlineFormItem  " size="mini" align="right">
          <el-button size="mini" type="primary" plain icon="el-icon-check" round>确定</el-button>
        </el-form-item>

        <el-divider content-position="left">休学信息</el-divider>

        <el-form-item label="预计复学时间：" size="mini" label-width="150px" align="left">
          <el-date-picker type="date" placeholder="选择日期">
          </el-date-picker>
        </el-form-item>

        <el-form-item style="text-align: center" class="not-inline width100p">
          <el-button type="primary" class="width150" icon="el-icon-upload">确定休学</el-button>
        </el-form-item>

      </el-form>
    </el-row>
  </div>
</template>

<script>

  import BackToWork from "../back/BackToWork";

  export default {
    name: 'StudentAbsence',
    components: {BackToWork},
    props: {},
    created() {
    },

    data() {
      return {}
    },

    methods: {}

  }
</script>
