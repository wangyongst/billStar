<template>
  <el-container id="body">
    <el-header class="body bill-header" size="mini">
      <el-button @click="refresh()"
                 style="float: right;" type="primary" plain size="mini"
                 class="header-btn marginRight20">刷新配置信息
      </el-button>
      <el-button @click="triggerNotice()"
                 style="float: right;" type="primary" plain size="mini"
                 class="header-btn marginRight20">触发营收通知
      </el-button>
    </el-header>
    <el-container style="width: 100%">
      <el-table stripe v-loading="loading" :data="list" size="mini" Charge="bill-table">
        <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
        <el-table-column prop="remark" label="配置名" width="210"></el-table-column>
        <el-table-column prop="value" label="配置值" width=""></el-table-column>
        <el-table-column label="操作" width="80">
          <template slot-scope="scope">
            <template v-if="scope.row.canEdit">
              <el-button class="normalHide" type="text" size="mini" @click="updateConfig(scope.row)">修改</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-container>
  </el-container>
</template>

<script>

  export default {
    name: 'SettingConfig',
    data() {
      return {
        list: [],
        config: {},
        loading: false,
        canUpdate: ["app.schoolCommonRemark", "app.managerRobotURL", "app.noticeSchoolYearBeginTime", "app.reportSchoolYearBeginTime"],
      }
    },
    mounted: function () {
      const _this = this;
      _this.listConfigs();
    },
    methods: {
      listConfigs() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/config/list').then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.list = res.data;
            _this.processList();
          } else {
            _this.baseErrorNotify(res.msg);
          }
          _this.loading = false;
        }, _this.operateFail);
      },
      refresh() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/config/refresh').then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.list = res.data;
            _this.processList();
          } else {
            _this.baseErrorNotify(res.msg);
          }
          _this.loading = false;
        }, _this.operateFail);
      },
      triggerNotice() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/billNotice/triggerNotice').then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify("触发成功，请去对应钉钉群查看通知");
          } else {
            _this.baseErrorNotify(res.msg);
          }
          _this.loading = false;
        }, _this.operateFail);
      },
      processList() {
        const _this = this;
        _this.list.forEach(function (ele) {
          ele.canEdit = _this.canUpdate.indexOf(ele.name) >= 0
        });
      },
      updateConfig(item) {
        const _this = this;
        _this.config = item;
        this.$prompt('请输入新值', '修改 ' + _this.config.name, {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: _this.config.value,
          inputType: "textarea"
        }).then(({value}) => _this.updateConfigPost(value))
          .catch(() => {
          });
      },
      updateConfigPost(value) {
        const _this = this;
        _this.config.value = value;
        _this.httpUtils.appPost('/config/update', _this.config).then(function (res) {
          if (parseInt(res.code) === 0) {
            _this.baseSuccessNotify(res.msg);
            _this.listConfigs();
          } else {
            _this.baseErrorNotify(res.msg);
          }
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

<style scoped>

</style>
