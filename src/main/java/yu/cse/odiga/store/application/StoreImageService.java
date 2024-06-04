package yu.cse.odiga.store.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yu.cse.odiga.global.S3.S3Util;
import yu.cse.odiga.store.dao.StoreImageRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.domain.StoreImage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreImageService {

    private final S3Util s3Util;
    private final StoreImageRepository storeImageRepository;
    
    public void upload(MultipartFile titleImageFile, List<MultipartFile> multipartFileList, Store store) throws IOException {

        String originalFileName = titleImageFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = "titleImage/" + uuid + "_" + originalFileName.replaceAll("\\s", "_");

        File uploadFile = s3Util.convert(titleImageFile);

        String uploadImageUrl = s3Util.putS3(uploadFile, uniqueFileName);
        s3Util.removeNewFile(uploadFile);

        store.setStoreTitleImage(uploadImageUrl);
        // 이미지 여러개 받는걸로 수정

        List<StoreImage> storeImages = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFileList){
            originalFileName = multipartFile.getOriginalFilename();
            uuid = UUID.randomUUID().toString();
            uniqueFileName = "storeImage/" + uuid + "_" + originalFileName.replaceAll("\\s", "_");

            uploadFile = s3Util.convert(multipartFile);

            uploadImageUrl = s3Util.putS3(uploadFile, uniqueFileName);
            s3Util.removeNewFile(uploadFile);

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

    public void updateFile(MultipartFile titleImage, List<MultipartFile> newFile, String oldFileName, Store store) throws IOException {
        // 기존 파일 삭제
        log.info("S3 oldFileName: " + oldFileName);
        s3Util.deleteFile(oldFileName);
        // 새 파일 업로드
        upload(titleImage, newFile, store);
    }
}
