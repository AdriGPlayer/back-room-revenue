package com.unideh.roomrevenue.roomrevenue.controllers;


import com.unideh.roomrevenue.roomrevenue.models.Item;
import com.unideh.roomrevenue.roomrevenue.models.ItemModel;
import com.unideh.roomrevenue.roomrevenue.models.TipoItem;
import com.unideh.roomrevenue.roomrevenue.repositories.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "http://localhost:3000")

public class ItemController {

    @Autowired
    private ItemRepository repository;

    @PostMapping("/save")
    public ResponseEntity<ItemModel> saveItem(@RequestBody ItemModel itemRequest){
        ItemModel item = itemRequest;
        repository.save(item);
        return ResponseEntity.ok(item);
    }

    // Método para guardar varios ítems
    @PostMapping("/saveAll")
    public ResponseEntity<List<ItemModel>> saveItems(@RequestBody List<ItemModel> itemsRequest){
        List<ItemModel> items = repository.saveAll(itemsRequest);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/findByTipoItem/{tipoItem}")
    public ResponseEntity<List<ItemModel>> getItemsByTipo(@PathVariable TipoItem tipoItem){
        List<ItemModel> items = repository.findByTipoItem(tipoItem);
        if(items.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(items);
    }
    @GetMapping("/getPrecioItem/{id}")
    public ResponseEntity<Double> getPriceItem(@PathVariable Long id) {
        Optional<ItemModel> itemModel = repository.findById(id);
        if (itemModel.isPresent()) {
            return ResponseEntity.ok(itemModel.get().getPrecio());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/findByItem/{item}")
    public ResponseEntity<ItemModel> getItemByName(@PathVariable Item item) {
        Optional<ItemModel> itemModel = repository.findByItem(item);
        if (itemModel.isPresent()) {
            return ResponseEntity.ok(itemModel.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeItem(@PathVariable Long id){
        Optional<ItemModel> item = repository.findById(id);
        if(item.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok("Item eliminado correctamente.");
        }
        else
            return ResponseEntity.notFound().build();

    }
}
