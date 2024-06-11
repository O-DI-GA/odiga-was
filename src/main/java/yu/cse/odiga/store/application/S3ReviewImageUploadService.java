package yu.cse.odiga.store.application;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yu.cse.odiga.global.S3.S3Util;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class S3ReviewImageUploadService {
    private final S3Util s3Util;

    public String upload(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = "review/" + uuid + "_" + originalFileName.replaceAll("\\s", "_");

        log.info("fileName: " + uniqueFileName);

        File uploadFile = s3Util.convert(multipartFile);

        String uploadImageUrl = s3Util.putS3(uploadFile, uniqueFileName);
        s3Util.removeNewFile(uploadFile);

        return uploadImageUrl;
    }
}