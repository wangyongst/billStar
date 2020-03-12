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
          <WeekSelect @dataChange="dayChange"></WeekSelect>
          <el-form-item label="教师姓名：">
            <el-input size="mini" style="width: 120px" v-model="query.teacherNameLike" placeholder="教师姓名"></el-input>
            <el-button class="btn-search" @click="listCourse" size="mini" plain round>查询</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="2">
      <el-col :span="2">
        <el-button type="primary" size="medium " style="font-size:medium"><p>{{page.courseCount.studentTotal}}</p>总人数</el-button>
      </el-col>
      <el-col :span="2">
        <el-button type="primary" size="medium " style="font-size:medium"><p>{{page.courseCount.courseTotal}}</p>总科次</el-button>
      </el-col>
      <el-col :span="2"  v-for="item in page.courseCount.subjectCounts">
        <el-button type="danger" size="medium " style="font-size:medium"><p>{{item.total}}</p>{{item.name}}</el-button>
      </el-col>
      <el-col :span="2">
        <el-button type="primary" size="medium " style="font-size:medium"><p>{{page.courseCount.mankeTotal}}</p>满科次</el-button>
      </el-col>
      <el-col :span="2">
        <el-button type="primary" size="medium " style="font-size:medium"><p>{{page.courseCount.manbanlv}}</p>满班率</el-button>
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
  import WeekSelect from "../select/WeekSelect";

  export default {
    name: 'CourseTable',
    components: {WeekSelect, SemesterSelect, BackToWork, ClassSelect, SchoolSelect, SubjectSelect},
    data() {
      return {
        page: {
          courseCount: {
            studentTotal: 0,
            courseTotal: 0,
            subjectCounts: [],
            mankeTotal: 0,
            manbanlv: "0.00%",
          },
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
          days: [],
        },
        loading: false
      }
    },
    mounted: function () {
      const _this = this;
      _this.listCourse();
    }
    ,

    methods: {
      schoolChange(val) {
        this.query.schoolIds = val;
      },
      subjectChange(val) {
        this.query.subjectIds = val;
      },
      semesterChange(val) {
        this.query.semesterIds = val;
      },
      classChange(val) {
        this.query.classIds = val;
      },
      dayChange(val) {
        this.query.days = val;
      },
      courseFormatter(row, col, val) {
        return row["subjectName"] + row["className"] + "+" + row["classNo"]
      },
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
          _this.page.records = res.list;
          _this.page.courseCount = res.object;
          console.log(_this.page);
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
