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
        <el-table-column prop="name" label="科目-班级" :formatter="showFullName" width=""></el-table-column>
        <el-table-column prop="createName" label="创建人" width="" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" :formatter="baseTableFormatTime" width="" align="center"></el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="220">
          <template slot-scope="scope">
            <el-button class="normalHide" @click="updateItem(scope.row)" round type="text"
                       size="small">修改
            </el-button>
            <el-button class="normalHide" @click="deleteClassType(scope.row.id)" round type="text"
                       size="small">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <!--    新增修改框-->
    <el-dialog class="bill-dialog" v-bind:title="classType.type === 0 ?'新增课程' : '修改课程'"
               :visible.sync="createDialogVisible">
      <el-form :model="classType">
        <el-form-item label="科目">
          <el-select v-model="classType.subjectId" placeholder="请选择科目">
            <el-option v-for="item in subjects" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="班级">
          <el-input v-model="classType.name" class="width100"></el-input>
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
    name: 'SettingClass',
    data() {
      return {
        list: [],
        loading: false,
        classType: {
          type: null,
          subjectId: null,
          name: null,
        },
        subjects: [],
        createDialogVisible: false,
      }
    },
    mounted: function () {
      const _this = this;
      _this.listClassType();
      _this.listSubjectForSelect();
    },
    methods: {
      listSubjectForSelect() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/sys/subject/list').then(function (res) {
          _this.loading = false;
          _this.subjects = res;
        }, _this.operateFail);
      },
      listClassType() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/sys/class/list').then(function (res) {
          _this.loading = false;
          _this.list = res;
        }, _this.operateFail);
      },
      deleteClassType(id) {
        const _this = this;
        _this.httpUtils.appPost('/sys/class/delete?id=' + id).then(function (res) {
          _this.listClassType();
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },
      //
      updateItem(item) {
        this.classType = {type: 1, id: item.id, subjectId: item.subjectId, name: item.name};
        this.createDialogVisible = true;
      },
      updatePost() {
        const _this = this;
        _this.httpUtils.appPost('/sys/class/update', _this.class).then(function (res) {
          _this.listClassType();
          _this.createDialogVisible = false;
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },
      //
      create() {
        this.class = {type: 0};
        this.createDialogVisible = true;
      },
      dialogCancel() {
        this.createDialogVisible = false;
      },
      dialogPost() {
        const _this = this;
        if (_this.class.type === 0) {
          _this.createPost();
        } else if (_this.class.type === 1) {
          _this.updatePost();
        }
      },
      createPost() {
        const _this = this;
        _this.httpUtils.appPost('/sys/class/save', _this.classType).then(function (res) {
          _this.listClassType();
          _this.createDialogVisible = false;
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },
      //
      operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r);
        _this.loading = false;
      }
      ,
      showFullName(row, col, val) {
        return row.subjectName + "-" + row.name;
      }
    }
  }
</script>

<style scoped>

</style>
