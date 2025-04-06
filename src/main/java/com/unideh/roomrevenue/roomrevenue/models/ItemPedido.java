package com.unideh.roomrevenue.roomrevenue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido implements Serializable {
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "cantidad")
    private int cantidad;

}
