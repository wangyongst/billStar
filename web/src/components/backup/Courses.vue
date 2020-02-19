<template>
  <el-row id="body">

    <el-col :span="24">
      <el-form label-width="100px" class="search-form">
        <DeptSchoolSelect @dataChange="deptSchoolIdChange"
                          @deptSchoolIdInitFinish="deptSchoolIdInitFinish"></DeptSchoolSelect>
        <ModelMultiSelect :model-for-select="classTypes" model-label="选择班级：" @dataChange="classTypesChange"/>
        <ModelMultiSelect :model-for-select="courseIndexes" model-label="选择班别：" @dataChange="courseIndexesChange"/>
        <!--教师-->
        <el-form-item clearable size="mini" label="教师" class="inlineFormItem">
          <el-input v-model="query.teacherNameLike" placeholder="请输入教师名" class="width120"></el-input>
        </el-form-item>
        <!--学期-筛选-->
        <el-form-item label="学期：" class="inlineFormItem" size="mini">
          <el-select v-model="query.semesterId"
                     placeholder="请选择"
                     clearable
                     style="width:120px">
            <el-option v-for="item in semesterForSelect"
                       :key="item.id"
                       :label="item.name"
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <!--        -->
        <el-form-item class="inlineFormItem">
          <el-button @click="searchFunc" size="mini" v-bind:disabled="this.loading">查询</el-button>
          <el-button @click="create" type="success" size="mini" plain round class="btn-create">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <el-col :span="24">
      <el-container style="width: 100%">
        <el-table v-loading="loading" :data="list" class="bill-table" style="width: 100%">
          <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
          <el-table-column prop="semesterName" label="学期" width=""></el-table-column>
          <el-table-column prop="deptSchoolName" label="校区" width="80"></el-table-column>
          <el-table-column :formatter="combineClassInfo" label="科目-班级班别" width="120">
            <template slot-scope="scope">
              <!--              <router-link :to="{ path: 'courseStudents?courseId='+scope.row.id}">{{combineClassInfo(scope.row)}}</router-link>-->
              <router-link :to="{ path: 'courseStudents/'+scope.row.id}">
                {{combineClassInfo(scope.row)}}
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="studentNumber" label="标准人数" width="80"></el-table-column>
          <el-table-column prop="currentStudentNumber" label="实际人数" width="80"></el-table-column>
          <el-table-column prop="fullClassRate" label="满班率" width="80" align="center"></el-table-column>
          <el-table-column prop="teacherName" label="教师" width=""></el-table-column>
          <el-table-column prop="classroomNo" label="教室号" width=""></el-table-column>
          <el-table-column prop="regularTime" label="上课时间" width=""></el-table-column>
          <el-table-column prop="createByName" label="创建人" width=""></el-table-column>
          <el-table-column prop="createDate" label="创建时间" :formatter="baseTableFormatTime"
                           width="160"></el-table-column>
          <el-table-column fixed="right" label="操作" align="left" width="150">
            <template slot-scope="scope">
              <el-button @click="updateCourse(scope.row)" type="text" size="mini" class="">修改</el-button>
              <el-button @click="deleteCourse(scope.row)" type="text" size="mini" class="">删除</el-button>
              <el-button @click="refreshCnt(scope.row.id)" type="text" size="mini" class="">刷新</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-container>
    </el-col>
    <!-- create dialog -->
    <el-dialog class="bill-dialog" v-bind:title="course.type === 0 ?'新增课程' : '修改课程'"
               :visible.sync="createDialogVisible">
      <el-form :model="course" size="mini">
        <el-form-item label="学期" :label-width="formLabelWidth">
          <el-select v-model="course.semesterId" placeholder="请选择学期" @change="deptSchoolChange"
                     v-bind:disabled="isSemesterDisabled">
            <el-option v-for="item in semesterForSelect" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="校区" :label-width="formLabelWidth">
          <el-select v-model="course.deptSchoolId" placeholder="请选择校区" @change="deptSchoolChange">
            <el-option v-for="item in deptSchools" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="班级" :label-width="formLabelWidth">
          <el-select v-model="course.dictCourseId" placeholder="请选择班级">
            <el-option v-for="item in classTypes" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="班别" :label-width="formLabelWidth">
          <el-select v-model="course.courseIndex" placeholder="请选择班别">
            <el-option v-for="item in courseIndexes" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标准人数" :label-width="formLabelWidth">
          <el-input v-model="course.studentNumber" class="width150"/>
        </el-form-item>
        <el-form-item label="教师" :label-width="formLabelWidth">
          <el-select v-model="course.teacherId" placeholder="请选择教师">
            <el-option v-for="item in teachers" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="教室号" :label-width="formLabelWidth">
          <el-input v-model="course.classroomNo" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="上课时间" :label-width="formLabelWidth">
          <el-input v-model="course.regularTime" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCancel">取 消</el-button>
        <el-button type="primary" @click="dialogPost">确 定</el-button>
      </div>
    </el-dialog>
    <!--    -->

  </el-row>

</template>

<script>
  import DeptSchoolSelect from "./forSelect/DeptSchoolSelect";
  import ModelMultiSelect from "./forSelect/ModelMultiSelect";

  export default {
    name: 'Courses',
    components: {DeptSchoolSelect, ModelMultiSelect},
    data() {
      return {
        isSemesterDisabled: true,
        semesterForSelect: [],
        createDialogVisible: false,
        formLabelWidth: '120px',
        // 列表数据
        list: [],
        // 查询参数
        query: {
          semesterId: null,
          teacherNameLike: null,
          dictCourseIdList: [],
          courseIndexList: [],
          deptSchoolIds: [],
          dictCourseId: null,
          teacherId: null,
          courseIndex: null,
          orderBy: 'create_date DESC'
        },
        // 新增课程，要素
        course: {
          type: 0,
          semesterName: "-",
          semesterId: null,
          teacherId: null,
          dictCourseId: null,
          courseIndex: null,
          regularTime: null,
          classroomNo: null,
          studentNumber: 0,
        },
        //
        deptSchools: [],
        teachers: [],
        classTypes: [],
        courseIndexes: [],
        loading: true,
      }
    },
    mounted: function () {
      const _this = this;
      _this.deptSchools = _this.loginUserDeptSchools();
      _this.fillClassTypes();
      // _this.fillTeachers();
      _this.fillClassIndexes();
      _this.fillSemesters();
    },
    methods: {
      // 加载学期
      fillSemesters() {
        const _this = this;
        _this.httpUtils.appGet('/semester/list').then(function (res) {
          _this.semesterForSelect = res.data;
          for (const index in _this.semesterForSelect) {
            const item = _this.semesterForSelect[index];
            if (item.isDefault && parseInt(item.isDefault) === 1) {
              _this.query.semesterId = item.id;
              _this.course.semesterId = item.id;
              break;
            }
          }
        }, _this.operateFail);
      },
      refreshCnt(id) {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/course/refreshCnt?courseId=' + id).then(function (res) {
          _this.baseSuccessNotify("操作成功");
          _this.loading = false;
          _this.listCourse();
        }, _this.operateFail);
      },
      classTypesChange(val) {
        this.query.dictCourseIdList = val;
      },
      courseIndexesChange(val) {
        this.query.courseIndexList = val;
      },
      deptSchoolIdInitFinish() {
        const _this = this;
        _this.listCourse();
      },
      searchFunc() {
        const _this = this;
        _this.listCourse();
      },
      deptSchoolIdChange(val) {
        this.query.deptSchoolIds = val;
      },
      deptSchoolChange(val) {
        const _this = this;
        _this.clearTeachers();
        _this.fillTeachers(val);
      },
      clearTeachers() {
        this.teachers = [];
      },
      fillTeachers(deptId) {
        const _this = this;
        _this.httpUtils.appPost('/teacher/listTeacher?deptSchoolId=' + deptId).then(function (res) {
          _this.teachers = res.data;
        }, _this.operateFail);
      },

      fillClassTypes() {
        const _this = this;
        _this.httpUtils.appGet('/classType/listVO').then(function (res) {
          _this.classTypes = res.data;
        }, _this.operateFail);
      },

      fillClassIndexes() {
        const _this = this;
        _this.httpUtils.appGet('/course/listClassIndex').then(function (res) {
          _this.courseIndexes = res.data;
        }, _this.operateFail);
      },

      listCourse() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/course/list', _this.query).then(function (res) {
          _this.loading = false;
          _this.list = res.data;
        }, _this.operateFail);
      },


      dialogCancel() {
        this.createDialogVisible = false;
        this.course = {};
      },


      dialogPost() {
        const _this = this;
        if (_this.course.type === 0) {
          _this.createPost();
        } else if (_this.course.type === 1) {
          _this.updateCoursePost();
        }
      },


      create() {
        this.isSemesterDisabled = false;
        this.createDialogVisible = true;
        this.course = {type: 0};
      },


      createPost() {
        const _this = this;
        _this.httpUtils.appPost('/course/create', _this.course).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
            _this.listCourse();
            _this.course = {};
            _this.createDialogVisible = false;
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },


      updateCourse(courseIn) {
        const _this = this;
        _this.isSemesterDisabled = true;
        _this.course = courseIn;
        _this.course.type = 1;
        _this.createDialogVisible = true;
        _this.fillTeachers(courseIn.deptSchoolId);
      },


      updateCoursePost() {
        const _this = this;
        _this.httpUtils.appPost('/course/update', _this.course).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
            _this.listCourse();
            _this.course = {};
            _this.createDialogVisible = false;
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },


      deleteCourse(model) {
        const _this = this;
        const msg = "确定删除 " + model.dictCourseName + model.courseIndexName + " ,教师为 " + model.teacherName + " 的课程";
        _this.baseConfirmDelete(msg, function () {
          _this.deleteCoursePost(model)
        });
      },


      deleteCoursePost(cmd) {
        const _this = this;
        _this.httpUtils.appGet('/course/delete?id=' + cmd.id).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
            _this.listCourse();
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },


      combineClassInfo(row, col, val) {
        return row.dictCourseName + row.courseIndexName;
      },


      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r.msg);
        _this.loading = false;
      },


    }
  }
</script>

<style scoped>

</style>
