//package yu.cse.odiga.auth.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.io.UrlResource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import yu.cse.odiga.auth.application.S3ProfileImageUploadService;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("api/v1/user/s3")
//@RequiredArgsConstructor
//public class S3Controller {
//
//    private final S3ProfileImageUploadService s3UploadService;
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            String fileUrl = s3UploadService.saveFile(file);
//            return ResponseEntity.ok(fileUrl);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 중 오류가 발생했습니다.");
//        }
//    }
//
//    @GetMapping("/download")
//    public ResponseEntity<UrlResource> downloadFile(@RequestParam("filename") String filename) {
//        return s3UploadService.downloadImage(filename);
//    }
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteFile(@RequestParam("filename") String filename) {
//        s3UploadService.deleteImage(filename);
//        return ResponseEntity.ok("파일이 성공적으로 삭제되었습니다.");
//    }
//}
