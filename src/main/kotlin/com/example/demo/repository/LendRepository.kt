package com.example.demo.repository

import com.example.demo.entity.Lending
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface LendRepository : JpaRepository<Lending, Long> {

    @Query("SELECT l FROM Lending l WHERE l.bookNo.bookNo = :bookNo AND l.status = true")
    fun findActiveLendByBookNo(@Param("bookNo") bookNo: Long): List<Lending>
}

