<template>
  <div class="mui-content ">
    <div class="mui-row backgroundColorWhite">
      <div v-for="nav in navs" class="mui-col-sm-4 mui-col-xs-4 border1">
        <li class="home-nav mui-table-view-cell height120 ">
          <i class="icon" v-bind:class="nav.icon" aria-hidden="true"></i>
          <router-link class="text" v-bind:to="nav.url">
            {{nav.name}}
          </router-link>
        </li>
      </div>
    </div>
    <div class="mui-row">
      <div class="mui-col-sm-12 mui-col-xs-12 marginTop20 t100" style="border-top:3px solid white ;padding:10px">
        <p>系统介绍···</p>
      </div>

    </div>
  </div>
</template>

<script>
  export default {
    name: 'Home',
    data() {
      return {
        navs: [
          {name: "所有证件", icon: "fa fa-credit-card", url: "certs"},
          {name: "新增证件", icon: "fa fa-plus", url: "certEdit"},
          {name: "证件类型", icon: "fa fa-navicon", url: "certTypes"},
          {name: "即将过期", icon: "fa fa-ambulance", url: "certs?expireOnly=1"},
          {name: "用户管理", icon: "fa fa-address-book", url: "users"},
          {name: "个人中心", icon: "fa fa-user-o", url: "me"},
        ]
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
