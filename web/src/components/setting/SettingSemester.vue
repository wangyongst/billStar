<template>
  <el-row id="body">

    <el-header class="body">
      <el-button @click="create()" style="float: right;margin-top:13px;" type="primary" plain size="small"
                 class="normalHide">新增
      </el-button>
    </el-header>

    <el-container style="width: 100%">
      <el-table v-loading="loading" :data="list" class="bill-table">
        <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
        <el-table-column label="学期名" width="">
          <template slot-scope="scope">
            {{scope.row.name}}
            <template v-if="scope.row.isDefault && parseInt(scope.row.isDefault) ===1">
              <el-tag size="mini">当前</el-tag>
            </template>
          </template>
        </el-table-column>
        <el-table-column prop="createByName" label="创建人" width="" align="center"></el-table-column>
        <el-table-column prop="createDate" label="创建时间" :formatter="baseTableFormatTime" width=""
                         align="center"></el-table-column>
        <el-table-column fixed="right" label="--" align="left" >
          <template slot-scope="scope">

            <el-button class="normalHide" @click="updateName(scope.row.id)" round type="text"
                       size="small">修改</el-button>
            <el-button class="normalHide" @click="deleteSemester(scope.row.id)" round type="text"
                       size="small">删除</el-button>
            <template v-if="!scope.row.isDefault || parseInt(scope.row.isDefault) ===0">
              <el-button class="normalHide" @click="setAsDefault(scope.row.id)" round
                         type="text" size="small">设为当前</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-container>

  </el-row>

</template>

<script>
  export default {
    name: 'SettingSemester',
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
      _this.listSemester();
    },
    methods: {
      listSemester() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/semester/list').then(function (res) {
          _this.loading = false;
          _this.list = res.data;
        }, _this.operateFail);
      },
      //
      deleteSemester(id) {
        const _this = this;
        _this.httpUtils.appPost('/semester/delete?id=' + id).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg, function () {

            });
            _this.listClassType();
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
        _this.httpUtils.appPost('/semester/update', cmd).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg, function () {

            });
            _this.listSemester();
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },
      setAsDefault(id) {
        const _this = this;
        _this.httpUtils.appPost('/semester/setAsDefault?id=' + id).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
            _this.listSemester();
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },
      //
      create() {
        const _this = this;
        this.$prompt('请输入新名称', '新增学期', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.createPost(value))
          .catch(() => {
          });
      },
      createPost(name) {
        const _this = this;
        _this.httpUtils.appPost('/semester/create?name=' + name).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg, function () {
              _this.listSemester();
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
