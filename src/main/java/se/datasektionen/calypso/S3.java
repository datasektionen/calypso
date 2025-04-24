// package se.datasektionen.calypso;

// import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
// import software.amazon.awssdk.regions.Region;
// import software.amazon.awssdk.services.s3.S3Client;
// import software.amazon.awssdk.services.s3.model.PutObjectRequest;
// import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

// import java.io.File;
// import java.net.URL;

// // bucket dsekt-calypso-assets
// // access key id AKIATUCF4UAOQC5G2U5Z
// // access key secret
// public class S3 {
//     public void initialize() {
//         String bucketName = "calypso";
//         String keyName = "images/<postId>.jpg"; // key = path in bucket
//         String filePath = "blablaimage.jpg"; // local file

//         Region region = Region.EU_NORTH_1;
//         S3Client s3 = S3Client.builder()
//                 .region(region)
//                 .credentialsProvider(ProfileCredentialsProvider.create())
//                 .build();

//         uploadPublicFile(s3, bucketName, keyName, filePath);

//         // Get the public URL
//         String publicUrl = "https://" + bucketName + ".s3." + region.id() + ".amazonaws.com/" + keyName;
//         System.out.println("Public URL: " + publicUrl);

//         s3.close();
//     }

//     public static void uploadPublicFile(S3Client s3, String bucketName, String keyName, String filePath) {
//         PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                 .bucket(bucketName)
//                 .key(keyName)
//                 .acl(ObjectCannedACL.PUBLIC_READ) // <-- Makes the object public
//                 .build();
//         try {
//             s3.putObject(putObjectRequest, new File(filePath).toPath());
//         } catch {
//             //blabla
//         }
//     }
// }