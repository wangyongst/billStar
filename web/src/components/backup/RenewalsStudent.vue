<template>
  <div>

    <BackToWork></BackToWork>

    <el-row>
      <el-col :span="24">
        <el-form label-width="100px" class="search-form" size="mini">
          <el-row>
            <el-form-item label="学生姓名：" class="inlineFormItem marginTop5" size="mini">
              <el-input style="width: 120px" placeholder="学生姓名"></el-input>
            </el-form-item>

            <el-form-item label="电话：" class="inlineFormItem marginTop5" size="mini">
              <el-input style="width: 120px" placeholder="电话"></el-input>
            </el-form-item>

            <el-form-item label="续费金额：" class="inlineFormItem marginTop5" size="mini">
              <el-input style="width: 120px" placeholder="续费金额"></el-input>
            </el-form-item>

            <el-form-item label="" class="inlineFormItem marginTop5" size="mini">
              <div style="width: 200px"></div>
            </el-form-item>

            <el-form-item class="inlineFormItem  " size="mini" align="right">
              <el-button size="mini" type="primary" plain icon="el-icon-success" round>保存</el-button>
              <el-button size="mini" type="primary" plain icon="el-icon-tickets" round>打印</el-button>
            </el-form-item>

          </el-row>

          <el-row>
            <el-form-item label="科目：" class="inlineFormItem" size="mini">
              <el-select
                placeholder="请选择"
                clearable
                style="width:120px">
                <el-option> 1</el-option>
                <el-option> 2</el-option>
                <el-option> 3</el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="班级：" class="inlineFormItem" size="mini">
              <el-select
                placeholder="请选择"
                clearable
                style="width:120px">
                <el-option> 1</el-option>
                <el-option> 2</el-option>
                <el-option> 3</el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="班别：" class="inlineFormItem" size="mini">
              <el-select
                placeholder="请选择"
                clearable
                style="width:120px">
                <el-option> 1</el-option>
                <el-option> 2</el-option>
                <el-option> 3</el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="到期时间：" class="inlineFormItem" size="mini">
              <el-date-picker
                type="date"
                placeholder="到期时间早于该日期"
                style="width: 200px">
              </el-date-picker>
            </el-form-item>

            <el-form-item class="inlineFormItem  " size="mini" align="right">
              <el-button size="mini" type="success" plain icon="el-icon-circle-plus" round>增加</el-button>
            </el-form-item>

          </el-row>
        </el-form>
      </el-col>
      <!--    数据表格-->
      <el-col :span="24">
        <el-table v-loading="loading" :data="page.list" class="bill-table" size="" style="width: 100%">
          <el-table-column label="" type="index" width="40" align="center">
          </el-table-column>
          <el-table-column label="在学课程" width="150" align="center">
          </el-table-column>
          <el-table-column label="在学课程" width="" align="center">
          </el-table-column>
          <el-table-column label="教师" width="60" align="center">
          </el-table-column>
          <el-table-column label="到期时间" width="500" align="center">
          </el-table-column>
        </el-table>
      </el-col>

      <el-col :span="24">
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
      </el-col>
    </el-row>
  </div>
</template>

<script>

  import BackToWork from "../back/BackToWork";

  export default {
    name: 'RenewalsStudent2',
    components: {BackToWork},
    data() {
      return {
        page: {
          total: 0,
          list: [],
        },
        query: {
          pageNo: 1,
          pageSize: 10,
          orderBy: 'bill_time DESC',
          data: {
            deptSchoolIds: [],
            expireEndTime: null,
            payTypeId: null,
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

    mounted: function () {
      const _this = this;
      eventBus.$on("billOperateSuccess", function () {
        _this.listBills();
      })
    },

    methods: {
      listBills() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appPost('/bill/listPage', _this.query).then(function (res) {
          _this.loading = false;
          _this.page.list = res.data.list;
          _this.page.total = res.data.total;
        }, _this.operateFail);
      },
      // 尝试分页
      currentPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listBills();
      }
      ,
      prevPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listBills();
      }
      ,
      nextPage(page) {
        const _this = this;
        _this.query.pageNo = page;
        _this.listBills();
      }
      ,
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
