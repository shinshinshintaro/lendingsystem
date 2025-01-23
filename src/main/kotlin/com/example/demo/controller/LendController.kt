package com.example.demo.controller

import com.example.demo.dto.LendDto
import com.example.demo.entity.Lending
import com.example.demo.exception.InvalidInputException
import com.example.demo.repository.BookRepository
import com.example.demo.repository.LendRepository
import com.example.demo.repository.StaffRepository
import com.example.demo.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.time.OffsetDateTime

@Controller
class LendController(
    val lendRepository: LendRepository,
    val bookRepository: BookRepository,
    val staffRepository: StaffRepository,
    var lendDto: LendDto
) {

    // ホーム画面0
    @GetMapping("/")
    fun getLends(): ModelAndView {
        val lends = lendRepository.findAll()
        return ModelAndView("lends").apply {
            addObject("lends", lends)
        }
    }

    // 本の番号入力画面1
    @GetMapping("/book")
    fun getLendBook(): ModelAndView {
        return ModelAndView("lend-bookNo-Input")
    }

    // 貸出中チェック画面3
    @GetMapping("/firstName")
    fun getLendFirstName(): ModelAndView {
        if (lendDto.bookNo == null) {
            return ModelAndView("error").apply {
                addObject("message", "Book number is required.")
            }
        }

        val activeLends = lendRepository.findActiveLendByBookNo(lendDto.bookNo!!.toLong())
        return if (activeLends.isNotEmpty()) {
            ModelAndView("lend-failure") // 貸出中の場合
        } else {
            ModelAndView("lend-family-name-select") // 正常
        }
    }

    // 本の詳細取得2
    @GetMapping("/book/search")
    fun getBookByBookNo(@RequestParam("bookNo") bookNo: Long): ModelAndView {
        val book = bookRepository.findById(bookNo).orElseThrow {
            InvalidInputException("Book not found for ID: $bookNo")
        }
        lendDto.bookNo = book.bookNo.toLong()

        // "lend-book-search"ビューにbookオブジェクトを渡す
        return ModelAndView("lend-book-search").apply {
            addObject("book", book)
        }
    }

    // 貸出詳細取得
    @GetMapping("/{id}")
    fun getLendById(@PathVariable("id") id: Long): ModelAndView {
        val lend = lendRepository.findById(id).orElse(null)
        return if (lend != null) {
            ModelAndView("lend-detail").apply {
                addObject("lend", lend)
            }
        } else {
            ModelAndView("error").apply {
                addObject("message", "Lend not found for ID: $id")
            }
        }
    }

    // 職員検索4
    @PostMapping("/staff/search")
    fun searchStaffByRow(@RequestParam initial: String): ModelAndView {
        val staffList = staffRepository.findByRow(initial)
        return ModelAndView("lend-staff-list").apply {
            addObject("staff", staffList)
        }
    }

    // 職員選択 & 貸出確認5
    @PostMapping("/staff/select")
    fun selectStaff(@RequestParam staffNo: Long): ModelAndView {
        val staff = staffRepository.findById(staffNo).orElseThrow {
            InvalidInputException("Staff not found for ID: $staffNo")
        }
        lendDto.staffNo = staff.staffNo.toLong()

        val book = lendDto.bookNo?.let {
            bookRepository.findById(it.toLong()).orElseThrow {
                InvalidInputException("Book not found for ID: ${lendDto.bookNo}")
            }
        }

        val lend = book?.let {
            Lending(
                id = null,
                bookNo = it,
                staffNo = staff,
                lendTime = OffsetDateTime.now(),
                status = true,
                returnTime = null
            )
        }

        return ModelAndView("lend-confirm").apply {
            addObject("lend", lend)
        }
    }

    // 貸出確定6
    @GetMapping("/confirm")
    fun confirmLend():  ModelAndView {
        val book = lendDto.bookNo?.let {
            bookRepository.findById(it).orElseThrow {
                InvalidInputException("Book not found for ID: ${lendDto.bookNo}")
            }
        }
        val staff = lendDto.staffNo?.let {
            staffRepository.findById(it).orElseThrow {
                InvalidInputException("Staff not found for ID: ${lendDto.staffNo}")
            }
        }

        val lending = book?.let {
            staff?.let { it1 ->
                Lending(
                    id = null,
                    bookNo = it,
                    staffNo = it1,
                    lendTime = OffsetDateTime.now(),
                    status = true,
                    returnTime = null
                )
            }
        }
        lending?.let { lendRepository.save(it) }
        return getLends()
    }

    // 貸出更新
    @PutMapping("/{id}")
    fun updateLendById(
        @PathVariable("id") id: Long,
        @RequestBody lendDto: LendDto
    ): ResponseEntity<ApiResponse<Lending>> {
        val updatedLend = lendRepository.findById(id).map { existingLend ->
            val book = lendDto.bookNo?.let {
                bookRepository.findById(it).orElseThrow {
                    InvalidInputException("Invalid bookNo: $it")
                }
            }
            val staff = lendDto.staffNo?.let {
                staffRepository.findById(it).orElseThrow {
                    InvalidInputException("Invalid staffNo: $it")
                }
            }
            existingLend.apply {
                this.bookNo = book ?: this.bookNo
                this.staffNo = staff ?: this.staffNo
                this.status = lendDto.status ?: this.status
                this.preUpdate()
            }
        }.orElseThrow {
            InvalidInputException("Lend not found for ID: $id")
        }
        return ResponseEntity.ok(
            ApiResponse(
                data = lendRepository.save(updatedLend),
                message = "Lend successfully updated.",
                status = HttpStatus.OK.toString()
            )
        )
    }

    // 貸出削除
    @DeleteMapping("/{id}")
    fun deleteLendById(@PathVariable("id") id: Long): ResponseEntity<ApiResponse<Unit>> {
        lendRepository.findById(id).ifPresentOrElse(
            { lendRepository.delete(it) },
            { throw InvalidInputException("Lend not found for ID: $id") }
        )
        return ResponseEntity.ok(
            ApiResponse(
                data = null,
                message = "Lend successfully deleted.",
                status = HttpStatus.OK.toString()
            )
        )
    }
}
