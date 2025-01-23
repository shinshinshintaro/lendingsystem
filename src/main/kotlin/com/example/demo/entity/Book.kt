package com.example.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "book")
class Book(
    @Id
    @Column(name = "book_no", nullable = false)
    var bookNo: String, // 文字列型に変更

    @Column(nullable = true)
    var title: String,

    @Column(name = "description")
    var description: String? = null,
)
