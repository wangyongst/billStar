<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">

          <SchoolSelect @dataChange="schoolChange"></SchoolSelect>

          <el-form-item label="姓名：" class="inlineFormItem">
            <el-input size="mini" style="width: 120px" v-model="query.studentNameLike" placeholder="姓名"></el-input>
          </el-form-item>

          <el-row>
            <el-col :span="6" :offset="16">
              <el-button @click="listStudentCourse" type="primary" style="width: 100px;" size="mini" plain round>查询</el-button>
            </el-col>
          </el-row>
        </el-form>


      </el-col>
      <!--    数据表格-->
      <el-col :span="24">
        <el-table v-loading="loading" :data="page.records" class="bill-table" size="" style="width: 100%">
          <el-table-column label="" type="index" width="40" align="center">
          </el-table-column>

          <el-table-column prop="schoolName" label="校区" width="200"></el-table-column>
          <el-table-column prop="name" label="姓名" width="200"></el-table-column>
          <el-table-column prop="mobile" label="电话" width="200"></el-table-column>
          <el-table-column label="课程" width="300" align="left" prop="name" :formatter="baseFormatCourse">
          </el-table-column>
          <el-table-column prop="teacherName" label="教师" width="200" align="center"></el-table-column>
          <el-table-column prop="expireTime" label="到期时间" :formatter="baseTableFormatTime" width="" align="center"></el-table-column>

          <el-table-column fixed="right" label="操作" align="rigth" width="180">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="updateItem(scope.row)">转班</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>

      <el-dialog class="bill-dialog" title="转班"
                 :visible.sync="createDialogVisible">
        <el-form>
          <el-form-item label="科目">
            <el-select v-model="ban.subjectId" placeholder="请选择科目" @change="listClassSelelect">
              <el-option v-for="item in page.subjects" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="班级">
            <el-select v-model="ban.classId" placeholder="请选择班级">
              <el-option v-for="item in page.classes" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="班别">
            <el-select v-model="ban.classNoId" placeholder="请选择班别" @change="getOneCourse">
              <el-option v-for="item in page.classnos" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="上课时间">
            <el-input v-model="page.courseTime"></el-input>
          </el-form-item>
          <el-form-item label="教师">
            <el-input v-model="page.teacherName"></el-input>
          </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogCancel">取 消</el-button>
          <el-button type="primary" @click="chargePost">确 定</el-button>
        </div>
      </el-dialog>

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
  import SchoolSelect from "../select/SchoolSelect";
  import BackToWork from "../back/BackToWork";

  export default {
    name: 'StudentBan',
    components: {BackToWork, SchoolSelect},
    data() {
      return {
        createDialogVisible: false,
        page: {
          total: 0,
          records: [],
          subjects: [],
          classes: [],
          classnos: [],
          courseTime: null,
          teacherName: null
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: [],
          studentNameLike: null
        },
        ban: {
          studentId: null,
          subjectId: null,
          classId: null,
          classNoId: null,
          courseId: null,
          newId: null
        },
        loading: false
      }
    },

    mounted: function () {
      const _this = this;
      _this.listSubjectSelect();
      _this.listClassNoSelect();
      _this.listStudentCourse();

    }
    ,

    methods: {
      updateItem(item) {
        const _this = this;
        _this.ban.courseId = item["courseId"];
        _this.ban.studentId = item["studentId"];
        _this.ban.newId = null;
        _this.page.courseTime = null
        _this.page.teacherName = null;
        _this.createDialogVisible = true;
      },
      listStudentCourse() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/course/pageBan', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.records = res.records;
          _this.page.total = res.total;
        }, _this.operateFail);
      }
      ,
      listSubjectSelect() {
        const _this = this;
        _this.httpUtils.appGet('/sys/subject/list').then(function (res) {
          _this.page.subjects = res;
        }, _this.operateFail);
      },

      listClassNoSelect() {
        const _this = this;
        _this.httpUtils.appGet('/sys/class/no/list').then(function (res) {
          _this.page.classnos = res;
        }, _this.operateFail);
      },

      listClassSelelect() {
        const _this = this;
        const cmd = {subjectId: _this.ban.subjectId};
        _this.httpUtils.appPost('/sys/class/listClass', cmd).then(function (res) {
          _this.page.classes = res;
        }, _this.operateFail);
      },

      getOneCourse() {
        const _this = this;
        _this.httpUtils.appPost('/course/main/get', _this.ban).then(function (res) {
          _this.ban.newId = res.id;
          _this.page.teacherName = res.teacherName;
          _this.page.courseTime = res.courseTime;
        }, _this.operateFail);
      },

      schoolChange(val) {
        this.query.schoolIds = val;
      }
      ,
      dialogCancel() {
        this.createDialogVisible = false;
      }
      ,
      chargePost(sid) {
        const _this = this;
        _this.httpUtils.appPost('/student/course/ban', _this.ban).then(function (res) {
          _this.listStudentCourse();
          _this.createDialogVisible = false;
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      }
      ,
      gotoPage(page) {
        const _this = this;
        _this.query.current = page;
        _this.listStudentCourse();
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
