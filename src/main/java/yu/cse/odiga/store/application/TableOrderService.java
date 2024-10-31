package yu.cse.odiga.store.application;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessagingException;

import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.global.util.FCMUtil;
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
import yu.cse.odiga.store.dto.TableOrderMenuHistoryDto;
import yu.cse.odiga.store.dto.TableOrderMenuforRegister;
import yu.cse.odiga.store.dto.TableOrderRegisterDto;
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

	@Transactional
	public void registerTableOrderList(Long storeId, int storeTableNumber,
		TableOrderRegisterDto tableOrderRegisterDto) throws FirebaseMessagingException {

		StoreTable storeTable = storeTableRepository.findByStoreIdAndTableNumber(storeId, storeTableNumber)
			.orElseThrow(
				() -> new BusinessLogicException("존재하지 않는 store table id 입니다.", HttpStatus.BAD_REQUEST.value()));

		List<TableOrderMenuforRegister> requestMenus = tableOrderRegisterDto.getTableOrderMenuforRegisters();

		if (storeTable.isTableEmpty()) {
			storeTable.changeTableStatusToInUse();
			storeTable.setTableOrderList(new ArrayList<>());
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

		for (TableOrderMenuforRegister menuDto : requestMenus) {
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

		String storeFcmToken = store.getPosDeviceFcmToken();

		fcmUtil.sendMessage(storeFcmToken, tableOrderRegisterDto.getTableOrderMenuforRegisters(), "order");
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

		fcmUtil.sendMessage(storeFcmToken, callStaffRequestDto.getNeedName(), "call");

	}
}
