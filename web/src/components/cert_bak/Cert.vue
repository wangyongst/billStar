<template>
  <div class="backgroundColorWhite">

    <template v-if="!cert.id">
      <div class="mui-row">
        <div class="mui-col-sm-12 mui-col-xs-12">
          <p class="marginTop30 t010">———— 正在加载数据 (⊙︿⊙) ————</p>
        </div>
      </div>
    </template>
    <template v-else>
      <div class="mui-row">
        <div class="mui-col-sm-12 mui-col-xs-12 cert-main t100">
          <h3 class="t010">{{cert.name}}</h3>
          <p class="t001 borderBottom1">
            <span>{{cert.creatorName}}</span>
            <span>{{this.commonUtils.formatTime(cert.createDate)}}</span>
          </p>
          <img v-bind:src="cert.imgUrl" width="100%" height="auto"/>
          <h5 class="marginTop20">类型:{{cert.typeName}}</h5>
          <h5 class="">编号:{{cert.documentCode}}</h5>
          <h5 class="">等级:{{cert.documentDegree}}</h5>
          <p class="item">发证单位:{{cert.createDepartment}}</p>
          <p class="item">联系人:{{cert.contractorName}}</p>
          <p class="item">联系电话:{{cert.contractorMobile}}</p>
          <p class="item">档案盒位置:{{cert.location}}</p>
          <p class="item">继续教育学习时间:{{this.commonUtils.formatDate(cert.continueEduDate)}}</p>
          <p class="item ">过期时间:{{this.commonUtils.formatDate(cert.expireDate)}}</p>
          <p class="item">提前{{cert.alarmBeforeDay}}天提醒</p>
          <p class="item">详情:{{cert.description}}</p>
          <p class="item borderBottom1 paddingBottom20">备注:{{cert.remarks}}</p>
          <div class="borderBottom1 paddingBottom10">
            <ImageUpload v-bind:imageUrls="cert.imgUrls" isReadOnly="true"/>
          </div>
        </div>
      </div>
      <div class="mui-row ">
        <div class="mui-col-sm-6 mui-col-xs-6">
          <button type="button" @click="deleteDoc" class="width100 mui-btn mui-btn-danger mui-btn-outlined">
            <span class="mui-icon mui-icon-trash"></span>
            删除
          </button>
        </div>
        <div class="mui-col-sm-6 mui-col-xs-6">
          <button type="button" @click="editDoc" class="width100 mui-btn mui-btn-primary mui-btn-outlined">
            <span class="mui-icon mui-icon mui-icon-compose"></span>
            编辑
          </button>
        </div>
      </div>
    </template>
    <div class="mui-row">
      <div class=" mui-col-sm-12 mui-col-xs-12 marginBottom20"></div>
    </div>
  </div>
</template>

<script>
  import ImageUpload from "./image/ImageUpload";

  export default {
    name: 'cert',
    components: {ImageUpload},
    data() {
      return {
        cert: {},
      }
    },
    mounted: function () {
      const _this = this;
      _this.fetchCert();
      const dd = _this.dd;


    },
    methods: {
      fetchCert() {
        const _this = this;
        _this.httpUtils.appGet('/certDoc/getVO?id=' + _this.$route.params.id).then(function (res) {
          _this.cert = res.data;
        }, _this.operateFail);
      },
      operateFail(r) {
        this.commonUtils.commonAlert(r.msg)
      },
      deleteDoc() {
        const _this = this;
        if (confirm("确定删除: " + _this.cert.name + " 数据?")) {
          _this.httpUtils.appPost('/certDoc/delete?id=' + _this.cert.id).then(function (res) {
            alert("删除成功");
            _this.$router.push("/certs")
          }, _this.operateFail);
        }
      },
      editDoc() {
        const _this = this;
        _this.$router.push({path: "/certEdit?id=" + _this.cert.id});
      }
    }
  }
</script>
