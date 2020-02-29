<template>
  <el-form-item label="选择老师：" size="mini" class="deptSchoolSelect">
    <el-checkbox-group v-model="deptSchoolIds">
      <el-checkbox size="mini"
                   :indeterminate="isIndeterminate"
                   v-model="checkAll"
                   @change="handleCheckAllChange"
                   border>全选
      </el-checkbox>
      <el-checkbox size="mini"
                   v-for="item in deptSchoolForSelect"
                   :label="item.name"
                   :key="item.id">{{item.name}}
      </el-checkbox>
    </el-checkbox-group>
  </el-form-item>

</template>
<script>
  export default {
    name: 'TeacherSelect',
    props: {
      deptQueryUrl: {
        required: false,
        default: '/teacher/listAllTeacher'
      }
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

    watch: {
      deptSchoolIds(val) {
        this.$emit("dataChange", val);
      }
    },

    mounted() {
      const _this = this;
      _this.listDeptSchoolForSelect();
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
            return ele.name
          });
        }
      },

      listDeptSchoolForSelect() {
        const _this = this;
        _this.httpUtils.appGet(_this.deptQueryUrl).then(function (res) {
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
    }
  }
</script>
