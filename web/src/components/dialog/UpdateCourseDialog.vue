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
      <el-divider content-position="left"></el-divider>

      <el-form-item label="学期">
        <el-select v-model="course.semesterId" placeholder="请选择">
          <el-option v-for="item in semesterSelect" :value="item.id" :label="item.name" :key="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="所属校区">
        <el-select v-model="course.schoolId" placeholder="请选择" @change="listTeacherSelect">
          <el-option v-for="item in schoolSelect" :value="item.id" :label="item.name" :key="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="科目">
        <el-select v-model="course.subjectId" placeholder="请选择" @change="listClassSelelect">
          <el-option v-for="item in subjectSelect" :value="item.id" :label="item.name" :key="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-divider content-position="left"></el-divider>

      <el-form-item label="班级">
        <el-select v-model="course.classId" placeholder="先选择科目">
          <el-option v-for="item in classSelect" :value="item.id" :label="item.name" :key="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="班别">
        <el-select v-model="course.classNoId" placeholder="请选择">
          <el-option v-for="item in classNoSelect" :value="item.id" :label="item.name" :key="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="班额">
        <el-input class="bill-cmd-input" v-model="course.classNum" type="number" placeholder="班额只能是数字"></el-input>
      </el-form-item>

      <el-divider content-position="left"></el-divider>

      <el-form-item label="教师">
        <el-select v-model="course.teacherId" placeholder="先选择校区">
          <el-option v-for="item in teacherSelect" :value="item.id" :label="item.name" :key="item.id">
          </el-option>
        </el-select>
      </el-form-item>


      <el-form-item label="教室" size="mini">
        <el-select v-model="course.classRoomId" placeholder="请选择">
          <el-option v-for="item in classRoomSelect" :value="item.id" :label="item.name" :key="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-divider content-position="left"></el-divider>

      <el-form-item label="上课时间" size="mini">
        <el-radio-group v-model="course.type">
          <el-radio size="mini" :label="1">
            每天&nbsp;&nbsp;
            <el-time-picker
              format="HH:mm"
              class="datetime"
              v-model="course.day.begin"
              type="datetime"
              style="width: 180px"
              placeholder="选择日期">
            </el-time-picker>
            &nbsp;&nbsp;开始
            <el-time-picker
              format="HH:mm"
              class="datetime"
              v-model="course.day.end"
              type="datetime"
              style="width: 180px"
              placeholder="选择日期">
            </el-time-picker>
            &nbsp;&nbsp;结束
          </el-radio>
          <br>
          <el-radio size="mini" :label="2">
            每周&nbsp;&nbsp;
            <br>
            <el-col :offset="1">
              <el-checkbox size="mini" :label="1"> 周一&nbsp;&nbsp;
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[0].begin"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;开始
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[0].end"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;结束
              </el-checkbox>
            </el-col>
            <br>
            <el-col :offset="1">
              <el-checkbox size="mini" :label="1"> 周二&nbsp;&nbsp;
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[1].begin"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;开始
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[1].end"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;结束
              </el-checkbox>
            </el-col>
            <br>
            <el-col :offset="1">
              <el-checkbox size="mini" :label="1"> 周三&nbsp;&nbsp;
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[2].begin"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;开始
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[2].end"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;结束
              </el-checkbox>
            </el-col>
            <br>
            <el-col :offset="1">
              <el-checkbox size="mini" :label="1">周四&nbsp;&nbsp;
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[3].begin"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;开始
                <el-time-picker
                  class="datetime"
                  v-model="course.dayList[3].end"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;结束
              </el-checkbox>
            </el-col>
            <br>
            <el-col :offset="1">
              <el-checkbox size="mini" :label="1">周五&nbsp;&nbsp;
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[4].begin"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;开始
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[4].end"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;结束
              </el-checkbox>
            </el-col>
            <br>
            <el-col :offset="1">
              <el-checkbox size="mini" :label="1">周六&nbsp;&nbsp;
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[5].begin"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;开始
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[5].end"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;结束
              </el-checkbox>
            </el-col>
            <br>
            <el-col :offset="1">
              <el-checkbox size="mini" :label="1">周日&nbsp;&nbsp;
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[6].begin"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;开始
                <el-time-picker
                  format="HH:mm"
                  class="datetime"
                  v-model="course.dayList[6].end"
                  type="datetime"
                  style="width: 180px"
                  placeholder="选择日期">
                </el-time-picker>
                &nbsp;&nbsp;结束
              </el-checkbox>
            </el-col>
          </el-radio>
        </el-radio-group>
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
    name: 'UpdateCourseDialog',
    props: ["id"],
    data() {
      return {
        courseLoading: false,
        dialogVisible: false,
        loading: false,
        dialogTitle: "",
        labelWidth: '80px',
        // 下拉选择数据
        subjectSelect: [],
        classSelect: [],
        classNoSelect: [],
        classRoomSelect: [],
        schoolSelect: [],
        semesterSelect: [],
        teacherSelect: [],
        // 表单数据
        course: this.initCourse()
      }
    },

    watch: {
      dialogVisible(val) {
        const _this = this;
        if (val === true) {
          _this.dialogTitle = "修改课程";
        }
      }
    },

    mounted: function () {
      const _this = this;
      _this.listSemesterSelect();
      _this.listSchoolSelect();
      _this.listSubjectSelect();
      _this.listClassNoSelect();
      _this.listClassRoomSelect();
      eventBus.$on('updateCourse', function () {
        _this.dialogVisible = true;
      });
    },

    methods: {
      initCourse() {
        const _this = this;
        return {
          id: _this.id,
          schoolId: null,
          subjectId: null,
          classId: null,
          teacherId: null,
          classNoId: null,
          classNum: null,
          classRoomId: null,
          type: null,
          day: {
            type: 1,
            day: "每天",
            begin: null,
            end: null
          },
          dayList: [
            {id: null, type: 2, day: "周一", begin: null, end: null},
            {id: null, type: 2, day: "周二", begin: null, end: null},
            {id: null, type: 2, day: "周三", begin: null, end: null},
            {id: null, type: 2, day: "周四", begin: null, end: null},
            {id: null, type: 2, day: "周五", begin: null, end: null},
            {id: null, type: 2, day: "周六", begin: null, end: null},
            {id: null, type: 2, day: "周日", begin: null, end: null}
          ]
        }
      },

      listSemesterSelect() {
        const _this = this;
        _this.httpUtils.appGet('/sys/semester/list').then(function (res) {
          _this.semesterSelect = res;
          for (const index in _this.semesterSelect) {
            const item = _this.semesterSelect[index];
            if (item.isDefault) {
              if (_this.course.semesterId == null) {
                _this.course.semesterId = item.id;
              }
              break;
            }
          }
        }, _this.operateFail);
      },

      listSchoolSelect() {
        const _this = this;
        _this.httpUtils.appGet('/schoolZone/listSchoolZone').then(function (res) {
          _this.schoolSelect = res;
        }, _this.operateFail);
      },

      listSubjectSelect() {
        const _this = this;
        _this.httpUtils.appGet('/sys/subject/list').then(function (res) {
          _this.subjectSelect = res;
        }, _this.operateFail);
      },

      listClassNoSelect() {
        const _this = this;
        _this.httpUtils.appGet('/sys/class/no/list').then(function (res) {
          _this.classNoSelect = res;
        }, _this.operateFail);
      },

      listClassRoomSelect() {
        const _this = this;
        _this.httpUtils.appGet('/sys/class/room/list').then(function (res) {
          _this.classRoomSelect = res;
        }, _this.operateFail);
      },

      listClassSelelect() {
        const _this = this;
        const cmd = {subjectId: _this.course.subjectId};
        _this.httpUtils.appPost('/sys/class/listClass', cmd).then(function (res) {
          _this.classSelect = res;
        }, _this.operateFail);
      },

      listTeacherSelect() {
        const _this = this;
        const cmd = {schoolId: _this.course.schoolId};
        _this.httpUtils.appPost('/teacher/listTeacher', cmd).then(function (res) {
          _this.teacherSelect = res;
        }, _this.operateFail);
      },

      doClose() {
        const _this = this;
        _this.course = _this.initCourse();
        this.$emit('dialogClose');
      },

      doPost() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost("/course/main/update", _this.course).then(function (res) {
          _this.loading = false;
          _this.dialogVisible = false;
          _this.baseSuccessNotify(res);
          _this.course = _this.initCourse();
        }, _this.operateFail);
      },

      operateFail(r) {
        const _this = this;
        _this.loading = false;
        _this.baseErrorNotify(r);
      }
    }
  }
</script>
