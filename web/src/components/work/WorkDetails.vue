<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">

          <SchoolSelect @dataChange="schoolChange"></SchoolSelect>

          <el-form-item label="开票区间：" size="mini">
            <el-radio-group v-model="query.time">
              <el-radio size="mini" label="1">今天</el-radio>
              <el-radio size="mini" label="2">本周</el-radio>
              <el-radio size="mini" label="3">本月</el-radio>
              <el-radio size="mini" label="4">上周</el-radio>
              <el-radio size="mini" label="5">上月</el-radio>
              <el-radio size="mini" label="6">选择区间</el-radio>
            </el-radio-group>
          </el-form-item>


          <StudentTypeSelect @dataChange="typeChange"></StudentTypeSelect>


          <el-row>
            <el-col :span="6" :offset="16">
              <el-button @click="listStudentCharge" type="primary" style="width: 100px;" size="mini" plain round>查询</el-button>
            </el-col>
          </el-row>
        </el-form>


      </el-col>
      <!--    数据表格-->
      <el-col :span="24">
        <el-table v-loading="loading" :data="page.records" class="bill-table" size="" style="width: 100%">
          <el-table-column label="" type="index" width="40" align="center">
          </el-table-column>

          <el-table-column label="类型" width="150" align="left" prop="type" :formatter="typeFomatter">
          </el-table-column>

          <el-table-column label="收费方式" width="150" align="left" prop="chargeName">
          </el-table-column>

          <el-table-column label="校区" width="150" align="left" prop="schoolName">
          </el-table-column>

          <el-table-column label="姓名" width="150" align="left" prop="studentName">
          </el-table-column>

          <el-table-column label="金额" width="150" align="left" prop="amount">
          </el-table-column>

          <el-table-column label="开票人" width="150" align="left" prop="createName">
          </el-table-column>

          <el-table-column label="开票时间" width="180" align="left" prop="createTime" :formatter="baseTableFormatTime">
          </el-table-column>

        </el-table>
      </el-col>

      <el-col :span="24">
        <el-pagination
          class="common-page"
          background
          layout="total,prev, pager, next"
          :total="page.total"
          :page-size="query.size"
          :current-page="query.current"
          @current-change="gotoPage"
          @prev-click="gotoPage"
          @next-click="gotoPage"
        ></el-pagination>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import ClassSelect from "../select/ClassSelect";
  import SchoolSelect from "../select/SchoolSelect";
  import SubjectSelect from "../select/SubjectSelect";
  import BackToWork from "../back/BackToWork";
  import StudentTypeSelect from "../select/StudentTypeSelect";

  export default {
    name: 'WorkDetails',
    components: {StudentTypeSelect, BackToWork, ClassSelect, SchoolSelect, SubjectSelect},
    data() {
      return {
        page: {
          total: 0,
          records: [],
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: [],
          time: null,
          begin: null,
          end: null,
          types: null
        },
        loading: false,
      }

    },

    mounted: function () {
      const _this = this;
      _this.query.current = 1;
      _this.listStudentCharge();
    },

    methods: {
      listStudentCharge() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/charge/pageV', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.records = res.records;
          _this.page.total = res.total;
        }, _this.operateFail);
      },
      schoolChange(val) {
        this.query.schoolIds = val;
      },
      typeChange(val) {
        this.query.types = val;
      },
      gotoPage(page) {
        const _this = this;
        _this.query.current = page;
        _this.listStudentCharge();
      },
      typeFomatter(row, col, val) {
        if(val == 1) return "新生";
        if(val == 2) return "续费";
        if(val == 3) return "补费";
      },
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r);
        _this.loading = false;
      }
    }
  }
</script>

<style scoped>

</style>
