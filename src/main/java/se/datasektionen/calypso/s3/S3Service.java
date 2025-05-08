package se.datasektionen.calypso.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(S3Client s3Client, @Value("${S3_BUCKET_NAME}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String uploadImage(File image, Long itemId) {
        var index = image.getName().lastIndexOf(".");

        var extension = index == -1 ? ".png" : image.getName().substring(index);

        if (extension != ".png" && extension != ".jpeg" && extension != ".jpg") return null;

        var key = "images/" + itemId.toString() + extension;
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        try {
            s3Client.putObject(request, image.toPath());
            return "https://" + bucketName + ".s3.eu-north-1.amazonaws.com/" + key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
