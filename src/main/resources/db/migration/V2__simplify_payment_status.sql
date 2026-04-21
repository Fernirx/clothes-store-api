ALTER TABLE `payments`
  MODIFY COLUMN `status` ENUM('PENDING','SUCCESS','FAILED') NOT NULL DEFAULT 'PENDING'
    COMMENT 'Trạng thái giao dịch VNPay:
    - PENDING = Đã tạo link, đang chờ user thanh toán (chưa có callback)
    - SUCCESS = Thanh toán thành công (callback success từ VNPay)
    - FAILED  = Thanh toán thất bại (user hủy hoặc callback lỗi)';
