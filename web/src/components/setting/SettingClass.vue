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
        <el-table-column prop="createByName" label="创建人" width="" align="center"></el-table-column>
        <el-table-column prop="createDate" label="创建时间" :formatter="baseTableFormatTime" width=""
                         align="center"></el-table-column>
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
          <el-select v-model="classType.parentId" placeholder="请选择科目">
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
        query: {
          orderBy: 'create_date DESC'
        },
        loading: false,
        classType: {
          parentId: null,
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
        _this.httpUtils.appGet('/subject/list').then(function (res) {
          _this.loading = false;
          _this.subjects = res.data;
        }, _this.operateFail);
      },
      listClassType() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/classType/list').then(function (res) {
          _this.loading = false;
          _this.list = res.data;
        }, _this.operateFail);
      },
      //
      deleteClassType(id) {
        const _this = this;
        _this.httpUtils.appPost('/classType/delete?id=' + id).then(function (res) {
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
      updateItem(item) {
        this.classType = {type: 1, id: item.id, parentId: item.parentId, name: item.name};
        this.createDialogVisible = true;
      },
      updatePost() {
        const _this = this;
        _this.httpUtils.appPost('/classType/update', _this.classType).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg, function () {

            });
            _this.listClassType();
            _this.createDialogVisible = false;
          } else {
            _this.baseErrorNotify(res.msg);
          }
        }, _this.operateFail);
      },
      //
      create() {
        this.classType = {type: 0};
        this.createDialogVisible = true;
      },
      dialogCancel() {
        this.createDialogVisible = false;
      },
      dialogPost() {
        const _this = this;
        if (_this.classType.type === 0) {
          _this.createPost();
        } else if (_this.classType.type === 1) {
          _this.updatePost();
        }
      },
      createPost() {
        const _this = this;
        _this.httpUtils.appPost('/classType/create', _this.classType).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg, function () {
              _this.listClassType();
              _this.createDialogVisible = false;
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
      showFullName(row, col, val) {
        return row.parentName + "-" + row.name;
      }
    }
  }
</script>

<style scoped>

</style>
