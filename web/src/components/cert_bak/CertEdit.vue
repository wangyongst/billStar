<template>
  <form class="mui-input-group">
    <div class="mui-input-row">
      <label>证件名</label>
      <input v-model="cert.name" type="text" class="mui-input-clear" placeholder="请输入证件名">
    </div>
    <div class="mui-input-row">
      <label>类型</label>
      <select v-model="cert.typeId">
        <template v-for="type in types">
          <option v-bind:value="type.id">{{type.name}}</option>
        </template>
      </select>
    </div>

    <div class="mui-input-row">
      <label>编号</label>
      <input v-model="cert.documentCode" type="text" class="mui-input-clear" placeholder="请输入编号">
    </div>

    <div class="mui-input-row">
      <label>等级</label>
      <input v-model="cert.documentDegree" type="text" class="mui-input-clear" placeholder="请输入等级">
    </div>

    <div class="mui-input-row">
      <label>发证单位</label>
      <input v-model="cert.createDepartment" type="text" class="mui-input-clear" placeholder="请输入发证单位">
    </div>

    <div class="mui-input-row">
      <label>联系人</label>
      <input v-model="cert.contractorName" type="text" class="mui-input-clear" placeholder="请输入联系人">
    </div>

    <div class="mui-input-row">
      <label>联系电话</label>
      <input v-model="cert.contractorMobile" type="text" class="mui-input-clear" placeholder="请输入联系电话">
    </div>

    <div class="mui-input-row" style="height: auto;">
      <label>继续教育学习时间</label>
      <input v-model="cert.continueEduDate" type="date" class="mui-input-clear" placeholder="继续教育学习时间"/>
    </div>

    <div class="mui-input-row" style="height: auto;">
      <label class="marginTop30">首图</label>
      <template v-if="this.commonUtils.isStringValid(cert.imgUrl)">
        <!--        修改-->
        <ImageUpload v-bind:imageUrls="[cert.imgUrl]" @imageUrlsChange="imgUrlChange" maxImages="1"/>
      </template>
      <template v-else>
        <!--        新增-->
        <ImageUpload @imageUrlsChange="imgUrlChange" maxImages="1"/>
      </template>
    </div>
    <div class="mui-input-row" style="height: auto;">
      <label>详情</label>
      <textarea v-model="cert.description" type="text" class="mui-input-clear" placeholder="描述信息" rows="6"></textarea>
    </div>
    <div class="mui-input-row" style="height: auto;">
      <label>档案盒位置</label>
      <textarea v-model="cert.location" type="text" class="mui-input-clear" placeholder="描述信息" rows="2"></textarea>
    </div>
    <div class="mui-input-row" style="height: auto;">
      <label>过期时间</label>
      <input v-model="cert.expireDate" type="date" class="mui-input-clear" placeholder="过期时间"/>
    </div>
    <div class="mui-input-row" style="height: auto;">
      <label>提前通知</label>
      <input v-model="cert.alarmBeforeDay" type="number" class="mui-input-clear" placeholder="过期前多少天，发送系统通知"/>
    </div>
    <div class="mui-input-row" style="height: auto;">
      <label>备注</label>
      <textarea v-model="cert.remarks" type="text" class="mui-input-clear" placeholder="描述信息" rows="3"></textarea>
    </div>

    <div class="mui-input-row" style="height: auto;">
      <label class="FL" style="margin-bottom: -10px;">细节图片</label>
      <ImageUpload class="" v-bind:imageUrls="cert.imgUrls" maxImages="8" @imageUrlsChange="imageUrlsChange"/>
    </div>

    <div class="">
      <template v-if="parseInt(id) > 0">
        <button type="button" class="mui-btn mui-btn-primary mui-btn-block marginTop30" style="margin-bottom: 0" @click="saveUpdate"><i class="fa fa-floppy-o"></i> 保存修改</button>
      </template>
      <template v-else>
        <button type="button" class="mui-btn mui-btn-primary mui-btn-block marginTop30" style="margin-bottom: 0" @click="submit"><i class="fa fa-plus-square-o"></i> 添加证件</button>
      </template>
    </div>
  </form>
</template>

<script>
  import ImageUpload from "../image/ImageUpload";

  export default {
    name: 'certEdit',
    components: {ImageUpload},
    data() {
      return {
        types: [],
        id: null,
        cert: {
          id: null,
          name: "",
          typeId: 0,
          description: "",
          location: "",
          expireDate: null,
          alarmBeforeDay: null,
          imgUrl: null,
          remarks: "",
          imgUrls: [],
          createDepartment:"",
          contractorName:"",
          contractorMobile:"",
          continueEduDate:null,
          documentCode:"",
          documentDegree:""
        }
      }
    },
    mounted: function () {
      const _this = this;
      _this.id = _this.$route.query.id;
      if (parseInt(_this.id) > 0) {
        _this.httpUtils.appGet('/certDoc/getVO?id=' + _this.id).then(function (res) {
          _this.cert = res.data;
          _this.cert.expireDate = _this.commonUtils.formatDate(res.data.expireDate);
        }, _this.operateFail);
      }
      _this.httpUtils.listAllCetTypes().then(function (res) {
        _this.types = res;
      });
    },
    methods: {
      imageUrlsChange(urls) {
        this.cert.imgUrls = urls;
      },
      imgUrlChange(urls) {
        this.cert.imgUrl = urls[0];
      },
      submit() {
        const _this = this;
        if (_this.isCertValid()) {
          _this.httpUtils.appPost("/certDoc/create", _this.cert).then(function (res) {
            _this.$router.push({path: "/certs"});
          }, function (r) {
            _this.commonUtils.commonAlert(r.msg);
          });
        }
      },
      isCertValid() {
        const _this = this;
        const cert = _this.cert;
        if (!_this.commonUtils.isStringValid(cert.name)) {
          alert("证件名不能为空");
          return false;
        }
        if (parseInt(cert.typeId) <= 0) {
          alert("请选择证件类型");
          return false;
        }
        if (!_this.commonUtils.isStringValid(cert.imgUrl)) {
          alert("首图不能为空");
          return false;
        }
        if (!_this.commonUtils.isStringValid(cert.description)) {
          alert("描述不能为空");
          return false;
        }
        return true;
      },

      /**
       * update
       */
      saveUpdate() {
        const _this = this;
        if (_this.isCertValid()) {
          _this.httpUtils.appPost("/certDoc/update", _this.cert).then(function (res) {
            _this.$router.push({path: "/cert/" + _this.cert.id});
          }, function (r) {
            _this.commonUtils.commonAlert(r.msg);
          });
        }
      },
      improveMainImage() {
        console.log("improveMainImage");
        const _this = this;
        if (_this.imgUrl != null) {
          return [_this.imgUrl];
        } else {
          return [];
        }

      }
    }
  }
</script>

<style>



</style>
