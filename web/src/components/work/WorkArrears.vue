<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">

          <SchoolSelect @dataChange="schoolChange"></SchoolSelect>
          <el-row>
            <el-col :span="4" :offset="1">
              <span style="font-family: 黑体">欠费总金额：{{page.arrears}}</span>
            </el-col>
            <el-col :span="6" :offset="10">
              <el-button @click="listArreas" type="primary" size="mini" plain round>查询</el-button>
            </el-col>
          </el-row>
        </el-form>

      </el-col>
      <!--    数据表格-->
      <el-col :span="24">
        <el-table v-loading="loading" :data="page.records" class="bill-table" size="" style="width: 100%">
          <el-table-column label="" type="index" width="40" align="center">
          </el-table-column>
          <el-table-column label="校区" width="150" align="left" prop="schoolName">
          </el-table-column>

          <el-table-column label="姓名" width="150" align="left" prop="name">
          </el-table-column>

          <el-table-column label="欠费金额" width="150" align="left" prop="arrears">
          </el-table-column>

          <el-table-column fixed="right" label="操作" align="left" width="300">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="showStudentCourse(scope.row.id)">课程详情</el-button>
              <el-button type="text" size="mini" @click="showStudentCharge(scope.row.id)">交费记录</el-button>
              <el-button type="text" size="mini" @click="updateItem(scope.row.id)">补费</el-button>
              <el-button type="text" size="mini" @click="updateArrears(scope.row.id)">修改金额</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>

      <el-dialog class="bill-dialog" title="补费"
                 :visible.sync="createDialogVisible">
        <el-form :model="charge">
          <el-form-item label="收费类型">
            <el-select v-model="charge.chargeId" placeholder="请选择收费类型">
              <el-option v-for="item in chargeSelect" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="金额">
            <el-input v-model="charge.amount" class="width100"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogCancel">取 消</el-button>
          <el-button type="primary" @click="chargePost">确 定</el-button>
        </div>
      </el-dialog>

      <StudentCourseDialog></StudentCourseDialog>
      <StudentChargeDialog></StudentChargeDialog>

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
  import SchoolSelect from "../select/SchoolSelect";
  import BackToWork from "../back/BackToWork";
  import StudentCourseDialog from "../dialog/StudentCourseDialog";
  import StudentChargeDialog from "../dialog/StudentChargeDialog";

  export default {
    name: 'WorkArrears',
    components: {StudentChargeDialog, StudentCourseDialog, BackToWork, SchoolSelect},
    data() {
      return {
        createDialogVisible: false,
        page: {
          total: 0,
          arrears: null,
          records: [],
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: []
        },
        charge: {
          studentId: null,
          chargeId: null,
          amount: null
        },
        chargeSelect: [],
        loading: false
      }
    },

    mounted: function () {
      const _this = this;
      _this.listChargeSelect()
      _this.listArreas();
    },

    methods: {
      showStudentCourse(val) {
        const _this = this;
        eventBus.$emit("studentCourse", val);
      },
      showStudentCharge(val) {
        const _this = this;
        eventBus.$emit("studentCharge", val);
      },
      schoolChange(val) {
        this.query.schoolIds = val;
      },
      updateArrears(id) {
        const _this = this;
        this.$prompt('请输入欠费金额', '修改', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.updateArrearsPost(id, value))
          .catch(() => {
          });
      },

      updateItem(item) {
        const _this = this;
        _this.charge = {studentId: item};
        _this.createDialogVisible = true;
      },

      dialogCancel() {
        this.createDialogVisible = false;
      },
      chargePost(sid) {
        const _this = this;
        _this.httpUtils.appPost('/student/main/buCharge', _this.charge).then(function (res) {
          _this.listArreas();
          _this.createDialogVisible = false;
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },

      listChargeSelect() {
        const _this = this;
        _this.httpUtils.appGet('/sys/charge/list').then(function (res) {
          _this.chargeSelect = res;
        }, _this.operateFail);
      },

      updateArrearsPost(sid, value) {
        const _this = this;
        const cmd = {id: sid, arrears: value};
        _this.httpUtils.appPost('/student/main/updateArrears', cmd).then(function (res) {
          _this.listArreas();
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },

      listArreas() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/main/pageArrear', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.records = res.page.records;
          _this.page.total = res.page.total;
          _this.page.arrears = res.object;
        }, _this.operateFail);
      },
      gotoPage(page) {
        const _this = this;
        _this.query.current = page;
        _this.listCourse();
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
