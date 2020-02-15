// 公共方法
export default class BillOperate {

  constructor() {
    console.log("load component bill operate");
  }

  // 新增
  static createBill() {
    eventBus.$emit("createBill");
  };

  // 续费
  static renewals(id) {
    eventBus.$emit("renewals", id);
  };

  // 补费
  static supplement(id) {
    eventBus.$emit("supplement", id);
  };

  // 退费
  static refund(id) {
    eventBus.$emit("refund", id);
  };

  // 转校
  static transferSchool(id) {
    eventBus.$emit("transferSchool", id);
  };

  // 转班
  static transferClass(id) {
    eventBus.$emit("transferClass", id);
  };

  // 修改信息
  static modifyInfo(id) {
    eventBus.$emit("modifyInfo", id);
  };

  // 修改信息
  static transferSemester(id) {
    eventBus.$emit("transferSemester", id);
  };


};
