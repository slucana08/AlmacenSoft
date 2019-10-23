package com.dms.almacensoft.features.recepciondespacho.lectura;

import com.dms.almacensoft.data.models.recepciondespacho.BodyConsultarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.BodyEliminarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.HijoConsulta;
import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.util.List;

interface LecturaContract {

    interface View extends BaseContractView {

        void showLecturas(List<HeaderConsulta> headerConsultaList);

        void onError(CustomDialog.IButton event, String message, int type);
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<LecturaContract.View>{

        void getLecturas(BodyConsultarLecturas body);

        void deleteLectura(HijoConsulta hijoConsulta);

        void deleteLecturaGroup(List<HijoConsulta> hijos);
    }


}
