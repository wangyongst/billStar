<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">

          <SchoolSelect @dataChange="schoolChange"></SchoolSelect>
          <SubjectSelect @dataChange="subjectChange"></SubjectSelect>
          <SemesterSelect @dataChange="semesterChange"></SemesterSelect>
          <el-row>
            <el-col :span="6" :offset="16">
              <el-button @click="listCourse" type="primary" style="width: 100px;" size="mini" plain round>查询</el-button>
              <el-button @click="newCourse" type="success" style="width: 100px;" size="mini" plain round>新增</el-button>
            </el-col>
          </el-row>

        </el-form>

      </el-col>
      <!--    数据表格-->
      <el-col :span="24">
        <el-table v-loading="loading" :data="page.records" class="bill-table" size="" style="width: 100%">
          <el-table-column label="" type="index" width="40" align="center">
          </el-table-column>

          <el-table-column label="校区" width="150" align="left" prop="schoolName">
          </el-table-column>

          <el-table-column label="教室号" width="100" align="left" prop="classRoom">
          </el-table-column>

          <el-table-column label="科目" width="150" align="left" prop="subjectName">
          </el-table-column>

          <el-table-column label="班级" width="150" align="left" prop="className">
          </el-table-column>

          <el-table-column label="班别" width="100" align="left" prop="classNo">
          </el-table-column>
          <el-table-column label="班额" width="100" align="left" prop="classNum">
          </el-table-column>
          <el-table-column label="教师" width="100" align="left" prop="teacherName">
          </el-table-column>
          <el-table-column label="上课时间" width="400" align="left" prop="courseTime">
          </el-table-column>
          <el-table-column fixed="right" label="操作" align="left" width="150">
            <template slot-scope="scope">
              <el-button @click="updateCourse(scope.row.id)" type="text" size="mini">修改
              </el-button>
              <el-button @click="deleteCourse(scope.row.id)" type="text" size="mini">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>

      <el-col :span="24">
        <el-pagination
          class="common-page"
          background
          layout="total,prev, pager, next"
          :total="page.total"
          :page-size="query.size"
          :current-page="query.current"
          @current-change="gotoPage"
          @prev-click="gotoPage"
          @next-click="gotoPage"
        ></el-pagination>
      </el-col>
      <NewCourseDialog :onclose="listCourse"></NewCourseDialog>
      <UpdateCourseDialog :id="updateId"></UpdateCourseDialog>
    </el-row>
  </div>
</template>

<script>
  import SchoolSelect from "../select/SchoolSelect";
  import SubjectSelect from "../select/SubjectSelect";
  import BackToWork from "../back/BackToWork";
  import SemesterSelect from "../select/SemesterSelect";
  import NewCourseDialog from "../dialog/NewCourseDialog";
  import UpdateCourseDialog from "../dialog/UpdateCourseDialog";

  export default {
    name: 'CourseManager',
    components: {UpdateCourseDialog, NewCourseDialog, SemesterSelect, BackToWork, SchoolSelect, SubjectSelect},
    data() {
      return {
        updateId: null,
        page: {
          total: 0,
          records: [],
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: [],
          subjectIds: [],
          semesterIds: [],
        },
        loading: false
      }
    },

    mounted: function () {
      const _this = this;
      _this.listCourse();
    },

    methods: {
      newCourse() {
        eventBus.$emit("newCourse");
      },
      updateCourse(val) {
        const _this = this;
        _this.updateId = val;
        eventBus.$emit("updateCourse");
      },
      schoolChange(val) {
        this.query.schoolIds = val;
      },
      subjectChange(val) {
        this.query.subjectIds = val;
      },
      semesterChange(val) {
        this.query.semesterIds = val;
      },
      deleteCourse(id) {
        const _this = this;
        _this.httpUtils.appPost('/course/main/delete?id=' + id).then(function (res) {
          _this.listCourse();
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },
      listCourse() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/course/main/page', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.records = res.records;
          _this.page.total = res.total;
        }, _this.operateFail);
      },
      gotoPage(page) {
        const _this = this;
        _this.query.current = page;
        _this.listCourse();
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
