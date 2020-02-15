<template>
  <el-container id="body">

    <el-header class="body">
      <span>学生课程</span>
    </el-header>

    <!--查询表单-->
    <el-form label-width="100px" class="search-form" size="mini">
      <DeptSchoolSelect @dataChange="deptSchoolIdChange"
                        @deptSchoolIdInitFinish="deptSchoolIdInitFinish"></DeptSchoolSelect>
      <ModelMultiSelect :model-for-select="classTypes" model-label="选择班级：" @dataChange="classTypesChange"/>
      <ModelMultiSelect :model-for-select="courseIndexes" model-label="选择班别：" @dataChange="courseIndexesChange"/>
      <el-form-item label="姓名：" size="mini" class="inlineFormItem">
        <el-input size="mini" style="width: 120px" v-model="query.data.studentNameLike" placeholder="学生姓名"></el-input>
      </el-form-item>
      <el-form-item label="手机号：" size="mini" class="inlineFormItem  ">
        <el-input size="mini" style="width: 120px" v-model="query.data.studentMobileLike"
                  placeholder="学生手机号"></el-input>
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
      <template v-if="queryConditionLoaded">
        <el-form-item class="inlineFormItem">
          <el-button @click="searchFunc" size="mini" class="bill-list-search" type="primary" plain icon="el-icon-search"
                     round>查询
          </el-button>
        </el-form-item>
      </template>
    </el-form>

    <!--    数据表格-->
    <el-container style="width: 100%">
      <el-table v-loading="loading" :data="page.list" class="bill-table" style="width: 100%">
        <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
        <el-table-column label="校区" prop="deptSchoolName" width=""></el-table-column>
        <el-table-column label="学生姓名" prop="student.name" width=""></el-table-column>
        <el-table-column label="学生电话" prop="student.mobile" width=""></el-table-column>
        <el-table-column label="课程" width="">
          <template slot-scope="scope">
            {{scope.row.course.dictCourseName}}{{scope.row.course.courseIndexName}}
          </template>
        </el-table-column>
        <el-table-column label="老师" prop="course.teacherName" width=""></el-table-column>
        <el-table-column label="开始时间" prop="beginTime" :formatter="baseTableFormatDate" width=""></el-table-column>
        <el-table-column label="过期时间" prop="expireTime" :formatter="baseTableFormatDate" width=""></el-table-column>
        <el-table-column label="升班时间" prop="riseClassTime" :formatter="baseTableFormatDate" width=""></el-table-column>
        <el-table-column label="操作" width="">
          <template slot-scope="scope">
            <el-button @click="showRefBills(scope.row.id)"
                       type="text" size="mini" class="">关联票据
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-container>

    <el-pagination
      class="common-page"
      background
      layout="total,prev, pager, next"
      :total="page.total"
      :page-size="query.pageSize"
      :current-page="query.pageNo"
      @current-change="currentPage"
      @prev-click="prevPage"
      @next-click="nextPage"
    ></el-pagination>
    <StudentCourseBillsDialog
      :dialog-visible="detailVisible"
      :studentCourseId="currentStudentCourseId"
      @dialogClose="refBillDialogClose"
    />
  </el-container>

</template>

<script>
  import DeptSchoolSelect from "./forSelect/DeptSchoolSelect";
  import ModelMultiSelect from "./forSelect/ModelMultiSelect";
  import StudentCourseBillsDialog from "./StudentCourseBillsDialog";

  export default {
    name: 'StudentCourse',
    components: {ModelMultiSelect, DeptSchoolSelect, StudentCourseBillsDialog},
    data() {
      return {
        queryConditionLoaded: false,
        currentStudentCourseId: null,
        detailVisible: false,
        classTypes: [],
        courseIndexes: [],
        activeArray: [],
        page: {
          total: 0,
          list: [],
        },
        query: {
          pageNo: 1,
          pageSize: 10,
          orderBy: 'update_date DESC',
          data: {
            isActive: 1,
            deptSchoolIds: [],
            dictCourseIdList: [],
            courseIndexList: [],
            studentNameLike: '',
            studentMobileLike: '',
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
          this.conditionLoaded();
        },
        deep: true,
      },
    },

    mounted: function () {
      const _this = this;
      _this.fillClassTypes();
      _this.fillClassIndexes();
      _this.activeArray = _this.appYesNoArray();
    },

    methods: {
      conditionLoaded() {
        if (this.query.data.dictCourseIdList.length > 0 && this.query.data.deptSchoolIds.length > 0) {
          this.queryConditionLoaded = true;
        }
      },
      showRefBills(id) {
        this.currentStudentCourseId = id;
        this.detailVisible = true;
      },
      refBillDialogClose() {
        this.detailVisible = false;
      },

      // 查询条件班级改变
      classTypesChange(val) {
        this.query.data.dictCourseIdList = val;
      },
      // 查询条件班别改变
      courseIndexesChange(val) {
        this.query.data.courseIndexList = val;
      },
      // 加载班级
      fillClassTypes() {
        const _this = this;
        _this.httpUtils.appGet('/classType/list').then(function (res) {
          _this.classTypes = res.data;
        }, _this.operateFail);
      },
      // 加载班别
      fillClassIndexes() {
        const _this = this;
        _this.httpUtils.appGet('/course/listClassIndex').then(function (res) {
          _this.courseIndexes = res.data;
        }, _this.operateFail);
      },
      // 部门ID加载完成
      deptSchoolIdInitFinish() {
        console.log("deptSchoolIdInitFinish");
      },
      // 查询事件
      searchFunc() {
        console.log("student course search function。param=" + JSON.stringify(this.query));
        this.listStudentCourse();
      },
      // 部门ID变化
      deptSchoolIdChange(val) {
        this.query.data.deptSchoolIds = val;
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

      operateSuccess() {
        const _this = this;
        _this.listStudentCourse();
      },


      // 尝试分页
      currentPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listStudentCourse();
      },
      // 上一页
      prevPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listStudentCourse();
      },
      // 下一页
      nextPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listStudentCourse();
      },
      // 操作失败
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(JSON.stringify(r));
        _this.loading = false;
      },

    }
  }
</script>

<style scoped>

</style>
