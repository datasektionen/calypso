package se.datasektionen.calypso.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(S3Client s3Client, @Value("${S3_BUCKET_NAME}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String uploadImage(MultipartFile image, Long itemId) {
        var index = image.getOriginalFilename().lastIndexOf(".");

        var extension = index == -1 ? ".png" : image.getOriginalFilename().substring(index);
        if (!extension.equals(".png") && !extension.equals(".jpeg") && !extension.equals(".jpg")) { 
            return null;
        } //TODO: should error if you sent funny extension
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
