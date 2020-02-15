// 公共方法
exports.install = function (Vue, options) {

  Vue.prototype.formatDept = function (list) {
    if (list && list.length > 0) {
      let names = [];
      list.forEach(function (ele) {
        // if (ele.id > 1)
        names.push(ele.name);
      });
      return names.join(' , ');
    }
  };

  Vue.prototype.loginUserDeptSchools = function () {
    const _this = this;
    const userString = window.localStorage.getItem("user");
    if (_this.baseIsNotBlank(userString)) {
      const user = JSON.parse(userString);
      return user.deptSchools;
    }
    return [];
  }


};
