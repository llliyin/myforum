package com.qly.myforum.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import com.qly.myforum.exception.CustomizeErrorCode;
import com.qly.myforum.exception.CustomizeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class UCloudProvider {

    @Value("${ucloud.ufile.public-key}")
    private String publicKey;

    @Value("${ucloud.ufile.region}")
    private String region;

    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName;

    @Value("${ucloud.ufile.private-key}")
    private String privateKey;

    @Value("${ucloud.ufile.suffix}")
    private String suffix;

    @Value("${ucloud.ufile.expires}")
    private int expiresDuration;

    public String upload(InputStream fileStream,String mimeType,String fileName){
        ObjectAuthorization OBJECT_AUTHORIZER = new UfileObjectLocalAuthorization(publicKey, privateKey);
        ObjectConfig config = new ObjectConfig(region, suffix);
//        防止文件名重复
        String generateName="";
        String[] split =  fileName.split("\\.");
        if(split.length>1){
           generateName = UUID.randomUUID().toString() + split[split.length - 1];
        }else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }

        try {

//            稍后对response进行判断，要是成功就将连接的地址进行返回
            PutObjectResultBean response = UfileClient.object(OBJECT_AUTHORIZER, config)
                    //springboot可以传递流，但是不能传递文件，所以可以看一下有没有方法可以利用流作为参数进行处理的
                    .putObject(fileStream, mimeType)
                    .nameAs(generateName)
                    .toBucket(bucketName)
                    /**
                     * 是否上传校验MD5, Default = true
                     */
                    //  .withVerifyMd5(false)
                    /**
                     * 指定progress callback的间隔, Default = 每秒回调
                     */
                    //  .withProgressConfig(ProgressConfig.callbackWithPercent(10))
                    /**
                     * 配置进度监听
                     */
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(long bytesWritten, long contentLength) {
                        }
                    })
                    .execute();
                    if(response !=null && response.getRetCode()==0){
                        String url = UfileClient.object(OBJECT_AUTHORIZER, config)
                                .getDownloadUrlFromPrivateBucket(generateName, bucketName, expiresDuration)
                                .createUrl();
                        return url;
                    }else {
                        log.error("upload error ,{}" ,response);
                        throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
                    }
        } catch (UfileClientException e) {
            log.error("upload error,{}",fileName,e);
            e.printStackTrace();
        } catch (UfileServerException e) {
            log.error("upload error,{}",fileName,e);
            e.printStackTrace();
        }
        return "";
    }


}
