package todo.oauth.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class BasicAuthInterceptor implements ClientHttpRequestInterceptor {

	private static final Logger log = LoggerFactory.getLogger(BasicAuthInterceptor.class);

	@Value("${api.auth.clientId}")
	String clientId;

	@Value("${api.auth.password}")
	String password;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		String plainCredentials = clientId + ":" + password;
		String base64Credentials = Base64.getEncoder()
				.encodeToString(plainCredentials.getBytes(StandardCharsets.UTF_8));
		// interceptメソッド内で、Basic認証のリクエストヘッダを追加
		request.getHeaders().add("Authorization", "Basic " + base64Credentials);

		ClientHttpResponse response = execution.execute(request, body);

		return response;
	}

}
