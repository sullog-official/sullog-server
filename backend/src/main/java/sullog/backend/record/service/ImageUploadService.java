package sullog.backend.record.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sullog.backend.record.error.exception.ImageUploadException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ImageUploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public ImageUploadService(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public List<String> uploadImageList(List<MultipartFile> multipartFileList) {
        if(multipartFileList.isEmpty()) {
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
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void uploadS3(MultipartFile multipartFile, String fileName, String contentType) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            log.error("S3에 업로드 중 예외 발생", e);
            throw new ImageUploadException("S3에 업로드 중 예외 발생"); // TODO common error 정의해서 던지기
        }
    }

    private String getFileContentType(String fileName) {
        String ext = fileName.split("\\.")[1];

        switch (ext) {
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            default:
                throw new ImageUploadException("올바른 이미지가 아닙니다."); // TODO common error로 정의해서 처리
        }
    }
}
