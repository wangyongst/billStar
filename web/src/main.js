// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.utils.conf with an alias.
import Vue from 'vue'
import axios from 'axios';
import App from './App'
import router from './router'
import AppConfig from './config/config.js'
import * as dd from 'dingtalk-jsapi';
import Print from 'vue-print-nb'


//bootstrapVue
// import bootstrapVue from 'bootstrap-vue';
// import {Toast} from 'bootstrap-vue/es/components';
// import 'bootstrap/dist/css/bootstrap.css'
// import 'bootstrap-vue/dist/bootstrap-vue.css'
//mui
// import './assets/mui/dist/css/mui.css'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/fontawesome/css/font-awesome.min.css';
import './assets/style/common.less';
import './assets/style/app.less';

//
import commonUtils from "./utils/CommonUtils";
import httpUtils from './utils/HttpUtils';
import base from './utils/base';
import * as app from './utils/app';
import env from './utils/env';
import userUtils from './utils/UserUtils';

import htmlPdf from './utils/HtmlPdf';
import obj from './utils/obj';
import store from './store/index';
import billOperate from './utils/BillOperate'


Vue.config.productionTip = false;
Vue.prototype.$http = axios;
Vue.prototype.$appConfig = AppConfig;
Vue.prototype.dd = dd;
Vue.prototype.commonUtils = commonUtils;
Vue.prototype.httpUtils = httpUtils;
Vue.prototype.billOperate = billOperate;

Vue.use(ElementUI);
Vue.use(base);
Vue.use(app);
Vue.use(Print);  //
Vue.use(env);
Vue.use(userUtils);
Vue.use(obj);
Vue.use(htmlPdf);

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title;   //这 to.meta.title 是在router里设置的
  } else {
    document.title = "bill-star";
  }
  next();
  if (to.query.expireOnly && to.query.expireOnly === '1') {
    commonUtils.setTitle("即将过期");
  } else {
    commonUtils.setTitle(to.meta.title);
  }
  commonUtils.setToHome();
});
window.eventBus = new Vue();

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: {App},
  template: '<App/>',
});


