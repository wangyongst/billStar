package com.tuofan.ding.response;

import lombok.Data;

@Data
public class ApiConfig {

    private String agentId; //: '', // 必填，微应用ID
    private String corpId; //: '',//必填，企业ID
    private long timeStamp;//: , // 必填，生成签名的时间戳
    private String nonceStr;// '', // 必填，生成签名的随机串
    private String signature;//: '', // 必填，签名
    private String url;
    private String appEnv;
}
