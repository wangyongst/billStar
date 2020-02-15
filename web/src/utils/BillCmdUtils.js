import appConfig from "../config/config"

const BillCmdUtils = {
  /**
   * 欠费输入框是否显示
   * @param type 操作类型
   * @returns {boolean} 是否显示
   */
  showArrearsField: function (type) {
    // 新生和续费的时候显示
    console.log("showArrearsField=", type);
    type = parseInt(type);
    return type === appConfig.billTypes.newBill
      || type === appConfig.billTypes.renewals
      || type === appConfig.billTypes.modifyInfo
      || type === appConfig.billTypes.transferClass;
  },

  isArrearsFieldDisabled: function (type) {
    // 新生和续费的时候显示
    console.log("showArrearsField=", type);
    type = parseInt(type);
    return type === appConfig.billTypes.modifyInfo
      || type === appConfig.billTypes.transferClass;
  },

  isSchoolZoneFieldDisabled: function (type) {
    // 新生转校的时候显示
    console.log("showArrearsField=", type);
    type = parseInt(type);
    return type !== appConfig.billTypes.newBill;
  },


  isAmountFieldDisabled: function (type) {
    // 新生转校的时候显示
    console.log("showArrearsField=", type);
    const typesNeedDisabled = [
      appConfig.billTypes.transferClass];
    return typesNeedDisabled.includes(type);
  },

  isCourseFieldDisabled: function (type) {
    const typesNeedDisabled = [appConfig.billTypes.refund];
    return typesNeedDisabled.includes(type);
  },

  isStudentFieldDisabled: function (type) {
    const typesNeedDisabled = [appConfig.billTypes.renewals,
      appConfig.billTypes.supplement,
      appConfig.billTypes.refund,
      appConfig.billTypes.transferClass,
      appConfig.billTypes.transferSchool,
      appConfig.billTypes.transferSemester,
      appConfig.billTypes.modifyInfo];
    return typesNeedDisabled.includes(type);
  },

  isSemesterDisabled: function (type) {
    return type === appConfig.billTypes.refund;
  },

  /**
   * 获取提交地址
   * @param type 类型
   * @returns {string|boolean}
   */
  getBillCmdUrl(type) {
    // 新增
    if (type === appConfig.billTypes.newBill) {
      return "/billOperate/createBill";
    }
    // 续费
    if (type === appConfig.billTypes.renewals) {
      return "/billOperate/renewals";
    }
    // 补费
    if (type === appConfig.billTypes.supplement) {
      return "/billOperate/supplement";
    }
    // 退费
    if (type === appConfig.billTypes.refund) {
      return "/billOperate/refund";
    }
    // 转校
    if (type === appConfig.billTypes.transferSchool) {
      return "/billOperate/transferSchool";
    }
    // 转班
    if (type === appConfig.billTypes.transferClass) {
      return "/billOperate/transferClass";
    }
    // 修改信息
    if (type === appConfig.billTypes.modifyInfo) {
      return "/billOperate/updateBill";
    }
    // 转期
    if (type === appConfig.billTypes.transferSemester) {
      return "/billOperate/transferSemester";
    }
    return false;
  },

  /**
   * 获取表单标题
   * @param type 类型
   * @returns {string} 标题
   */
  getBillCmdTitle(type) {
    // 新增
    if (type === appConfig.billTypes.newBill) {
      return "新票据";
    }
    // 续费
    if (type === appConfig.billTypes.renewals) {
      return "续费";
    }
    // 补费
    if (type === appConfig.billTypes.supplement) {
      return "补费";
    }
    // 退费
    if (type === appConfig.billTypes.refund) {
      return "退费";
    }
    // 转校
    if (type === appConfig.billTypes.transferSchool) {
      return "转校";
    }
    // 转班
    if (type === appConfig.billTypes.transferClass) {
      return "转班";
    }
    // 转班
    if (type === appConfig.billTypes.transferSemester) {
      return "转期";
    }
    // 修改信息
    if (type === appConfig.billTypes.modifyInfo) {
      return "修改票据信息";
    }
    return "——";
  }


};

export default BillCmdUtils;
