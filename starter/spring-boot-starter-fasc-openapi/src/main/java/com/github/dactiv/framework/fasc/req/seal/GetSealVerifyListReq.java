package com.github.dactiv.framework.fasc.req.seal;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 * @Author： Fadada
 * @Date: 2022/10/8
 */
public class GetSealVerifyListReq extends BaseReq {

	private String openCorpId;

	public String getOpenCorpId() {
		return openCorpId;
	}

	public void setOpenCorpId(String openCorpId) {
		this.openCorpId = openCorpId;
	}
}
