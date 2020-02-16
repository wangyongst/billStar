import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/Home'
import Teachers from '../components/Teachers'
import SchoolZones from "../components/SchoolZones";
import ClassTypes from "../components/ClassTypes";
import ChargeTypes from "../components/ChargeTypes";
import Courses from "../components/Courses";
import Students from "../components/Students";
import Bills from "../components/Bills";
import YearReport from "../components/YearReport";
import PayTypeReport from "../components/PayTypeReport";
import Configs from "../components/Configs";
import StudentCourses from "../components/StudentCourses"
import StudentCourseBills from "../components/StudentCourseBills";
import Semesters from "../components/Semesters";
import Subjects from "../components/Subjects";
import CourseStudents from "../components/CourseStudents";
import SubjectStudentReport from "../components/SubjectStudentReport";
import RenewalsStudent from "../components/RenewalsStudent";

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
      path: '/renewalsStudent',
      name: '续费',
      component: RenewalsStudent,
      meta: {
        title: '续费'
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
      path: '/home',
      name: '工作台',
      component: Home,
      meta: {
        title: '工作台'
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
      path: '/bills',
      name: '票据信息',
      component: Bills,
      meta: {
        title: '票据信息'
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
