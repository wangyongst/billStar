<template>
  <el-row id="body">

    <el-col :span="24">
      <el-header class="body">
        <el-button @click="create()" style="float: right;margin-top:13px;" type="primary" plain size="small"
                   class="normalHide">新增
        </el-button>
      </el-header>
    </el-col>

    <el-col :span="24">
      <el-table v-loading="loading" :data="list" class="bill-table">
        <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="学生学校"></el-table-column>
        <el-table-column prop="createName" label="创建人" width="" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" :formatter="baseTableFormatTime" width="" align="center"></el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="220">
          <template slot-scope="scope">
            <el-button class="normalHide" @click="updateItem(scope.row)" round type="text"
                       size="small">修改
            </el-button>
            <el-button class="normalHide" @click="deleteItem(scope.row.id)" round type="text"
                       size="small">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <!--    新增修改框-->
    <el-dialog class="bill-dialog" v-bind:title="classNo.type === 0 ?'新增学生学校' : '修改学生学校'"
               :visible.sync="createDialogVisible">
      <el-form :model="classNo">
        <el-form-item label="学生学校">
          <el-input v-model="classNo.name" class="width100"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCancel">取 消</el-button>
        <el-button type="primary" @click="dialogPost">确 定</el-button>
      </div>
    </el-dialog>
  </el-row>

</template>

<script>
  export default {
    name: 'SettingMyShcool',
    data() {
      return {
        list: [],
        loading: false,
        classNo: {
          type: null,
          name: null,
        },
        createDialogVisible: false,
      }
    },
    mounted: function () {
      const _this = this;
      _this.listClassNo();
    },
    methods: {
      listClassNo() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/sys/my/school/list').then(function (res) {
          _this.loading = false;
          _this.list = res;
        }, _this.operateFail);
      },
      deleteItem(id) {
        const _this = this;
        _this.httpUtils.appPost('/sys/my/school/delete?id=' + id).then(function (res) {
          _this.listClassNo();
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },
      updateItem(item) {
        this.classNo = {type: 1, id: item.id, name: item.name};
        this.createDialogVisible = true;
      },
      updatePost() {
        const _this = this;
        _this.httpUtils.appPost('/sys/my/school/update', _this.classNo).then(function (res) {
          _this.listClassNo();
          _this.createDialogVisible = false;
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },
      create() {
        this.classNo = {type: 0};
        this.createDialogVisible = true;
      }
      ,
      dialogCancel() {
        this.createDialogVisible = false;
      },
      dialogPost() {
        const _this = this;
        if (_this.classNo.type === 0) {
          _this.createPost();
        } else if (_this.classNo.type === 1) {
          _this.updatePost();
        }
      },
      createPost() {
        const _this = this;
        _this.httpUtils.appPost('/sys/my/school/save', _this.classNo).then(function (res) {
          _this.listClassNo();
          _this.createDialogVisible = false;
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r);
        _this.loading = false;
      }
    }
  }
</script>

<style scoped>

</style>
