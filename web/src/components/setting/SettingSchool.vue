<template>
  <el-container id="body">

    <el-header class="body">校区管理
      <el-button @click="synDeptSchools"
                 style="float: right;margin-top:13px;"
                 type="primary"
                 plain
                 size="small" class="normalHide">从钉钉同步
      </el-button>
    </el-header>

    <el-container style="width: 100%">
      <el-table v-loading="loading" :data="list" class="bill-table">
        <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="一级部门名" width="150"></el-table-column>
        <el-table-column prop="phone" label="电话" width=""></el-table-column>
        <el-table-column label="群机器人地址" width="">
          <template slot-scope="scope">
            <template v-if="scope.row.groupWebHook === null || scope.row.groupWebHook === '' ">
              <el-tag type="info" size="mini">未设置</el-tag>
              <el-button @click="updateGroupWebHook(scope.row.id ,scope.row.groupWebHook)" round type="text" size="mini"
                         class="normalHide">设置
              </el-button>
            </template>
            <template v-else>
              <el-tag size="mini">已设置</el-tag>
              <el-button @click="updateGroupWebHook(scope.row.id ,scope.row.groupWebHook)" round type="text" size="mini"
                         class="normalHide">修改
              </el-button>
              <el-button @click="triggerTestMsg(scope.row.id)" round type="text" size="mini"
                         class="normalHide">测试触发
              </el-button>
            </template>
          </template>
        </el-table-column>

        <el-table-column label="是否校区" width="100" align="center" prop="isSchoolZone" :formatter="baseYesNo">
        </el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="220" class="normalHide">
          <template slot-scope="scope">
            <template v-if="scope.row.isSchoolZone === true">
              <el-button @click="setAsNotSchoolZone(scope.row.id)" type="text" size="small" class="normalHide width100">
                不是校区
              </el-button>
            </template>
            <template v-if="scope.row.isSchoolZone === false">
              <el-button @click="setAsSchoolZone(scope.row.id)" round type="text" size="small"
                         class="normalHide width100">设为校区
              </el-button>
            </template>
            <el-button @click="updatePhone(scope.row.id ,scope.row.name)" round type="text" size="small"
                       class="normalHide">修改电话
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-container>
  </el-container>

</template>

<script>
  export default {
    name: 'SettingSchool',
    data() {
      return {
        list: [],
        loading: false,
      }
    },
    created: function () {
      const _this = this;
      _this.listSchoolZone();
    },
    methods: {
      synDeptSchools() {
        const _this = this;
        _this.loading = true;
        _this.httpUtils.appGet('/ding/synDept').then(function (res) {
          _this.loading = false;
          _this.baseSuccessNotify("操作成功，即将刷新校区列表");
          _this.listSchoolZone();
        }, _this.operateFail);
      },
      listSchoolZone() {
        const _this = this;
        _this.loading = true;
        console.log("res")
        _this.httpUtils.appGet('/schoolZone/list').then(function (res) {
          _this.loading = false;
          _this.list = res;
        }, _this.operateFail);
      },
      updateSchoolZone(cmd) {
        const _this = this;
        _this.httpUtils.appPost('/schoolZone/update', cmd).then(function (res) {
          _this.listSchoolZone();
        }, _this.operateFail);
      },
      // 置为校区
      setAsSchoolZone(sid) {
        const cmd = {id: sid, isSchoolZone: 1};
        this.updateSchoolZone(cmd);
      },
      // 置为非校区
      setAsNotSchoolZone(sid) {
        const cmd = {id: sid, isSchoolZone: 0};
        this.updateSchoolZone(cmd);
      },
      updatePhone(sid, name) {
        const _this = this;
        this.$prompt('请输入电话', '修改 ' + name + ' 联系电话', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.updatePhonePost(sid, value))
          .catch(() => {
          });
      },
      updatePhonePost(sid, phone) {
        const _this = this;
        const cmd = {id: sid, phone: phone};
        this.updateSchoolZone(cmd);
      }
      ,
      updateGroupWebHook(sid, webHook) {
        const _this = this;
        _this.$prompt('请输入新地址', '设置/修改机器人地址', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => _this.updateWebHookPost(sid, value))
          .catch(() => {
          });
      },
      updateWebHookPost(sid, webHook) {
        const _this = this;
        const cmd = {id: sid, groupWebHook: webHook};
        this.updateSchoolZone(cmd);
      },
      triggerTestMsg(sid) {
        const _this = this;
        _this.httpUtils.appPost('/schoolZone/sendTestMessage?deptId=' + sid).then(function (res) {
          _this.baseSuccessNotify(res);
        }, _this.operateFail);
      },
      seeGroupWebHookURL(val) {
        const _this = this;
        this.$alert("<p>" + val + "</p>", '群机器人地址', {
          dangerouslyUseHTMLString: true
        });
      }, operateFail(r) {
        const _this = this;
        _this.baseErrorNotify(r);
        _this.loading = false;
      }
    }
  }
</script>

<style scoped>

</style>
