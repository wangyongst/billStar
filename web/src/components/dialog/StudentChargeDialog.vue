<template>
  <el-dialog :close-on-click-modal="false"
             class="bill-dialog bill-cmd-dialog"
             :title="dialogTitle"
             :visible.sync="dialogVisible"
             v-loading="loading"
             size="mini">
    <el-form class="demo-form-inline" :inline="true" label-width="70px" size="mini">

      <el-form-item class="inlineFormItem  " size="mini" align="center">
        <!--    数据表格-->
        <el-col :span="24">
          <el-table v-loading="loading" :data="list" class="bill-table" size="" style="width: 100%">
            <el-table-column label="支付类型" width="150" align="center" prop="chargeName">
            </el-table-column>
            <el-table-column label="金额" width="500" align="center" prop="amount">
            </el-table-column>
          </el-table>
        </el-col>
      </el-form-item>

    </el-form>

  </el-dialog>
</template>

<script>
  export default {
    name: 'StudentChargeDialog',
    data() {
      return {
        courseLoading: false,
        dialogVisible: false,
        loading: false,
        dialogTitle: "交费记录",
        labelWidth: '80px',
        list: []
      }
    },

    mounted: function () {
      const _this = this;
      eventBus.$on('studentCharge', function (val) {
        _this.listStudentCharge(val);
        _this.dialogVisible = true;
      });
    },

    methods: {
      listStudentCharge(val) {
        const _this = this;
        _this.httpUtils.appGet('/student/charge/list/' + val).then(function (res) {
          _this.list = res;
          _this.createDialogVisible = false;
        }, _this.operateFail);
      },
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r);
        _this.loading = false;
      }
    }
  }
</script>
