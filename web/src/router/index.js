import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/Home'
import WorkDetails from "../components/work/WorkDetails";
import SearchArrears from "../components/search/SearchArrears";
import SearchLose from "../components/search/SearchLose";
import WorkArrears from "../components/work/WorkArrears";
import SearchExpire from "../components/search/SearchExpire";
import SearchOvertime from "../components/search/SearchOvertime";
import StudentResumption from "../components/student/StudentResumption";
import StudentTransfer from "../components/student/StudentTransfer";
import StudentChange from "../components/student/StudentChange";
import StudentAbsence from "../components/student/StudentAbsence";
import ReportYear from "../components/report/ReportYear";
import SettingSchool from "../components/setting/SettingSchool";
import SettingTeacher from "../components/setting/SettingTeacher";
import SettingCharge from "../components/setting/SettingCharge";
import SettingConfig from "../components/setting/SettingConfig";
import SettingSubject from "../components/setting/SettingSubject";
import SettingSemester from "../components/setting/SettingSemester";
import SettingClass from "../components/setting/SettingClass";
import ReportCharge from "../components/report/ReportCharge";

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: '工作台',
      component: Home,
      meta: {
        title: '工作台'
      }
    },
    {
      path: '/home',
      name: '工作台',
      component: Home,
      meta: {
        title: '工作台'
      }
    },
    {
      path: '/work/details',
      name: '收费查询',
      component: WorkDetails,
      meta: {
        title: '收费查询'
      }
    },
    {
      path: '/work/arrears',
      name: '欠费查询',
      component: WorkArrears,
      meta: {
        title: '欠费查询'
      }
    },
    {
      path: '/search/expire',
      name: '到期',
      component: SearchExpire,
      meta: {
        title: '到期'
      }
    },
    {
      path: '/search/overtime',
      name: '超期',
      component: SearchOvertime,
      meta: {
        title: '超期'
      }
    },
    {
      path: '/search/lose',
      name: '流失',
      component: SearchLose,
      meta: {
        title: '流失'
      }
    },
    {
      path: '/search/arrears',
      name: '欠费',
      component: SearchArrears,
      meta: {
        title: '欠费'
      }
    },
    {
      path: '/student/change',
      name: '转班',
      component: StudentChange,
      meta: {
        title: '转班'
      }
    },
    {
      path: '/student/absence',
      name: '休学',
      component: StudentAbsence,
      meta: {
        title: '休学'
      }
    },
    {
      path: '/student/resumption',
      name: '复学办理',
      component: StudentResumption,
      meta: {
        title: '复学办理'
      }
    },
    {
      path: '/student/transfer',
      name: '转期',
      component: StudentTransfer,
      meta: {
        title: '转期'
      }
    },
    {
      path: '/report/year',
      name: '年月报表',
      component: ReportYear,
      meta: {
        title: '年月报表'
      }
    },
    {
      path: '/report/charge',
      name: '支付类型报表',
      component: ReportCharge,
      meta: {
        title: '支付类型报表'
      }
    },

    {
      path: '/setting/school',
      name: '校区管理',
      component: SettingSchool,
      meta: {
        title: '校区管理'
      }
    },
    {
      path: '/setting/teacher',
      name: '员工管理',
      component: SettingTeacher,
      meta: {
        title: '员工管理'
      }
    },
    {
      path: '/setting/charge',
      name: '收费类型',
      component: SettingCharge,
      meta: {
        title: '收费类型'
      }
    },
    {
      path: '/setting/config',
      name: '功能设置',
      component: SettingConfig,
      meta: {
        title: '功能设置'
      }
    },
    {
      path: '/setting/subject',
      name: '科目设置',
      component: SettingSubject,
      meta: {
        title: '科目设置'
      }
    },
    {
      path: '/setting/semester',
      name: '学期设置',
      component: SettingSemester,
      meta: {
        title: '学期设置'
      }
    },
    {
      path: '/setting/class',
      name: '班级设置',
      component: SettingClass,
      meta: {
        title: '班级设置'
      }
    }
  ]
})
