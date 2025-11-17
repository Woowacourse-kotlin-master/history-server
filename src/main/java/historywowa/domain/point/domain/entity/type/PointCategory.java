package historywowa.domain.point.domain.entity.type;

import historywowa.global.infra.exception.error.HistoryException;
import historywowa.global.infra.exception.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PointCategory {
    HERITAGE("문화재 이미지"),
    EVENT("이벤트 응모");

    private final String key;

    PointCategory(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    // Enum 매핑용 메서드
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PointCategory from(String key) {
        return Arrays.stream(PointCategory.values())
                .filter(r -> r.getKey().equalsIgnoreCase(key)) // 대소문자 구분 안함
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("이상한 타입이네요.: " + key));
    }

    public static PointCategory getByValue(String value) {
        for (PointCategory role : PointCategory.values()) {
            if (role.key.equals(value)) {
                return role;
            }
        }
        throw new HistoryException(ErrorCode.INVALID_ROLE);
    }
}
