package com.unideh.roomrevenue.roomrevenue.models;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "items")
@Data
public class ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double precio;

    @Enumerated(EnumType.STRING)
    private TipoItem tipoItem;

    @Enumerated(EnumType.STRING)
    private Item item;


}
