// 公共方法
exports.install = function (Vue, options) {
  Vue.prototype.baseSuccessNotify = function (msg, onCLose) {
    this.$notify({
      title: '操作成功',
      message: msg,
      type: 'success',
      duration: 1000,
      onClose: function (r) {
        if (onCLose) {
          onCLose(r);
        }
      }
    });
  };
  Vue.prototype.baseErrorNotify = function (msg) {
    this.$notify({
      title: '操作失败',
      message: msg,
      type: 'error',
      offset: 50
    });
  };

  Vue.prototype.baseFormatTime = function (val) {
    if (!val) {
      return "-";
    }
    let date = new Date(val);
    let Y = date.getFullYear() + '-';
    let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) + '-' : date.getMonth() + 1 + '-';
    let D = date.getDate() < 10 ? '0' + date.getDate() + ' ' : date.getDate() + ' ';
    let h = date.getHours() < 10 ? '0' + date.getHours() + ':' : date.getHours() + ':';
    let m = date.getMinutes() < 10 ? '0' + date.getMinutes() + ':' : date.getMinutes() + ':';
    let s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
    return Y + M + D + h + m + s;
  };


  Vue.prototype.baseFormatCourse = function (row, col, val) {
    return row["className"] + "|" + row["classNo"] + "|" + row["teacherName"] + "|" + row["courseTime"];
  };


  Vue.prototype.baseFormatTimeOnly = function (val) {
    if (!val) {
      return "-";
    }
    let date = new Date(val);
    let h = date.getHours() < 10 ? '0' + date.getHours() + ':' : date.getHours() + ':';
    let m = date.getMinutes() < 10 ? '0' + date.getMinutes() + ':' : date.getMinutes();
    return h + m;
  };

  Vue.prototype.baseFormatDate = function (val) {
    if (!val) {
      return "-";
    }
    let date = new Date(val);
    let Y = date.getFullYear() + '-';
    let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) + '-' : date.getMonth() + 1 + '-';
    let D = date.getDate() < 10 ? '0' + date.getDate() + ' ' : date.getDate() + ' ';
    return Y + M + D;
  };

  Vue.prototype.baseFormatMonth = function (val) {
    if (!val) {
      return "-";
    }
    let date = new Date(val);
    let Y = date.getFullYear().toString().substr(2, 2);
    let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1;
    return Y + "-" + M;
  };

  Vue.prototype.baseTableFormatTime = function (row, col, val) {
    return Vue.prototype.baseFormatTime(val);
  };

  Vue.prototype.baseTableFormatDate = function (row, col, val) {
    return Vue.prototype.baseFormatDate(val);
  };

  Vue.prototype.baseConfirmDelete = function (msg, func) {
    this.$confirm(msg, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(() => {
      func();
    }).catch(() => {
      console.log("操作已取消")
    });
  };

  Vue.prototype.baseYesNo = function (row, col, val) {
    return val == true ? "是" : "否";
  };

  Vue.prototype.baseYesNoInt = function (row, col, val) {
    return parseInt(val) === 1 ? "是" : "否";
  };

  Vue.prototype.baseSex = function (row, col, val) {
    return val === true ? "男" : "女";
  };

  Vue.prototype.baseSexInt = function (val) {
    return parseInt(val) === 1 ? "男" : "女";
  };

  Vue.prototype.baseFormatMoney = function (val) {
    if (!val) {
      val = "";
    }
    return "￥" + val;
  };

  Vue.prototype.baseIsNotBlank = function (val) {
    return val && val !== '' && val !== "" && val !== null;
  };

  Vue.prototype.baseIsBlank = function (val) {
    return !Vue.prototype.baseIsNotBlank(val);
  };

  Vue.prototype.baseIsValidNumber = function (val) {
    return !val && val != null && parseInt(val) >= 0;
  };

  Vue.prototype.baseIsNotValidNumber = function (val) {
    return !Vue.prototype.baseIsValidNumber(val);
  };

  Vue.prototype.baseIsValidFloat = function (val) {
    return !val && val != null && parseFloat(val) !== 0;
  };

  Vue.prototype.baseIsNotValidFloat = function (val) {
    return !Vue.prototype.baseIsValidFloat(val);
  }

};
