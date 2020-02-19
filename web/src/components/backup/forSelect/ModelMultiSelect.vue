<!--
支持多选/全选|通用组件
++++++++++++++++++++++++++++++++++++++++++++++++++++++++
modelForSelect 必传，用来选择。
modelLabel     选传，用来显示这是啥。
——————————————————————————————————————
dataChange     选择内容变化的时候，把结果传出
++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-->
<template>
  <el-form-item v-bind:label="modelLabel" v-bind:size="size" class="modelMultiSelect">
    <el-checkbox-group v-model="modelIds">
      <el-checkbox v-bind:size="size"
                   :indeterminate="isIndeterminate"
                   v-model="checkAll"
                   @change="handleCheckAllChange"
                   border>全选
      </el-checkbox>
      <el-checkbox v-bind:size="size"
                   v-for="item in modelForSelect"
                   :label="item.id"
                   :key="item.id">{{item.name}}
      </el-checkbox>
    </el-checkbox-group>
  </el-form-item>

</template>
<script>
  export default {
    name: 'ModelMultiSelect',
    props: {
      modelForSelect: {
        // 必传的数据
        require: true,
      },
      modelLabel: {
        require: false,
        default: "选择"
      },
      size: {
        require: false,
        default: "mini"
      },
      isIndeterminate: {
        require: false,
        default: true
      }
    },
    data() {
      return {
        checkAll: true,
        modelIds: []
      };
    },

    watch: {
      modelIds(val) {
        // 数据选择变化时，将事件抛出去。
        this.$emit("dataChange", val);
      },
      modelForSelect() {
        // 数据初始化的时候，默认全选的处理
        if (this.isIndeterminate) {
          this.processCheckAll();
        }
      }
    },

    mounted() {
      if (this.isIndeterminate) {
        this.processCheckAll();
      }
    },

    methods: {
      // 处理全选的点击
      handleCheckAllChange() {
        const _this = this;
        _this.checkAll = !_this.checkAll;
        _this.processCheckAll();
      },
      // 全部选择
      processCheckAll() {
        const _this = this;
        _this.isIndeterminate = _this.checkAll;
        if (!_this.checkAll) {
          _this.modelIds = [];
          return;
        }
        if (_this.modelForSelect && _this.modelForSelect.length > 0) {
          _this.modelIds = _this.modelForSelect.map(function (ele) {
            return ele.id;
          });
        }
      },

    }
  }
</script>
