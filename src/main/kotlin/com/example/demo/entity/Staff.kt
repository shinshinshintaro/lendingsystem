package com.example.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "staff")
class Staff(
    @Id
    @Column(name = "staff_no")
    var staffNo: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var initial: String
)