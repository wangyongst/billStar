<template>
  <el-form-item label="选择班级：" size="mini" class="deptSchoolSelect">
    <el-checkbox-group v-model="deptSchoolIds">
      <el-checkbox size="mini"
                   :indeterminate="isIndeterminate"
                   v-model="checkAll"
                   @change="handleCheckAllChange"
                   border>全选
      </el-checkbox>
      <el-checkbox size="mini"
                   v-for="item in deptSchoolForSelect"
                   :label="item.id"
                   :key="item.id">{{item.name}}
      </el-checkbox>
    </el-checkbox-group>
  </el-form-item>

</template>
<script>
  export default {
    name: 'ClassSelect',
    props: {
      deptQueryUrl: {
        required: false,
        default: '/sys/class/listBySubject'
      },
      subjectIds: null
    },
    data() {
      const _this = this;
      return {
        checkAll: true,
        deptSchoolIds: [],
        deptSchoolForSelect: [],
        isIndeterminate: true
      };
    },

    computed: {
      newSubjectIds() {
        return this.subjectIds;
      }
    },

    watch: {
      deptSchoolIds(val) {
        this.$emit("dataChange", val);
      },
      newSubjectIds(val) {
        const _this = this;
        const cmd = {subjectIds: val};
        _this.httpUtils.appPost(_this.deptQueryUrl, cmd).then(function (res) {
          _this.deptSchoolForSelect = res;
          _this.processCheckAll();
          _this.$emit("deptSchoolIdInitFinish")
        }, _this.operateFail);
      },
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r);
        _this.loading = false;
      },
    },

    mounted() {
      const _this = this;
      console.log(_this.subjectIds);
    },

    methods: {
      handleCheckAllChange() {
        const _this = this;
        _this.checkAll = !_this.checkAll;
        _this.processCheckAll();
      },

      processCheckAll() {
        const _this = this;
        _this.isIndeterminate = _this.checkAll;
        if (!_this.checkAll) {
          _this.deptSchoolIds = [];
          return;
        }
        if (_this.deptSchoolForSelect && _this.deptSchoolForSelect.length > 0) {
          _this.deptSchoolIds = _this.deptSchoolForSelect.map(function (ele) {
            return ele.id
          });
        }
      },
    }
  }
</script>
