package yu.cse.odiga.store.dto;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties({"location"})
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponseDto {
	private Long storeId;
	private String storeName;
	private String address;
	private Point location;
	private String phoneNumber;
	private int reviewCount;
	private String storeCategory;
}
