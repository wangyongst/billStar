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

          <el-table-column label="姓名" width="150" align="left" prop="name">
          </el-table-column>

          <el-table-column label="到期时间" width="150" align="left" prop="expireTime" :formatter="baseTableFormatDate">
          </el-table-column>

          <el-table-column label="课程" width="300" align="left" prop="courseTime" :formatter="baseFormatCourse">
          </el-table-column>

          <el-table-column label="教师" width="100" align="left" prop="teacherName">
          </el-table-column>

          <el-table-column fixed="right" label="操作" align="left" width="180">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="updateItem(scope.row.studentId)">退费</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>

      <el-dialog class="bill-dialog" title="退费"
                 :visible.sync="createDialogVisible">
        <el-form :model="charge">
          <el-form-item label="科目">
            <el-select v-model="charge.chargeId" placeholder="请选择收费类型">
              <el-option v-for="item in chargeSelect" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="金额">
            <el-input v-model="charge.amount" class="width100"></el-input>
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
  import ClassSelect from "../select/ClassSelect";
  import SchoolSelect from "../select/SchoolSelect";
  import SubjectSelect from "../select/SubjectSelect";
  import TeacherSelect from "../select/TeacherSelect";
  import BackToWork from "../back/BackToWork";

  export default {
    name: 'WorkTui',
    components: {BackToWork, ClassSelect, SchoolSelect, SubjectSelect, TeacherSelect},
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
          schoolIds: []
        },
        charge: {
          studentId: null,
          chargeId: null,
          amount: null
        },
        chargeSelect: [],
        loading: false
      }
    },

    mounted: function () {
      const _this = this;
      _this.listChargeSelect()
      _this.listArreas();
    },

    methods: {
      schoolChange(val) {
        this.query.schoolIds = val;
      },
      updateArrears(id) {
        const _this = this;
        this.$prompt('请输入退费金额', '修改', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.updateXuPost(id, value))
          .catch(() => {
          });
      },

      updateItem(item) {
        const _this = this;
        _this.charge = {studentId: item};
        _this.createDialogVisible = true;
      },

      dialogCancel() {
        this.createDialogVisible = false;
      },
      chargePost(sid) {
        const _this = this;
        _this.httpUtils.appPost('/student/main/tui', _this.charge).then(function (res) {
          _this.listArreas();
          _this.createDialogVisible = false;
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },

      listChargeSelect() {
        const _this = this;
        _this.httpUtils.appGet('/sys/charge/list').then(function (res) {
          _this.chargeSelect = res;
        }, _this.operateFail);
      },


      listArreas() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/student/course/pageXu', _this.query).then(function (res) {
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
