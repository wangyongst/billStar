<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">

          <SchoolSelect @dataChange="schoolChange"></SchoolSelect>
          <el-row>
            <el-col :span="6" :offset="16">
              <el-button @click="listArreas" type="primary" style="width: 100px;" size="mini" plain round>查询</el-button>
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

          <el-table-column label="姓名" width="150" align="left" prop="studentName">
          </el-table-column>

          <el-table-column label="欠费金额" width="150" align="left" prop="arrears">
          </el-table-column>

          <el-table-column label="到期时间" width="150" align="left" prop="expireTime" :formatter="baseTableFormatDate">
          </el-table-column>

          <el-table-column label="课程" width="300" align="left" prop="courseId" :formatter="baseFormatCourse">
          </el-table-column>

          <el-table-column label="教师" width="100" align="left" prop="teacherName">
          </el-table-column>

          <el-table-column fixed="right" label="操作" align="left" width="180">
            <template slot-scope="scope">
              <el-button type="text" size="mini">交费记录</el-button>
              <el-button type="text" size="mini" class="">补费</el-button>
              <el-button type="text" size="mini" @click="updateArrears(scope.row.id)">修改金额</el-button>
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
    </el-row>
  </div>
</template>

<script>
  import ClassSelect from "../select/ClassSelect";
  import SchoolSelect from "../select/SchoolSelect";
  import SubjectSelect from "../select/SubjectSelect";
  import TeacherSelect from "../select/TeacherSelect";
  import BackToWork from "../back/BackToWork";

  export default {
    name: 'WorkArrears',
    components: {BackToWork, ClassSelect, SchoolSelect, SubjectSelect, TeacherSelect},
    data() {
      return {
        page: {
          total: 0,
          records: [],
        },
        query: {
          current: 1,
          size: 10,
          deptSchoolIds: []
        },
        loading: false
      }
    },

    mounted: function () {
      const _this = this;
      _this.listArreas();
    },

    methods: {
      schoolChange(val) {
        this.query.data.schoolIds = val;
      },

      updateArrears(id) {
        const _this = this;
        this.$prompt('请输入新金额', '修改', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.updateArrearsPost(id, value))
          .catch(() => {
          });
      }
      ,
      updateArrearsPost(sid, value) {
        const _this = this;
        const cmd = {id: sid, arrears: value};
        _this.httpUtils.appPost('/student/main/updateArrears', cmd).then(function (res) {
          _this.listArreas();
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },

      listArreas() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/course/pageArrears', _this.query).then(function (res) {
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
