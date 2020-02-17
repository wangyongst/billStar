<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">

          <SchoolSelect></SchoolSelect>
          <SubjectSelect></SubjectSelect>
          <ClassSelect></ClassSelect>
          <TeacherSelect></TeacherSelect>

          <el-form-item label="开票区间：" size="mini">
            <el-radio-group>
              <el-radio size="mini" label="今天"></el-radio>
              <el-radio size="mini" label="本周"></el-radio>
              <el-radio size="mini" label="本月"></el-radio>
              <el-radio size="mini" label="上周"></el-radio>
              <el-radio size="mini" label="上月"></el-radio>
              <el-radio size="mini" label="区间"></el-radio>
            </el-radio-group>
          </el-form-item>


          <el-form-item label="选择类型：" size="mini">
            <el-checkbox-group>
              <el-checkbox size="mini" disabled>全选</el-checkbox>
              <el-checkbox size="mini" disabled>补费</el-checkbox>
              <el-checkbox size="mini" disabled>新生</el-checkbox>
              <el-checkbox size="mini" disabled>续费</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>


      </el-col>
      <!--    数据表格-->
      <el-col :span="24">
        <el-table v-loading="loading" :data="page.list" class="bill-table" size="" style="width: 100%">
          <el-table-column label="" type="index" width="40" align="center">
          </el-table-column>

          <el-table-column label="类型" width="150" align="left">
          </el-table-column>

          <el-table-column label="收费方式" width="150" align="left">
          </el-table-column>

          <el-table-column label="校区" width="150" align="left">
          </el-table-column>

          <el-table-column label="姓名" width="150" align="left">
          </el-table-column>

          <el-table-column label="金额" width="150" align="left">
          </el-table-column>

          <el-table-column label="开票人" width="150" align="left">
          </el-table-column>

          <el-table-column label="时间" width="150" align="left">
          </el-table-column>

          <el-table-column label="课程" width="150" align="left">
          </el-table-column>


          <el-table-column fixed="right" label="操作" align="left" width="180">
            <el-button type="text" size="mini" class="">修改</el-button>
          </el-table-column>
        </el-table>
      </el-col>

      <el-col :span="24">
        <el-pagination
          class="common-page"
          background
          layout="total,prev, pager, next"
          :total="page.total"
          :page-size="query.pageSize"
          :current-page="query.pageNo"
          @current-change="currentPage"
          @prev-click="prevPage"
          @next-click="nextPage"
        ></el-pagination>
      </el-col>
      <BillInfoDialogTable :dialogVisible="detailDialogVisible"
                           @dialogClose="detailDialogClose"
                           :bill="bill"/>
    </el-row>
  </div>
</template>

<script>
  import ClassSelect from "./forSelect/ClassSelect";
  import SchoolSelect from "./forSelect/SchoolSelect";
  import SubjectSelect from "./forSelect/SubjectSelect";
  import TeacherSelect from "./forSelect/TeacherSelect";
  import BackToWork from "./back/BackToWork";

  export default {
    name: 'Arrears',
    components: {BackToWork, ClassSelect, SchoolSelect, SubjectSelect, TeacherSelect},
    data() {
      return {
        createDialogVisible: false,
        detailDialogVisible: false,
        page: {
          total: 0,
          list: [],
        },
        query: {
          pageNo: 1,
          pageSize: 10,
          orderBy: 'bill_time DESC',
          data: {
            deptSchoolIds: [],
            expireEndTime: null,
            payTypeId: null,
          }
        },
        loading: false,
        bill: {
          student: {
            name: null,
          },
          courses: []
        },
      }

    },

    mounted: function () {
      const _this = this;
      eventBus.$on("billOperateSuccess", function () {
        _this.listBills();
      })
    },

    methods: {
      searchFunc(queryIn) {
        console.log("response searchFunc");
        this.query = this.deepCopy(queryIn);
        this.listBills();
      },
      deptSchoolIdChange(val) {
        this.query.data.deptSchoolIds = val;
      },
      showBillInfo(item) {
        const _this = this;
        _this.bill = item;
        _this.detailDialogVisible = true;
      }
      ,
      listBills() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/bill/listPage', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.list = res.data.list;
          _this.page.total = res.data.total;
        }, _this.operateFail);
      },
      formatterYesNo(row, column, v) {
        return parseInt(v) === 1 ? "是" : "否";
      },

      // 新增
      create() {
        this.billOperate.createStudent();
      },
      // 续费-弹框
      renewals(row) {
        this.billOperate.renewals(row.id);
      },
      // 补费-弹框
      supplement(row) {
        this.billOperate.supplement(row.id);
      },
      // 退费-弹框
      refund(row) {
        this.billOperate.refund(row.id);
      },
      // 转班-弹框
      transferClass(row) {
        this.billOperate.transferClass(row.id);
      },
      transferSemester(row) {
        this.billOperate.transferSemester(row.id);
      },
      // 转校-弹框
      transferSchool(row) {
        this.billOperate.transferSchool(row.id);
      },
      // 修改-弹框
      updateInfo(row) {
        this.billOperate.modifyInfo(row.id);
      },

      studentSuspend(row) {
        const _this = this;
        const studentId = row.student.id;
        const msg = '确定需要对学生 ' + row.student.name + ' 执行休学操作？';
        _this.baseConfirmDelete(msg, function () {
          _this.studentSuspendPost(studentId);
        });
      }
      ,
      studentSuspendPost(studentId) {
        const _this = this;
        _this.httpUtils.appPost('/student/suspend?id=' + studentId).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
          } else {
            _this.baseErrorNotify(res.msg);

          }
        }, _this.operateFail);
      },
      detailDialogClose() {
        const _this = this;
        _this.detailDialogVisible = false;
      },
      operateSuccess() {
        const _this = this;
        // _this.detailDialogVisible = false;
        _this.listBills();
      },

      // 尝试分页
      currentPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listBills();
      }
      ,
      prevPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listBills();
      }
      ,
      nextPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listBills();
      }
      ,
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(JSON.stringify(r));
        _this.loading = false;
      },
      showArrearsUpdateBtn(bill) {
        console.log(bill.initialArrears);
        const _this = this;
        if (parseInt(bill.type) === _this.$appConfig.billTypes.renewals || parseInt(bill.type) === _this.$appConfig.billTypes.newBill) {
          console.log(parseFloat(bill.initialArrears));
          if (parseFloat(bill.initialArrears) === 0) {
            return true;
          }
        }
        return false;
      },
      updateArrears(id) {
        const _this = this;
        this.$prompt('请输入欠费金额', '欠费金额请写正数', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.updateArrearsPost(id, value))
          .catch(() => {
          });
      },
      updateArrearsPost(id, value) {
        const _this = this;
        const data = {modelId: id, arrears: value};
        _this.httpUtils.appPost('/bill/updateArrears', data).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
            _this.listBills();
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      }

    }
  }
</script>

<style scoped>

</style>
