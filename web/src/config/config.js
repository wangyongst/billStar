const AppConfig = {};

AppConfig.urlPrefix = "bill";

AppConfig.billTypes = {
  newBill: 1,
  renewals: 2,
  supplement: 3,
  refund: 4,
  transferSchool: 5,
  transferClass: 0,
  transferSemester:6,
  modifyInfo: 9,
};

export default AppConfig;
