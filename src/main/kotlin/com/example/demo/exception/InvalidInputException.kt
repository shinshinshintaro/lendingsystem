package com.example.demo.exception

// RuntimeExceptionを継承したカスタム例外クラス
class InvalidInputException(message: String) : RuntimeException(message)
