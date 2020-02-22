import axios from 'axios';
import AppConfig from "../config/config";
import CommonUtils from "./CommonUtils";

export default class HttpUtils {

  constructor() {

  }

  /**
   * 封装get，只用写请求成功和失败的实现
   * @param url
   * @param config
   * @returns {Promise<any>}
   */
  static appGet(url, config) {
    return new Promise(function (resolve, reject) {
      config = HttpUtils.improveConfig(config);
      axios.get(AppConfig.urlPrefix + url, config).then(response => {
        console.log("GET:URL=" + url)
        if (CommonUtils.isResponseOK(response)) {
          resolve(response.data.message);
        } else {
          reject(response.data.message);
        }
      }).catch(e => {
        HttpUtils.handleException(url, e);
      }).finally(obj => {
        HttpUtils.finallyLog(url, obj);
      });
    });
  }

  /**
   * 封装post，只用写请求成功和失败的实现
   * @param url
   * @param data
   * @param config
   * @returns {Promise<any>}
   */
  static appPost(url, data, config) {
    const _this = this;
    return new Promise(function (resolve, reject) {
      const urlPost = AppConfig.urlPrefix + url;
      config = HttpUtils.improveConfig(config);
      console.log("POST:URL=" + urlPost + "|data=" + JSON.stringify(data) + "|config=" + JSON.stringify(config))
      axios.post(urlPost, data, config).then(response => {
        if (CommonUtils.isResponseOK(response)) {
          resolve(response.data.message);
        } else {
          reject(response.data.message);
        }
      }).catch(e => {
        HttpUtils.handleException(url, e);
      }).finally(obj => {
        HttpUtils.finallyLog(url, obj);
      });
    });

  }

  static handleException(url, e) {
    alert("数据请求发生异常，请联系管理员。URL:" + url + "提示:" + e.message);
  }

  static finallyLog(url, obj) {
    console.log("数据请求URL:" + url);
  }

  static getUserId() {
    const userString = window.localStorage.getItem("user");
    const user = JSON.parse(userString);
    if (user != null) {
      return user.id;
    } else {
      return -1;
    }
  }

  static improveConfig(config) {
    if (config == null) {
      config = {
        headers: {
          "USER_ID": this.getUserId()
        }
      }
    } else {
      config.headers.USER_ID = this.getUserId();
    }
    return config;
  }


}
