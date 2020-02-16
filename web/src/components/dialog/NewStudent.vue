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
      <el-form-item label="姓名" size="mini">
        <el-input class="bill-cmd-input" v-model="bill.student.name"
                  v-bind:disabled="fieldDisabled.studentField"
                  placeholder="学生姓名"></el-input>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input class="bill-cmd-input" v-model="bill.student.mobile"
                  v-bind:disabled="fieldDisabled.studentField"
                  placeholder="手机号"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="bill.student.gender" v-bind:disabled="fieldDisabled.studentField">
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <br>
      <el-form-item label="学校">
        <el-input class="bill-cmd-input" v-model="bill.student.schoolName" placeholder="学校"></el-input>
      </el-form-item>
      <el-form-item label="班级">
        <el-input class="bill-cmd-input" v-model="bill.student.className" placeholder="班级"></el-input>
      </el-form-item>
      <el-form-item label="是否接  ">
        <el-radio-group v-bind:value="bill.isTransferred" v-model="bill.isTransferred">
          <el-radio :label="1" :value="1">是</el-radio>
          <el-radio :label="0" :value="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="住址" class="not-inline address-item">
        <el-input width="400px" v-model="bill.student.address" placeholder="住址"></el-input>
      </el-form-item>

      <!-- 所属校区 -->
      <el-divider content-position="left">所属校区</el-divider>
      <el-form-item label="所属校区">
        <el-select v-bind:value="bill.deptSchoolId"
                   v-model="bill.deptSchoolId"
                   placeholder="请选择"
                   v-bind:disabled="fieldDisabled.schoolZone"
                   @change="deptSchoolChange">
          <el-option v-for="item in loginUserDeptSchools()"
                     :value="item.id"
                     :label="item.name"
                     :key="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <!--      学期-->
      <el-form-item label="学期：" class="inlineFormItem" size="mini">
        <el-select v-model="bill.semesterId"
                   placeholder="请选择"
                   clearable
                   v-bind:disabled="fieldDisabled.semester"
                   @change="semesterChange">
          <el-option v-for="item in semesterForSelect"
                     :key="item.id"
                     :label="item.name"
                     :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <!-- 课程1 -->
      <el-divider content-position="left" class="">课程信息</el-divider>
      <el-main class="cmdCourseMain" v-loading="courseLoading"
               element-loading-background="#f0f0f0"
               element-loading-text="正在根据【校区+学期】加载课程数据···">
        <el-form-item label="课程1">
          <el-select v-bind:value="bill.billCourseList[0].courseId"
                     v-model="bill.billCourseList[0].courseId"
                     @change="courseSelectChange(0)"
                     v-bind:disabled="fieldDisabled.course"
                     placeholder="请选择">
            <el-option v-for="item in coursesSelect"
                       :key="item.id"
                       :label="appBillCourseName(item)"
                       :value="item.id">
            </el-option>
          </el-select>
          <span class="marginLeft20 courseLabel">{{bill.billCourseList[0].courseLabel}}</span>
        </el-form-item>
        <br>
        <!--      开始时间-->
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="bill.billCourseList[0].beginTime"
            type="date"
            v-bind:disabled="fieldDisabled.course"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <!--      升班时间-->
        <el-form-item label="升班时间">
          <el-date-picker
            v-model="bill.billCourseList[0].riseClassTime"
            type="date"
            v-bind:disabled="fieldDisabled.course"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <!--      过期时间-->
        <el-form-item label="过期时间">
          <el-date-picker
            v-model="bill.billCourseList[0].expireTime"
            type="date"
            v-bind:disabled="fieldDisabled.course"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>

        <el-divider class="marginX5"></el-divider>
        <!-- 课程2 -->
        <el-form-item label="课程2">
          <el-select v-bind:value="bill.billCourseList[1].courseId"
                     v-model="bill.billCourseList[1].courseId"
                     placeholder="请选择"
                     clearable
                     v-bind:disabled="fieldDisabled.course"
                     @change="courseSelectChange(1)">
            <el-option v-for="item in coursesSelect"
                       :key="item.id"
                       :label="appBillCourseName(item)"
                       :value="item.id"></el-option>
          </el-select>
          <span class="marginLeft20 courseLabel">{{bill.billCourseList[1].courseLabel}}</span>
        </el-form-item>
        <!--      开始时间-->
        <br>
        <el-form-item label="开始时间">
          <el-date-picker
            v-bind:disabled="fieldDisabled.course"
            v-model="bill.billCourseList[1].beginTime"
            type="date"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <!--      升班时间-->
        <el-form-item label="升班时间">
          <el-date-picker
            v-model="bill.billCourseList[1].riseClassTime"
            type="date"
            v-bind:disabled="fieldDisabled.course"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <!--      过期时间-->
        <el-form-item label="过期时间">
          <el-date-picker
            v-model="bill.billCourseList[1].expireTime"
            type="date"
            v-bind:disabled="fieldDisabled.course"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
      </el-main>

      <!-- 支付信息     -->
      <el-divider content-position="left" style="margin-top: 0">费用</el-divider>
      <el-form-item label="支付方式">
        <el-select v-model="bill.payTypeId"
                   placeholder="请选择"
                   v-bind:disabled="fieldDisabled.amount">
          <el-option v-for="item in payTypeForSelect"
                     :key="item.id"
                     :label="item.name"
                     :value="item.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="金额">
        <el-input v-model="bill.amount" class="bill-cmd-input" placeholder="金额"
                  v-bind:disabled="fieldDisabled.amount"></el-input>
      </el-form-item>

      <template v-if="fieldShow.arrears">
        <el-form-item label="欠费">
          <el-input v-model="bill.currentArrears" class="bill-cmd-input" v-bind:disabled="fieldDisabled.arrears"
                    placeholder="欠费。没有则不填。"></el-input>
        </el-form-item>
      </template>

      <template v-if="bill.type===9">
        <el-form-item label="开票时间" class="">
          <el-date-picker
            class="datetime"
            v-model="bill.billTime"
            type="datetime"
            style="width: 180px"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
      </template>

      <el-form-item label="备注" class="not-inline address-item">
        <el-input type="text" width="400px" v-model="bill.remark" placeholder="备注信息"></el-input>
      </el-form-item>
      <el-divider content-position="left"></el-divider>
      <el-form-item style="text-align: center" class="not-inline width100p">
        <el-button type="primary" @click="doPost" class="width150" icon="el-icon-upload">保存</el-button>
      </el-form-item>

    </el-form>

  </el-dialog>
</template>

<script>
  // import billCmdUtils from "../../utils/BillCmdUtils";

  export default {
    name: 'NewStudent',
    // 传入dialogVisible/操作类型operateType/关联单refBillId，进行下一步操作
    props: {},
    created() {
      // 全局监听新增、续费、补费、欠费、退费等操作

    },

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
        payTypeForSelect: [],
        deptSchoolForSelect: [],
        semesterForSelect: [],
        // 表单数据
        bill: this.initNullBill(),
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
          _this.dialogTitle = "新生收费";
          _this.loadRefBill(_this.refBillId, function () {
            // 处理某些表单项的显示
            _this.processFields();
          });
        }
      }
    },

    mounted: function () {
      const _this = this;
      _this.listCourseForSelect();
      _this.listChargeTypeForSelect();
      _this.listSemesterForSelect();
      _this.getDefaultRemark();
      // 新增
      eventBus.$on('createStudent', function () {
        console.log(this);
        _this.dialogVisible = true;
        // _this.operateType = _this.$appConfig.billTypes.newBill;
      });
    },

    methods: {
      processFields() {
        const _this = this;
        if (_this.bill.billCourseList && _this.bill.billCourseList.length === 1) {
          _this.bill.billCourseList.push(_this.initNullCourse());
        }
      },
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
