package com.dms.almacensoft.features.inventario.lecturas;

import com.dms.almacensoft.data.models.inventario.HijoConsultaInventario;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * {@link HeaderConsultaInventario} actua como contenedor de la las listas que se organizan en {@link HeaderConsultaInventarioAdapter}
 */
public class HeaderConsultaInventario extends ExpandableGroup<HijoConsultaInventario> {

    public HeaderConsultaInventario(String title, List<HijoConsultaInventario> items) {
        super(title, items);
    }
}
