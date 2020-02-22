<template>
  <el-container id="body">

    <el-header class="body">
      收费类型管理
      <el-button @click="create()" style="float: right;margin-top:13px;" type="primary" plain size="small"
                 class="normalHide">新增
      </el-button>
    </el-header>

    <el-container style="width: 100%">
      <el-table v-loading="loading" :data="list" Charge="bill-table">
        <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="类型" width=""></el-table-column>
        <el-table-column prop="createName" label="创建人" width="" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" :formatter="baseTableFormatTime" width="" align="center"></el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="220">
          <template slot-scope="scope">
            <el-button class="normalHide" @click="updateName(scope.row.id)" round type="text" size="small">修改
            </el-button>
            <el-button class="normalHide" @click="deleteChargeType(scope.row.id)" round type="text" size="small">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-container>
  </el-container>
</template>

<script>

  export default {
    name: 'SettingCharge',
    data() {
      return {
        list: [],
        loading: false,
      }
    },
    created: function () {
      const _this = this;
      _this.listChargeType();
    },
    methods: {
      listChargeType() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/sys/chargeType/list').then(function (res) {
          _this.loading = false;
          _this.list = res;
        }, _this.operateFail);
      },
      saveOrUpdateChargeType(cmd) {
        const _this = this;
        _this.httpUtils.appPost('/sys/chargeType/saveOrUpdate', cmd).then(function (res) {
          _this.listChargeType();
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },
      //
      deleteChargeType(id) {
        const _this = this;
        _this.httpUtils.appPost('/sys/chargeType/delete?id=' + id).then(function (res) {
          _this.listChargeType();
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      }
      ,
      //
      updateName(id) {
        const _this = this;
        this.$prompt('请输入新名称', '修改', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.updateNamePost(id, value))
          .catch(() => {
          });
      }
      ,
      updateNamePost(sid, name) {
        const _this = this;
        const cmd = {id: sid, name: name};
        _this.saveOrUpdateChargeType(cmd);
      }
      ,
      //
      create() {
        const _this = this;
        this.$prompt('请输入新名称', '新增', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.createPost(value))
          .catch(() => {
          });
      }
      ,
      createPost(name) {
        const _this = this;
        const cmd = {name: name};
        _this.saveOrUpdateChargeType(cmd);
      }
      ,
      //
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r);
        _this.loading = false;
      }
      ,
    }
  }
</script>

<style scoped>

</style>
