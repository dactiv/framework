package com.github.dactiv.framework.fasc.test;

import com.github.dactiv.framework.fasc.client.DocClient;

//@SpringBootTest
public class DocClientTest {

    //@Autowired
    private DocClient docClient;

    //@Test
    public void testGenerateSignTask() throws Exception {
        /*File localFile = new File("C:/Users/olale/OneDrive/文档/陈小菠-Java-广州.pdf");
        FileInputStream fileInputStream = new FileInputStream(localFile);
        BaseRes<GetUploadUrlRes> res = docClient.getUploadFileUrl(new GetUploadUrlReq(FileTypeEnum.DOC.getCode()));

        docClient.upload(new BufferedInputStream(fileInputStream), res.getData().getUploadUrl());

        BaseRes<FileProcessRes> fileProcess = docClient.process(new FileProcessReq(List.of(new FddFileUrl(FileTypeEnum.DOC.getCode(), res.getData().getFddFileUrl(), "陈小菠-Java-广州.pdf"))));*/
    }
}
