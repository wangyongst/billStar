import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/Home'
import WorkDetails from "../components/work/WorkDetails";
import SearchBackfee from "../components/search/SearchBackfee";
import SearchLose from "../components/search/SearchLose";
import WorkArrears from "../components/work/WorkArrears";
import SearchExpire from "../components/search/SearchExpire";
import SearchOvertime from "../components/search/SearchOvertime";
import StudentFu from "../components/student/StudentFu";
import StudentXiu from "../components/student/StudentXiu";
import ReportYear from "../components/report/ReportYear";
import SettingSchool from "../components/setting/SettingSchool";
import SettingTeacher from "../components/setting/SettingTeacher";
import SettingCharge from "../components/setting/SettingCharge";
import SettingConfig from "../components/setting/SettingConfig";
import SettingSubject from "../components/setting/SettingSubject";
import SettingSemester from "../components/setting/SettingSemester";
import SettingClass from "../components/setting/SettingClass";
import ReportCharge from "../components/report/ReportCharge";
import CourseTable from "../components/course/CourseTable";
import ReportStudents from "../components/report/ReportStudents";
import CourseManager from "../components/course/CourseManager";
import SettingClassNo from "../components/setting/SettingClassNo";
import SettingClassRoom from "../components/setting/SettingClassRoom";
import WorkXu from "../components/work/WorkXu";
import WorkTui from "../components/work/WorkTui";
import StudentQi from "../components/student/StudentQi";
import StudentBan from "../components/student/StudentBan";
import SettingMyShcool from "../components/setting/SettingMyShcool";
import SettingMyClass from "../components/setting/SettingMyClass";

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
      path: '/work/tui',
      name: '退费',
      component: WorkTui,
      meta: {
        title: '退费'
      }
    },
    {
      path: '/work/xu',
      name: '续费',
      component: WorkXu,
      meta: {
        title: '续费'
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
      path: '/search/backfee',
      name: '欠费',
      component: SearchBackfee,
      meta: {
        title: '欠费'
      }
    },
    {
      path: '/search/backfee',
      name: '退费',
      component: SearchBackfee,
      meta: {
        title: '退费'
      }
    },
    {
      path: '/student/ban',
      name: '转班',
      component: StudentBan,
      meta: {
        title: '转班'
      }
    },
    {
      path: '/student/xiu',
      name: '休学',
      component: StudentXiu,
      meta: {
        title: '休学'
      }
    },
    {
      path: '/student/fu',
      name: '复学办理',
      component: StudentFu,
      meta: {
        title: '复学办理'
      }
    },
    {
      path: '/student/qi',
      name: '转期',
      component: StudentQi,
      meta: {
        title: '转期'
      }
    },
    {
      path: '/course/table',
      name: '课程表',
      component: CourseTable,
      meta: {
        title: '课程表'
      }
    },
    {
      path: '/course/manager',
      name: '课程管理',
      component: CourseManager,
      meta: {
        title: '课程管理'
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
      path: '/report/student',
      name: '在校生',
      component: ReportStudents,
      meta: {
        title: '在校生'
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
    },
    {
      path: '/setting/class/no',
      name: '班别设置',
      component: SettingClassNo,
      meta: {
        title: '班级设置'
      }
    },
    {
      path: '/setting/class/room',
      name: '教室设置',
      component: SettingClassRoom,
      meta: {
        title: '教室设置'
      }
    },
    {
      path: '/setting/my/school',
      name: '学生学校',
      component: SettingMyShcool,
      meta: {
        title: '学生学校'
      }
    },
    {
      path: '/setting/my/class',
      name: '学生班级',
      component: SettingMyClass,
      meta: {
        title: '学生班级'
      }
    }
  ]
})
