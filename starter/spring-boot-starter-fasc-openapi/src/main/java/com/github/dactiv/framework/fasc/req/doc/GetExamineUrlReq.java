package com.github.dactiv.framework.fasc.req.doc;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;
import com.github.dactiv.framework.fasc.bean.common.OpenId;

/**
 * @author zhoufucheng
 * @date 2022/12/28 0028 16:01
 */
public class GetExamineUrlReq extends BaseReq {
    private OpenId initiator;
    private String fileId;

    public OpenId getInitiator() {
        return initiator;
    }

    public void setInitiator(OpenId initiator) {
        this.initiator = initiator;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
