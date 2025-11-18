package historywowa.domain.heritage.domain.entity;

import historywowa.domain.member.domain.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name = "heritage")
public class Heritage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK: 문화재 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("문화재 분석을 요청한 사용자 ID")
    private Member member;

    @Comment("문화재 이미지 또는 리소스 URL")
    @Column(nullable = false, length = 500)
    private String heritageUrl;

    @Comment("문화재에 대한 설명 텍스트")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String heritageText;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    private Heritage(Member member, String heritageUrl, String heritageText) {
        this.member = member;
        this.heritageUrl = heritageUrl;
        this.heritageText = heritageText;
    }


}
