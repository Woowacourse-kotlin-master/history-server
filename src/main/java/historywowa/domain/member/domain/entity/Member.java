package historywowa.domain.member.domain.entity;

import historywowa.domain.heritage.domain.entity.Heritage;
import historywowa.domain.oauth2.domain.entity.SocialProvider;
import historywowa.domain.point.domain.entity.Point;
import historywowa.domain.point.domain.entity.PointHistory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name = "member")
public class Member {
    @Id
    @Column(unique = true, nullable = false)
    private String id;

    // 1) 문화재 이미지 리스트 조회
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heritage> heritages = new ArrayList<>();

    // 2) 포인트
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Point point;

    // 3) 포인트 내역 조회
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointHistory> pointHistories = new ArrayList<>();


    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_provider", nullable = false)
    private SocialProvider socialProvider;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    // 관리자 비번 null이어도됨
    private String password;


    // 첫 로그인일시에는 false 이후에는 계속 true
    @Column(name = "name_flag", nullable = false)
    private boolean nameFlag;

    @Builder
    public Member(String id, String email, String name, String profile,
                  SocialProvider socialProvider, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.socialProvider = socialProvider;
        this.role = role;
        this.nameFlag = false;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateProfile(String profile) {
        this.profile = profile;
    }

    public void updateIsActivateNameFlag() {
        this.nameFlag = true;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(); // 한국시간
        }

    }
}