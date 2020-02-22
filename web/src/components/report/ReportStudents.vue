<template>
  <el-container id="body">

    <el-header class="body">学生信息</el-header>

    <el-form label-width="100px" class="search-form">
      <SchoolSelect></SchoolSelect>
      <el-form-item label="姓名：" class="inlineFormItem">
        <el-input size="mini" style="width: 120px" v-model="query.data.nameLike" placeholder="姓名"></el-input>
      </el-form-item>
      <el-form-item label="手机号：" class="inlineFormItem  ">
        <el-input size="mini" style="width: 120px" v-model="query.data.mobileLike" placeholder="手机号"></el-input>
      </el-form-item>
      <el-form-item label="是否欠费：" class="inlineFormItem" size="mini">
        <el-select v-model="query.data.hasArrears"
                   placeholder="欠费"
                   clearable
                   size="mini"
                   style="width:80px">
          <el-option v-for="item in hasArrearsArray"
                     :key="item.id"
                     :label="item.name"
                     :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="inlineFormItem">
        <el-button @click="searchFunc" size="mini" class="bill-list-search" type="primary" plain icon="el-icon-search"
                   round>查询
        </el-button>
      </el-form-item>
    </el-form>

    <el-container style="width: 100%">
      <el-table v-loading="loading" :data="page.list" class="bill-table">
        <el-table-column label="序号" type="index" width="60" align="center"></el-table-column>
        <el-table-column prop="deptSchoolName" label="校区" width=""></el-table-column>
        <el-table-column prop="name" label="姓名" width=""></el-table-column>
        <el-table-column prop="mobile" label="电话" width="120"></el-table-column>
        <el-table-column prop="currentArrears" label="欠费" width="60"></el-table-column>
        <el-table-column prop="gender" label="性别" :formatter="baseSex" width=""></el-table-column>
        <el-table-column prop="schoolName" label="学校" width=""></el-table-column>
        <el-table-column prop="className" label="班级" width=""></el-table-column>
        <el-table-column prop="address" label="住址" width=""></el-table-column>
        <el-table-column prop="isSuspended" label="休学" :formatter="baseYesNo" width="60"
                         align="center"></el-table-column>
        <el-table-column prop="createByName" label="创建人" width="" align="center"></el-table-column>
        <el-table-column prop="createDate" label="创建时间" :formatter="baseTableFormatTime"
                         width="100" align="center"></el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="150">
          <template slot-scope="scope">
            <template v-if="scope.row.isSuspended === 1">
              <el-button @click="reinstate(scope.row)" type="text" size="mini" class="">复学</el-button>
            </template>
            <template v-else>
              <el-button @click="suspend(scope.row)" type="text" size="mini" class="">休学</el-button>
            </template>
            <el-button @click="updateStudent(scope.row)" type="text" size="mini" class="">修改</el-button>
            <el-button @click="deleteStudent(scope.row)" type="text" size="mini" class="">删除</el-button>
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

    <el-dialog class="bill-dialog" title="修改信息" :visible.sync="dialogVisible">
      <el-form :model="student">
        <el-form-item label="姓名" :label-width="formLabelWidth">
          <el-input v-model="student.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号" :label-width="formLabelWidth">
          <el-input v-model="student.mobile" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别" :label-width="formLabelWidth">
          <el-radio-group v-model="student.gender">
            <el-radio :label='1'>男</el-radio>
            <el-radio :label='2'>女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="学校" :label-width="formLabelWidth">
          <el-input v-model="student.schoolName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="住址" :label-width="formLabelWidth">
          <el-input v-model="student.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCancel">取 消</el-button>
        <el-button type="primary" @click="dialogPost">确 定</el-button>
      </div>
    </el-dialog>

  </el-container>

</template>

<script>

  import SchoolSelect from "../select/SchoolSelect";

  export default {
    name: 'ReportStudent',
    components: {SchoolSelect},
    data() {
      return {
        dialogVisible: false,
        formLabelWidth: '120px',
        hasArrearsArray: [],
        page: {
          total: 0,
          list:
            [],
        },
        student: {
          gender: 1,
        },
        query: {
          orderBy: 'create_date DESC',
          pageSize: 10,
          pageNo: 1,
          data: {
            hasArrears: null,
            mobileLike: null,
            nameLike: null,
          }
        }
        ,
        loading: true,
      }
    },
    mounted: function () {
      const _this = this;
      _this.hasArrearsArray = _this.appYesNoArray();
    },
    methods: {
      deptSchoolIdInitFinish() {
        const _this = this;
        _this.listStudent();
      },
      searchFunc() {
        const _this = this;
        _this.listStudent();
      },
      deptSchoolIdChange(val) {
        this.query.data.deptSchoolIds = val;
      },
      listStudent() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/listPage', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.list = res.data.list;
          _this.page.total = res.data.total;
        }, _this.operateFail);
      },
      //
      updateStudent(val) {
        const _this = this;
        _this.student = _this.deepCopy(val);
        _this.dialogVisible = true;
      },

      reinstatePost(val) {
        const _this = this;
        _this.httpUtils.appPost('/student/reinstate?id=' + val.id).then(function () {
          _this.listStudent();
        }, _this.operateFail);
      },
      suspendPost(val) {
        const _this = this;
        _this.httpUtils.appPost('/student/suspend?id=' + val.id).then(function () {
          _this.listStudent();
        }, _this.operateFail);
      },
      reinstate(val) {
        const _this = this;
        const msg = "确定对学生：" + val.name + " 进行复学操作? ";
        _this.baseConfirmDelete(msg, function () {
          _this.reinstatePost(val);
        });
      },
      suspend(val) {
        const _this = this;
        const msg = "确定对学生：" + val.name + " 进行休学操作? ";
        _this.baseConfirmDelete(msg, function () {
          _this.suspendPost(val);
        });
      },
      deleteStudent(val) {
        const _this = this;
        const msg = "删除不可撤销，学生删除后，其开票信息会一起删除。是否继续删除学生：" + val.name + " 的记录? ";
        _this.baseConfirmDelete(msg, function () {
          _this.deleteStudentPost(val.id);
        });
      },
      deleteStudentPost(id) {
        const _this = this;
        _this.httpUtils.appPost('/student/delete?id=' + id).then(function () {
          _this.listStudent();
        }, _this.operateFail);
      },


      dialogCancel() {
        const _this = this;
        _this.dialogVisible = false;
        _this.student = {};
      },
      dialogPost() {
        const _this = this;
        _this.httpUtils.appPost('/student/update', _this.student).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
            _this.listStudent();
            _this.course = {};
            _this.dialogVisible = false;
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },
      // 尝试分页
      currentPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listStudent();
      },
      prevPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listStudent();
      },
      nextPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listStudent();
      },

      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r);
        _this.loading = false;
      },
    }
  }
</script>

<style scoped>

</style>
