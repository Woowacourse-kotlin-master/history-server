package historywowa.domain.openai.application.service.impl

import historywowa.domain.openai.application.service.OpenAIVisionService
import historywowa.domain.openai.dto.image.req.OpenAIVisionReq
import historywowa.domain.openai.dto.image.res.OpenAIVisionRes
import historywowa.global.infra.exception.error.ErrorCode
import historywowa.global.infra.exception.error.HistoryException
import historywowa.global.infra.feignclient.openai.OpenAIVisionFeignClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.Base64

@Service
class OpenAIVisionServiceImpl(
    private val openAIVisionFeignClient: OpenAIVisionFeignClient
) : OpenAIVisionService {

    @Value("\${openai.api.key}")
    private lateinit var apiKey: String

    @Value("\${openai.text.model}")
    private lateinit var model: String

    private val log = LoggerFactory.getLogger(javaClass)

    override fun analyzeHeritageImage(imageUrl: String): String {
        return try {
            // 1) 이미지 URL -> Base64 Data URL 변환
            val dataUrl = convertImageUrlToDataUrl(imageUrl)

            // 2) Vision 요청 생성
            val request = buildVisionRequest(dataUrl)

            // 3) OpenAI Vision API 호출
            val response = callVisionAPI(request)

            // 4) 결과 텍스트 추출
            extractContent(response)
        } catch (e: Exception) {
            log.error("문화재 이미지 분석 실패: {}", e.message, e)
            throw HistoryException(ErrorCode.OPENAI_NOT_EXIST)
        }
    }

    /**
     * 이미지 URL을 다운로드해서 Base64 data url 로 변환
     */
    private fun convertImageUrlToDataUrl(imageUrl: String): String {
        return try {
            URL(imageUrl).openStream().use { inputStream ->
                ByteArrayOutputStream().use { baos ->
                    val buffer = ByteArray(8_192)
                    while (true) {
                        val read = inputStream.read(buffer)
                        if (read == -1) break
                        baos.write(buffer, 0, read)
                    }

                    val imageBytes = baos.toByteArray()
                    val base64 = Base64.getEncoder().encodeToString(imageBytes)

                    // 여기 MIME 타입은 필요하면 이미지 헤더 보고 바꾸면 됨 (jpeg, png 등)
                    "data:image/jpeg;base64,$base64"
                }
            }
        } catch (e: Exception) {
            log.error("이미지 다운로드/인코딩 실패: {}", e.message, e)
            throw HistoryException(ErrorCode.OPENAI_NOT_EXIST)
        }
    }

    /**
     * Vision용 요청 DTO 생성
     */
    private fun buildVisionRequest(base64Data: String): OpenAIVisionReq {
        val prompt = buildVisionPrompt()

        val textContent = OpenAIVisionReq.TextContent(
            type = "text",
            text = prompt
        )

        val imageUrl = OpenAIVisionReq.ImageUrl(
            url = base64Data
        )

        val imageContent = OpenAIVisionReq.ImageContent(
            type = "image_url",
            image_url = imageUrl
        )

        val message = OpenAIVisionReq.Message(
            role = "user",
            content = listOf(textContent, imageContent)
        )

        return OpenAIVisionReq(
            model = model,
            messages = listOf(message)
        )
    }

    /**
     * FeignClient를 통해 OpenAI Vision API 호출
     */
    private fun callVisionAPI(request: OpenAIVisionReq): OpenAIVisionRes {
        return openAIVisionFeignClient.generateVision(
            "Bearer $apiKey",
            request
        )
    }

    /**
     * 응답에서 결과 텍스트(content) 추출
     */
    private fun extractContent(response: OpenAIVisionRes): String {
        // Kotlin data class는 프로퍼티로 접근 (choices(), message() 같은 함수 호출 X)
        val choices = response.choices

        if (choices.isEmpty()) {
            throw HistoryException(ErrorCode.OPENAI_NOT_EXIST)
        }

        val firstChoice = choices[0]
        val message = firstChoice.message
        val content = message.content

        if (content.isBlank()) {
            throw HistoryException(ErrorCode.OPENAI_NOT_EXIST)
        }

        return content
    }

    /**
     * Vision 모델에 넘길 프롬프트
     */
    private fun buildVisionPrompt(): String =
        """
        너는 한국 문화재 전문가이자 시각 분석 전문가야.
        내가 제공하는 이미지를 정밀하게 분석해서 아래 기준에 따라 한국어로 답변해줘.

        [주의]
        - 이미지를 분석했을 때 한국 문화재로 보이지 않으면 "이 이미지는 한국 문화재로 추정되지 않습니다."라고 명확하게 말해줘.
        - 사물, 음식, 인물, 동물, 풍경 등 문화재와 무관한 사진이면 절대로 문화재라고 추정하지 마.
        - 비슷한 외형이 있어도 근거가 없으면 문화재로 단정하지 말고 "확신할 수 없음"과 이유를 반드시 설명해.

        [분석 기준]
        1) 어떤 문화재인지 추정할 수 있는 고유 특징 설명
        2) 형태, 재질, 구조, 색상 등 시각적 단서 분석
        3) 한국 문화재 명칭(정확한 공식 명칭 추정)
        4) 해당 문화재가 속하는 시대/지역/종류(사찰/탑/부도/불상 등) 분석
        5) 추가로 추정 가능한 역사적 맥락 또는 의미 설명

        [출력 형식]
        - 자연어 문장으로 자세하게 설명
        - 불필요한 말투·중복 제거
        - "확실하지 않은 경우"는 그 이유를 함께 설명
        - 문화재가 아닐 경우에도 정중하게 이유와 함께 설명

        이미지를 기반으로 문화재를 최대한 정확하고 신중하게 판단해줘.
        """.trimIndent()
}
