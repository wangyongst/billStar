// 公共方法
export default {
  install(Vue, options) {
    Vue.prototype.deepCopy = function (val) {
      return JSON.parse(JSON.stringify(val));
    };

  }
};
