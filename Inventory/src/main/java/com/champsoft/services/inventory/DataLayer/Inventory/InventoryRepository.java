/* ============================
   InventoryRepository.java
   ============================ */
package com.champsoft.services.inventory.DataLayer.Inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Optional<Inventory> findByInventoryIdentifier_InventoryId(String inventoryId);
}
