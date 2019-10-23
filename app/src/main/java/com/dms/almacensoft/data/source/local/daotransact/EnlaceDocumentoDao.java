package com.dms.almacensoft.data.source.local.daotransact;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

@Dao
public interface EnlaceDocumentoDao {

    @Query("DELETE FROM EnlaceDocumento")
    void deleteAll();

}
