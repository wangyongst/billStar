<template>
  <div class="marginBottom20">
    <form class="mui-input-group">
      <div class="mui-input-row">
        <label>类型</label>
        <select v-model="queryForm.typeId" @change="queryFormChange">
          <option value="0" selected>-全部-</option>
          <template v-for="type in types">
            <option v-bind:value="type.id">{{type.name}}</option>
          </template>
        </select>
      </div>
      <div class="mui-input-row">
        <label>证件</label>
        <input type="text" @change="queryFormChange" v-model="queryForm.nameLike" class="mui-input-clear"
               placeholder="请输入证件名关键字搜索">
      </div>
    </form>
    <template v-if="certs.length === 0">
      <p class="marginTop30">———— 正在加载数据 (⊙︿⊙) ————</p>
    </template>
    <template v-else>
      <template v-for="cert in certs" class="mui-table-view">
        <CertCard v-bind:cert="cert"/>
      </template>
    </template>
  </div>
</template>

<script>
  import CertCard from "./CertCard.vue";

  export default {
    name: 'certs',
    components: {CertCard},
    data() {
      return {
        certs: [],
        expireOnly: "",
        query: {
          typeId: 0,
          nameLike: ""
        },
        queryForm: {
          typeId: 0,
          nameLike: ""
        },
        types: [],
      }
    },
    mounted: function () {
      const _this = this;
      _this.expireOnly = _this.$route.query.expireOnly;
      _this.fetchList();
      const dd = _this.dd;
      dd.ready(function () {

      });
      _this.httpUtils.listAllCetTypes().then(function (res) {
        _this.types = res;
      })

    },
    methods:
      {
        fetchList() {
          const _this = this;
          _this.query.expireOnly = _this.expireOnly;
          const request = {pageNo: 0, pageSize: 100, data: _this.query};
          _this.httpUtils.appPost('/certDoc/listVOPage', request).then(function (res) {
            _this.certs = res.data.list;
          }, _this.operateFail);
        }
        ,
        operateFail(r) {
          this.commonUtils.commonAlert(r.msg)
        }
        ,
        queryFormChange() {
          const _this = this;
          _this.queryForm.nameLike = _this.queryForm.nameLike.trim();
          const newQueryFormJson = JSON.stringify(_this.queryForm);
          const queryFormJson = JSON.stringify(_this.query);
          console.log("newQueryFormJson:" + newQueryFormJson);
          console.log("queryFormJson:" + queryFormJson);
          if (newQueryFormJson === queryFormJson) {
            return;
          }
          for (const key in _this.queryForm) {
            _this.query[key] = _this.queryForm[key];
          }
          _this.fetchList();
        }

      }
  }
</script>
