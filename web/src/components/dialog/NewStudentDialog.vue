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
        <el-input class="bill-cmd-input" v-model="student.name"
                  placeholder="学生姓名"></el-input>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input class="bill-cmd-input" v-model="student.mobile"
                  placeholder="手机号"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="student.sex">
          <el-radio :label="1">男</el-radio>
          <el-radio :label="0">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <br>
      <el-form-item label="学校">
        <el-input class="bill-cmd-input" v-model="student.myschool" placeholder="学校"></el-input>
      </el-form-item>
      <el-form-item label="班级">
        <el-input class="bill-cmd-input" v-model="student.myclass" placeholder="班级"></el-input>
      </el-form-item>
      <el-form-item label="是否接  ">
        <el-radio-group v-model="student.accept">
          <el-radio :label="1" :value="1">是</el-radio>
          <el-radio :label="0" :value="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="住址" class="not-inline address-item">
        <el-input width="400px" v-model="student.address" placeholder="住址"></el-input>
      </el-form-item>

      <!-- 所属校区 -->
      <el-divider content-position="left">所属校区</el-divider>

      <!--      学期-->
      <el-form-item label="学期：" class="inlineFormItem" size="mini">
        <el-select v-model="student.semesterId" placeholder="请选择" @change="listCourseSelect">
          <el-option v-for="item in semesterSelect" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="所属校区">
        <el-select v-model="student.schoolId" placeholder="请选择" @change="listCourseSelect">
          <el-option v-for="item in schoolSelect" :value="item.id" :label="item.name" :key="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <!-- 课程1 -->
      <el-divider content-position="left" class="">课程信息</el-divider>
      <el-main class="cmdCourseMain" v-loading="courseLoading"
               element-loading-background="#f0f0f0"
               element-loading-text="正在根据【校区+学期】加载课程数据···">
        <el-form-item label="课程1">
          <el-select v-model="student.courseList[0].courseId" @change="courseSelectChange(0)" placeholder="请选择">
            <el-option v-for="item in courseSelect" :key="item.id" :label="makeCourseName(item)" :value="item.id">
            </el-option>
          </el-select>
          <span class="marginLeft20 courseLabel">{{student.courseList[0].label}}</span>
        </el-form-item>
        <br>
        <!--      开始时间-->
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="student.courseList[0].beginTime"
            type="date" placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <!--      升班时间-->
        <el-form-item label="升班时间">
          <el-date-picker
            v-model="student.courseList[0].riseTime"
            type="date"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <!--      过期时间-->
        <el-form-item label="过期时间">
          <el-date-picker
            v-model="student.courseList[0].expireTime"
            type="date"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>

        <el-divider class="marginX5"></el-divider>
        <!-- 课程2 -->
        <el-form-item label="课程2">
          <el-select v-model="student.courseList[1].courseId" placeholder="请选择" clearable @change="courseSelectChange(1)">
            <el-option v-for="item in courseSelect" :key="item.id" :label="makeCourseName(item)" :value="item.id"></el-option>
          </el-select>
          <span class="marginLeft20 courseLabel">{{student.courseList[1].label}}</span>
        </el-form-item>
        <!--      开始时间-->
        <br>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="student.courseList[1].beginTime"
            type="date"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <!--      升班时间-->
        <el-form-item label="升班时间">
          <el-date-picker
            v-model="student.courseList[1].riseTime"
            type="date"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <!--      过期时间-->
        <el-form-item label="过期时间">
          <el-date-picker
            v-model="student.courseList[1].expireTime"
            type="date"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
      </el-main>

      <!-- 支付信息     -->
      <el-divider content-position="left" style="margin-top: 0">费用</el-divider>
      <el-form-item label="支付方式">
        <el-select v-model="student.charge.chargeId" placeholder="请选择">
          <el-option v-for="item in chargeSelect"
                     :key="item.id"
                     :label="item.name"
                     :value="item.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="金额">
        <el-input v-model="student.charge.amount" class="bill-cmd-input" placeholder="金额" type="number" step="0.01" min="0.01"></el-input>
      </el-form-item>


      <el-form-item label="备注" class="not-inline address-item">
        <el-input type="text" width="400px" v-model="student.remark" placeholder="备注信息"></el-input>
      </el-form-item>
      <el-divider content-position="left"></el-divider>
      <el-form-item style="text-align: center" class="not-inline width100p">
        <el-button type="primary" @click="doPost" class="width150" icon="el-icon-upload">保存</el-button>
      </el-form-item>

    </el-form>

  </el-dialog>
</template>

<script>
  export default {
    name: 'NewStudentDialog',
    props: {},
    data() {
      return {
        courseLoading: false,
        dialogVisible: false,
        loading: false,
        dialogTitle: "",
        labelWidth: '80px',
        defaultRemark: '',
        // 下拉选择数据
        courseSelect: [],
        chargeSelect: [],
        schoolSelect: [],
        semesterSelect: [],
        // 表单数据
        student: this.initStuent()
      }
    },

    watch: {
      dialogVisible(val) {
        const _this = this;
        if (val === true) {
          _this.dialogTitle = "新生收费";
          _this.processFields();
        }
      }
    },

    mounted: function () {
      const _this = this;
      _this.listSchoolSelect();
      _this.listChargeSelect();
      _this.listSemesterSelect();
      // _this.getDefaultRemark();
      // 新增
      eventBus.$on('newStudent', function () {
        console.log(this);
        _this.dialogVisible = true;
      });
    },

    methods: {
      initStuent() {
        const _this = this;
        return {
          schoolId: null,
          mobile: "",
          remark: '',
          name: "",
          myschool: "",
          myclass: "",
          sex: null,
          accept: null,
          charge:{
            chargeId: null,
            amount: null,
          },
          courseList: [
            {courseId: null, expireTime: null, label: "*课程信息*", beginTime: null, riseTime: null},
            {courseId: null, expireTime: null, label: "*课程信息*", beginTime: null, riseTime: null}
          ],
          address: ""
        }
      },
      processFields() {
        const _this = this;
        if (_this.courseList && _this.courseList.length === 1) {
          _this.courseList.push(_this.initNullCourse());
        }
      },
      // 加载学期
      listSemesterSelect() {
        const _this = this;
        _this.httpUtils.appGet('/sys/semester/list').then(function (res) {
          _this.semesterSelect = res;
          for (const index in _this.semesterSelect) {
            const item = _this.semesterSelect[index];
            if (item.isDefault) {
              _this.student.semesterId = item.id;
              break;
            }
          }
        }, _this.operateFail);
      },

      // getDefaultRemark() {
      //   const _this = this;
      //   _this.httpUtils.appGet('/config/getByName?name=app.schoolCommonRemark').then(function (res) {
      //     _this.student.remark = res.value;
      //   }, _this.operateFail);
      // },

      listChargeSelect() {
        const _this = this;
        _this.httpUtils.appGet('/sys/charge/list').then(function (res) {
          _this.chargeSelect = res;
        }, _this.operateFail);
      },

      listSchoolSelect() {
        const _this = this;
        _this.httpUtils.appGet('/schoolZone/listSchoolZone').then(function (res) {
          _this.schoolSelect = res;
        }, _this.operateFail);
      },

      listCourseSelect() {
        const _this = this;
        const cmd = {subjectId: _this.student.subjectId, semesterId: _this.student.semesterId};
        _this.courseLoading = true;
        _this.httpUtils.appPost('/course/main/list', cmd).then(function (res) {
          _this.courseSelect = res;
          _this.courseLoading = false;
        }, _this.operateFail);
      },

      doClose() {
        const _this = this;
        _this.student = _this.initStuent();
        _this.$emit('dialogClose');
      },

      doPost() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost("/student/main/create", _this.student).then(function (res) {
          _this.loading = false;
          _this.dialogVisible = false;
          _this.student = _this.initStuent();
          _this.baseSuccessNotify(res);

        }, _this.operateFail);
      },

      courseSelectChange(selectIndex) {
        const _this = this;
        const courseId = _this.student.courseList[selectIndex].courseId;
        console.log(courseId);
        if (_this.student.courseList[0].courseId != null && _this.student.courseList[0].courseId === _this.student.courseList[1].courseId) {
          _this.baseErrorNotify("两门课程选择重复，请重新选择");
          _this.student.courseList[selectIndex].courseId = null;
          _this.student.courseList[selectIndex].label = _this.defaultCourseLabel();
          return;
        }
        _this.courseSelect.forEach(function (item) {
          if (item.id === courseId) {
            _this.student.courseList[selectIndex].label = _this.makeCourseLabel(item);
          }
        });
      },

      defaultCourseLabel() {
        return "*课程信息*";
      },
      initNullCourse() {
        return {courseId: null, expireTime: null, courseLabel: "*课程信息*", beginTime: null, riseTime: null};
      },

      makeCourseName(item) {
        return item.subjectName + " + " + item.className + "+" + item.teacherName;
      },

      makeCourseLabel(item) {
        return " 教室号：" + item.classRoom;
      },

      operateFail(r) {
        const _this = this;
        _this.loading = false;
        _this.baseErrorNotify(r);
      }
    }
  }
</script>
