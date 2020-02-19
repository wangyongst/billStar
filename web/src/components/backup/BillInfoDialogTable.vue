<!-- 设计做显示 -->
<template>
  <el-dialog class="bill-item" title="" :visible.sync="visible" @close="doClose">
    <table id="tablePrintZone">
      <tr>
        <td colspan="8" class="title">{{this.dialogTitle}}{{bill.deptSchoolName}}收据</td>
      </tr>
      <tr>
        <td colspan="2" class="label">开票时间</td>
        <td colspan="4" class="value">{{this.baseFormatTime(bill.billTime)}}</td>
        <td class="label">开票人</td>
        <td class="value">{{bill.billCreatorName}}</td>
      </tr>
      <!--    第一行 学生信息-->
      <tr>
        <td class="label label1">学生</td>
        <td class="value value1">{{bill.student.name}}</td>
        <td class="label label2">电话</td>
        <td class="value value2" colspan="3">{{bill.student.mobile}}</td>
        <!--        <td class="label label3">校区电话</td>-->
        <!--        <td class="value value3">{{bill.deptSchoolPhone}}</td>-->
        <!--        <td class="label label4">家长签字</td>-->
        <!--        <td class="value value4"></td>-->
        <td class="label">家长签字</td>
        <td class="value"></td>
      </tr>
      <!--    第2行 金额信息-->
      <tr>
        <td class="label label1">金额</td>
        <td class="value value1">{{bill.amount}}</td>
        <td class="label label2">大写金额</td>
        <td class="value value2">{{this.appDigitUppercase(bill.amount)}}</td>
        <td class="label label3">是否接</td>
        <td class="value value3">{{this.baseYesNoVal(bill.isTransferred)}}</td>
        <td class="label label4">班级</td>
        <td class="value value4">{{bill.student.className}}</td>
      </tr>

      <!--    第2行 课程报班信息-->
      <template v-if="this.bill.courses && this.bill.courses.length>=1">
        <tr>
          <td class="label label1">课程</td>
          <td class="value value1">{{this.formatClass(bill.courses[0])}}</td>
          <td class="label label2">上课时间</td>
          <td class="value value2">{{this.bill.courses[0].regularTime}}</td>
          <td class="label label3">教室号</td>
          <td class="value value3"> {{this.bill.courses[0].classroomNo}}</td>
          <td class="label label4">开始时间<br>过期时间</td>
          <td class="value value4">{{this.baseFormatDate(bill.courses[0].beginTime)}}<br>{{this.baseFormatDate(bill.courses[0].expireTime)}}
          </td>
        </tr>
      </template>

      <template v-if="this.bill.courses && this.bill.courses.length>=2">
        <tr>
          <td class="label label1">课程</td>
          <td class="value value1">{{this.formatClass(bill.courses[1])}}</td>
          <td class="label label2">上课时间</td>
          <td class="value value2">{{this.bill.courses[1].regularTime}}</td>
          <td class="label label3">教室号</td>
          <td class="value value3"> {{this.bill.courses[1].classroomNo}}</td>
          <td class="label label4">开始时间<br>过期时间</td>
          <td class="value value4">{{this.baseFormatDate(bill.courses[1].beginTime)}}<br>{{this.baseFormatDate(bill.courses[1].expireTime)}}
          </td>
        </tr>
      </template>

      <!--    校区 校区电话 -->
      <tr>
        <td class="label ">校区</td>
        <td class="value" colspan="3">{{bill.deptSchoolName}}</td>
        <td class="label">校区电话</td>
        <td class="value" colspan="3">{{bill.deptSchoolPhone}}</td>
      </tr>
      <!--      &lt;!&ndash;    住址 &ndash;&gt;-->
      <tr>
        <td class="label ">备注</td>
        <td class="value" colspan="7"> {{bill.remark}}</td>
         </tr>
    </table>
    <!--    </el-container>-->
    <el-footer class="txt010" style="padding-top:30px;">
      <el-button v-print="'#tablePrintZone'" size="small" type="primary" style="width: 200px;">打印</el-button>
      <el-button @click="saveToPDF" size="small" type="primary" plain style="width: 200px;">保存为PDF</el-button>
    </el-footer>
  </el-dialog>
</template>

<script>

  export default {

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
      }
    },
    watch: {
      dialogVisible(val) {
        this.visible = val;
        if (val) {
          console.log("打开弹框");
        }
      }
    },
    mounted: function () {
    },
    methods: {
      doClose() {
        this.$emit('dialogClose');//子组件对openStatus修改后向父组件发送事件通知
      },
      saveToPDF() {
        const _this = this;
        _this.getPdf(_this.bill.student.name + "_" + _this.bill.student.mobile, "#tablePrintZone");
      }
    }

  }
</script>

<style scoped>


</style>
