# 加字段
# billCourse beginTime riseClassTime
# bill initialArrears , currentArrears modelId
# student currentArrears
# 加表
# studentCourse


# 需要注意的事项
# 1、学生欠费做成加字段


# 欠费数据处理。用票据的欠费数据更新学生欠费数据。
update t_student s,
    (select sum(current_arrears) as arrears, student_id from t_bill group by student_id) t
set s.current_arrears = t.arrears
where s.id = t.student_id;

# 票据操作需要注意
# 1、新生 开票允许欠费，且新增的学生也有欠费。
# 2、续费 开票允许欠费，欠费自动累加到学生欠费。
# 3、补费仅针对有欠费票据，补费操作不允许再进行其他操作（续费等等）。反应在按钮上。
# 4、转班基本不变。
# 5、转校，欠费学生不可转校。反应在按钮上。
# 6、修改，欠费金额不可修改。


# 【学生课程】数据初始化的脚本

INSERT INTO `bill_star`.`t_student_course`
( `dept_school_id`, `student_id`, `course_id`, `begin_time`, `rise_class_time`, `expire_time`, `create_date`, `create_by`, `update_date`, `update_by`, `is_active`, `remark`)
select
    b.dept_school_id,
    b.student_id as student_id ,
    bc.course_id as course_id ,
    bc.begin_time ,
    bc.rise_class_time ,
    bc.expire_time ,
    sysdate(),
    -1,
    sysdate(),
    -1,
    1,
    ''
FROM t_bill_course bc left join t_bill b ON bc.bill_id = b.id
WHERE bc.id in (
    select max(t.bill_course_id) from (
                                          select
                                              bc.id as bill_course_id,
                                              b.student_id as student_id ,
                                              bc.course_id as course_id
                                          FROM t_bill_course bc left join t_bill b on bc.bill_id = b.id
                                      ) t group by t.course_id,t.student_id  );


