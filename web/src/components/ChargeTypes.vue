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
        <el-table-column prop="createByName" label="创建人" width="" align="center"></el-table-column>
        <el-table-column prop="createDate" label="创建时间" :formatter="baseTableFormatTime" width=""
                         align="center"></el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="220" >
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
    name: 'ChargeTypes',
    data() {
      return {
        list: [],
        query: {
          orderBy: 'create_date DESC'
        },
        loading: false,
      }
    },
    mounted: function () {
      const _this = this;
      _this.listChargeType();
    },
    methods: {
      listChargeType() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/chargeType/list').then(function (res) {
          _this.loading = false;
          _this.list = res.data;
        }, _this.operateFail);
      },
      //
      deleteChargeType(id) {
        const _this = this;
        _this.httpUtils.appPost('/chargeType/delete?id=' + id).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg, function () {

            });
            _this.listChargeType();
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },
      //
      updateName(id) {
        const _this = this;
        this.$prompt('请输入新名称', '修改', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.updateNamePost(id, value))
          .catch(() => {
          });
      },
      updateNamePost(sid, name) {
        const _this = this;
        const cmd = {id: sid, name: name};
        _this.httpUtils.appPost('/chargeType/update', cmd).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg, function () {

            });
            _this.listChargeType();
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },
      //
      create() {
        const _this = this;
        this.$prompt('请输入新名称', '新增', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.createPost(value))
          .catch(() => {
          });
      },
      createPost(name) {
        const _this = this;
        _this.httpUtils.appPost('/chargeType/create?name=' + name).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg, function () {
              _this.listChargeType();
            });
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
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
