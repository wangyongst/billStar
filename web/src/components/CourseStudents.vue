<template>
  <el-row id="body">
    <!--查询表单-->
    <el-col :span="24">
      <el-form label-width="100px" class="search-form" size="mini">
        <el-tag class="marginLeft20" type="warning"
                v-loading="courseLoading"
                element-loading-spinner="el-icon-loading"
                element-loading-background="#f0f0f0">{{courseInfo}}
        </el-tag>
        <el-form-item label="是否超期：" class="inlineFormItem" size="mini">
          <el-select v-model="query.data.isExpired"
                     placeholder="是否超期"
                     clearable
                     size="mini"
                     style="width:80px">
            <el-option v-for="item in activeArray"
                       :key="item.id"
                       :label="item.name"
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否有效：" class="inlineFormItem" size="mini">
          <el-select v-model="query.data.isActive"
                     placeholder="是否有效"
                     clearable
                     size="mini"
                     style="width:80px">
            <el-option v-for="item in activeArray"
                       :key="item.id"
                       :label="item.name"
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="inlineFormItem">
          <el-button @click="searchFunc" size="mini" class="bill-list-search" type="primary"
                     plain icon="el-icon-search" round>查询
          </el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <el-col>
      <!--    数据表格-->
      <el-container style="width: 100%">
        <el-table v-loading="loading" :data="page.list" class="bill-table" style="width: 100%"
                  @cell-mouse-enter="enterTableCell"
                  @cell-mouse-leave="leaveTableCell">
          <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
          <el-table-column label="校区" prop="deptSchoolName" width=""></el-table-column>
          <el-table-column label="学生姓名" @click="showStudentCourses" prop="student.name" width="">
            <template slot-scope="scope">
              <el-button type="text" @click="showStudentCourses(scope.row)">{{scope.row.student.name}}</el-button>
            </template>
          </el-table-column>
          <el-table-column label="学生电话" prop="student.mobile" width=""></el-table-column>
          <el-table-column label="课程" width="">
            <template slot-scope="scope">
              {{scope.row.course.dictCourseName}}{{scope.row.course.courseIndexName}}
            </template>
          </el-table-column>
          <el-table-column label="老师" prop="course.teacherName" width=""></el-table-column>
          <el-table-column label="开始时间" prop="beginTime" :formatter="baseTableFormatDate" width=""></el-table-column>
          <el-table-column label="过期时间" prop="expireTime" :formatter="baseTableFormatDate" width=""></el-table-column>
          <el-table-column label="升班时间" prop="riseClassTime" :formatter="baseTableFormatDate"
                           width=""></el-table-column>
          <el-table-column label="操作" width="">
            <template slot-scope="scope">
              <el-button @click="showRefBills(scope.row)"
                         type="text" size="mini" class="">关联票据
              </el-button>
              <template v-if="scope.row.isActive && parseInt(scope.row.isActive) === 1">
                <el-button @click="setAsDeActive(scope.row.id)"
                           type="text" size="mini" class="">置为失效
                </el-button>
              </template>
            </template>
          </el-table-column>
        </el-table>
      </el-container>
    </el-col>
    <!--    学生，所报的所有课程的弹框-->
    <StudentCoursesDialogForSC
      :dialog-visible="studentCoursesDialogVisible"
      :student-courses="studentCourses"
      :dialog-title="studentCoursesDialogTitle"
      @dialogClose="refBillDialogClose"
    />
    <!--    学生，所报的所有开票记录的弹框-->
    <SCBillsDialogForSC
      :dialog-visible="scBillDialogVisible"
      :bills="scBills"
      :dialog-title="scBillDialogTitle"
      @dialogClose="refBillDialogClose"
    />
  </el-row>

</template>

<script>
  import StudentCoursesDialogForSC from "./StudentCoursesDialogForSC";
  import SCBillsDialogForSC from "./SCBillsDialogForSC";

  export default {
    name: 'CourseStudents',
    components: {StudentCoursesDialogForSC, SCBillsDialogForSC},
    data() {
      return {
        courseLoading: false,
        courseInfo: '',
        queryConditionLoaded: false,
        currentStudentCourseId: null,
        classTypes: [],
        courseIndexes: [],
        activeArray: [],
        // 学生课程弹框信息 begin
        studentCoursesDialogVisible: false,
        studentCourses: [],
        studentCoursesDialogTitle: '',
        // 学生课程弹框信息 end
        // 学生课程开票信息弹框 begin
        scBillDialogVisible: false,
        scBills: [],
        scBillDialogTitle: '',
        // 学生课程开票信息弹框 end
        course: {
          dictCourseName: null,
          teacherName: null,
          courseIndexName: null,
        },
        page: {
          list: [],
          total: null,
        },
        query: {
          pageNo: 1,
          pageSize: 200,
          orderBy: 'update_date DESC',
          data: {
            isExpired: 0,
            isActive: 1,
            courseId: null,
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

    // 作用到查询条件上，查询条件充分后再显示查询按钮
    watch: {
      query: {
        handler(val) {
          console.log(JSON.stringify(val));
        },
        deep: true,
      },
      // 变更的时候重新加载
      '$route': function (to, from) {
        const _this = this;
        if (to.path.indexOf("/courseStudents") >= 0) {
          const courseIdTmp = to.params.courseId;
          console.log("courseId = " + _this.query.data.courseId);
          if (courseIdTmp !== _this.query.data.courseId) {
            console.log("course id 由 " + _this.query.data.courseId + " 变更为 " + courseIdTmp);
            _this.query.data.courseId = courseIdTmp;
            _this.initPage();
          }
        }
      }
    },

    mounted: function () {
      console.log("mounted");
      const _this = this;
      _this.activeArray = _this.appYesNoArray();
      const courseQueryId = _this.$route.query.courseId;
      const courseParamId = _this.$route.params.courseId;
      const courseId = courseQueryId ? courseQueryId : courseParamId;
      if (courseId && parseInt(courseId) > 0) {
        console.log("courseId =" + courseId);
        _this.query.data.courseId = courseId;
        _this.initPage();
      } else {
        alert("无法加载页面，课程ID数据没有传入")
      }
    },

    methods: {
      // 查看当前学生的课程
      showStudentCourses(row) {
        const _this = this;
        console.log("show student id = " + row.id + " courses");
        if (row.studentCourses) {
          _this.openStudentCoursesDialog(row);
        } else {
          const _this = this;
          _this.loading = true;
          const query = {data: {studentId: row.student.id, isActive: 1}};
          _this.httpUtils.appPost('/studentCourse/listPage', query).then(function (res) {
            _this.loading = false;
            row.studentCourses = res.data.list;
            _this.openStudentCoursesDialog(row);
          }, _this.operateFail);
        }
      },
      // 查看关联的票据
      showRefBills(row) {
        const _this = this;
        console.log("show student id = " + row.id + " courses");
        if (row.bills) {
          _this.openSCBillDialog(row);
        } else {
          const _this = this;
          _this.loading = true;
          _this.httpUtils.appGet('/bill/listByStudentCourseId?studentCourseId=' + row.id).then(function (res) {
            _this.loading = false;
            row.bills = res.data;
            _this.openSCBillDialog(row);
          }, _this.operateFail);
        }
      },
      // 打开学生-课程的弹框
      openStudentCoursesDialog(row) {
        const _this = this;
        _this.studentCoursesDialogVisible = true;
        _this.studentCourses = row.studentCourses;
        _this.studentCoursesDialogTitle = row.student.name;
      },
      // 打开学生-课程-票据的弹框
      openSCBillDialog(row) {
        const _this = this;
        _this.scBillDialogVisible = true;
        _this.scBills = row.bills;
        _this.scBillDialogTitle = row.student.name + " - " + row.course.dictCourseName + row.course.courseIndexName;
      },

      // 进入表单
      enterTableCell(row, column, cell, event) {
        // if (column.property === 'student.name') {
        //   console.log("enter cell , student id = " + row.student.id);
        // }
      },
      // 离开表单
      leaveTableCell(row, column, cell, event) {
        // if (column.property === 'student.name') {
        //   console.log("leave cell , student id = " + row.student.id);
        // }
      },
      initPage() {
        this.getCourse();
        this.searchFunc();
      },

      refBillDialogClose() {
        this.studentCoursesDialogVisible = false;
        this.scBillDialogVisible = false;
      },

      setAsDeActive(id) {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/studentCourse/setAsDeActive?id=' + id).then(function (res) {
          _this.loading = false;
          this.baseSuccessNotify("操作成功");
          this.listStudentCourse();
        }, _this.operateFail);
      },
      // 查询事件
      searchFunc() {
        console.log("student course search function。param=" + JSON.stringify(this.query));
        this.listStudentCourse();
      },
      // 查询功能
      listStudentCourse() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/studentCourse/listPage', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.list = res.data.list;
          _this.page.total = res.data.total;
        }, _this.operateFail);
      },

      // 查询功能
      getCourse() {
        const _this = this;
        _this.courseLoading = true;
        _this.httpUtils.appGet('/course/get?id=' + _this.query.data.courseId).then(function (res) {
          _this.course = res.data;
          _this.courseLoading = false;
          _this.generateCourseInfo();
        }, _this.operateFail);
      },

      // 课程信息
      generateCourseInfo() {
        this.courseInfo = this.course.semesterName + " " + this.course.deptSchoolName + " " +
          this.course.dictCourseName + this.course.courseIndexName + " " + this.course.teacherName;
      },
      operateSuccess() {
        const _this = this;
        _this.listStudentCourse();
      },
      // 操作失败
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(JSON.stringify(r));
        _this.loading = false;
        _this.courseLoading = false;
      },

    }
  }
</script>

<style scoped>

</style>
