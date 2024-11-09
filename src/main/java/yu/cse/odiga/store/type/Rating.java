package yu.cse.odiga.store.type;

import lombok.Getter;

@Getter
public enum Rating {
	ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

	private final int value;

	Rating(int value) {
		this.value = value;
	}

}
