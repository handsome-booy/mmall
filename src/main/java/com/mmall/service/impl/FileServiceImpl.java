package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 把上传后的文件名返回回去
     * @param file
     * @param path 该参数代表要上传的目录文件夹位置
     * @return
     */
    public String upload(MultipartFile file, String path) {
        //拿到原始上传文件的名字
        String fileName = file.getOriginalFilename();
        //拿到文件扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        //我们最终上传的文件名字,通过uuid防止重名
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件>>>上传的文件名为:{},上传的路径为：{},最中国的新文件名为：{}", fileName, path, uploadFileName);

        //声明目录文件夹
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        //创建我们的上传文件:文件夹路径+上传文件的新名字
        File targetFile = new File(path, uploadFileName);

        try {
            //将上传的原始文件复制到我们创建的新文件上
            file.transferTo(targetFile);

            //将targetFile上传到我们的ftp服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            //上传完文件后，删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }

        return targetFile.getName();
    }
}
