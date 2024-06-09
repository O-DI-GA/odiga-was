package yu.cse.odiga.store.type;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderCondition {
    STORE_LIKE_COUNT("LIKE"), STORE_REVIEW_COUNT("REVIEW"), STORE_WAITING_COUNT("WAITING");

    private final String value;

}
