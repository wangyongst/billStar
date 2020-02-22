// import * as dd from "dingtalk-jsapi";
// import router from '../router';
// import ElementUI from 'element-ui';
import AppConfig from "../config/config"

export default class CommonUtils {

  constructor() {

  }

  /**
   * 字符串是否是有效字符串
   * @param msg
   * @returns {boolean}
   */
  static isStringValid(msg) {
    return msg !== undefined && msg !== null && msg !== "" && msg !== '';
  }

  static isResponseOK(response) {
    const success = response.data.code;
    return success === 1;
  }

  static handleAjaxResponse(response) {
    const resOk = this.isResponseOK(response);
    return new Promise(function (resolve, reject) {
      if (resOk) {
        resolve(response.data);
      } else {
        reject(response.data);
      }
    });
  }

  static handleAjaxResWithDefaultReject(response, event) {
    const resOk = CommonUtils.isResponseOK(response);
    return new Promise(function (resolve, reject) {
      if (resOk) {
        resolve(response.data);
      } else {
        CommonUtils.commonAlert(response.data.msg, event);
        event.preventDefault();
      }
    });
  }

  static commonAlert(msg) {
    const _this = this;
    _this.$notify({
      title: '成功',
      message: 'msg',
      type: 'success'
    });
  }

  static commonErrorAlert(msg) {
    alert(msg);
  }

  static setTitle(title) {

  }

  static setToHome() {

  }

  static improveImgUrl(imgUrl) {
    return window.location.protocol + "//" + window.location.host + imgUrl;
  }


  static formatNumber(n) {
    n = n.toString();
    return n[1] ? n : '0' + n;
  }

  static formatTime(date) {
    if (date == null) {
      return "";
    }
    if (typeof date === 'number') {
      date = new Date(date);
    }
    const
      year = date.getFullYear();
    const
      month = date.getMonth() + 1;
    const
      day = date.getDate();
    const
      hour = date.getHours();
    const
      minute = date.getMinutes();
    const
      second = date.getSeconds();
    return [year, month, day].map(this.formatNumber).join('-') + ' ' + [hour, minute, second].map(this.formatNumber).join(':');
  }

  static formatDate(date) {
    if (date == null) {
      return "";
    }
    if (typeof date === 'number') {
      date = new Date(date);
    }
    const
      year = date.getFullYear();
    const
      month = date.getMonth() + 1;
    const
      day = date.getDate();
    return [year, month, day].map(this.formatNumber).join('-');
  }

  static appBillType(val) {
    let typeName = "";
    switch (val) {
      case AppConfig.billTypes.newBill:
        typeName = "新生";
        break;
      case AppConfig.billTypes.renewals:
        typeName = "续费";
        break;
      case AppConfig.billTypes.supplement:
        typeName = "补费";
        break;
      case AppConfig.billTypes.refund:
        typeName = "退费";
        break;
      case AppConfig.billTypes.transferSchool:
        typeName = "转校";
        break;
      case AppConfig.billTypes.transferSemester:
        typeName = "转期";
        break;
      default:
        typeName = "未知";
        break;
    }
    return typeName;
  };

  static appTableBillType(row, col, val) {
    return CommonUtils.appBillType(val);
  };

}
