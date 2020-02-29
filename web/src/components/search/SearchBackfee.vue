<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">

          <SchoolSelect></SchoolSelect>

          <el-form-item label="退费时间：" size="mini">
            <el-radio-group>
              <el-radio size="mini">
                最近&nbsp;&nbsp;
                <el-input size="mini" placeholder="月数" style="width: 20px"></el-input>
                &nbsp;&nbsp;&nbsp;&nbsp;月
              </el-radio>
            </el-radio-group>
          </el-form-item>

          <TeacherSelect></TeacherSelect>

        </el-form>

      </el-col>
      <!--    数据表格-->
      <el-col :span="24">
        <el-table v-loading="loading" :data="page.list" class="bill-table" size="" style="width: 100%">
          <el-table-column label="" type="index" width="40" align="center">
          </el-table-column>

          <el-table-column label="校区" width="150" align="left">
          </el-table-column>

          <el-table-column label="原因" width="150" align="left">
          </el-table-column>

          <el-table-column label="最后课程" width="150" align="left">
          </el-table-column>

          <el-table-column label="姓名" width="150" align="left">
          </el-table-column>

          <el-table-column label="金额" width="150" align="left">
          </el-table-column>

          <el-table-column label="教师" width="150" align="left">
          </el-table-column>

          <el-table-column label="退费时间" width="150" align="left">
          </el-table-column>

          <el-table-column label="备注" width="150" align="left">
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
    </el-row>
  </div>
</template>

<script>
  import ClassSelect from "../select/ClassSelect";
  import SchoolSelect from "../select/SchoolSelect";
  import SubjectSelect from "../select/SubjectSelect";
  import BackToWork from "../back/BackToWork";
  import TeacherSelect from "../select/TeacherSelect";

  export default {
    name: 'SearchBackfee',
    components: {TeacherSelect, BackToWork, ClassSelect, SchoolSelect, SubjectSelect},
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
      },
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
