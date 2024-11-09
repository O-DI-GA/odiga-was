package yu.cse.odiga.store.domain;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yu.cse.odiga.menu.domain.Category;
import yu.cse.odiga.owner.domain.Owner;
import yu.cse.odiga.reservation.domain.AvailableReservationTime;
import yu.cse.odiga.review.domain.Review;
import yu.cse.odiga.waiting.domain.Waiting;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String storeName;

	@Column
	private String phoneNumber;

	@Column
	private String address;

	@Column(columnDefinition = "geometry(Point, 4326)")
	@JsonIgnore
	private Point location;

	@Column
	private int tableCount;

	@Builder.Default
	private int reviewCount = 0;

	@Builder.Default
	private int likeCount = 0;

	@Column
	private String storeCategory;

	@Column
	@Setter
	private String posDeviceFcmToken;

	@ManyToOne
	@JoinColumn
	@JsonIgnore
	private Owner owner;

	@Column
	private String storeTitleImage;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StoreImage> storeImages = new ArrayList<>();

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LikeStore> likeStores = new ArrayList<>();

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Category> categories = new ArrayList<>();

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StoreTable> tables = new ArrayList<>();

	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
	private List<Waiting> waitingList = new ArrayList<>();

	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
	private List<Review> reviewList = new ArrayList<>();

	@OneToMany(mappedBy = "availableReservationTime", fetch = FetchType.LAZY)
	private List<AvailableReservationTime> availableReservationTimeList = new ArrayList<>();

	public boolean isNotStoreOwner(Long ownerId) { // 메소드 명 바꿀필요가 있어보임
		return !this.getOwner().getId().equals(ownerId);
	}

	public void increaseReviewCount() {
		setReviewCount(this.getReviewCount() + 1);
	}
}
