<!-- 设计做显示
 20200117 ——现在没有在使用
 -->
<template>
  <el-dialog class="bill-item" title="" :visible.sync="visible" @close="doClose">
    <el-row id="printZone" class="print-zone bt bl br">
      <p class="bill-title">{{this.dialogTitle}}{{bill.deptSchoolName}}收据</p>
      <el-row class="bt">
        <el-col  v-bind:span="cols.col1+cols.col2">
          <div class="label">开票时间</div>
        </el-col>
        <el-col v-bind:span="24-(cols.col1+cols.col2)" class="pure-text txt100">
          {{this.baseFormatTime(bill.billTime)}}
        </el-col>
      </el-row>
      <!--    第一行 学生信息-->
      <el-row>
        <!--      姓名-->
        <el-col  v-bind:span="cols.col1">
          <div class="label">学生</div>
        </el-col>
        <el-col v-bind:span="cols.col2" class="pure-text txt100">
          {{bill.student.name}}
        </el-col>
        <!--      电话-->
        <el-col v-bind:span="cols.col3">
          <div class="label">电话</div>
        </el-col>
        <el-col v-bind:span="cols.col4" class="pure-text txt100">
          {{bill.student.mobile}}
        </el-col>
        <!--      性别-->
        <el-col v-bind:span="cols.col5">
          <div class="label">性别1</div>
        </el-col>
        <el-col v-bind:span="cols.col6" class="pure-text txt100">
          {{this.baseSex(bill.student.gender)}}
        </el-col>
        <!--      -->
        <el-col v-bind:span="cols.col7">
          <div class="label">学校</div>
        </el-col>
        <el-col v-bind:span="cols.col8" class="pure-text txt100">
          {{bill.student.schoolName}}
        </el-col>
      </el-row>

      <!--    第2行 金额信息-->
      <el-row>
        <!--      -->
        <el-col v-bind:span="cols.col1">
          <div class="label">金额</div>
        </el-col>
        <el-col v-bind:span="cols.col2" class="pure-text txt100">
          {{bill.amount}}
        </el-col>
        <!--      -->
        <el-col v-bind:span="cols.col3">
          <div class="label">大写金额</div>
        </el-col>
        <el-col v-bind:span="cols.col4" class="pure-text txt100">
          {{this.appDigitUppercase(bill.amount)}}
        </el-col>
        <!--      -->
        <el-col v-bind:span="cols.col5">
          <div class="label">是否接</div>
        </el-col>
        <el-col v-bind:span="cols.col6" class="pure-text txt100">
          {{this.baseYesNo(bill.isTransferred)}}
        </el-col>
        <!--      -->
        <el-col v-bind:span="cols.col7">
          <div class="label">班级</div>
        </el-col>
        <el-col v-bind:span="cols.col8" class="pure-text txt100">
          {{bill.student.className}}
        </el-col>
      </el-row>

      <BillInfoDialogCourse :billCourse="bill.courses[0]" :cols="cols"/>
      <BillInfoDialogCourse :billCourse="bill.courses[1]" :cols="cols"/>
      <!--    校区 校区电话 -->
      <el-row>
        <!--      -->
        <el-col v-bind:span="cols.col1">
          <div class="label">校区</div>
        </el-col>
        <el-col v-bind:span="(12-cols.col1)" class="pure-text txt100">
          {{bill.deptSchoolName}}
        </el-col>
        <!--      -->
        <el-col v-bind:span="cols.col5">
          <div class="label">校区电话</div>
        </el-col>
        <el-col v-bind:span="(12-cols.col5)" class="pure-text txt100">
          {{bill.deptSchoolPhone}}
        </el-col>
        <!--      -->
      </el-row>
      <!--    住址 -->
      <el-row>
        <!--      -->
        <el-col v-bind:span="cols.col1">
          <div class="label">住址</div>
        </el-col>
        <el-col v-bind:span="12-cols.col1" class="pure-text txt100">
          {{bill.student.address}}
        </el-col>
        <!--      -->
        <el-col v-bind:span="cols.col5">
          <div class="label">家长签字</div>
        </el-col>
        <el-col v-bind:span="12-cols.col5">

        </el-col>
        <!--      -->
      </el-row>

      <!--    备注 -->
      <el-row>
        <!--      -->
        <el-col v-bind:span="cols.col1">
          <div class="label">备注</div>
        </el-col>
        <el-col v-bind:span="(18-cols.col1)" class="pure-text txt100">
          {{bill.remark}}
        </el-col>
        <!--      -->
        <el-col v-bind:span="cols.col7">
          <div class="label">开票人</div>
        </el-col>
        <el-col v-bind:span="cols.col8" class="pure-text">
          {{bill.billCreatorName}}
        </el-col>
        <!--      -->
      </el-row>
    </el-row>
    <!--    </el-container>-->
    <el-footer class="txt010" style="padding-top:30px;">
      <el-button v-print="'#printZone'" size="small" type="primary" style="width: 200px;">打印</el-button>
    </el-footer>
  </el-dialog>
</template>

<script>
  import BillInfoDialogCourse from './BillInfoDialogCourse';

  export default {
    components: {BillInfoDialogCourse},

    name: 'DialogBill',
    props: {
      dialogTitle: {
        required: false,
        default: "分界线美术学校",
      },
      dialogVisible: {
        required: false,
        default: false,
      },
      bill: {
        required: true,
      }
    },

    data() {
      return {
        visible: this.dialogVisible,
        cols: {
          col1: 2,
          col2: 3,
          col3: 2,
          col4: 5,
          col5: 3,
          col6: 3,
          col7: 3,
          col8: 3,
        }
      }
    },
    watch: {
      dialogVisible(val) {
        const _this = this;
        this.visible = val;
        if (val) {
          // _this.bill = _this.props.billIn;
          // alert(JSON.stringify(_this.bill));
        }
      }
    },
    mounted: function () {
      this.listCourseForSelect();
    },


    methods: {
      listCourseForSelect() {
        const _this = this;
        _this.httpUtils.appGet('/course/listAllICanSee').then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.coursesSelect = res.data;
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },
      doClose() {
        this.$emit('dialogClose');//子组件对openStatus修改后向父组件发送事件通知
      },
    }

  }
</script>

<style scoped>

</style>
