package yu.cse.odiga.global.util;

import org.springframework.stereotype.Component;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FCMUtil {
	private final FirebaseMessaging firebaseMessaging;

	public void sendMessage(String token, Object data, String type) throws FirebaseMessagingException {

		Gson gson = new Gson();

		String jsonData = gson.toJson(data);

		Message message = Message.builder()
			.setToken(token)
			.putData("type", type)
			.putData("data", jsonData)
			.build();

		firebaseMessaging.send(message);
	}
}
