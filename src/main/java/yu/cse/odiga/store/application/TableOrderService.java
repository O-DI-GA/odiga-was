package yu.cse.odiga.store.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessagingException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.global.util.FCMUtil;
import yu.cse.odiga.history.application.VisitCountService;
import yu.cse.odiga.menu.dao.MenuRepository;
import yu.cse.odiga.menu.domain.Menu;
import yu.cse.odiga.store.dao.StoreRepository;
import yu.cse.odiga.store.dao.StoreTableRepository;
import yu.cse.odiga.store.dao.TableOrderMenuRepository;
import yu.cse.odiga.store.dao.TableOrderRepository;
import yu.cse.odiga.store.domain.Store;
import yu.cse.odiga.store.domain.StoreTable;
import yu.cse.odiga.store.domain.TableOrder;
import yu.cse.odiga.store.domain.TableOrderMenu;
import yu.cse.odiga.store.dto.CallStaffRequestDto;
import yu.cse.odiga.store.dto.PosCallFcmResponse;
import yu.cse.odiga.store.dto.PosOrderFcmResponse;
import yu.cse.odiga.store.dto.TableOrderManageDto;
import yu.cse.odiga.store.dto.TableOrderMenuHistoryDto;
import yu.cse.odiga.store.dto.TableOrderMenuforManage;
import yu.cse.odiga.store.type.PaymentStatus;
import yu.cse.odiga.store.type.TableStatus;

@Service
@RequiredArgsConstructor
public class TableOrderService {
	private final TableOrderRepository tableOrderRepository;
	private final StoreTableRepository storeTableRepository;
	private final TableOrderMenuRepository tableOrderMenuRepository;
	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;
	private final FCMUtil fcmUtil;

	private final VisitCountService visitCountService;

	@Transactional
	public void registerTableOrderList(Long storeId, int storeTableNumber,
		TableOrderManageDto tableOrderManageDto) throws FirebaseMessagingException {

		StoreTable storeTable = storeTableRepository.findByStoreIdAndTableNumber(storeId, storeTableNumber)
			.orElseThrow(
				() -> new BusinessLogicException("존재하지 않는 store table id 입니다.", HttpStatus.BAD_REQUEST.value()));

		List<TableOrderMenuforManage> requestMenus = tableOrderManageDto.getTableOrderMenuforManages();

		if (storeTable.isTableEmpty()) {
			storeTable.changeTableStatusToInUse();
			storeTable.setTableOrderList(new ArrayList<>());
			visitCountService.incrementVisitCount(storeId, LocalDateTime.now().getHour());
		}

		TableOrder currentTableOrder = storeTable.getTableOrderList().stream()
			.filter(order -> order.getPaymentStatus() == PaymentStatus.PENDING)
			.findFirst()
			.orElseGet(() -> {
				TableOrder newTableOrder = TableOrder.builder()
					.paymentStatus(PaymentStatus.PENDING)
					.storeTable(storeTable)
					.tableOrderMenuList(new ArrayList<>())
					.build();
				storeTable.addNewTableOrder(newTableOrder);
				tableOrderRepository.save(newTableOrder);
				return newTableOrder;
			});

		for (TableOrderMenuforManage menuDto : requestMenus) {
			Menu menu = menuRepository.findByCategory_Store_IdAndMenuName(storeId, menuDto.getMenuName())
				.orElseThrow(() -> new BusinessLogicException("존재하지 않는 메뉴입니다: " + menuDto.getMenuName(),
					HttpStatus.BAD_REQUEST.value()));

			TableOrderMenu existingTableOrderMenu = currentTableOrder.getTableOrderMenuList().stream()
				.filter(orderMenu -> orderMenu.getMenu().equals(menu))
				.findFirst()
				.orElse(null);

			if (existingTableOrderMenu != null) {
				existingTableOrderMenu.setMenuCount(existingTableOrderMenu.getMenuCount() + menuDto.getMenuCount());
			} else {
				TableOrderMenu newTableOrderMenu = TableOrderMenu.builder()
					.menu(menu)
					.menuCount(menuDto.getMenuCount())
					.tableOrder(currentTableOrder)
					.build();
				currentTableOrder.getTableOrderMenuList().add(newTableOrderMenu);
				tableOrderMenuRepository.save(newTableOrderMenu);
			}
		}
		Store store = storeRepository.findById(storeId).orElseThrow(
			() -> new BusinessLogicException("존재하지 않는 Store Id 입니다.", HttpStatus.BAD_REQUEST.value()));

//		String storeFcmToken = store.getPosDeviceFcmToken();
//
//		PosOrderFcmResponse posFCMResponse = new PosOrderFcmResponse(storeTableNumber,
//			tableOrderManageDto.getTableOrderMenuforManages());
//
//		fcmUtil.sendMessage(storeFcmToken, posFCMResponse, "order");
		storeTableRepository.save(storeTable);
	}

	public TableOrderMenuHistoryDto getInSueTableOrderListByStoreIdAndTableNumber(Long storeId, int storeTableNumber) {
		StoreTable storeTable = storeTableRepository.findByStoreIdAndTableNumberAndTableStatus(storeId,
				storeTableNumber, TableStatus.INUSE)
			.orElseThrow(() -> new BusinessLogicException("사용중인 테이블이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		TableOrder tableOrder = tableOrderRepository.findByStoreTableIdAndPaymentStatus(storeTable.getId(),
				PaymentStatus.PENDING)
			.orElseThrow(() -> new BusinessLogicException("사용중인 테이블이 아닙니다.", HttpStatus.BAD_REQUEST.value()));

		return TableOrderMenuHistoryDto.from(tableOrder);
	}

	public List<TableOrderMenuHistoryDto> getAllInuseTableOrderList(Long storeId) {
		List<StoreTable> storeTables = storeTableRepository.findByStoreIdAndTableStatus(storeId, TableStatus.INUSE);

		List<TableOrder> tableOrderList = new ArrayList<>();

		for (StoreTable storeTable : storeTables) {
			List<TableOrder> tableOrders = storeTable.getTableOrderList();
			for (TableOrder tableOrder : tableOrders) {
				if (tableOrder.getPaymentStatus().equals(PaymentStatus.PENDING)) {
					tableOrderList.add(tableOrder);
					break;
				}
			}
		}

		return tableOrderList.stream()
			.map(TableOrderMenuHistoryDto::from).toList();
	}

	public TableOrderMenuHistoryDto findByTableOrderHistoryByTableOrderId(Long tableOrderId) {
		TableOrder tableOrder = tableOrderRepository.findById(tableOrderId).orElseThrow();
		return TableOrderMenuHistoryDto.from(tableOrder);
	}

	public void callStaff(Long storeId, CallStaffRequestDto callStaffRequestDto) throws FirebaseMessagingException {
		Store store = storeRepository.findById(storeId).orElseThrow(
			() -> new BusinessLogicException("존재하지 않는 Store Id 입니다.", HttpStatus.BAD_REQUEST.value()));

		String storeFcmToken = store.getPosDeviceFcmToken();

		PosCallFcmResponse posFCMResponse = new PosCallFcmResponse(callStaffRequestDto.getTableNumber(),
			callStaffRequestDto.getCallMessage());

		fcmUtil.sendMessage(storeFcmToken, posFCMResponse, "call");

	}

	@Transactional
	public void cancelTableOrderList(Long storeId, int storeTableNumber,
		TableOrderManageDto tableOrderManageDto) {

		StoreTable storeTable = storeTableRepository.findByStoreIdAndTableNumber(storeId, storeTableNumber)
			.orElseThrow(
				() -> new BusinessLogicException("존재하지 않는 store table id 입니다.", HttpStatus.BAD_REQUEST.value()));

		TableOrder currentTableOrder = storeTable.getTableOrderList().stream()
			.filter(order -> order.getPaymentStatus() == PaymentStatus.PENDING)
			.findFirst()
			.orElseThrow(() -> new BusinessLogicException("현재 대기 중인 주문이 없습니다.", HttpStatus.BAD_REQUEST.value()));

		List<TableOrderMenuforManage> cancelMenus = tableOrderManageDto.getTableOrderMenuforManages();

		for (TableOrderMenuforManage cancelMenuDto : cancelMenus) {
			Menu menu = menuRepository.findByCategory_Store_IdAndMenuName(storeId, cancelMenuDto.getMenuName())
				.orElseThrow(() -> new BusinessLogicException("존재하지 않는 메뉴입니다: " + cancelMenuDto.getMenuName(),
					HttpStatus.BAD_REQUEST.value()));

			TableOrderMenu existingTableOrderMenu = currentTableOrder.getTableOrderMenuList().stream()
				.filter(orderMenu -> orderMenu.getMenu().equals(menu))
				.findFirst()
				.orElseThrow(
					() -> new BusinessLogicException("취소하려는 메뉴가 현재 주문에 포함되어 있지 않습니다: " + cancelMenuDto.getMenuName(),
						HttpStatus.BAD_REQUEST.value()));

			int updatedCount = existingTableOrderMenu.getMenuCount() - cancelMenuDto.getMenuCount();

			if (updatedCount > 0) {
				existingTableOrderMenu.setMenuCount(updatedCount);
			} else {
				currentTableOrder.getTableOrderMenuList().remove(existingTableOrderMenu);
				tableOrderMenuRepository.delete(existingTableOrderMenu);
			}
		}

		storeTableRepository.save(storeTable);
	}
}
