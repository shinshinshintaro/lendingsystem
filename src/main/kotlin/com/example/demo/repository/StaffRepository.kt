package com.example.demo.repository

import com.example.demo.entity.Staff
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StaffRepository : JpaRepository<Staff, Long> {
    @Query("SELECT s FROM Staff s WHERE s.initial = :initial")
    fun findByRow(@Param("initial") initial: String): List<Staff>
}