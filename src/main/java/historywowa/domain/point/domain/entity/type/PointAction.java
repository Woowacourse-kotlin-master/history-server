package historywowa.domain.point.domain.entity.type;

import historywowa.global.infra.exception.error.HistoryException;
import historywowa.global.infra.exception.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PointAction {
    EARN("적립"),
    USE("차감");

    private final String key;

    PointAction(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    // Enum 매핑용 메서드
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PointAction from(String key) {
        return Arrays.stream(PointAction.values())
                .filter(r -> r.getKey().equalsIgnoreCase(key)) // 대소문자 구분 안함
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("이상한 타입이네요.: " + key));
    }

    public static PointAction getByValue(String value) {
        for (PointAction role : PointAction.values()) {
            if (role.key.equals(value)) {
                return role;
            }
        }
        throw new HistoryException(ErrorCode.INVALID_ROLE);
    }
}
