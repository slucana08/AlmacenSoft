package com.dms.almacensoft.features.inventario.diferencial;

import com.dms.almacensoft.data.entities.dbtransact.ProductoDiferencial;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * {@link HeaderDiferencial} actua como contenedor de la las listas que se organizan en {@link HeaderDiferencialAdapter}
 */

public class HeaderDiferencial extends ExpandableGroup<ProductoDiferencial> {

    public HeaderDiferencial(String title, List<ProductoDiferencial> items) {
        super(title, items);
    }
}
