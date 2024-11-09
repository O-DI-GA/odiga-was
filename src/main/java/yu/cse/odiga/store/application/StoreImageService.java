package yu.cse.odiga.store.application;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import yu.cse.odiga.global.S3.S3Util;
import yu.cse.odiga.store.dao.StoreImageRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.domain.StoreImage;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreImageService {

	private final S3Util s3Util;
	private final StoreImageRepository storeImageRepository;

	public void uploadTitleImage(MultipartFile titleImageFile, Store store) throws IOException {
		String originalFileName = titleImageFile.getOriginalFilename();
		String uuid = UUID.randomUUID().toString();
		String uniqueFileName = "titleImage/" + uuid + "_" + originalFileName.replaceAll("\\s", "_");

		File uploadFile = s3Util.convert(titleImageFile);

		String uploadImageUrl = s3Util.putS3(uploadFile, uniqueFileName);
		s3Util.removeNewFile(uploadFile);

		store.setStoreTitleImage(uploadImageUrl);
	}

	public void uploadStoreImage(List<MultipartFile> multipartFileList, Store store) throws IOException {
		List<StoreImage> storeImages = new ArrayList<>();

		for (MultipartFile multipartFile : multipartFileList) {
			String originalFileName = multipartFile.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String uniqueFileName = "storeImage/" + uuid + "_" + originalFileName.replaceAll("\\s", "_");

			File uploadFile = s3Util.convert(multipartFile);

			String uploadImageUrl = s3Util.putS3(uploadFile, uniqueFileName);
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

	public void updateTitleImage(MultipartFile titleImage, String oldFileName, Store store) throws IOException {
		if (oldFileName != null && !oldFileName.isEmpty()) {
			s3Util.deleteFile(oldFileName);
		}
		uploadTitleImage(titleImage, store);
	}

	@Transactional
	public void updateStoreImage(List<MultipartFile> newFileList, Store store) throws IOException {
		List<StoreImage> originImages = store.getStoreImages();

		for (StoreImage storeImage : originImages) {
			s3Util.deleteFile(storeImage.getPostImageUrl());
		}

		originImages.clear();

		List<StoreImage> newStoreImages = new ArrayList<>();
		for (MultipartFile multipartFile : newFileList) {
			String originalFileName = multipartFile.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String uniqueFileName = "storeImage/" + uuid + "_" + originalFileName.replaceAll("\\s", "_");

			File uploadFile = s3Util.convert(multipartFile);
			String uploadImageUrl = s3Util.putS3(uploadFile, uniqueFileName);
			s3Util.removeNewFile(uploadFile);

			StoreImage newStoreImage = StoreImage.builder()
				.postImageUrl(uploadImageUrl)
				.store(store)
				.createDate(LocalDateTime.now())
				.build();

			newStoreImages.add(newStoreImage);
		}
		originImages.addAll(newStoreImages);
		storeImageRepository.saveAll(newStoreImages);
	}
}