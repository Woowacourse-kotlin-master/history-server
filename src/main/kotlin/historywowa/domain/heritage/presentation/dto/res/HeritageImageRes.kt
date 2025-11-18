package historywowa.domain.heritage.presentation.dto.res

data class HeritageImageRes(
        val heritageUrl: String,
        val heritageText: String
) {
    companion object {
        fun of(heritageUrl: String, heritageText: String): HeritageImageRes {
            return HeritageImageRes(heritageUrl, heritageText)
        }
    }
}
