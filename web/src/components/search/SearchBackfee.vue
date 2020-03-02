<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">

          <SchoolSelect></SchoolSelect>

          <el-form-item label="退费时间：" size="mini">
            <el-radio-group>
              <el-radio size="mini">
                最近&nbsp;&nbsp;
                <el-input size="mini" placeholder="月数" style="width: 20px"></el-input>
                &nbsp;&nbsp;&nbsp;&nbsp;月
              </el-radio>
            </el-radio-group>
          </el-form-item>

          <TeacherSelect></TeacherSelect>

        </el-form>

      </el-col>
      <!--    数据表格-->
      <el-col :span="24">
        <el-table v-loading="loading" :data="page.list" class="bill-table" size="" style="width: 100%">
          <el-table-column label="" type="index" width="40" align="center">
          </el-table-column>

          <el-table-column label="校区" width="150" align="left" prop="schoolName">
          </el-table-column>

          <el-table-column label="原因" width="150" align="left">
          </el-table-column>

          <el-table-column label="最后课程" width="150" align="left">
          </el-table-column>

          <el-table-column label="姓名" width="150" align="left" prop="name">
          </el-table-column>

          <el-table-column label="金额" width="150" align="left" prop="amount">
          </el-table-column>

          <el-table-column label="教师" width="150" align="left" prop="teacherName">
          </el-table-column>

          <el-table-column label="退费时间" width="150" align="left" prop="createTime">
          </el-table-column>

          <el-table-column label="备注" width="150" align="left">
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
    </el-row>
  </div>
</template>

<script>
  import ClassSelect from "../select/ClassSelect";
  import SchoolSelect from "../select/SchoolSelect";
  import SubjectSelect from "../select/SubjectSelect";
  import BackToWork from "../back/BackToWork";
  import TeacherSelect from "../select/TeacherSelect";

  export default {
    name: 'SearchBackfee',
    components: {TeacherSelect, BackToWork, ClassSelect, SchoolSelect, SubjectSelect},
    data() {
      return {
        page: {
          total: 0,
          list: [],
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: [],
          teacherIds: [],
          month: null,
        },
        loading: false
      }

    },

    mounted: function () {
      const _this = this;
      _this.listStudentCourse();
    },

    methods: {
      schoolIdChange(val) {
        this.query.schoolIds = val;
      },
      listStudentCourse() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/course/pageTui', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.list = res.records;
          _this.page.total = res.total;
        }, _this.operateFail);
      },
      schoolChange(val) {
        this.query.schoolIds = val;
      },
      gotoPage(page) {
        const _this = this;
        _this.query.current = page;
        _this.listStudentCourse();
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
