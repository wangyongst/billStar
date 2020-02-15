<template>
  <el-row id="body">

    <el-col :span="24">
      <el-form label-width="100px" class="search-form">
        <!--        校区-->
        <DeptSchoolSelect @dataChange="deptSchoolIdChange"></DeptSchoolSelect>
        <!--        学期-->
        <el-form-item label="学期：" size="mini">
          <el-select v-model="query.semesterId"
                     placeholder="请选择"
                     clearable
                     style="width:120px" size="mini">
            <el-option v-for="item in semesterForSelect"
                       :key="item.id"
                       :label="item.name"
                       :value="item.id"></el-option>
          </el-select>
          <!--        查询按钮-->
          <el-button class="btn-search FR marginRight20" @click="searchFunc" size="mini">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <el-col :span="24">
      <el-container style="width: 100%">
        <el-table stripe v-loading="loading" :data="list" class="bill-table">
          <el-table-column label="校区" prop="deptSchoolName" width="80" align="center"></el-table-column>
          <!--        动态列-->
          <template v-for="item in dynamicColumn">
            <el-table-column v-bind:label="item.label"
                             v-bind:prop="item.prop" width="" align="center">
              <template slot-scope="scope">
                {{showCellValue(scope.row , item.prop)}}
              </template>
            </el-table-column>
          </template>
          <el-table-column label="总计(在校/超期)" prop="deptTotal" :formatter="formatRowCellValue" width=""
                           align="center"></el-table-column>
        </el-table>
      </el-container>
    </el-col>

  </el-row>

</template>

<script>
  import DeptSchoolSelect from "./forSelect/DeptSchoolSelect";

  export default {
    name: 'SubjectStudentReport',
    components: {DeptSchoolSelect},
    data() {
      return {
        list: [],
        query: {
          deptSchoolIds: [],
          semesterId: null,
        },
        dynamicColumn: [],
        deptSchoolForSelect: [],
        semesterForSelect: [],
        loading: false,
      }
    },
    mounted: function () {
      this.listSemesterForSelect();
    },
    methods: {
      showCellValue(row, key) {
        const val = row.subjectStudentMap[key];
        return this.formatCellValue(val);
      },
      formatCellValue(val) {
        if (!val || val === "" || val === '') {
          return "0/0";
        }
        const jsonVal = JSON.stringify(val);
        console.log(jsonVal);
        const json = this.deepCopy(val);
        return json.inDateCnt + ' / ' + json.outDateCnt;
      },
      formatRowCellValue(row, col, val) {
        return this.formatCellValue(val);
      },
      searchFunc() {
        const _this = this;
        console.log(JSON.stringify(_this.query));
        _this.loading = true;
        _this.httpUtils.appPost('/report/subjectStudentReport', _this.query).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.dynamicColumn = res.data.dynamicTitle;
            _this.list = res.data.content;
            _this.loading = false;
          } else {
            _this.baseErrorNotify(res.msg);
            _this.loading = false;
          }
        }, _this.operateFail);
      },
      // 加载学期
      listSemesterForSelect() {
        const _this = this;
        _this.httpUtils.appGet('/semester/list').then(function (res) {
          _this.semesterForSelect = res.data;
          for (const index in _this.semesterForSelect) {
            const item = _this.semesterForSelect[index];
            if (item.isDefault && parseInt(item.isDefault) === 1) {
              _this.query.semesterId = item.id;
              break;
            }
          }
        }, _this.operateFail);
      },
      deptSchoolIdChange(val) {
        this.query.deptSchoolIds = val;
      },
      //
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r.msg);
        _this.loading = false;
      },

    }
  }
</script>

<style scoped>

</style>
