// 公共方法
exports.install = function (Vue, options) {



  Vue.prototype.initNullCourse = function () {
    return {courseId: null, expireTime: null, courseLabel: "*课程信息*", beginTime: null, riseClassTime: null};
  };
  /**
   *
   * @param item
    {
      "id":5,
      "createDate":1567239365000,
      "updateDate":1567240809000,
      "createBy":-1,
      "updateBy":-1,
      "deptSchoolId":111459538,
      "teacherId":-1,
      "dictCourseId":3,
      "courseIndex":3,
      "regularTime":"每周三、周五10点到11点",
      "classroomNo":"008",
      "dictCourseName":"色彩",
      "teacherName":"测试员工",
      "deptSchoolName":"市场部",
      "courseIndexName":"三",
      "createByName":"测试员工",
      "updateByName":"测试员工"
  }
   *
   * @returns {*}
   */
  Vue.prototype.appBillCourseName = function (item) {
    return item.dictCourseName + item.courseIndexName + " + " + item.teacherName;
  };

  Vue.prototype.appBillCourseLabel = function (item) {
    return " 教室号：" + item.classroomNo + " 上课时间：" + item.regularTime;
  };


  Vue.prototype.appDigitUppercase = function (n) {
    const fraction = ['角', '分'];
    const digit = [
      '零', '壹', '贰', '叁', '肆',
      '伍', '陆', '柒', '捌', '玖'
    ];
    const unit = [
      ['元', '万', '亿'],
      ['', '拾', '佰', '仟']
    ];
    const head = n < 0 ? '欠' : '';
    n = Math.abs(n);
    let s = '';
    for (let i = 0; i < fraction.length; i++) {
      s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
    }
    s = s || '整';
    n = Math.floor(n);
    for (let i = 0; i < unit[0].length && n > 0; i++) {
      let p = '';
      for (let j = 0; j < unit[1].length && n > 0; j++) {
        p = digit[n % 10] + unit[1][j] + p;
        n = Math.floor(n / 10);
      }
      s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
    }
    return head + s.replace(/(零.)*零元/, '元')
      .replace(/(零.)+/g, '零')
      .replace(/^整$/, '零元整');
  };

  Vue.prototype.formatClass = function (course) {
    if (course) {
      return course.dictCourseName + course.courseIndexName;
    }
    return '-';
  };

  Vue.prototype.appYesNoArray = function () {
    return [ {id: 1, name: "是"}, {id: 0, name: "否"}]
  };

  Vue.prototype.goToPage = function (url) {
    const _this = this;
    _this.$router.push({path: url});
  };

  Vue.prototype.formatTableClass1=function(row, col, v)
  {
    if (row.courses && row.courses.length >= 1) {
      return this.formatClass(row.courses[0]);
    }
  };

  Vue.prototype.formatTableClass2=function(row, col, v)
  {
    if (row.courses && row.courses.length >= 2) {
      return this.formatClass(row.courses[1]);
    }
  };

};
