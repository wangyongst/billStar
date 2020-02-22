<template>
  <el-form-item label="选择校区：" size="mini" class="deptSchoolSelect">
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
    name: 'SchoolSelect',
    props: {
      deptQueryUrl: {
        required: false,
        default: '/schoolZone/listSchoolZone'
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

    created() {
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
            return ele.id
          });
        }
      },

      listDeptSchoolForSelect() {
        const _this = this;
        _this.httpUtils.appGet(_this.deptQueryUrl).then(function (res) {
          _this.deptSchoolForSelect = res;
          _this.processCheckAll();
          _this.$emit("initFinish")
        }, _this.operateFail);
      },
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r.msg);
        _this.loading = false;
      },
    }
  }
</script>
