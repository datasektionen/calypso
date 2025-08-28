package se.datasektionen.calypso.s3;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import se.datasektionen.calypso.util.FileUtils;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(S3Client s3Client, @Value("${S3_BUCKET_NAME}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String uploadImage(MultipartFile image, String extension, Long itemId) {
        var key = "images/" + itemId.toString() + extension;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        try {
            s3Client.putObject(request, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));
            return "https://" + bucketName + ".s3.eu-north-1.amazonaws.com/" + key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
