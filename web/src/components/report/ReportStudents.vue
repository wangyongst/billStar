<template>
  <el-container id="body">

    <el-header class="body">学生信息</el-header>

    <el-form label-width="100px" class="search-form">
      <SchoolSelect></SchoolSelect>
      <el-form-item label="姓名：" class="inlineFormItem">
        <el-input size="mini" style="width: 120px" v-model="query.nameLike" placeholder="姓名"></el-input>
      </el-form-item>
      <el-form-item label="手机号：" class="inlineFormItem  ">
        <el-input size="mini" style="width: 120px" v-model="query.mobileLike" placeholder="手机号"></el-input>
      </el-form-item>
      <el-form-item label="是否欠费：" class="inlineFormItem" size="mini">
        <el-select v-model="query.isrrears"
                   placeholder="欠费"
                   clearable
                   size="mini"
                   style="width:80px">
          <el-option label="是" :value="1"></el-option>
          <el-option label="否" :value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="inlineFormItem">
        <el-button @click="listStudent" size="mini" class="bill-list-search" type="primary" plain icon="el-icon-search"
                   round>查询
        </el-button>
      </el-form-item>
    </el-form>

    <el-container style="width: 100%">
      <el-table v-loading="loading" :data="page.records" class="bill-table">
        <el-table-column label="序号" type="index" width="60" align="center"></el-table-column>
        <el-table-column prop="schoolName" label="校区" width=""></el-table-column>
        <el-table-column prop="name" label="姓名" width=""></el-table-column>
        <el-table-column prop="mobile" label="电话" width="120"></el-table-column>
        <el-table-column prop="arrears" label="欠费" width="60"></el-table-column>
        <el-table-column prop="sex" label="性别" :formatter="baseSex" width=""></el-table-column>
        <el-table-column prop="myschool" label="学校" width=""></el-table-column>
        <el-table-column prop="myclass" label="班级" width=""></el-table-column>
        <el-table-column prop="address" label="住址" width=""></el-table-column>
        <el-table-column prop="type" label="休学" :formatter="isStop" width="60" align="center"></el-table-column>
        <el-table-column prop="createName" label="创建人" width="" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" :formatter="baseTableFormatTime" width="100" align="center"></el-table-column>
<!--        <el-table-column fixed="right" label="操作" align="center" width="150">-->
<!--&lt;!&ndash;          <template slot-scope="scope">&ndash;&gt;-->
<!--&lt;!&ndash;            <template v-if="scope.row.type === 3">&ndash;&gt;-->
<!--&lt;!&ndash;              <el-button @click="reinstate(scope.row)" type="text" size="mini" class="">复学</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;            </template>&ndash;&gt;-->
<!--&lt;!&ndash;            <template v-else>&ndash;&gt;-->
<!--&lt;!&ndash;              <el-button @click="suspend(scope.row)" type="text" size="mini" class="">休学</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;            </template>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-button @click="updateStudent(scope.row)" type="text" size="mini" class="">修改</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-button @click="deleteStudent(scope.row)" type="text" size="mini" class="">删除</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          </template>&ndash;&gt;-->
<!--        </el-table-column>-->
      </el-table>
    </el-container>

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

    <!--    <el-dialog class="bill-dialog" title="修改信息" :visible.sync="dialogVisible">-->
    <!--      <el-form :model="student">-->
    <!--        <el-form-item label="姓名" :label-width="formLabelWidth">-->
    <!--          <el-input v-model="student.name" autocomplete="off"></el-input>-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="手机号" :label-width="formLabelWidth">-->
    <!--          <el-input v-model="student.mobile" autocomplete="off"></el-input>-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="性别" :label-width="formLabelWidth">-->
    <!--          <el-radio-group v-model="student.gender">-->
    <!--            <el-radio :label='1'>男</el-radio>-->
    <!--            <el-radio :label='2'>女</el-radio>-->
    <!--          </el-radio-group>-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="学校" :label-width="formLabelWidth">-->
    <!--          <el-input v-model="student.schoolName" autocomplete="off"></el-input>-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="住址" :label-width="formLabelWidth">-->
    <!--          <el-input v-model="student.address" autocomplete="off"></el-input>-->
    <!--        </el-form-item>-->
    <!--      </el-form>-->
    <!--      <div slot="footer" class="dialog-footer">-->
    <!--        <el-button @click="dialogCancel">取 消</el-button>-->
    <!--        <el-button type="primary" @click="dialogPost">确 定</el-button>-->
    <!--      </div>-->
    <!--    </el-dialog>-->

  </el-container>

</template>

<script>

  import SchoolSelect from "../select/SchoolSelect";

  export default {
    name: 'ReportStudent',
    components: {SchoolSelect},
    data() {
      return {
        page: {
          total: 0,
          records: []
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: [],
          isrrears: null,
          mobileLike: null,
          nameLike: null,
        }
        ,
        loading: true,
      }
    },
    mounted: function () {
      const _this = this;
      _this.listStudent();
    },
    methods: {
      isStop(val) {
        if (val && val == 3) return "是";
        return "否";
      },
      deptSchoolIdChange(val) {
        this.query.data.deptSchoolIds = val;
      },
      listStudent() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/main/pageV', _this.query).then(function (res) {
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
