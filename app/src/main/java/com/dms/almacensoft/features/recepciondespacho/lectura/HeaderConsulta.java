package com.dms.almacensoft.features.recepciondespacho.lectura;

import com.dms.almacensoft.data.models.recepciondespacho.HijoConsulta;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * {@link HeaderConsulta} actua como contenedor de la las listas que se organizan en {@link HeaderConsultaAdapter}
 */
public class HeaderConsulta extends ExpandableGroup<HijoConsulta> {

    public HeaderConsulta(String title, List<HijoConsulta> items) {
        super(title, items);
    }

}
