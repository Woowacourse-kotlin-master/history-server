package historywowa.domain.heritage.domain.entity

import historywowa.domain.member.domain.entity.Member
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@Entity
@DynamicUpdate
@Table(name = "heritage")
class Heritage(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("문화재 분석을 요청한 사용자 ID")
    val member: Member,

    @Comment("문화재 이미지 또는 리소스 URL")
    @Column(nullable = false, length = 500)
    val heritageUrl: String,

    @Comment("문화재에 대한 설명 텍스트")
    @Column(nullable = false, columnDefinition = "TEXT")
    val heritageText: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK: 문화재 ID")
    var id: Long? = null

    @Column(nullable = false)
    @CreationTimestamp
    var createdAt: LocalDateTime = LocalDateTime.now()
}
