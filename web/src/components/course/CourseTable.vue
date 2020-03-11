<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">
          <SchoolSelect @dataChange="schoolChange"></SchoolSelect>
          <SubjectSelect @dataChange="subjectChange"></SubjectSelect>
          <ClassSelect v-bind:subjectIds="query.subjectIds" @dataChange="classChange"></ClassSelect>
          <SemesterSelect @dataChange="semesterChange"></SemesterSelect>
          <el-form-item label="开票区间：" size="mini">
            <el-radio-group v-model="query.day">
              <el-radio size="mini" label="周一">周一</el-radio>
              <el-radio size="mini" label="周二">周二</el-radio>
              <el-radio size="mini" label="周三">周三</el-radio>
              <el-radio size="mini" label="周四">周四</el-radio>
              <el-radio size="mini" label="周五">周五</el-radio>
              <el-radio size="mini" label="周六">周六</el-radio>
              <el-radio size="mini" label="周日">周日</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="教师姓名：">
            <el-input size="mini" style="width: 120px" v-model="query.teacherNameLike" placeholder="教师姓名"></el-input>
            <el-button class="btn-search" @click="listCourse" size="mini" plain round>查询</el-button>
          </el-form-item>
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
                      <el-button type="primary"><p>{{page.studentTotal}}</p>总人数</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>{{page.courseTotal}}</p>总科次</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content bg-purple"></div>
                <el-card class="box-card">
                  <div>
                    <el-button type="primary"><p>{{page.meishuTotal}}</p>美术</el-button>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="4">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>{{page.shufaTotal}}</p>书法</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>{{page.mankeTotal}}</p>满科次</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>{{page.manbanlv}}</p>满班率</el-button>
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
        <el-table-column label="满班率" :formatter="formatterBan"></el-table-column>
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
  import SemesterSelect from "../select/SemesterSelect";

  export default {
    name: 'CourseTable',
    components: {SemesterSelect, BackToWork, ClassSelect, SchoolSelect, SubjectSelect},
    data() {
      return {
        page: {
          studentTotal: 0,
          courseTotal: 0,
          meishuTotal: 0,
          shufaTotal: 0,
          mankeTotal: 0,
          manbanlv: 0,
          records: [],
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: [],
          subjectIds: [],
          semesterIds: [],
          classIds: [],
          teacherNameLike: null,
          day: null,
        },
        loading: false
      }
    },
    mounted: function () {
      const _this = this;
      _this.listCourseCount();
      _this.listCourse();
    }
    ,

    methods: {
      listCourseCount() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/course/main/count').then(function (res) {
          _this.loading = false;
          _this.page.studentTotal = res.studentTotal;
          _this.page.courseTotal = res.courseTotal;
          _this.page.meishuTotal = res.meishuTotal;
          _this.page.shufaTotal = res.shufaTotal;
          _this.page.mankeTotal = res.mankeTotal;
          _this.page.records = res;
          _this.page.manbanlv = res.manbanlv;
          _this.loading = false;
        }, _this.operateFail);
      }
      ,
      schoolChange(val) {
        this.query.schoolIds = val;
      }
      ,
      subjectChange(val) {
        this.query.subjectIds = val;
      }
      ,
      semesterChange(val) {
        this.query.semesterIds = val;
      }
      ,
      classChange(val) {
        this.query.classIds = val;
      }
      ,
      courseFormatter(row, col, val) {
        return row["className"] + "+" + row["classNo"]
      }
      ,
      courseTimeFormatter(row, col, val) {
        const _this = this;
        return _this.formatterTime(row["begin"]) + "-" + _this.formatterTime(row["end"])
      }
      ,
      formatterTime(val) {
        if (!val) {
          return "-"
        }
        return val.substring(0, val.length - 3);
      }
      ,
      formatterBan(row, col, val) {
        return row["studentNum"] / row["classNum"] * 100 + "%";
      }
      ,
      subjectChange(val) {
        this.query.subjectIds = val;
      }
      ,
      semesterChange(val) {
        this.query.semesterIds = val;
      }
      ,
      listCourse() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/course/main/list', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.records = res;
        }, _this.operateFail);
      }
      ,
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
