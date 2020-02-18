import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/Home'
import Teachers from '../components/Teachers'
import SchoolZones from "../components/SchoolZones";
import ClassTypes from "../components/ClassTypes";
import ChargeTypes from "../components/ChargeTypes";
import Courses from "../components/Courses";
import Students from "../components/Students";
import YearReport from "../components/YearReport";
import PayTypeReport from "../components/PayTypeReport";
import Configs from "../components/Configs";
import StudentCourses from "../components/StudentCourses"
import StudentCourseBills from "../components/StudentCourseBills";
import Semesters from "../components/Semesters";
import Subjects from "../components/Subjects";
import CourseStudents from "../components/CourseStudents";
import SubjectStudentReport from "../components/SubjectStudentReport";
import WorkDetails from "../components/work/WorkDetails";
import SearchArrears from "../components/search/SearchArrears";
import SearchLose from "../components/search/SearchLose";
import WorkArrears from "../components/work/WorkArrears";
import SearchExpire from "../components/search/SearchExpire";
import SearchOvertime from "../components/search/SearchOvertime";

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
        title: '超期'
      }
    },

    {
      path: '/semesters',
      name: '学期信息',
      component: Semesters,
      meta: {
        title: '学期信息'
      }
    },
    {
      path: '/subjects',
      name: '科目信息',
      component: Subjects,
      meta: {
        title: '科目信息'
      }
    },
    {
      path: '/teachers',
      name: '所有教师',
      component: Teachers,
      meta: {
        title: '所有教师'
      }
    },
    {
      path: '/schoolZones',
      name: '所有校区',
      component: SchoolZones,
      meta: {
        title: '所有校区'
      }
    },
    {
      path: '/classTypes',
      name: '所有班级',
      component: ClassTypes,
      meta: {
        title: '所有班级'
      }
    },
    {
      path: '/chargeTypes',
      name: '收费类型',
      component: ChargeTypes,
      meta: {
        title: '收费类型'
      }
    },
    {
      path: '/courses',
      name: '所有课程',
      component: Courses,
      meta: {
        title: '所有课程'
      }
    },
    {
      path: '/students',
      name: '学生信息',
      component: Students,
      meta: {
        title: '学生信息'
      }
    },

    {
      path: '/yearReport',
      name: '月度报表',
      component: YearReport,
      meta: {
        title: '月度报表'
      }
    },
    {
      path: '/payTypeReport',
      name: '支付类型报表',
      component: PayTypeReport,
      meta: {
        title: '支付类型报表'
      }
    },
    {
      path: '/configs',
      name: '基础配置',
      component: Configs,
      meta: {
        title: '基础配置'
      }
    },
    {
      path: '/studentCourses',
      name: '学生课程',
      component: StudentCourses,
      meta: {
        title: '学生课程'
      }
    },
    {
      path: '/courseStudents/:courseId',
      name: '课程学生',
      component: CourseStudents,
      meta: {
        title: '课程学生'
      }
    },
    {
      path: '/studentCourseBills',
      name: '学生课程开票记录',
      component: StudentCourseBills,
      meta: {
        title: '学生课程开票记录'
      }
    },
    {
      path: '/subjectStudentReport',
      name: '科目学生统计',
      component: SubjectStudentReport,
      meta: {
        title: '科目学生统计'
      }
    }

  ]
})
