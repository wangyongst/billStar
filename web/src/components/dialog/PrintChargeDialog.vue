<template>
  <el-dialog class="bill-item" title="" :visible.sync="dialogVisible">
    <el-row id="printChargeCourseDialog">
      <p align="center">{{charge.mainSchool}}{{charge.schoolName}}收据</p>
      <el-row align="center">
        <el-col :span="7">
          <div class="label">姓名:{{charge.studentName}}</div>
        </el-col>
        <el-col :span="7">
          <div class="label">电话:{{charge.mobile}}</div>
        </el-col>
        <el-col :span="7">
          <div class="label">开票时间:{{charge.billTime}}</div>
        </el-col>
      </el-row>
      <el-row v-for="item in charge.courseList">
        <el-row align="center">
          <el-col :span="7">
            <div class="label">课程</div>
          </el-col>
          <el-col :span="7">
            <div class="label">上课时间</div>
          </el-col>
          <el-col :span="7">
            <div class="label">预计到期时间</div>
          </el-col>
        </el-row>
        <el-row align="center">
          <el-col :span="7">
            <div class="label">{{item.className}}{{item.classNo}}</div>
          </el-col>
          <el-col :span="7">
            <div class="label">{{item.courseTime}}</div>
          </el-col>
          <el-col :span="7">
            <div class="label">{{timeFormatter(item.expireTime)}}</div>
          </el-col>
        </el-row>
      </el-row>
      <el-row align="center">
        <el-col :span="7">
          <div class="label">收费金额</div>
        </el-col>
        <el-col :span="7">
          <div class="label">{{charge.amount}}元</div>
        </el-col>
        <el-col :span="7">
          <div class="label">大写{{charge.bigAmount}}</div>
        </el-col>
      </el-row>
      <el-row align="center">
        <el-col :span="7">
          <div class="label">学校电话</div>
        </el-col>
        <el-col :span="7">
          <div class="label">{{charge.schoolMobile}}</div>
        </el-col>
      </el-row>
      <el-row align="center">
        <!--      -->
        <el-col align="left">
          <div class="label">备注: {{charge.remarks}}</div>
        </el-col>
      </el-row>
    </el-row>
    <!--    </el-container>-->
    <el-footer class="txt010" style="padding-top:30px;">
      <el-button v-print="'#printChargeCourseDialog'" size="small" type="primary" style="width: 200px;">打印</el-button>
    </el-footer>
  </el-dialog>
</template>

<script>
  export default {
    name: 'PrintChargeDialog',
    data() {
      return {
        dialogVisible: false,
        loading: false,
        charge: {
          amount: null,
          bigAmount: null,
          schoolName: null,
          mainSchool: null,
          studentName: null,
          mobile: null,
          schoolMobile: null,
          billTime: null,
          remarks: null,
          courseList: []
        }
      }
    },
    mounted: function () {
      const _this = this;
      eventBus.$on('printCharge', function (val) {
        console.log(val);
        _this.getCharge(val);
        _this.dialogVisible = true;
      });
    },


    methods: {
      getCharge(val) {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/student/charge/get/' + val).then(function (res) {
          _this.loading = false;
          _this.charge = res;
        }, _this.operateFail);
      },
      timeFormatter(v) {
        const _this = this;
        return _this.baseFormatDate(v);
      },
      doClose() {
        this.$emit('dialogClose');
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
