package com.github.dactiv.framework.fasc.req.user;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 * @author zhoufucheng
 * @date 2023/8/9 9:17
 */
public class GetFaceRecognitionStatusReq extends BaseReq {
    private String serialNo ;

    private Integer getFile ;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getGetFile() {
        return getFile;
    }

    public void setGetFile(Integer getFile) {
        this.getFile = getFile;
    }
}
