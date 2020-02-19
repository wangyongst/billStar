<template>
  <!--查询表单-->
  <el-form label-width="100px" class="search-form" size="mini">
    <DeptSchoolSelect @dataChange="deptSchoolIdChange"
                      @deptSchoolIdInitFinish="deptSchoolIdInitFinish"></DeptSchoolSelect>
    <ModelMultiSelect :model-for-select="classTypes" model-label="选择班级：" @dataChange="classTypesChange"/>
    <ModelMultiSelect :model-for-select="courseIndexes" model-label="选择班别：" @dataChange="courseIndexesChange"/>
    <ModelMultiSelect :model-for-select="billTypeForSelect" model-label="选择类型：" @dataChange="billTypeChange"/>
    <!--      支付方式筛选-->
    <el-form-item label="支付方式：" class="inlineFormItem" size="mini">
      <el-select v-model="query.data.payTypeId"
                 placeholder="请选择"
                 clearable
                 style="width:120px">
        <el-option v-for="item in payTypeForSelect"
                   :key="item.id"
                   :label="item.name"
                   :value="item.id"></el-option>
      </el-select>
    </el-form-item>
    <!--到期时间筛选-->
    <el-form-item label="到期时间：" class="inlineFormItem" size="mini">
      <el-date-picker
        v-model="query.data.expireEndTime"
        type="date"
        placeholder="到期时间早于该日期"
        style="width:170px">
      </el-date-picker>
    </el-form-item>

    <!--学期-筛选-->
    <el-form-item label="学期：" class="inlineFormItem" size="mini">
      <el-select v-model="query.data.semesterId"
                 placeholder="请选择"
                 clearable
                 style="width:120px">
        <el-option v-for="item in semesterForSelect"
                   :key="item.id"
                   :label="item.name"
                   :value="item.id"></el-option>
      </el-select>
    </el-form-item>

    <!--开票时间区间筛选-->
    <el-form-item label="开票时间：" class="inlineFormItem" size="mini">
      <el-date-picker
        size="mini"
        v-model="dateValue"
        type="daterange"
        align="right"
        unlink-panels
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :picker-options="pickerOptions">
      </el-date-picker>
    </el-form-item>
    <el-form-item label="学生姓名：" class="inlineFormItem marginTop5" size="mini">
      <el-input style="width: 120px" v-model="query.data.studentName" placeholder="学生姓名"></el-input>
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

    <el-form-item class="inlineFormItem  " size="mini">
      <el-button @click="searchFunc" size="mini" class="bill-list-search" type="primary"
                 plain icon="el-icon-search" round>查询
      </el-button>
      <el-button @click="create" type="success" size="mini" plain round class="btn-create">新增</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import DeptSchoolSelect from "./forSelect/DeptSchoolSelect";
  import ModelMultiSelect from "./forSelect/ModelMultiSelect";

  export default {
    name: 'BillSearchForm',
    components: {DeptSchoolSelect, ModelMultiSelect},
    data() {
      return {
        hasArrearsArray: [],
        payTypeForSelect: [],
        semesterForSelect: [],
        dateValue: [],
        classTypes: [],
        courseIndexes: [],
        billTypeForSelect: [
          {id: 1, name: "新生"},
          {id: 2, name: "续费"},
          {id: 3, name: "补费"},
          {id: 4, name: "退费"},
          {id: 5, name: "转校"},
          {id: 6, name: "转期"}
        ],
        query: {
          pageNo: 1,
          pageSize: 10,
          orderBy: 'bill_time DESC',
          data: {
            semesterId: null,
            deptSchoolIds: [],
            courseIndexList: [],
            dictCourseIdList: [],
            typeList: [],
            expireEndTime: null,
            payTypeId: null,
            startDate: null,
            endDate: null,
            studentName: "",
            hasArrears: null,
          }
        },
        loading: true,
        pickerOptions: {
          shortcuts: [
            {
              text: '今天',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                picker.$emit('pick', [start, end]);
              }
            }, {
              text: '昨天',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24);
                end.setTime(end.getTime() - 3600 * 1000 * 24);
                picker.$emit('pick', [start, end]);
              }
            },
            {
              text: '最近一周',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                picker.$emit('pick', [start, end]);
              }
            }, {
              text: '最近一个月',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                picker.$emit('pick', [start, end]);
              }
            }, {
              text: '最近三个月',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                picker.$emit('pick', [start, end]);
              }
            }]
        },
      }

    }
    ,
    mounted: function () {
      const _this = this;
      _this.listChargeTypeForSelect();
      _this.hasArrearsArray = _this.appYesNoArray();
      _this.fillClassIndexes();
      _this.fillClassTypes();
      _this.fillSemesters();
    }
    ,
    methods: {
      billTypeChange(val) {
        this.query.data.typeList = val;
      },
      classTypesChange(val) {
        this.query.data.dictCourseIdList = val;
      },
      courseIndexesChange(val) {
        this.query.data.courseIndexList = val;
      },
      create() {
        this.$emit('createClick');
      },
      // 加载学期
      fillSemesters() {
        const _this = this;
        _this.httpUtils.appGet('/semester/list').then(function (res) {
          _this.semesterForSelect = res.data;
          for (const index in _this.semesterForSelect) {
            const item = _this.semesterForSelect[index];
            if (item.isDefault && parseInt(item.isDefault) === 1) {
              _this.query.data.semesterId = item.id;
              break;
            }
          }
        }, _this.operateFail);
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
      deptSchoolIdInitFinish() {
        console.log("receive deptSchoolId init");
        // this.searchFunc();
      }
      ,
      searchFunc() {
        const _this = this;
        if (_this.dateValue && _this.dateValue.length >= 2) {
          _this.query.data.startDate = _this.dateValue[0];
          _this.query.data.endDate = _this.dateValue[1];
        } else {
          _this.query.data.startDate = null;
          _this.query.data.endDate = null;
        }
        this.$emit("searchFunction", _this.query);
      },
      deptSchoolIdChange(val) {
        this.query.data.deptSchoolIds = val;
        console.log("deptSchoolIds changed to " + JSON.stringify(val));
      }
      ,
      listChargeTypeForSelect() {
        const _this = this;
        _this.httpUtils.appGet('/chargeType/list').then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.payTypeForSelect = res.data;
          } else {
            _this.baseErrorNotify(res.msg);

          }
        }, _this.operateFail);
      },
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(JSON.stringify(r));
        _this.loading = false;
      }
      ,
    }
  }
</script>

<style scoped>

</style>
