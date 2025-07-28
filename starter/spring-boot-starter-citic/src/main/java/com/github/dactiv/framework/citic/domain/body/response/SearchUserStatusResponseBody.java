package com.github.dactiv.framework.citic.domain.body.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.github.dactiv.framework.citic.domain.metadata.BasicResponseMetadata;

import java.io.Serial;

/**
 * @author maurice.chen
 */
public class SearchUserStatusResponseBody extends BasicResponseMetadata {

    @Serial
    private static final long serialVersionUID = 1402600926033955985L;

    /**
     * 审核失败原因
     */
    @JacksonXmlProperty(localName = "CHECK_FAIL_REASON")
    private String checkFailReason;

    @JacksonXmlProperty(localName = "USER_CHECK_ST")
    private String userCheckStatus;

    @JacksonXmlProperty(localName = "MCHNT_ID")
    private String merchantId;

    /**
     * 用户 id
     */
    @JacksonXmlProperty(localName = "USER_ID")
    private String userId;

    /**
     * 真实姓名
     */
    @JacksonXmlProperty(localName = "USER_NM")
    private String realName;

    /**
     * 用户状态
     */
    @JacksonXmlProperty(localName = "USER_ST")
    private String userStatus;
}
