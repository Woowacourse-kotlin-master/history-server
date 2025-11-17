package historywowa.global.infra.feignclient.ios;

import feign.Logger;
import feign.Request;
import feign.codec.ErrorDecoder;
import historywowa.global.infra.exception.error.ErrorCode;
import historywowa.global.infra.exception.error.HistoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AppleOAuth2FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                10000,  // 연결 타임아웃 (밀리초)
                30000   // 읽기 타임아웃 (밀리초)
        );
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new AppleOAuth2ErrorDecoder();
    }

    @Slf4j
    public static class AppleOAuth2ErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();

        @Override
        public Exception decode(String methodKey, feign.Response response) {
            log.error("Apple OAuth2 API 에러 - Method: {}, Status: {}, Reason: {}",
                    methodKey, response.status(), response.reason());

            if (response.status() == 400) {
                throw new HistoryException(ErrorCode.APPLE_JWT_ERROR);
            } else if (response.status() == 401) {
                throw new HistoryException(ErrorCode.APPLE_JWT_ERROR);
            } else if (response.status() >= 500) {
                throw new HistoryException(ErrorCode.APPLE_JWT_ERROR);
            }

            return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}