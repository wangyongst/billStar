<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">
          <SchoolSelect></SchoolSelect>
          <SubjectSelect></SubjectSelect>
          <ClassSelect></ClassSelect>
          <TeacherSelect></TeacherSelect>
          <SemesterSelect></SemesterSelect>
        </el-form>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
          </div>
          <div>
            <el-row>
              <el-col :span="4">
                <div class="grid-content bg-purple">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>121212</p>总人数</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>121212</p>总科目</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content bg-purple"></div>
                <el-card class="box-card">
                  <div>
                    <el-button type="primary"><p>121212</p>美术</el-button>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="4">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>121212</p>书法</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>121212</p>满科次</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>121212</p>满班率</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-table v-loading="loading" :data="page.records" class="bill-table">
        <el-table-column prop="classRoom" label="教室"></el-table-column>
        <el-table-column prop="day" label="天"></el-table-column>
        <el-table-column label="课程" :formatter="courseFormatter"></el-table-column>
        <el-table-column label="上课时间" :formatter="courseTimeFormatter"></el-table-column>
        <el-table-column prop="teacherName" label="教师"></el-table-column>
        <el-table-column prop="studentNum" label="现有人数"></el-table-column>
        <el-table-column prop="classNum" label="核定人数"></el-table-column>
        <el-table-column prop="" label="满班率"></el-table-column>
        <el-table-column prop="schoolName" label="校区"></el-table-column>
      </el-table>
    </el-row>

  </div>
</template>

<script>
  import ClassSelect from "../select/ClassSelect";
  import SchoolSelect from "../select/SchoolSelect";
  import SubjectSelect from "../select/SubjectSelect";
  import BackToWork from "../back/BackToWork";
  import TeacherSelect from "../select/TeacherSelect";
  import SemesterSelect from "../select/SemesterSelect";

  export default {
    name: 'CourseTable',
    components: {SemesterSelect, TeacherSelect, BackToWork, ClassSelect, SchoolSelect, SubjectSelect},
    data() {
      return {
        updateId: null,
        page: {
          records: [],
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: [],
          subjectIds: [],
          semesterIds: [],
          classIds: [],
        },
        loading: false
      }
    },

    mounted: function () {
      const _this = this;
      _this.listCourse();
    },

    methods: {
      schoolChange(val) {
        this.query.schoolIds = val;
      },
      courseFormatter(row, col, val) {
        return row["className"] + "+" + row["classNo"]
      },
      courseTimeFormatter(row, col, val) {
        const _this = this;
        return _this.formatterTime(row["begin"]) + "-" + _this.formatterTime(row["end"])
      },
      formatterTime(val) {
        if (!val) {
          return "-"
        }
        return val.substring(0, val.length - 3);
      },
      subjectChange(val) {
        this.query.subjectIds = val;
      },
      semesterChange(val) {
        this.query.semesterIds = val;
      },
      listCourse() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/course/main/list', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.records = res;
        }, _this.operateFail);
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
