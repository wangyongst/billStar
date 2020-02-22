<template>
  <el-container v-bind:class="userAuthClass+' ' +appEnv()" id="app">

    <!--    登录中-->
    <template v-if="loginResult === 0">
      <h1 style="text-align: center;margin-top: 200px;width: 100%">---正在执行自动登录---</h1>
    </template>

    <!--    登录失败-->
    <template v-else-if="loginResult === 1 ">
      <h1 style="text-align: center;margin-top: 200px;width: 100%">自动登录失败，请联系管理员</h1>
    </template>

    <!-- 登录成功-->
    <template v-else>
      <AppMenu :is-collapse="menuCollapse"/>
      <el-container>
        <!--  菜单的收起与展开 -->
        <el-header class="main-header">
          <!--  控制菜单的按钮展开与收起 -->
          <template v-if="menuCollapse">
            <el-button class="collapse-btn" icon="el-icon-s-unfold" plain
                       @click="menuCollapse=!menuCollapse"></el-button>
          </template>
          <template v-else>
            <el-button class="collapse-btn" icon="el-icon-s-fold" plain @click="menuCollapse=!menuCollapse"></el-button>
          </template>
          <!--  动态tab页-->
          <el-tabs
            class="school-tabs"
            v-model="activeIndex"
            type="border-card"
            closable
            @tab-click="tabClick"
            v-if="options.length"
            @tab-remove="tabRemove">
            <el-tab-pane
              :key="item.name"
              v-for="(item) in options"
              :label="item.name"
              :name="item.route">
            </el-tab-pane>
          </el-tabs>
        </el-header>
        <keep-alive>
          <router-view></router-view>
        </keep-alive>
      </el-container>
    </template>
  </el-container>
</template>

<script>
  import AppMenu from './components/AppMenu';

  export default {
    name: 'App',
    components: {AppMenu},
    data() {
      return {
        menuCollapse: true,
        appConfig: {},
        code: '',
        loginResult: 0,
        userAuthClass: 'none'
      }
    },
    created() {
      eventBus.$on('globalFunc', function (data) {
        alert(JSON.stringify(data));
      })
    },
    computed: {
      options() {
        return this.$store.state.options;
      },
      activeIndex: {
        get() {
          return this.$store.state.activeIndex;
        },
        set(val) {
          this.$store.commit('set_active_index', val);
        }
      }
    },
    methods: {
      tabClick(tab) {
        console.log("options=" + JSON.stringify(this.$store.state.options));
        let path = this.activeIndex;
        this.$router.push({path: path});
      },
      tabRemove(targetName) {
        console.log(targetName);
        this.$store.commit('delete_tabs', targetName);
        if (this.activeIndex === targetName) {
          // 设置当前激活的路由
          if (this.options && this.options.length >= 1) {
            this.$store.commit('set_active_index', this.options[this.options.length - 1].route);
            this.$router.push({path: this.activeIndex});
          } else {
            this.$router.push({path: '/'});
          }
        }
      },
      initApp() {
        const _this = this;
        _this.httpUtils.appGet('/dingParam/getCorpInfo').then(function (res) {
          _this.appConfig = res;
          _this.loginByCode(_this.appConfig.corpId);
        }, function (r) {
          _this.baseErrorNotify(r);
        });
      },
      loginByCode(corpId) {
        const _this = this;
        window.localStorage.removeItem("user");
        console.log("requestAuthCode begin");
        _this.dd.runtime.permission.requestAuthCode({
          corpId: corpId,
          onSuccess: function (result) {
            _this.loginByCodePost(result.code);
          },
          onFail: function (err) {
            _this.baseErrorNotify("loginByCode-requestAuthCode-onFail=" + JSON.stringify(err));
            _this.loginError();
          }
        }).catch(function (e) {
          // 不在钉钉环境的处理
          _this.loginByCodePost("appTestCode");
        })
      },

      loginByCodePost(code) {
        const _this = this;
        console.log("requestAuthCode:" + code);
        _this.httpUtils.appGet('/login/loginByCode?code=' + code).then(function (res) {
          const user = res;
          _this.loginResult = 2;
          if (user.isAppAdmin === 1) {
            _this.userAuthClass = 'admin'
          } else {
            _this.userAuthClass = 'normal'
          }
          window.localStorage.setItem("user", JSON.stringify(user));
        }, function (r) {
          _this.baseErrorNotify(r);
          _this.loginError();
        }).catch(function (e) {
          _this.baseErrorNotify("loginByCode-requestAuthCode-onSuccess-catch=" + JSON.stringify(e));
          _this.loginError();
        })
      },

      loginError() {
        const _this = this;
        _this.loginResult = 1;
      },
      processPath(path) {
        console.log("path=" + path);
        // return path.split('/')[1];
        return path;
      },
    },
    watch: {
      '$route'(to) {
        let flag = false;
        console.log("route-to", to);
        let optionName = to.name;
        for (let option of this.options) {
          if (option.name === optionName) {
            if (option.route !== this.processPath(to.path)) {
              console.log("to replace path");
              this.$store.commit('replace_tabs', {route: this.processPath(to.path), name: optionName});
            }
            flag = true;
            this.$store.commit('set_active_index', this.processPath(to.path));
            break
          }
        }
        if (!flag) {
          this.$store.commit('add_tabs', {route: this.processPath(to.path), name: optionName});
          this.$store.commit('set_active_index', this.processPath(to.path));
        }
      }
    },
    mounted() {
      this.initApp();
    }
  }

</script>

<style>
  #app {
    font-family: '微软雅黑', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }


</style>
