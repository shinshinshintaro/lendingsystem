package com.example.demo.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.OffsetDateTime
import jakarta.persistence.*

@Entity
@Table(name = "lend")
class Lending(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lend_id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) // `Book`エンティティとの関連を定義
    @JoinColumn(name = "book_no", referencedColumnName = "book_no", nullable = false) // 外部キー制約を設定
    var  bookNo: Book,

    @ManyToOne(fetch = FetchType.LAZY) // `Book`エンティティとの関連を定義
    @JoinColumn(name = "staff_no", referencedColumnName = "staff_no", nullable = false) // 外部キー制約を設定
    var  staffNo: Staff,

    @Column(name = "lend_time")
    @CreatedDate    // <- 追記
    var lendTime: OffsetDateTime?, // <- ?を付ける

    @Column(name = "status")
    var status: Boolean = false,

    @Column(name = "return_time")
    @LastModifiedDate   // <- 追記
    var returnTime: OffsetDateTime? = null// <- ?を付ける
) {
    // 以下を追記

    @PrePersist
    fun prePersist() {
        lendTime = OffsetDateTime.now()
        //returnTime = OffsetDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        returnTime = OffsetDateTime.now()
    }
}