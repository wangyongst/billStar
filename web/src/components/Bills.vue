<template>
  <el-row>

    <!--查询表单-->
    <el-col :span="24">
      <BillSearchForm @searchFunction="searchFunc" @createClick="create"/>
    </el-col>
    <!--    数据表格-->
    <el-col :span="24">
      <el-table v-loading="loading" :data="page.list" class="bill-table" size="" style="width: 100%">
        <el-table-column label="" type="index" width="40" align="center">
        </el-table-column>
        <el-table-column prop="deptSchoolName" label="校区" width="150" align="left">
          <template slot-scope="scope">
            {{scope.row.deptSchoolName}} / {{commonUtils.appBillType(scope.row.type)}}<br>
            <span style="font-size:0.8em">{{scope.row.billNo}}</span>
          </template>
        </el-table-column>
        <!--        <el-table-column prop="type" label="类型" :formatter="commonUtils.appTableBillType" width="50"></el-table-column>-->
        <!--        <el-table-column prop="payTypeName" label="支付方式" width=""></el-table-column>-->
        <el-table-column label="金额信息" width="" align="center">
          <template slot-scope="scope">
            {{scope.row.payTypeName}}<br>
            <el-button @click="showBillInfo(scope.row)"
                       type="text"
                       size="mini" class="">{{scope.row.amount}}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="" label="欠费" width="60">
          <template slot-scope="scope">
            <template v-if="showArrearsUpdateBtn(scope.row)">
              <el-button @click="updateArrears(scope.row.id)"
                         type="text"
                         size="mini">补录
              </el-button>
            </template>
            <template v-else>{{scope.row.currentArrears}}</template>
          </template>


        </el-table-column>
        <!--        <el-table-column prop="student.name" label="学生" width=""></el-table-column>-->
        <el-table-column label="学生" width="120">
          <template slot-scope="scope">
            {{scope.row.student.name}}<br>
            {{scope.row.student.mobile}}
          </template>
        </el-table-column>
        <!--课程信息-->
        <el-table-column label="课程/教师/开始/升班/过期时间" width="500">
          <template slot-scope="scope">
            {{formatTableClass1(scope.row)}}
            / {{scope.row.courses[0].teacherName}}
            / {{baseFormatDate(scope.row.courses[0].beginTime)}}
            / {{baseFormatDate(scope.row.courses[0].riseClassTime)}}
            / {{baseFormatDate(scope.row.courses[0].expireTime)}}
            <template v-if="scope.row.courses.length > 1">
              <br>{{formatTableClass2(scope.row)}}
              / {{scope.row.courses[1].teacherName}}
              / {{baseFormatDate(scope.row.courses[1].beginTime)}}
              / {{baseFormatDate(scope.row.courses[1].riseClassTime)}}
              / {{baseFormatDate(scope.row.courses[1].expireTime)}}
            </template>
          </template>
        </el-table-column>
        <!-- 开票人-->
        <el-table-column prop="billCreatorName" label="开票人" width=""></el-table-column>
        <el-table-column prop="billTime" label="开票时间" :formatter="baseTableFormatTime"
                         width="100" align="center"></el-table-column>
        <el-table-column fixed="right" label="操作" align="left" width="180">
          <template slot-scope="scope">
            <template v-if="scope.row.type !== 3">
              <el-button @click="renewals(scope.row)" type="text" size="mini" class="">续费</el-button>
              <template v-if="scope.row.currentArrears > 0">
                <el-button @click="supplement(scope.row)" type="text" size="mini" class="">补费</el-button>
              </template>
              <el-button @click="refund(scope.row)" type="text" size="mini" class="">退费</el-button>
              <el-button @click="updateInfo(scope.row)" type="text" size="mini" class="">修改</el-button>
              <br>
              <el-button @click="transferClass(scope.row)" type="text" size="mini" class="">转班</el-button>
              <el-button @click="transferSemester(scope.row)" type="text" size="mini" class="">转期</el-button>
              <template v-if="scope.row.currentArrears <= 0">
                <el-button @click="transferSchool(scope.row)" type="text" size="mini" class="">转校</el-button>
              </template>
              <el-button @click="studentSuspend(scope.row)" type="text" size="mini" class="">休学</el-button>
            </template>
          </template>
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

</template>

<script>
  import BillInfoDialogTable from "./billDialog/BillInfoDialogTable";
  import BillSearchForm from "./BillSearchForm";

  export default {
    name: 'Bills',
    components: {BillSearchForm, BillInfoDialogTable},
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
        this.billOperate.createBill();
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
