package com.dms.almacensoft.data.source.local.daotransact;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbtransact.ProductoDiferencial;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ProductoDiferencialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<ProductoDiferencial> entities);

    @Query("DELETE FROM ProductoDiferencial")
    void deleteAll();

    @Query("SELECT * FROM ProductoDiferencial")
    Single<List<ProductoDiferencial>> getAllProductoDiferencial();

    @Query("SELECT COUNT(*) FROM ProductoDiferencial")
    Single<Integer> getProductoDiferencialCount();
}
