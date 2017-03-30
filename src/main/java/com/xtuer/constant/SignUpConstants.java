package com.xtuer.constant;

public interface SignUpConstants {
    int S_UNREVIEWED    = 0; // 待审核
    int S_REVIEWED      = 1; // 已审核
    int S_REVIEWED_FAIL = 2; // 未通过
    int T_LOSE          = 1; // 丧失,时限：终生
    int T_CANCEL2       = 2; // 撤销（品行不良、侮辱学生）,时限：5年
    int T_CANCEL1       = 3; // 撤销（弄虚作假、骗取教师资格）,时限：5年
    int T_CHEAT         = 4; // 考试作弊：3年
    int UN_DO_DURATION  = 5; // 撤销处罚年限
    int STATUS_UN_DO    = 30;  // 撤销注册
    int STATUS_QUALIFIED     = 19; // ***注册合格
    int STATUS_CERT_LATER    = 27; // 暂缓注册
    int ENROLL_DURATION      = 54; // 注册间隔是54个月
    int DELETE_STATUS_NORMAL = 0; // 正常
    int DELETE_STATUS_FORBID = 1; // 被限制
    int DELETE_STATUS_DELETE = 2; // 删除
    int T_GLOBAL    = 5; // 资格中心
    int T_PROVINCE  = 4; // 省级机构
    int T_CITY      = 3; // 市级机构
    int T_COUNTY    = 2; // 县级机构
    int T_LOCAL     = 1; // 确认机构
    int T_ABROAD    = 0;
    int STATUS_UN_CERT = 15;
    int STATUS_UN_CONF = 6;
    int DATA_FROM_USER_ADD = 0;  // 网上申报
    int DATA_FROM_ADMIN_ADD = 1; // 后台采集

    int EXAM_TYPE_NO_EXAM = 0;
    int EXAM_TYPE_EXAM = 1;

    int TYPE_CERT = 1;//代表认定
    int TYPE_ENROLL = 2;//代表注册
    int TYPE_COMMON = 0;

    //统考标示使用状态
    int STATUS_USED = 1;// 使用
    int STATUS_NO_USED = 0;// 未使用

    //最高学位
    int ID_DEGREE = 6;
    //字典数据启用状态,0未启用,1启用
    int DICT_STATUS_ENABLE = 1;
    int DICT_STATUS_DISABLE = 0;


}
