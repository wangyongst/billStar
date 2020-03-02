<template>
  <div>
    <BackToWork></BackToWork>

    <el-row>

      <!--查询表单-->
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">

          <SchoolSelect @dataChange="schoolChange"></SchoolSelect>

          <el-form-item label="姓名：" class="inlineFormItem">
            <el-input size="mini" style="width: 120px" v-model="query.nameLike" placeholder="姓名"></el-input>
          </el-form-item>

          <el-row>
            <el-col :span="6" :offset="16">
              <el-button @click="listStudent" type="primary" style="width: 100px;" size="mini" plain round>查询</el-button>
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
          <el-table-column prop="createName" label="创建人" width="200" align="center"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" :formatter="baseTableFormatTime" width="" align="center"></el-table-column>

          <el-table-column fixed="right" label="操作" align="rigth" width="180">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="updateItem(scope.row.id)">休学</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>

      <el-dialog class="bill-dialog" title="休学"
                 :visible.sync="createDialogVisible">
        <el-form>
          <el-form-item label="预计复学时间">
            <el-date-picker
              v-model="xiu.fuTime"
              type="date"
              placeholder="选择日期">
            </el-date-picker>
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
    name: 'StudentXiu',
    components: {BackToWork, SchoolSelect},
    data() {
      return {
        createDialogVisible: false,
        page: {
          total: 0,
          records: [],
        },
        query: {
          current: 1,
          size: 10,
          schoolIds: [],
          nameList: null
        },
        xiu: {
          studentId: null,
          fuTime: null
        },
        loading: false
      }
    }
    ,

    mounted: function () {
      const _this = this;
      _this.listStudent();
    }
    ,

    methods: {
      updateItem(item) {
        const _this = this;
        _this.xiu.studentId = item;
        _this.createDialogVisible = true;
      }
      ,
      listStudent() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/main/pageXiu', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.records = res.records;
          _this.page.total = res.total;
        }, _this.operateFail);
      }
      ,
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
        _this.httpUtils.appPost('/student/main/xiu', _this.xiu).then(function (res) {
          _this.listStudent();
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
