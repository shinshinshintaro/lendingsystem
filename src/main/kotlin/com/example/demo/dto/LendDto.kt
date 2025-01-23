package com.example.demo.dto

import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class LendDto(
    var bookNo: Long? = null,  // 引数なしで初期化できるようにする
    var staffNo: Long? = null,
    var status: Boolean? = null,
    var lendTime: OffsetDateTime? = null
)