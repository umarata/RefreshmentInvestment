package com.tribyssapps.refreshmentinvestment.database;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

@Dao
public interface InvestmentDAO {
    @Query("SELECT balance FROM investment order by id desc")
    double getCurrentBalance();

    @Insert
    Long insertInvestment(InvestmentEntity investmentEntity);

    @Query("SELECT * from investment ")
    List<InvestmentEntity> getAllInvestments();

   // @Query("Select * from investment where date between :startDate And :endDate")
  //  List<InvestmentEntity> getInvestmentFromDateRange(Date startDate, Date endDate);



    @Query("SELECT * FROM investment WHERE date BETWEEN :from AND :to")
    @TypeConverters({Converters.class})
    List<InvestmentEntity> getInvestmentFromDateRange(Date from, Date to);


    @Query("SELECT SUM(cr) as cr, SUM(dr) as dr ,id,balance,date,name from investment WHERE date BETWEEN :from AND :to")
    @TypeConverters({Converters.class})
    InvestmentEntity getBalanceFromRange(Date from,Date to);


    @Query("SELECT SUM(cr) as cr from investment WHERE date BETWEEN :from AND :to")
    @TypeConverters({Converters.class})
    double getCreditFromRange(Date from,Date to);

    @Query("SELECT SUM(dr) as dr from investment WHERE date BETWEEN :from AND :to")
    @TypeConverters({Converters.class})
    double getDebitFromRange(Date from,Date to);
}
