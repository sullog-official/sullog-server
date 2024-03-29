package sullog.backend.record.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sullog.backend.common.error.ErrorCode;
import sullog.backend.record.error.exception.RecordException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ImageUploadServiceAwsImpl implements ImageUploadService{

    private AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("#{'${allow-image-format}'.split(',')}")
    private List<String> allowedExtensions;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public List<String> uploadImageList(List<MultipartFile> multipartFileList) {
        if(null == multipartFileList) {
            return Collections.emptyList();
        }

        List<String> pathList = new ArrayList<>();
        for (MultipartFile multipartFile: multipartFileList) {
            pathList.add(uploadToS3(multipartFile));
        }

        return pathList;
    }

    private String uploadToS3(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();

        //파일 형식 구하기
        String contentType = getFileContentType(fileName);

        //S3 서버에 업로드
        uploadS3(multipartFile, fileName, contentType);

        // 경로 리턴
        return s3Client.getUrl(bucket, fileName).toString();
    }

    private void uploadS3(MultipartFile multipartFile, String fileName, String contentType) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(multipartFile.getSize());

            s3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            log.error("S3에 업로드 중 예외 발생", e);
            throw new RecordException(ErrorCode.IMAGE_STORAGE_ERROR);
        }
    }

    private String getFileContentType(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (false == allowedExtensions.contains(ext)) {
            throw new RecordException(ErrorCode.IMAGE_FORMAT_ERROR);
        }

        return "image/"+ext;
    }
}
