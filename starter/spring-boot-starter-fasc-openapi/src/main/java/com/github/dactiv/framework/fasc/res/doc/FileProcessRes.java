package com.github.dactiv.framework.fasc.res.doc;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

import java.util.List;

/**
 * @author gongj
 * @date 2022/7/13
 */
public class FileProcessRes extends BaseBean {
    private List<FileId> fileIdList;

    public List<FileId> getFileIdList() {
        return fileIdList;
    }

    public void setFileIdList(List<FileId> fileIdList) {
        this.fileIdList = fileIdList;
    }
}
