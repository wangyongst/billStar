<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <el-row>
        <el-form label-width="100px" class="search-form" size="mini">
          <el-row>
            <el-form-item label="姓名：" class="inlineFormItem">
              <el-autocomplete
                class="inline-input"
                v-model="student.id"
                :fetch-suggestions="querySearch"
                placeholder="姓名"
                :trigger-on-focus="false"
                @select="handleSelect"
              ></el-autocomplete>
            </el-form-item>


            <el-form-item label="电话：" class="inlineFormItem">
              <el-input class="label" disabled>{{student.mobile}}</el-input>
            </el-form-item>

            <el-form-item class="inlineFormItem">
              <el-button @click="getOneStudent" type="primary" style="width: 100px;" size="mini" plain round>搜索</el-button>
            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item label="入校时间：" class="inlineFormItem">
              <el-input class="label" disabled>{{student.mobile}}</el-input>
            </el-form-item>

            <el-form-item label="公办学校：" class="inlineFormItem">
              <el-input class="label" disabled>{{student.mobile}}</el-input>
            </el-form-item>

            <el-form-item label="年级：" class="inlineFormItem">
              <el-input class="label" disabled>{{student.mobile}}</el-input>
            </el-form-item>

            <el-form-item label="状态：" class="inlineFormItem">
              <el-input class="label" disabled>{{student.mobile}}</el-input>
            </el-form-item>
          </el-row>
        </el-form>
      </el-row>
      <!--    数据表格-->
      <el-row>
        <el-table v-loading="loading" :data="page.courseList" class="bill-table" size="" style="width: 100%">
          <el-table-column label="在读班级" width="200"></el-table-column>
          <el-table-column prop="name" label="任课教师" width="200"></el-table-column>
          <el-table-column prop="mobile" label="到期时间" width="200"></el-table-column>
          <el-table-column label="升班时间" align="left" prop="name" :formatter="baseFormatCourse">
          </el-table-column>
          <el-table-column prop="teacherName" label="教师" width="200" align="center"></el-table-column>
          <el-table-column prop="expireTime" label="到期时间" :formatter="baseTableFormatTime" width="" align="center"></el-table-column>

        </el-table>
      </el-row>

      <!--    数据表格-->
      <el-row>
        <el-table v-loading="loading" :data="page.courseHistoryList" class="bill-table" size="" style="width: 100%">
          <el-table-column label="升班记录" width="200"></el-table-column>
          <el-table-column prop="name" label="班别" width="200"></el-table-column>
          <el-table-column prop="mobile" label="时间" width="200"></el-table-column>
          <el-table-column label="结束时间" align="left" prop="name" :formatter="baseFormatCourse"></el-table-column>
          <el-table-column prop="teacherName" label="教师" width="200" align="center"></el-table-column>
        </el-table>
      </el-row>

      <!--    数据表格-->
      <el-row>
        <el-table v-loading="loading" :data="page.courseList" class="bill-table" size="" style="width: 100%">
          <el-table-column label="在读班级" width="200"></el-table-column>
          <el-table-column prop="name" label="任课教师" width="200"></el-table-column>
          <el-table-column prop="mobile" label="到期时间" width="200"></el-table-column>
          <el-table-column label="升班时间" align="left" prop="name" :formatter="baseFormatCourse">
          </el-table-column>
          <el-table-column prop="teacherName" label="教师" width="200" align="center"></el-table-column>
          <el-table-column prop="expireTime" label="到期时间" :formatter="baseTableFormatTime" width="" align="center"></el-table-column>

        </el-table>
      </el-row>

    </el-row>
  </div>
</template>

<script>
  import BackToWork from "../back/BackToWork";

  export default {
    name: 'StudentMain',
    components: {BackToWork},
    data() {
      return {
        page: {
          studentList:null,
          courseList: null,
          courseHistoryList: null,
          chargeList: null
        },
        query: {
          current: 1,
          size: 10,
          studentNameLike: null
        },
        student: {
          id: null,
          mobile: null,
          createTime: null,
          myschool: null,
          myclass: null,
          type: null
        },
        loading: false
      }
    },

    methods: {
      querySearch(queryString, cb) {
        const _this = this;
        _this.httpUtils.appGet('/student/main/listByNameLike/'+ queryString).then(function (res) {
          _this.page.studentList = res;
          console.log(_this.page.studentList);
        }, _this.operateFail);
      },
      handleSelect(item) {
        // console.log(item);
      },
      listCourseHistory() {
        const _this = this;
        _this.httpUtils.appGet('/sys/subject/list').then(function (res) {
          _this.page.subjects = res;
        }, _this.operateFail);
      },

      listCourse() {
        const _this = this;
        _this.httpUtils.appGet('/sys/class/no/list').then(function (res) {
          _this.page.classnos = res;
        }, _this.operateFail);
      },
      getOneStudent() {
        const _this = this;
        _this.httpUtils.appGet('/student/main/get/' + _this.student.id,).then(function (res) {
          _this.student = res;
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
