// 公共方法
exports.install = function (Vue, options) {

  /**
   * 是否生产环境
   * @returns {boolean} true or false
   */
  Vue.prototype.appEnv = function () {
    // return 'dev';
    return 'prod';
  };

};
