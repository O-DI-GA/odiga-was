package yu.cse.odiga.store.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yu.cse.odiga.store.dao.StoreImageRepository;
import yu.cse.odiga.store.dao.StoreTitleImageRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.domain.StoreImage;
import yu.cse.odiga.store.domain.StoreTItleImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreImageService {

    private final AmazonS3 amazonS3;
    private final StoreImageRepository storeImageRepository;
    private final StoreTitleImageRepository storeTitleImageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void upload(MultipartFile titleImageFile, List<MultipartFile> multipartFileList, Store store) throws IOException {

        String originalFileName = titleImageFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        File uploadFile = convert(titleImageFile);

        String uploadImageUrl = putS3(uploadFile, uniqueFileName);
        removeNewFile(uploadFile);

        StoreTItleImage storeTItleImage = StoreTItleImage.builder()
                .postImageUrl(uploadImageUrl)
                .store(store)
                .createDate(LocalDateTime.now())
                .build();

        storeTitleImageRepository.save(storeTItleImage);      // 개짜침

        // 이미지 여러개 받는걸로 수정

        List<StoreImage> storeImages = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFileList){
            originalFileName = multipartFile.getOriginalFilename();
            uuid = UUID.randomUUID().toString();
            uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

            uploadFile = convert(multipartFile);

            uploadImageUrl = putS3(uploadFile, uniqueFileName);
            removeNewFile(uploadFile);

            StoreImage storeImage = StoreImage.builder()
                    .postImageUrl(uploadImageUrl)
                    .store(store)
                    .createDate(LocalDateTime.now())
                    .build();

            storeImages.add(storeImage);

            storeImageRepository.save(storeImage);
        }

        store.setStoreImages(storeImages);

    }

    private File convert(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        File convertFile = new File(uniqueFileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                log.error("파일 변환 중 오류 발생: {}", e.getMessage());
                throw e;
            }
            return convertFile;
        }
        throw new IllegalArgumentException(String.format("파일 변환에 실패했습니다. %s", originalFileName));
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    public void deleteFile(String fileName) {
        try {
            // URL 디코딩을 통해 원래의 파일 이름을 가져옴
            String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("Deleting file from S3: " + decodedFileName);
            amazonS3.deleteObject(bucket, decodedFileName);
        } catch (UnsupportedEncodingException e) {
            log.error("Error while decoding the file name: {}", e.getMessage());
        }
    }

    public void updateFile(MultipartFile titleImage, List<MultipartFile> newFile, String oldFileName, Store store) throws IOException {
        // 기존 파일 삭제
        log.info("S3 oldFileName: " + oldFileName);
        deleteFile(oldFileName);
        // 새 파일 업로드
        upload(titleImage, newFile, store);
    }
}
