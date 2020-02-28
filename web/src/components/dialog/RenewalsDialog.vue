<template>
  <el-dialog :close-on-click-modal="false"
             class="bill-dialog bill-cmd-dialog"
             :title="dialogTitle"
             :visible.sync="dialogVisible"
             v-loading="loading"
             size="mini"
             @close="doClose">
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

      <el-divider content-position="left">续费</el-divider>

      <el-form-item label="续费金额">
        <el-input class="bill-cmd-input"
                  placeholder="续费金额"></el-input>
      </el-form-item>

      <el-form-item label="欠费金额">
        <el-input class="bill-cmd-input"
                  placeholder="欠费金额"></el-input>
      </el-form-item>

      <el-form-item class="inlineFormItem  " size="mini" align="right">
        <el-button size="mini" type="primary" plain icon="el-icon-success" round>保存</el-button>
        <el-button size="mini" type="primary" plain icon="el-icon-tickets" round>打印</el-button>
      </el-form-item>

      <el-divider content-position="left">课程</el-divider>

      <el-form-item label="科目：" class="inlineFormItem" size="mini">
        <el-select v-model="charge.subjectId"
                   placeholder="请选择"
                   clearable
                   @change="subjectChange">
          <el-option v-for="item in subjectSelect"
                     :key="item.id"
                     :label="item.name"
                     :value="item.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="班级：" class="inlineFormItem" size="mini">
        <el-select v-model="charge.classId"
                   placeholder="请选择"
                   clearable
                   @change="classChange">
          <el-option v-for="item in classSelect"
                     :key="item.id"
                     :label="item.name"
                     :value="item.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="班别：" class="inlineFormItem" size="mini">
        <el-select v-model="charge.classNo"
                   placeholder="请选择"
                   clearable
                   @change="classNoChange">
          <el-option v-for="item in classNoSelect"
                     :key="item.id"
                     :label="item.name"
                     :value="item.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="到期时间" class="">
        <el-date-picker
          class="datetime"
          v-model="bill.expireTime"
          type="datetime"
          style="width: 180px"
          placeholder="选择日期">
        </el-date-picker>
      </el-form-item>


      <el-form-item class="inlineFormItem  " size="mini" align="right">
        <el-button size="mini" type="success" plain icon="el-icon-circle-plus" round>增加</el-button>
      </el-form-item>

      <el-divider content-position="left">课程信息</el-divider>

      <el-form-item class="inlineFormItem  " size="mini" align="right">
        <!--    数据表格-->
        <el-col :span="24">
          <el-table>
            <el-table-column label="" type="index" width="40" align="center">
            </el-table-column>
            <el-table-column label="在学课程" width="150" align="center">
            </el-table-column>
            <el-table-column label="在学课程" width="" align="center">
            </el-table-column>
            <el-table-column label="教师" width="60" align="center">
            </el-table-column>
            <el-table-column label="到期时间" width="500" align="center">
            </el-table-column>
          </el-table>
        </el-col>
      </el-form-item>

    </el-form>

  </el-dialog>
</template>

<script>
  export default {
    name: 'RenewalsDialog',
    props: {},
    data() {
      return {
        courseLoading: false,
        dialogVisible: false,
        defaultSemesterId: null,
        operateType: 0,
        refBillId: null,
        loading: false,
        dialogTitle: "",
        labelWidth: '80px',
        defaultRemark: '',
        // 下拉选择数据
        coursesSelect: [],
        chargeSelect: [],
        fieldShow: {
          arrears: false,
        },
        // 控制部分字段的可编辑属性
        fieldDisabled: {
          arrears: false,
          amount: false,
          mobile: false,
          schoolZone: false,
          course: false,
          studentField: false,
          semester: false,
        }
      }
    },

    watch: {
      dialogVisible(val) {
        const _this = this;
        if (val === true) {
          _this.dialogTitle = "续费";
          // _this.loadRefBill(_this.refBillId, function () {
          //   // 处理某些表单项的显示
          //   _this.processFields();
          // });
        }
      }
    },

    mounted: function () {
      const _this = this;
      // _this.listCourseSelect();
      // _this.listChargeTypeForSelect();
      // _this.listSemesterForSelect();
      // _this.getDefaultRemark();
      // 新增
      eventBus.$on('renewals', function () {
        _this.dialogVisible = true;
      });
    },

    methods: {
      // 加载学期
      listSemesterForSelect() {
        const _this = this;
        _this.httpUtils.appGet('/semester/list').then(function (res) {
          _this.semesterForSelect = res.data;
          console.log("list semester for select");
          for (const index in _this.semesterForSelect) {
            const item = _this.semesterForSelect[index];
            if (item.isDefault && parseInt(item.isDefault) === 1) {
              if (_this.bill.semesterId == null) {
                _this.bill.semesterId = item.id;
                _this.defaultSemesterId = item.id;
              }
              break;
            }
          }
        }, _this.operateFail);
      },

      getDefaultRemark() {
        const _this = this;
        _this.httpUtils.appGet('/config/getByName?name=app.schoolCommonRemark').then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.defaultRemark = res.data.value;
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },

      listChargeTypeForSelect() {
        const _this = this;
        _this.httpUtils.appGet('/chargeType/list').then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.payTypeForSelect = res.data;
          } else {
            _this.baseErrorNotify(res.msg);

          }
        }, _this.operateFail);
      },

      listDeptSchoolForSelect() {
        const _this = this;
        _this.httpUtils.appPost('/schoolZone/listSchoolZone').then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.deptSchoolForSelect = res.data;
          } else {
            _this.baseErrorNotify(res.msg);

          }
        }, _this.operateFail);
      },

      listCourseForSelect() {
        let deptSchoolId;
        // if (this.operateType === this.$appConfig.billTypes.transferSchool) {
        //   deptSchoolId = this.bill.newDeptSchoolId;
        // } else {
        deptSchoolId = this.bill.deptSchoolId;
        // }
        if (!deptSchoolId) {
          return;
        }
        if (!this.bill.semesterId) {
          return;
        }
        const _this = this;
        _this.courseLoading = true;
        _this.httpUtils.appGet('/course/listCourses?deptSchoolId='
          + deptSchoolId
          + "&semesterId="
          + this.bill.semesterId).then(function (res) {
          _this.courseLoading = false;
          if (parseInt(res.code) === 0) {
            _this.coursesSelect = res.data;
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },

      doClose() {
        this.bill = this.initNullBill();
        this.$emit('dialogClose');//子组件对openStatus修改后向父组件发送事件通知
      },

      doPost() {
        const _this = this;
        const url = _this.getUrl();
        if (!url) {
          _this.baseErrorNotify("系统错误，请联系管理员");
        }
        _this.loading = true;
        _this.httpUtils.appPost(url, _this.bill).then(function (res) {
          _this.loading = false;
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
            _this.dialogVisible = false;
            _this.bill = _this.initNullBill();
            eventBus.$emit('billOperateSuccess');
          } else {
            _this.loading = false;
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },

      getUrl() {
        return "/billOperate/createStudent";
      },

      courseSelectChange(selectIndex) {
        const _this = this;
        const val = _this.bill.billCourseList[selectIndex].courseId;
        console.log(JSON.stringify(_this.bill.billCourseList));
        if (_this.bill.billCourseList[0].courseId != null &&
          _this.bill.billCourseList[0].courseId === _this.bill.billCourseList[1].courseId) {
          _this.baseErrorNotify("两门课程选择重复，请重新选择");
          _this.bill.billCourseList[selectIndex].courseId = null;
          _this.bill.billCourseList[selectIndex].courseLabel = _this.defaultCourseLabel();
          return;
        }
        _this.coursesSelect.forEach(function (item) {
          if (item.id === val) {
            _this.bill.billCourseList[selectIndex].courseLabel = item.courseLabel;
          }
        });
      },

      deptSchoolChange() {
        const _this = this;
        _this.bill.billCourseList = [
          _this.initNullCourse(),
          _this.initNullCourse()
        ];
        _this.coursesSelect = [];
        _this.listCourseForSelect();
      },

      semesterChange() {
        const _this = this;
        _this.bill.billCourseList = [
          _this.initNullCourse(),
          _this.initNullCourse()
        ];
        _this.coursesSelect = [];
        _this.listCourseForSelect();
      },

      defaultCourseLabel() {
        return "*课程信息*";
      },

      operateFail(r) {
        const _this = this;
        _this.loading = false;
        _this.baseErrorNotify(r.msg);
      },

      initNullBill() {
        console.log("initNullBill");
        const _this = this;
        return {
          deptSchoolId: null,
          semesterId: _this.defaultSemesterId,
          remark: '',
          student: {
            name: "",
            schoolName: "",
            gender: null,
          },
          isTransferred: null,
          payTypeId: null,
          amount: null,
          billCourseList: [
            _this.initNullCourse(),
            _this.initNullCourse(),
          ],
          newDeptSchoolId: null,
          currentArrears: null,
        }
      }
    }

  }
</script>
