package yu.cse.odiga.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.global.S3.S3Util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class S3ProfileImageUploadService {

    private final S3Util s3Util;

    public void upload(MultipartFile multipartFile, User user) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = "profile/" + uuid + "_" + originalFileName.replaceAll("\\s", "_");

        log.info("fileName: " + uniqueFileName);

        File uploadFile = s3Util.convert(multipartFile);

        String uploadImageUrl = s3Util.putS3(uploadFile, uniqueFileName);
        s3Util.removeNewFile(uploadFile);

        user.setProfileImageUrl(uploadImageUrl);

    }

    public void updateFile(MultipartFile newFile, String oldFileName, User user) throws IOException {
        // 기존 파일 삭제
        log.info("S3 oldFileName: " + oldFileName);
        s3Util.deleteFile(oldFileName);
        // 새 파일 업로드
        upload(newFile, user);
    }

}