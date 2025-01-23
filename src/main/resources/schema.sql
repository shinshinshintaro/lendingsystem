-- 本テーブル
CREATE TABLE IF NOT EXISTS book (
    bookNo VARCHAR(50) NOT NULL PRIMARY KEY, -- 主キーを文字列型に変更
    title VARCHAR(255) NOT NULL,
    description TEXT
);

-- スタッフテーブル
CREATE TABLE IF NOT EXISTS staff (
    staffNo VARCHAR(50) NOT NULL PRIMARY KEY, -- 主キーを文字列型に変更
    name VARCHAR(50) NULL,
    initial VARCHAR(50) NULL
);

-- 貸出テーブル
CREATE TABLE IF NOT EXISTS lend (
    lendId BIGINT AUTO_INCREMENT PRIMARY KEY, -- 貸出テーブルの主キー
    bookNo VARCHAR(50) NOT NULL,              -- 外部キーを文字列型に変更
    staffNo VARCHAR(50) NOT NULL,             -- 外部キーを文字列型に変更
    lendTime DATE NOT NULL,
    status BOOLEAN DEFAULT FALSE,
    returnTime DATE DEFAULT NULL,

    -- 外部キー制約
    CONSTRAINT fk_book FOREIGN KEY (bookNo) REFERENCES book(bookNo)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_staff FOREIGN KEY (staffNo) REFERENCES staff(staffNo)
        ON DELETE CASCADE ON UPDATE CASCADE
);
