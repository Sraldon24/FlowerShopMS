/* ========================
   FlowerRepository.java
   ======================== */
package com.champsoft.services.inventory.DataLayer.Flowers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowerRepository extends JpaRepository<Flower, Integer> {

    Optional<Flower> findByFlowersIdentifier_FlowerNumber(String flowerNumber);

    Optional<Flower> findByInventoryIdentifier_InventoryIdAndFlowersIdentifier_FlowerNumber(String inventoryId,
                                                                                            String flowerNumber);

    List<Flower> findAllByInventoryIdentifier_InventoryId(String inventoryId);

    List<Flower> findByInventoryIdentifier_InventoryId(String inventoryId);
}
