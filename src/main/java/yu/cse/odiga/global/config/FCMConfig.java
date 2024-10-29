package yu.cse.odiga.global.config;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FCMConfig {

	@Bean
	public FirebaseMessaging firebaseMessaging() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("fcm/fcmKey.json");

		FirebaseApp firebaseApp = null;

		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(classPathResource.getInputStream()))
			.build();

		List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

		if (firebaseAppList.isEmpty()) {
			firebaseApp = FirebaseApp.initializeApp(options);
		} else {
			for (FirebaseApp app : firebaseAppList) {
				if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
					firebaseApp = app;
				}
			}
		}

		return FirebaseMessaging.getInstance(firebaseApp);
	}
}
