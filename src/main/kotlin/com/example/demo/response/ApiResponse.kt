package com.example.demo.response

// APIレスポンス用のデータクラス
data class ApiResponse<T>(
    val status: String,    // レスポンスのステータス（例: "success", "error" など）
    val message: String,   // レスポンスメッセージ（例: エラー内容や成功通知）
    val data: Any? = null  // 任意のデータ（オプション）
)
