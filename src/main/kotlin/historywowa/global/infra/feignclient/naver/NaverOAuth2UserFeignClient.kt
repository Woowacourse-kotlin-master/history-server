package historywowa.global.infra.feignclient.naver

import historywowa.domain.oauth2.presentation.dto.res.naver.NaverUserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "NaverOAuth2UserInfo",
    url = "https://openapi.naver.com"
)
interface NaverOAuth2UserFeignClient {

    @GetMapping("/v1/nid/me")
    fun getUserInfo(
        @RequestHeader("Authorization") accessToken: String
    ): NaverUserResponse
}
