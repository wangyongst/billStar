<template>
  <el-row id="body">

    <el-header class="body">
      <!--      班级管理-->
      <el-button @click="create()" style="float: right;margin-top:13px;" type="primary" plain
                 size="small" class="normalHide">新增
      </el-button>
    </el-header>

    <el-container>
      <el-table v-loading="loading" :data="list" class="bill-table" style="overflow-y: scroll">
        <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="科目" width=""></el-table-column>
        <el-table-column prop="createName" label="创建人" width="" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" :formatter="baseTableFormatTime" width="" align="center"></el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="220">
          <template slot-scope="scope">
            <el-button class="normalHide" @click="updateName(scope.row.id)" round type="text" size="small">修改</el-button>
            <el-button class="normalHide" @click="deleteSubject(scope.row.id)" round type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-container>

  </el-row>

</template>

<script>
  export default {
    name: 'SettingSubject',
    data() {
      return {
        list: [],
        loading: false,
      }
    },
    mounted: function () {
      const _this = this;
      _this.listSubject();
    },
    methods: {
      listSubject() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/sys/subject/list').then(function (res) {
          _this.loading = false;
          _this.list = res;
        }, _this.operateFail);
      },
      saveOrUpdateSubject(cmd) {
        const _this = this;
        _this.httpUtils.appPost('/sys/subject/saveOrUpdate', cmd).then(function (res) {
          _this.listSubject();
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },
      deleteSubject(id) {
        const _this = this;
        _this.httpUtils.appPost('/sys/subject/delete?id=' + id).then(function (res) {
          _this.listSubject();
          _this.baseSuccessNotify(res.msg);
        }, _this.operateFail);
      },
      //
      updateName(id) {
        const _this = this;
        this.$prompt('请输入新名称', '修改科目', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.updateNamePost(id, value))
          .catch(() => {
          });
      },
      updateNamePost(sid, name) {
        const _this = this;
        const cmd = {id: sid, name: name};
        _this.saveOrUpdateSubject(cmd);
      },
      //
      create() {
        const _this = this;
        this.$prompt('请输入新名称', '新增科目', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.createPost(value))
          .catch(() => {
          });
      },
      createPost(name) {
        const _this = this;
        const cmd = {name: name};
        _this.saveOrUpdateSubject(cmd);
      },
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
