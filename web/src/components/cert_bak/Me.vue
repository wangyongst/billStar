<template>
  <div class=" me">
    <div class="t010">
      <img width="80%" height="auto" v-bind:src="user.avatar" style="border-radius:50%">
    </div>
    <ul class="mui-table-view t100">
      <li class="mui-table-view-cell"><i class="fa fa-user-circle-o marginRight10" aria-hidden="true"></i>{{user.name}}
      </li>
      <li class="mui-table-view-cell"><i class="fa fa-phone-square marginRight10" aria-hidden="true"></i><span>{{user.mobile}}</span>
      </li>
      <li class="mui-table-view-cell"><i class="fa fa-envelope-square marginRight10"></i>{{user.email}}</li>
    </ul>

    <ul class="mui-table-view marginTop30 backgroundColorWhite marginBottom20">
      <li class="mui-table-view-cell" @click="triggerUserSyn"><i class="fa fa-hand-o-right" aria-hidden="true"></i> 触发数据同步</li>
      <li class="mui-table-view-cell" @click="triggerExpireMessage"><i class="fa fa-hand-o-right" aria-hidden="true"></i> 触发过期提醒</li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: 'me',
    data() {
      return {
        user: {}
      }
    },
    mounted: function () {
      const user = window.localStorage.getItem("user");
      this.user = JSON.parse(user);
    },
    methods: {
      triggerUserSyn() {
        const _this = this;
        if (confirm("确定触发数据同步？") === true) {
          _this.httpUtils.appGet("/ding/synOrganization").then(function () {
            alert("同步已完成");
          }, function (r) {
            _this.commonUtils.commonAlert(r.msg);
          })
        }
      },
      triggerExpireMessage() {
        const _this = this;
        if (confirm("确定触发过期提醒？") === true) {
          _this.httpUtils.appGet("/ding/triggerExpireMessage").then(function (res) {
            alert("找到" + res.data + "条过期数据，已发送提醒");
          }, function (r) {
            _this.commonUtils.commonAlert(r.msg);
          })
        }
      }
    }
  }
</script>
