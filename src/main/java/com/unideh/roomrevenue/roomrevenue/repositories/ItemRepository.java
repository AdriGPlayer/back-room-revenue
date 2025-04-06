package com.unideh.roomrevenue.roomrevenue.repositories;

import com.unideh.roomrevenue.roomrevenue.models.Item;
import com.unideh.roomrevenue.roomrevenue.models.ItemModel;
import com.unideh.roomrevenue.roomrevenue.models.TipoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<ItemModel, Long> {
    List<ItemModel> findByTipoItem(TipoItem tipoItem);
    Optional<ItemModel> findByItem(Item item);
}
