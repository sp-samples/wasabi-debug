package sp.samples;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.ChecksumAlgorithm;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;

public class Wasabi {

    public S3Client createClientWithRegion(AwsBasicCredentials credentials, Region region){
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(region)
                .endpointOverride(URI.create("https://s3." + region + ".wasabisys.com"))
                .build();
    }

    public void pushFileContent(String bucket, String key, FileMetadata fileMetaData, S3Client client){
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .checksumAlgorithm(ChecksumAlgorithm.SHA256)
                .build();
        try {
            PutObjectResponse response = client.putObject(request,
                    RequestBody.fromInputStream(fileMetaData.getContent(), fileMetaData.getSizeInBytes()));
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public static void main(String[] args) {
        Wasabi wasabi = new Wasabi();
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                "V7RFALALT7ABFAEWIAHY",
                "lc5YTMQ5dOsBqmAJn8CgVGen5oWCI3R6hlPzwcz1");
        String bucket = "sample-dough"; // Bucket with full access to above credentials in the region us-east-1.
        try (
        S3Client client = wasabi.createClientWithRegion(credentials, Region.US_EAST_1)) {
            String fileContent = "Put this in file a.txt";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
            FileMetadata fileMetadata = new FileMetadata("a.txt", inputStream, (long) fileContent.length());
            wasabi.pushFileContent(bucket, "text_files", fileMetadata, client);
        }
    }

}
