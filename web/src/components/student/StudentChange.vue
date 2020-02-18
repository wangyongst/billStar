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

        <el-divider content-position="left">原班信息</el-divider>

        <el-form-item class="inlineFormItem  " size="mini" align="right">
          <el-col :span="24">
            <el-table>
              <el-table-column label="" type="index" width="40" align="center">
              </el-table-column>
              <el-table-column label="班别" width="150" align="center">
              </el-table-column>
              <el-table-column label="时间" width="" align="center">
              </el-table-column>
              <el-table-column label="教师" width="60" align="center">
              </el-table-column>
            </el-table>
          </el-col>
        </el-form-item>

        <el-divider content-position="left">转班信息</el-divider>

        <el-form-item class="inlineFormItem  " size="mini" align="right">
          <el-col :span="24">
            <el-table>
              <el-table-column label="" type="index" width="40" align="center">
              </el-table-column>
              <el-table-column label="班别" width="150" align="center">
              </el-table-column>
              <el-table-column label="时间" width="" align="center">
              </el-table-column>
              <el-table-column label="教师" width="60" align="center">
              </el-table-column>
            </el-table>
          </el-col>
        </el-form-item>

        <el-form-item style="text-align: center" class="not-inline width100p">
          <el-button type="primary" class="width150" icon="el-icon-upload">确定转班</el-button>
        </el-form-item>

      </el-form>
    </el-row>
  </div>
</template>

<script>

  import BackToWork from "../back/BackToWork";

  export default {
    name: 'StudentChange',
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
