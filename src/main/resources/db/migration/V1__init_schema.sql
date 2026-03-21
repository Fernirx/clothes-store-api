-- Model: Clothes E-commerce Database    Version: 1.0
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


-- =====================================================
-- CORE & AUTHENTICATION TABLES
-- =====================================================

-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của người dùng',
  -- Thông tin xác thực
  `email` VARCHAR(100) NOT NULL COMMENT 'Email đăng nhập, duy nhất trong hệ thống',
  `password_hash` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Mật khẩu đã hash (NULL nếu đăng nhập qua Google OAuth2)',
  `provider` ENUM('LOCAL', 'GOOGLE') NOT NULL DEFAULT 'LOCAL' COMMENT 'Phương thức đăng nhập của tài khoản:
    - "LOCAL" = Đăng nhập bằng tài khoản và mật khẩu truyền thống
    - "GOOGLE" = Đăng nhập qua Google bằng giao thức OAuth2',
  `provider_id` VARCHAR(255) NULL DEFAULT NULL COMMENT 'ID từ nhà cung cấp OAuth (Google sub claim), NULL nếu LOCAL',
  -- Phân quyền và trạng thái
  `role` ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER' COMMENT 'Phân quyền của tài khoản trong hệ thống:
    - "USER" = Khách hàng, có thể xem sản phẩm, mua hàng và quản lý đơn hàng của mình
    - "ADMIN" = Quản trị viên hệ thống, có toàn quyền quản lý người dùng, dữ liệu và cấu hình hệ thống',
  `is_verified` BOOLEAN NOT NULL DEFAULT 0 COMMENT 'Trạng thái xác thực email: 0=chưa xác thực, 1=đã xác thực',
  `is_active` BOOLEAN NOT NULL DEFAULT 1 COMMENT 'Trạng thái tài khoản: 1=hoạt động, 0=bị khóa',
  -- Timestamp
  `last_login` DATETIME NULL DEFAULT NULL COMMENT 'Thời điểm đăng nhập gần nhất',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo tài khoản',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật gần nhất',
  -- Primary Key và UNIQUE
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_users_provider` (`provider` ASC, `provider_id` ASC) VISIBLE,
  INDEX `idx_users_role_active` (`role` ASC, `is_active` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_520_ci
COMMENT = 'Bảng quản lý tài khoản người dùng - chứa thông tin xác thực và phân quyền';


-- -----------------------------------------------------
-- Table `user_profiles`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `user_profiles` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của profile',
  -- Foreign Keys
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT 'Liên kết với bảng users (1-1 relationship)',
  -- Thông tin cá nhân
  `first_name` VARCHAR(100) NOT NULL COMMENT 'Tên người dùng (VD: An)',
  `last_name` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Họ và tên đệm (VD: Nguyễn Văn)',
  `avatar_url`       VARCHAR(500) NULL DEFAULT NULL COMMENT 'URL ảnh đại diện',
  `avatar_public_id` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Cloudinary public_id để xóa/cập nhật avatar',
  `date_of_birth` DATE NULL DEFAULT NULL COMMENT 'Ngày sinh',
  -- Địa chỉ giao hàng (chỉ USER dùng, ADMIN để NULL)
  `shipping_name` VARCHAR(200) NULL DEFAULT NULL COMMENT 'Tên người nhận',
  `shipping_phone` VARCHAR(15) NULL DEFAULT NULL COMMENT 'SĐT người nhận',
  `shipping_street` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Số nhà, tên đường',
  `shipping_ward` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Phường/Xã',
  `shipping_district` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Quận/Huyện',
  `shipping_province` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Tỉnh/Thành phố',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo profile',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật gần nhất',
  -- Primary Key
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_profiles_name` (`last_name` ASC, `first_name` ASC) VISIBLE,
  INDEX `idx_profiles_shipping_phone` (`shipping_phone` ASC) VISIBLE,
  -- Foreign Key
  CONSTRAINT `fk_user_profiles_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Thông tin cá nhân và địa chỉ giao hàng của người dùng';


-- -----------------------------------------------------
-- Table `guest_sessions`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `guest_sessions` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của phiên khách',
  -- Thông tin session
  `guest_token` VARCHAR(64) NOT NULL COMMENT 'Token duy nhất để nhận diện khách chưa đăng nhập, liên kết với giỏ hàng',
  -- Thời gian
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo phiên guest',
  `last_active` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm hoạt động cuối',
  `expires_at` DATETIME NOT NULL COMMENT 'Thời điểm hết hạn (thường sau 30 ngày không hoạt động, sau đó xóa session và giỏ hàng)',
  -- Primary Key và UNIQUE
  PRIMARY KEY (`id`),
  UNIQUE INDEX `guest_token_UNIQUE` (`guest_token` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_guest_sessions_expires` (`expires_at` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_520_ci
COMMENT = 'Phiên làm việc của khách chưa đăng ký - cho phép mua hàng không cần tài khoản';


-- -----------------------------------------------------
-- Table `token_blacklist`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `token_blacklist` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của bản ghi blacklist',
  -- Foreign Keys
  `user_id` BIGINT UNSIGNED NULL DEFAULT NULL COMMENT 'ID người dùng sở hữu token (NULL nếu không xác định được)',
  -- Thông tin token
  `token` VARCHAR(512) NOT NULL COMMENT 'JWT token bị vô hiệu hóa',
  `reason` ENUM('LOGOUT', 'REVOKED') NOT NULL DEFAULT 'LOGOUT' COMMENT 'Lý do token bị đưa vào danh sách blacklist:
    - "LOGOUT" = Người dùng chủ động đăng xuất khỏi hệ thống
    - "REVOKED" = Quản trị viên hoặc hệ thống thu hồi quyền truy cập (token bị vô hiệu)',
  `expires_at` DATETIME NOT NULL COMMENT 'Thời điểm token hết hạn (để tự động dọn dẹp)',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm thêm vào blacklist',
  -- Primary Key và UNIQUE
  PRIMARY KEY (`id`),
  -- Indexes
  INDEX `idx_blacklist_user` (`user_id` ASC) VISIBLE,
  INDEX `idx_blacklist_token` (`token`(255) ASC) VISIBLE,
  -- Foreign Key Constraints
  CONSTRAINT `fk_token_blacklist_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_520_ci
COMMENT = 'Danh sách JWT token bị vô hiệu hóa - ngăn sử dụng lại token sau logout';


-- =====================================================
-- PRODUCT CATALOG TABLES
-- =====================================================

-- -----------------------------------------------------
-- Table `categories`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `categories` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của danh mục',
  -- Foreign Keys
  `parent_id` BIGINT UNSIGNED NULL DEFAULT NULL COMMENT 'ID danh mục cha (NULL=danh mục gốc, có giá trị=danh mục con)',
  -- Thông tin danh mục
  `name` VARCHAR(100) NOT NULL COMMENT 'Tên danh mục (VD: Áo, Quần, Váy)',
  `slug` VARCHAR(100) NOT NULL COMMENT 'URL-friendly name (VD: ao-thun-nam)',
  `description` TEXT NULL DEFAULT NULL COMMENT 'Mô tả chi tiết về danh mục',
  -- Cấu hình hiển thị
  `display_order` INT NOT NULL DEFAULT 0 COMMENT 'Thứ tự hiển thị (số nhỏ hơn hiển thị trước)',
  `is_active` BOOLEAN NOT NULL DEFAULT 1 COMMENT 'Trạng thái: 1=hiển thị, 0=ẩn',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo danh mục',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật gần nhất',
  -- Primary Key và UNIQUE
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `slug_UNIQUE` (`slug` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_categories_parent_active` (`parent_id` ASC, `is_active` ASC) VISIBLE,
  -- Foreign Key Constraints
  CONSTRAINT `fk_categories_parent`
    FOREIGN KEY (`parent_id`)
    REFERENCES `categories` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Danh mục sản phẩm quần áo - hỗ trợ phân cấp đa tầng';


-- -----------------------------------------------------
-- Table `brands`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `brands` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của thương hiệu',
  -- Thông tin thương hiệu
  `name` VARCHAR(100) NOT NULL COMMENT 'Tên thương hiệu (VD: Zara, H&M, Uniqlo, Nike)',
  `slug` VARCHAR(100) NOT NULL COMMENT 'URL-friendly name (VD: zara, h-and-m)',
  `description` TEXT NULL DEFAULT NULL COMMENT 'Mô tả về thương hiệu: lịch sử, xuất xứ, đặc điểm',
  `logo_url`       VARCHAR(500) NULL DEFAULT NULL COMMENT 'URL logo thương hiệu',
  `logo_public_id` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Cloudinary public_id để xóa/cập nhật logo',
  -- Cấu hình
  `is_active` BOOLEAN NOT NULL DEFAULT 1 COMMENT 'Trạng thái: 1=đang kinh doanh, 0=tạm ngừng',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm thêm thương hiệu',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật gần nhất',
  -- Primary Key và UNIQUE
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `slug_UNIQUE` (`slug` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_brands_active` (`is_active` ASC) VISIBLE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Thương hiệu quần áo';


-- -----------------------------------------------------
-- Table `products`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `products` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của sản phẩm',
  -- Foreign Keys
  `brand_id` BIGINT UNSIGNED NOT NULL COMMENT 'Liên kết với thương hiệu',
  -- Thông tin cơ bản
  `code` VARCHAR(50) NOT NULL COMMENT 'Mã sản phẩm duy nhất (VD: SP001)',
  `name` VARCHAR(255) NOT NULL COMMENT 'Tên sản phẩm (VD: Áo thun nam basic)',
  `slug` VARCHAR(255) NOT NULL COMMENT 'URL-friendly name (VD: ao-thun-nam-basic)',
  `description` TEXT NULL DEFAULT NULL COMMENT 'Mô tả chi tiết sản phẩm',
  -- Thuộc tính chung
  `gender` ENUM('MEN', 'WOMEN', 'UNISEX', 'KIDS') NOT NULL DEFAULT 'UNISEX' COMMENT 'Giới tính: Nam/Nữ/Unisex/Trẻ em',
  `material` VARCHAR(200) NULL DEFAULT NULL COMMENT 'Chất liệu vải (VD: 100% Cotton, Polyester)',
  `origin_country` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Quốc gia sản xuất',
  -- Giá cơ bản (có thể override ở variant)
  `base_price` DECIMAL(15,2) NOT NULL COMMENT 'Giá bán cơ bản (VNĐ)',
  `original_price` DECIMAL(15,2) NULL DEFAULT NULL COMMENT 'Giá gốc trước giảm giá (VNĐ)',
  `cost_price` DECIMAL(15,2) NULL DEFAULT NULL COMMENT 'Giá vốn (VNĐ) - chỉ admin xem',
  -- Trạng thái và badges
  `is_new` BOOLEAN NOT NULL DEFAULT 0 COMMENT 'Badge NEW: 1=hiển thị, 0=không',
  `is_on_sale` BOOLEAN NOT NULL DEFAULT 0 COMMENT 'Badge SALE: 1=hiển thị, 0=không',
  `is_active` BOOLEAN NOT NULL DEFAULT 1 COMMENT 'Trạng thái: 1=hiển thị trên web, 0=ẩn',
  -- Thống kê
  `sold_count` INT NOT NULL DEFAULT 0 COMMENT 'Tổng số lượng đã bán',
  `view_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Số lượt xem',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm thêm sản phẩm',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật gần nhất',
  -- Primary Key và UNIQUE
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE,
  UNIQUE INDEX `slug_UNIQUE` (`slug` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_products_name` (`name` ASC) VISIBLE,
  INDEX `idx_products_brand_active` (`brand_id` ASC, `is_active` ASC) VISIBLE,
  INDEX `idx_products_gender` (`gender` ASC) VISIBLE,
  INDEX `idx_products_new_sale` (`is_new` ASC, `is_on_sale` ASC) VISIBLE,
  -- Foreign Keys
  CONSTRAINT `fk_products_brand`
    FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Sản phẩm quần áo - thông tin chung';


-- -----------------------------------------------------
-- Table `product_variants`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `product_variants` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của biến thể',
  -- Foreign Keys
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT 'Liên kết với sản phẩm',
  -- Thuộc tính biến thể
  `size` VARCHAR(20) NOT NULL COMMENT 'Kích thước (VD: S, M, L, XL, XXL, 28, 30...)',
  `color` VARCHAR(50) NOT NULL COMMENT 'Màu sắc (VD: Đỏ, Xanh navy)',
  `color_hex` VARCHAR(7) NULL DEFAULT NULL COMMENT 'Mã màu hex (VD: #FF0000) để hiển thị ô màu',
  -- Giá riêng (NULL = dùng base_price của product)
  `price` DECIMAL(15,2) NULL DEFAULT NULL COMMENT 'Giá riêng của biến thể (NULL = lấy base_price từ products)',
  -- Quản lý kho
  `sku` VARCHAR(100) NOT NULL COMMENT 'Mã SKU duy nhất (VD: SP001-RED-M)',
  `stock_quantity` INT NOT NULL DEFAULT 0 COMMENT 'Tồn kho của biến thể này',
  `min_stock_level` INT NOT NULL DEFAULT 5 COMMENT 'Mức tồn kho tối thiểu - cảnh báo khi xuống dưới',
  -- Hiển thị
  `display_order` INT NOT NULL DEFAULT 0 COMMENT 'Thứ tự hiển thị màu (số nhỏ hiển thị trước) - variant display_order=0 là ảnh mặc định trên card',
  -- Trạng thái
  `is_active` BOOLEAN NOT NULL DEFAULT 1 COMMENT 'Trạng thái: 1=còn bán, 0=ngừng bán',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo biến thể',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật gần nhất',
  -- Primary Key và UNIQUE
  PRIMARY KEY (`id`),
  UNIQUE INDEX `sku_UNIQUE` (`sku` ASC) VISIBLE,
  UNIQUE INDEX `variant_UNIQUE` (`product_id` ASC, `size` ASC, `color` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_variants_product_order` (`product_id` ASC, `display_order` ASC) VISIBLE,
  INDEX `idx_variants_product_active` (`product_id` ASC, `is_active` ASC) VISIBLE,
  INDEX `idx_variants_stock` (`stock_quantity` ASC) VISIBLE,
  -- Foreign Keys
  CONSTRAINT `fk_variants_product`
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Biến thể sản phẩm - mỗi tổ hợp size+màu có giá và tồn kho riêng, display_order=0 hiển thị mặc định';


-- -----------------------------------------------------
-- Table `product_images`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `product_images` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của ảnh',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT 'Liên kết với sản phẩm',
  `color` VARCHAR(50) NOT NULL COMMENT 'Tên màu - dùng để nhóm và match với product_variants.color',
  `color_hex` VARCHAR(7) NULL DEFAULT NULL COMMENT 'Mã hex tương ứng - đồng bộ với product_variants.color_hex',
  `image_url` VARCHAR(500) NOT NULL COMMENT 'URL ảnh trên Cloudinary',
  `image_public_id` VARCHAR(255) NOT NULL COMMENT 'Cloudinary public_id để xóa/cập nhật ảnh',
  `is_primary` BOOLEAN NOT NULL DEFAULT 0 COMMENT 'Ảnh chính của màu: 1=hiển thị trên card, 0=ảnh phụ trong gallery',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm thêm ảnh',
  PRIMARY KEY (`id`),
  INDEX `idx_images_product_color` (`product_id` ASC, `color` ASC) VISIBLE,
  INDEX `idx_images_product_color_primary` (`product_id` ASC, `color` ASC, `is_primary` ASC) VISIBLE,
  CONSTRAINT `fk_images_product`
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Ảnh sản phẩm - nhóm theo màu, dùng chung cho mọi size cùng màu';


-- -----------------------------------------------------
-- Table `product_categories`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `product_categories` (
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT 'ID sản phẩm',
  `category_id` BIGINT UNSIGNED NOT NULL COMMENT 'ID danh mục',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm liên kết',
  -- Primary Key
  PRIMARY KEY (`product_id`, `category_id`),
  -- Indexes
  INDEX `idx_product_categories_category` (`category_id` ASC) VISIBLE,
  -- Foreign Keys
  CONSTRAINT `fk_product_categories_product`
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_product_categories_category`
    FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Quan hệ nhiều-nhiều giữa sản phẩm và danh mục';


-- =====================================================
-- INVENTORY & SUPPLIER MANAGEMENT
-- =====================================================

-- -----------------------------------------------------
-- Table `suppliers`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `suppliers` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID nhà cung cấp',
  -- Thông tin cơ bản
  `name` VARCHAR(200) NOT NULL COMMENT 'Tên nhà cung cấp',
  `code` VARCHAR(50) NOT NULL COMMENT 'Mã nhà cung cấp (VD: SUP-001)',
  -- Liên hệ
  `email` VARCHAR(100) NULL COMMENT 'Email liên hệ',
  `phone` VARCHAR(20) NULL COMMENT 'Số điện thoại',
  `contact_person` VARCHAR(100) NULL COMMENT 'Người liên hệ',
  -- Địa chỉ
  `address` TEXT NULL COMMENT 'Địa chỉ đầy đủ',
  -- Trạng thái
  `is_active` BOOLEAN NOT NULL DEFAULT 1 COMMENT '1=hoạt động, 0=ngừng',
  `notes` TEXT NULL COMMENT 'Ghi chú',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  -- Primary Key và UNIQUE
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_suppliers_active` (`is_active` ASC) VISIBLE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Nhà cung cấp quần áo';


-- -----------------------------------------------------
-- Table `purchases`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `purchases` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID phiếu nhập hàng',
  -- Foreign Keys
  `supplier_id` BIGINT UNSIGNED NOT NULL COMMENT 'ID nhà cung cấp',
  `created_by` BIGINT UNSIGNED NULL COMMENT 'ID người tạo phiếu',
  `received_by` BIGINT UNSIGNED NULL COMMENT 'ID người nhận hàng',
  -- Thông tin cơ bản
  `purchase_code` VARCHAR(50) NOT NULL COMMENT 'Mã phiếu nhập (VD: PUR-2025-00001)',
  `supplier_invoice_no` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Số hóa đơn từ nhà cung cấp',
  -- Tài chính
  `subtotal` DECIMAL(15,2) NOT NULL DEFAULT 0 COMMENT 'Tổng giá sản phẩm',
  `discount_amount` DECIMAL(15,2) NOT NULL DEFAULT 0 COMMENT 'Chiết khấu',
  `tax_amount` DECIMAL(15,2) NOT NULL DEFAULT 0 COMMENT 'Thuế VAT',
  `shipping_cost` DECIMAL(15,2) NOT NULL DEFAULT 0 COMMENT 'Phí vận chuyển',
  `total_cost` DECIMAL(15,2) NOT NULL COMMENT 'Tổng chi phí',
  `payment_status` ENUM('PENDING', 'PAID') NOT NULL DEFAULT 'PENDING' COMMENT 'Trạng thái thanh toán:
    - "PENDING" = Chưa thanh toán
    - "PAID" = Đã thanh toán đầy đủ',
  -- Trạng thái phiếu
  `status` ENUM('DRAFT', 'CONFIRMED', 'RECEIVED', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'DRAFT' COMMENT 'Trạng thái phiếu:
    - "DRAFT" = Nháp, chưa xác nhận
    - "CONFIRMED" = Đã xác nhận, đã đặt hàng với nhà cung cấp, chờ hàng về
    - "RECEIVED" = Hàng đã về, đang kiểm tra
    - "COMPLETED" = Hoàn tất, đã nhập kho và cập nhật tồn kho
    - "CANCELLED" = Hủy phiếu',
  -- Ghi chú
  `notes` TEXT NULL DEFAULT NULL COMMENT 'Ghi chú chung về đơn nhập',
  `issues` TEXT NULL DEFAULT NULL COMMENT 'Vấn đề phát hiện (hàng hỏng, thiếu, khác spec)',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo phiếu',
  `confirmed_at` DATETIME NULL DEFAULT NULL COMMENT 'Thời điểm xác nhận đặt hàng với nhà cung cấp',
  `received_at` DATETIME NULL DEFAULT NULL COMMENT 'Thời điểm nhận hàng',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
  -- Primary Key và UNIQUE
  PRIMARY KEY (`id`),
  UNIQUE INDEX `purchase_code_UNIQUE` (`purchase_code` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_purchases_supplier` (`supplier_id` ASC) VISIBLE,
  INDEX `idx_purchases_status` (`status` ASC) VISIBLE,
  INDEX `idx_purchases_payment_status` (`payment_status` ASC) VISIBLE,
  -- Foreign Key Constraints
  CONSTRAINT `fk_purchases_supplier`
    FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_purchases_created_by`
    FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_purchases_received_by`
    FOREIGN KEY (`received_by`) REFERENCES `users` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Phiếu nhập hàng - ghi lại đơn nhập từ nhà cung cấp, chi phí, thanh toán';


-- -----------------------------------------------------
-- Table `purchase_items`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `purchase_items` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID chi tiết dòng nhập',
  -- Foreign Keys
  `purchase_id` BIGINT UNSIGNED NOT NULL COMMENT 'ID phiếu nhập hàng',
  `variant_id` BIGINT UNSIGNED NOT NULL COMMENT 'ID biến thể nhập',
  -- Số lượng & Giá
  `quantity_ordered` INT UNSIGNED NOT NULL COMMENT 'Số lượng đặt mua',
  `quantity_received` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Số lượng thực tế nhận được',
  `unit_cost` DECIMAL(15,2) NOT NULL COMMENT 'Giá nhập/1 cái (COGS)',
  `line_total` DECIMAL(15,2) NOT NULL COMMENT 'Thành tiền = quantity_ordered * unit_cost',
  -- Chất lượng & Kiểm tra
  `quality_status` ENUM('PENDING', 'OK', 'DEFECTIVE', 'PARTIALLY_DEFECTIVE', 'RETURNED') NOT NULL DEFAULT 'PENDING'
    COMMENT 'Trạng thái kiểm chất lượng:
    - "PENDING" = Chưa kiểm tra
    - "OK" = Đạt tiêu chuẩn
    - "DEFECTIVE" = Lỗi toàn bộ
    - "PARTIALLY_DEFECTIVE" = Lỗi một phần
    - "RETURNED" = Trả lại supplier',
  `defective_qty` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Số lượng hỏng / lỗi',
  `defect_reason` TEXT NULL DEFAULT NULL COMMENT 'Lý do lỗi',
  -- Nhập kho
  `is_received` BOOLEAN NOT NULL DEFAULT 0 COMMENT 'Đã nhập vào kho: 1=yes, 0=no',
  `received_date` DATETIME NULL DEFAULT NULL COMMENT 'Ngày nhập vào kho',
  -- Ghi chú
  `notes` TEXT NULL DEFAULT NULL COMMENT 'Ghi chú chi tiết về dòng này',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm thêm',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
  -- Primary Key
  PRIMARY KEY (`id`),
  -- Indexes
  INDEX `idx_purchase_items_purchase` (`purchase_id` ASC) VISIBLE,
  INDEX `idx_purchase_items_variant` (`variant_id` ASC) VISIBLE,
  INDEX `idx_purchase_items_quality_status` (`quality_status` ASC) VISIBLE,
  INDEX `idx_purchase_items_is_received` (`is_received` ASC) VISIBLE,
  -- Foreign Key Constraints
  CONSTRAINT `fk_purchase_items_purchase`
    FOREIGN KEY (`purchase_id`) REFERENCES `purchases` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_purchase_items_variant`
    FOREIGN KEY (`variant_id`) REFERENCES `product_variants` (`id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Chi tiết phiếu nhập - danh sách biến thể nhập kèm giá (COGS), số lượng, chất lượng';


-- -----------------------------------------------------
-- Table `inventory_transactions`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `inventory_transactions` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID giao dịch kho',
  -- Foreign Keys
  `variant_id` BIGINT UNSIGNED NOT NULL COMMENT 'Biến thể thay đổi tồn kho (size + màu cụ thể)',
  `created_by` BIGINT UNSIGNED NULL DEFAULT NULL COMMENT 'Admin thực hiện (NULL = hệ thống tự động)',
  -- Loại giao dịch
  `type` ENUM('IN', 'OUT', 'ADJUST') NOT NULL COMMENT 'Loại giao dịch kho:
    - IN = Nhập kho (mua hàng từ nhà cung cấp, nhập hàng mới về)
    - OUT = Xuất kho (bán hàng cho khách, xuất hủy hàng hỏng)
    - ADJUST = Điều chỉnh tồn kho (kiểm kê phát hiện chênh lệch, sửa lỗi nhập liệu)',
  -- Số lượng thay đổi
  `quantity` INT NOT NULL COMMENT 'Số lượng thay đổi (dương = nhập, âm = xuất)',
  `old_stock` INT NOT NULL COMMENT 'Số lượng tồn TRƯỚC giao dịch',
  `new_stock` INT NOT NULL COMMENT 'Số lượng tồn SAU giao dịch',
  -- Tham chiếu
  `reference_type` ENUM('ORDER', 'PURCHASE_ITEM', 'RETURN', 'ADJUSTMENT') NULL DEFAULT NULL
    COMMENT 'Giao dịch này liên quan đến gì:
    - ORDER = Xuất kho do bán hàng (reference_id = order_id)
    - PURCHASE_ITEM = Nhập kho từ dòng cụ thể trong phiếu (reference_id = purchase_item_id)
    - RETURN = Nhập lại do khách trả hàng (reference_id = return_id)
    - ADJUSTMENT = Xuất thủ công hoặc kiểm kê (reference_id = stock_adjustment_items.id)',
  `reference_id` BIGINT UNSIGNED NULL DEFAULT NULL COMMENT 'ID tham chiếu tương ứng với reference_type',
  `note` TEXT NULL DEFAULT NULL COMMENT 'Ghi chú chi tiết về giao dịch',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm giao dịch (KHÔNG được sửa)',
  -- Primary Key
  PRIMARY KEY (`id`),
  -- Indexes
  INDEX `idx_inventory_variant_date` (`variant_id` ASC, `created_at` ASC) VISIBLE,
  INDEX `idx_inventory_type` (`type` ASC) VISIBLE,
  INDEX `idx_inventory_reference` (`reference_type` ASC, `reference_id` ASC) VISIBLE,
  INDEX `idx_inventory_created_by` (`created_by` ASC) VISIBLE,
  -- Foreign Keys
  CONSTRAINT `fk_inventory_variant`
    FOREIGN KEY (`variant_id`) REFERENCES `product_variants` (`id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_inventory_user`
    FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Lịch sử xuất nhập kho - ghi lại MỌI thay đổi tồn kho, KHÔNG được xóa/sửa, chỉ được thêm mới';


-- -----------------------------------------------------
-- Table `stock_adjustments`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `stock_adjustments` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID phiếu điều chỉnh kho',
  `created_by` BIGINT UNSIGNED NULL COMMENT 'Admin tạo phiếu',
  `code` VARCHAR(50) NOT NULL COMMENT 'Mã phiếu (VD: ADJ-2025-00001)',
  `type` ENUM('EXPORT', 'STOCKTAKE') NOT NULL COMMENT 'Loại phiếu:
    - EXPORT = Xuất thủ công (hủy hàng lỗi, xuất nội bộ, tặng)
    - STOCKTAKE = Kiểm kê (điều chỉnh về số thực tế)',
  `status` ENUM('DRAFT', 'CONFIRMED', 'CANCELLED') NOT NULL DEFAULT 'DRAFT',
  `reason` VARCHAR(255) NOT NULL COMMENT 'Lý do điều chỉnh (VD: Hủy hàng lỗi đợt kiểm kê T6)',
  `notes` TEXT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `confirmed_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE,
  INDEX `idx_adjustments_type_status` (`type` ASC, `status` ASC) VISIBLE,
  CONSTRAINT `fk_adjustments_user`
    FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Phiếu điều chỉnh kho thủ công - xuất hủy hàng lỗi và kiểm kê';


-- -----------------------------------------------------
-- Table `stock_adjustment_items`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `stock_adjustment_items` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID dòng điều chỉnh',
  `adjustment_id` BIGINT UNSIGNED NOT NULL COMMENT 'ID phiếu điều chỉnh',
  `variant_id` BIGINT UNSIGNED NOT NULL COMMENT 'Biến thể điều chỉnh',
  `quantity_change` INT NOT NULL COMMENT 'Thay đổi số lượng (dương=tăng, âm=giảm)',
  `quantity_before` INT NOT NULL COMMENT 'Tồn kho trước điều chỉnh',
  `quantity_after` INT NOT NULL COMMENT 'Tồn kho sau điều chỉnh',
  `note` VARCHAR(255) NULL COMMENT 'Ghi chú cho dòng này',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_adj_items_adjustment` (`adjustment_id` ASC) VISIBLE,
  INDEX `idx_adj_items_variant` (`variant_id` ASC) VISIBLE,
  CONSTRAINT `fk_adj_items_adjustment`
    FOREIGN KEY (`adjustment_id`) REFERENCES `stock_adjustments` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_adj_items_variant`
    FOREIGN KEY (`variant_id`) REFERENCES `product_variants` (`id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Chi tiết phiếu điều chỉnh kho - từng variant bị thay đổi';


-- =====================================================
-- ORDER & SHOPPING TABLES
-- =====================================================

-- -----------------------------------------------------
-- Table `carts`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `carts` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của giỏ hàng',
  `user_id` BIGINT UNSIGNED NULL DEFAULT NULL COMMENT 'ID người dùng (NULL nếu là khách)',
  `guest_token` VARCHAR(64) NULL DEFAULT NULL COMMENT 'Token nhận diện khách chưa đăng nhập (NULL nếu đã đăng nhập)',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo giỏ',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật gần nhất',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_cart_user_UNIQUE` (`user_id` ASC) VISIBLE,
  UNIQUE INDEX `user_cart_guest_UNIQUE` (`guest_token` ASC) VISIBLE,
  CONSTRAINT `fk_carts_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_carts_guest_token`
    FOREIGN KEY (`guest_token`) REFERENCES `guest_sessions` (`guest_token`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Giỏ hàng - hỗ trợ cả user đăng nhập và guest';


-- -----------------------------------------------------
-- Table `cart_items`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `cart_items` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của item',
  `cart_id` BIGINT UNSIGNED NOT NULL COMMENT 'Liên kết với giỏ hàng',
  `variant_id` BIGINT UNSIGNED NOT NULL COMMENT 'Liên kết với biến thể (size + màu cụ thể)',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT 'Số lượng (tối thiểu 1)',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm thêm vào giỏ',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật số lượng',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `cart_variant_UNIQUE` (`cart_id` ASC, `variant_id` ASC) VISIBLE,
  INDEX `idx_cart_items_cart` (`cart_id` ASC) VISIBLE,
  INDEX `idx_cart_items_variant` (`variant_id` ASC) VISIBLE,
  CONSTRAINT `fk_cart_items_cart`
    FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cart_items_variant`
    FOREIGN KEY (`variant_id`) REFERENCES `product_variants` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Sản phẩm trong giỏ hàng - lưu theo variant (size + màu cụ thể)';


-- -----------------------------------------------------
-- Table `orders`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `orders` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của đơn hàng',
  `user_id` BIGINT UNSIGNED NULL DEFAULT NULL COMMENT 'ID người dùng (NULL nếu khách)',
  `guest_token` VARCHAR(64) NULL DEFAULT NULL COMMENT 'Token của khách - tra cứu qua session',
  `code` VARCHAR(50) NOT NULL COMMENT 'Mã đơn hàng (VD: ORD-2025-00001)',
  -- Trạng thái
  `status` ENUM('PENDING','CONFIRMED','PROCESSING','SHIPPING','DELIVERED','CANCELLED','REFUNDED') NOT NULL DEFAULT 'PENDING'
    COMMENT 'Trạng thái đơn hàng:
    - PENDING = Chờ xác nhận
    - CONFIRMED = Đã xác nhận
    - PROCESSING = Đang đóng gói
    - SHIPPING = Đang vận chuyển
    - DELIVERED = Đã giao thành công
    - CANCELLED = Đã hủy
    - REFUNDED = Đã hoàn tiền',
  `payment_status` ENUM('UNPAID','PAID','FAILED','REFUNDED','CANCELLED','EXPIRED') NOT NULL DEFAULT 'UNPAID'
    COMMENT 'Trạng thái thanh toán:
    - UNPAID = Chưa thanh toán (COD hoặc chưa hoàn tất online)
    - PAID = Đã thanh toán thành công
    - FAILED = Thanh toán thất bại
    - REFUNDED = Đã hoàn tiền
    - CANCELLED = Giao dịch bị hủy
    - EXPIRED = Hết hạn thanh toán',
  `payment_method` ENUM('VNPAY', 'COD') NOT NULL COMMENT 'Phương thức thanh toán',
  -- Thông tin giao hàng (snapshot địa chỉ tại thời điểm đặt)
  `recipient_name` VARCHAR(200) NOT NULL COMMENT 'Tên người nhận',
  `recipient_phone` VARCHAR(15) NOT NULL COMMENT 'SĐT người nhận',
  `shipping_street` VARCHAR(255) NOT NULL COMMENT 'Số nhà, tên đường',
  `shipping_ward` VARCHAR(100) NOT NULL COMMENT 'Phường/Xã',
  `shipping_district` VARCHAR(100) NOT NULL COMMENT 'Quận/Huyện',
  `shipping_province` VARCHAR(100) NOT NULL COMMENT 'Tỉnh/Thành phố',
  -- Tài chính
  `subtotal` DECIMAL(15,2) NOT NULL COMMENT 'Tổng tiền sản phẩm',
  `shipping_fee` DECIMAL(15,2) NOT NULL DEFAULT 0 COMMENT 'Phí vận chuyển',
  `discount_amount` DECIMAL(15,2) NOT NULL DEFAULT 0 COMMENT 'Số tiền giảm giá',
  `total_amount` DECIMAL(15,2) NOT NULL COMMENT 'Tổng thanh toán = subtotal + shipping_fee - discount_amount',
  `coupon_code` VARCHAR(50) NULL DEFAULT NULL COMMENT 'Mã giảm giá đã dùng',
  -- Ghi chú
  `note` TEXT NULL DEFAULT NULL COMMENT 'Ghi chú của khách',
  `admin_note` TEXT NULL DEFAULT NULL COMMENT 'Ghi chú nội bộ',
  -- Hủy đơn
  `cancelled_reason` TEXT NULL DEFAULT NULL COMMENT 'Lý do hủy',
  `cancelled_by` ENUM('CUSTOMER', 'ADMIN', 'SYSTEM') NULL DEFAULT NULL COMMENT 'Người hủy đơn',
  `cancelled_at` DATETIME NULL DEFAULT NULL COMMENT 'Thời điểm hủy',
  -- Vận chuyển
  `confirmed_at` DATETIME NULL DEFAULT NULL COMMENT 'Thời điểm admin xác nhận',
  `estimated_delivery_date` DATE NULL DEFAULT NULL COMMENT 'Ngày giao dự kiến',
  `tracking_number` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Mã vận đơn',
  `delivered_at` DATETIME NULL DEFAULT NULL COMMENT 'Thời điểm giao thành công',
  -- Timestamp
  `expired_at` DATETIME NOT NULL COMMENT 'Hết hạn thanh toán (tạo đơn + 24h)',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo đơn',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
  -- Primary Key và UNIQUE
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_orders_user_status` (`user_id` ASC, `status` ASC) VISIBLE,
  INDEX `idx_orders_guest` (`guest_token` ASC) VISIBLE,
  INDEX `idx_orders_payment_status` (`payment_status` ASC) VISIBLE,
  INDEX `idx_orders_created` (`created_at` ASC) VISIBLE,
  -- Constraints
  CONSTRAINT `fk_orders_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_orders_guest_token`
    FOREIGN KEY (`guest_token`) REFERENCES `guest_sessions` (`guest_token`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Đơn hàng - bảng chính quản lý thông tin đặt hàng';


-- -----------------------------------------------------
-- Table `order_items`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `order_items` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của item',
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT 'Liên kết với đơn hàng',
  `variant_id` BIGINT UNSIGNED NOT NULL COMMENT 'Biến thể tại thời điểm đặt',
  -- Snapshot tại thời điểm đặt
  `product_code` VARCHAR(50) NOT NULL COMMENT 'Mã sản phẩm snapshot',
  `product_name` VARCHAR(255) NOT NULL COMMENT 'Tên sản phẩm snapshot',
  `variant_sku` VARCHAR(100) NOT NULL COMMENT 'SKU snapshot',
  `variant_size` VARCHAR(20) NOT NULL COMMENT 'Size snapshot',
  `variant_color` VARCHAR(50) NOT NULL COMMENT 'Màu snapshot',
  -- Số lượng và giá
  `quantity` INT NOT NULL COMMENT 'Số lượng đặt',
  `unit_price` DECIMAL(15,2) NOT NULL COMMENT 'Giá bán tại thời điểm đặt',
  `discount_amount` DECIMAL(15,2) NOT NULL DEFAULT 0 COMMENT 'Giảm giá cho item này',
  `subtotal` DECIMAL(15,2) NOT NULL COMMENT 'Thành tiền = unit_price * quantity - discount_amount',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm thêm item',
  -- Primary Key
  PRIMARY KEY (`id`),
  -- Indexes
  INDEX `idx_order_items_order` (`order_id` ASC) VISIBLE,
  INDEX `idx_order_items_variant` (`variant_id` ASC) VISIBLE,
  -- Foreign Keys
  CONSTRAINT `fk_order_items_order`
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_order_items_variant`
    FOREIGN KEY (`variant_id`) REFERENCES `product_variants` (`id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Chi tiết đơn hàng - snapshot sản phẩm tại thời điểm đặt';


-- -----------------------------------------------------
-- Table `order_status_history`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `order_status_history` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của lịch sử',
  -- Foreign Keys
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT 'Liên kết với đơn hàng',
  `changed_by` BIGINT UNSIGNED NULL DEFAULT NULL COMMENT 'ID người thực hiện (NULL nếu hệ thống tự đổi)',
  -- Thông tin thay đổi
  `old_status` ENUM('PENDING','CONFIRMED','PROCESSING','SHIPPING','DELIVERED','CANCELLED','REFUNDED')
    NULL DEFAULT NULL COMMENT 'Trạng thái cũ trước khi thay đổi:
    - PENDING    = Chờ xác nhận
    - CONFIRMED  = Đã xác nhận
    - PROCESSING = Đang đóng gói
    - SHIPPING   = Đang vận chuyển
    - DELIVERED  = Đã giao thành công
    - CANCELLED  = Đã hủy
    - REFUNDED   = Đã hoàn tiền
    (NULL khi đơn hàng mới được tạo lần đầu)',
  `new_status` ENUM('PENDING','CONFIRMED','PROCESSING','SHIPPING','DELIVERED','CANCELLED','REFUNDED')
    NOT NULL COMMENT 'Trạng thái mới sau khi thay đổi:
    - PENDING    = Chờ xác nhận
    - CONFIRMED  = Đã xác nhận
    - PROCESSING = Đang đóng gói
    - SHIPPING   = Đang vận chuyển
    - DELIVERED  = Đã giao thành công
    - CANCELLED  = Đã hủy
    - REFUNDED   = Đã hoàn tiền',
  -- Timestamp (immutable - không có updated_at)
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm thay đổi trạng thái',
  -- Primary Key
  PRIMARY KEY (`id`),
  -- Indexes
  INDEX `idx_status_history_order_created` (`order_id` ASC, `created_at` ASC) VISIBLE,
  INDEX `idx_status_history_changed_by` (`changed_by` ASC) VISIBLE,
  -- Foreign Keys
  CONSTRAINT `fk_status_history_order`
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_status_history_user`
    FOREIGN KEY (`changed_by`) REFERENCES `users` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Lịch sử thay đổi trạng thái đơn hàng - audit trail bất biến';


-- -----------------------------------------------------
-- Table `payments`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `payments` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của giao dịch thanh toán',
  -- Foreign Keys
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT 'Liên kết với đơn hàng',
  -- Thông tin thanh toán
  `amount` DECIMAL(15,2) NOT NULL COMMENT 'Số tiền thanh toán (VNĐ)',
  `status` ENUM('PENDING','SUCCESS','FAILED','EXPIRED') NOT NULL DEFAULT 'PENDING'
    COMMENT 'Trạng thái giao dịch VNPay:
    - PENDING = Đã tạo link, đang chờ user thanh toán (chưa có callback)
    - SUCCESS = Thanh toán thành công (callback success từ VNPay)
    - FAILED  = Thanh toán thất bại (user hủy hoặc callback lỗi)
    - EXPIRED = Hết hạn thanh toán (quá 15 phút không thực hiện)',
  `transaction_id` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Mã giao dịch từ VNPay (vnp_TransactionNo)',
  `response_code` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Mã phản hồi VNPay (vnp_ResponseCode) - 00=thành công',
  `response_message` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Thông báo phản hồi từ VNPay - hiển thị cho user khi thất bại',
  `paid_at` DATETIME NULL DEFAULT NULL COMMENT 'Thời điểm thanh toán thành công',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo giao dịch',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
  -- Primary Key
  PRIMARY KEY (`id`),
  -- Indexes
  INDEX `idx_payments_order` (`order_id` ASC) VISIBLE,
  INDEX `idx_payments_transaction` (`transaction_id` ASC) VISIBLE,
  INDEX `idx_payments_status` (`status` ASC) VISIBLE,
  -- Foreign Key Constraints
  CONSTRAINT `fk_payments_order`
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Giao dịch thanh toán VNPay - lưu callback và trạng thái từng lần thanh toán';


-- -----------------------------------------------------
-- Table `coupons`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `coupons` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của mã giảm giá',
  -- Thông tin mã
  `code` VARCHAR(50) NOT NULL COMMENT 'Mã giảm giá khách nhập (VD: SUMMER2025)',
  `description` TEXT NULL DEFAULT NULL COMMENT 'Mô tả chương trình giảm giá',
  -- Loại giảm giá
  `discount_type` ENUM('PERCENTAGE','FIXED_AMOUNT') NOT NULL
    COMMENT 'Loại hình giảm giá:
    - PERCENTAGE   = Giảm theo % trên tổng đơn hàng
    - FIXED_AMOUNT = Giảm số tiền cố định (VD: 50.000đ)',
  `discount_value` DECIMAL(15,2) NOT NULL COMMENT 'Giá trị giảm (VD: 10 = giảm 10%, 50000 = giảm 50k)',
  -- Điều kiện áp dụng
  `min_order_amount` DECIMAL(15,2) NULL DEFAULT NULL COMMENT 'Giá trị đơn tối thiểu để áp dụng (NULL = không giới hạn)',
  `max_discount_amount` DECIMAL(15,2) NULL DEFAULT NULL COMMENT 'Số tiền giảm tối đa - chỉ áp dụng với PERCENTAGE (NULL = không giới hạn)',
  -- Giới hạn sử dụng
  `usage_limit` INT NULL DEFAULT NULL COMMENT 'Tổng số lần dùng tối đa (NULL = không giới hạn)',
  `used_count` INT NOT NULL DEFAULT 0 COMMENT 'Số lần đã dùng - tăng khi đơn CONFIRMED',
  `user_usage_limit` INT NULL DEFAULT NULL COMMENT 'Số lần mỗi user được dùng (NULL = không giới hạn, 1 = mỗi user 1 lần)',
  -- Thời gian hiệu lực
  `start_date` DATETIME NOT NULL COMMENT 'Thời điểm bắt đầu hiệu lực',
  `end_date` DATETIME NOT NULL COMMENT 'Thời điểm hết hiệu lực',
  -- Trạng thái
  `is_active` BOOLEAN NOT NULL DEFAULT 1 COMMENT 'Trạng thái: 1=kích hoạt, 0=tạm ngưng',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo mã',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
  -- Primary Key và UNIQUE
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_coupons_active_dates` (`is_active` ASC, `start_date` ASC, `end_date` ASC) VISIBLE,
  -- Check constraints
  CONSTRAINT `chk_coupon_dates`
    CHECK (`end_date` > `start_date`),
  CONSTRAINT `chk_coupon_discount_value`
    CHECK (`discount_value` > 0),
  CONSTRAINT `chk_coupon_percentage_range`
    CHECK (`discount_type` != 'PERCENTAGE' OR `discount_value` <= 100)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Mã giảm giá - hỗ trợ giảm theo % và số tiền cố định';


-- -----------------------------------------------------
-- Table `coupon_usage`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `coupon_usage` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của lịch sử',
  -- Foreign Keys
  `coupon_id` BIGINT UNSIGNED NOT NULL COMMENT 'Liên kết với mã giảm giá',
  `user_id` BIGINT UNSIGNED NULL DEFAULT NULL COMMENT 'ID người dùng đã đăng nhập (NULL nếu guest)',
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT 'Liên kết với đơn hàng',
  -- Anti-spam tracking (snapshot tại thời điểm dùng)
  `email` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Email tại thời điểm đặt hàng - check spam cho cả user và guest',
  `phone` VARCHAR(15) NULL DEFAULT NULL COMMENT 'SĐT tại thời điểm đặt hàng - check spam cho cả user và guest',
  -- Thông tin sử dụng
  `discount_amount` DECIMAL(15,2) NOT NULL COMMENT 'Số tiền thực tế đã giảm',
  -- Timestamp (immutable)
  `used_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm sử dụng mã',
  -- Primary Key
  PRIMARY KEY (`id`),
  -- Unique: 1 đơn hàng chỉ dùng 1 coupon
  UNIQUE INDEX `order_UNIQUE` (`order_id` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_coupon_usage_coupon` (`coupon_id` ASC) VISIBLE,
  INDEX `idx_coupon_usage_user` (`user_id` ASC) VISIBLE,
  INDEX `idx_coupon_usage_email` (`coupon_id` ASC, `email` ASC) VISIBLE,
  INDEX `idx_coupon_usage_phone` (`coupon_id` ASC, `phone` ASC) VISIBLE,
  -- Foreign Key Constraints
  CONSTRAINT `fk_coupon_usage_coupon`
    FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_coupon_usage_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_coupon_usage_order`
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Lịch sử sử dụng mã giảm giá - tracking và chống spam theo email/phone';


-- =====================================================
-- USER ENGAGEMENT & CONTENT TABLES
-- =====================================================

-- -----------------------------------------------------
-- Table `product_reviews`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `product_reviews` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của đánh giá',
  -- Foreign Keys
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT 'Sản phẩm được đánh giá',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT 'Người dùng đánh giá (chỉ user đăng nhập, đã mua hàng)',
  -- Nội dung đánh giá
  `rating` TINYINT UNSIGNED NOT NULL COMMENT 'Điểm đánh giá từ 1-5 sao',
  `comment` TEXT NULL DEFAULT NULL COMMENT 'Nội dung đánh giá chi tiết',
  -- Trạng thái
  `is_approved` BOOLEAN NOT NULL DEFAULT 1 COMMENT 'Trạng thái duyệt: 1=hiển thị công khai, 0=ẩn/chờ duyệt',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo đánh giá',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm chỉnh sửa',
  -- Primary Key
  PRIMARY KEY (`id`),
  -- Unique: 1 user chỉ review 1 lần cho 1 sản phẩm
  UNIQUE INDEX `user_product_UNIQUE` (`user_id` ASC, `product_id` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_reviews_product_approved` (`product_id` ASC, `is_approved` ASC) VISIBLE,
  INDEX `idx_reviews_product_rating` (`product_id` ASC, `rating` ASC) VISIBLE,
  INDEX `idx_reviews_user` (`user_id` ASC) VISIBLE,
  -- Check constraints
  CONSTRAINT `chk_reviews_rating`
    CHECK (`rating` BETWEEN 1 AND 5),
  -- Foreign Keys
  CONSTRAINT `fk_reviews_product`
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reviews_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Đánh giá sản phẩm - chỉ user đã mua, tối đa 1 review/sản phẩm/user';


-- -----------------------------------------------------
-- Table `product_comments`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `product_comments` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của bình luận',
  -- Foreign Keys
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT 'Sản phẩm được bình luận',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT 'Người bình luận (chỉ user đăng nhập)',
  `parent_id` BIGINT UNSIGNED NULL DEFAULT NULL COMMENT 'ID comment cha (NULL=comment gốc, có giá trị=reply)',
  -- Nội dung
  `content` TEXT NOT NULL COMMENT 'Nội dung bình luận',
  -- Trạng thái
  `is_approved` BOOLEAN NOT NULL DEFAULT 1 COMMENT 'Trạng thái duyệt: 1=hiển thị, 0=ẩn',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm bình luận',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm chỉnh sửa',
  -- Primary Key
  PRIMARY KEY (`id`),
  -- Indexes
  INDEX `idx_comments_product_approved` (`product_id` ASC, `is_approved` ASC) VISIBLE,
  INDEX `idx_comments_parent` (`parent_id` ASC) VISIBLE,
  INDEX `idx_comments_user` (`user_id` ASC) VISIBLE,
  -- Foreign Keys
  CONSTRAINT `fk_comments_product`
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comments_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comments_parent`
    FOREIGN KEY (`parent_id`) REFERENCES `product_comments` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Bình luận sản phẩm - hỗ trợ reply lồng nhau, không giới hạn số lần';


-- -----------------------------------------------------
-- Table `wishlists`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `wishlists` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất',
  -- Foreign Keys
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT 'Người dùng (chỉ user đăng nhập mới có wishlist)',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT 'Sản phẩm yêu thích',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm thêm vào wishlist',
  -- Primary Key
  PRIMARY KEY (`id`),
  -- Unique: 1 user không thể thêm cùng 1 sản phẩm 2 lần
  UNIQUE INDEX `user_product_UNIQUE` (`user_id` ASC, `product_id` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_wishlists_user` (`user_id` ASC) VISIBLE,
  -- Foreign Keys
  CONSTRAINT `fk_wishlists_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_wishlists_product`
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Danh sách yêu thích - lưu sản phẩm user muốn mua sau';


-- -----------------------------------------------------
-- Table `notifications`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `notifications` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất của thông báo',
  -- Đối tượng nhận
  `target_type` ENUM('USER','ADMIN','ALL') NOT NULL
    COMMENT 'Đối tượng nhận:
    - USER  = Gửi cho 1 user cụ thể (ORDER, PAYMENT, REVIEW)
    - ADMIN = Gửi cho admin (INVENTORY, đơn hàng mới...)
    - ALL   = Broadcast toàn hệ thống (PROMOTION, SYSTEM)',
  `target_user_id` BIGINT UNSIGNED NULL DEFAULT NULL
    COMMENT 'ID user nhận (chỉ có giá trị khi target_type=USER hoặc ADMIN, NULL khi ALL)',
  -- Nội dung
  `type` ENUM('ORDER','PAYMENT','PROMOTION','SYSTEM','REVIEW','INVENTORY') NOT NULL
    COMMENT 'Loại thông báo:
    - ORDER     = Cập nhật đơn hàng
    - PAYMENT   = Trạng thái thanh toán
    - PROMOTION = Khuyến mãi, mã giảm giá (thường là ALL)
    - SYSTEM    = Hệ thống, bảo trì (thường là ALL)
    - REVIEW    = Nhắc nhở đánh giá sau mua hàng
    - INVENTORY = Cảnh báo tồn kho thấp (ADMIN only)',
  `title` VARCHAR(255) NOT NULL COMMENT 'Tiêu đề thông báo',
  `message` TEXT NOT NULL COMMENT 'Nội dung chi tiết',
  `link` VARCHAR(500) NULL DEFAULT NULL COMMENT 'Link điều hướng khi click',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
  -- Primary Key
  PRIMARY KEY (`id`),
  -- Indexes
  INDEX `idx_notifications_target_user` (`target_type` ASC, `target_user_id` ASC) VISIBLE,
  INDEX `idx_notifications_type` (`type` ASC) VISIBLE,
  INDEX `idx_notifications_created` (`created_at` ASC) VISIBLE,
  -- Foreign Key
  CONSTRAINT `fk_notifications_target_user`
    FOREIGN KEY (`target_user_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Thông báo hệ thống - cá nhân và broadcast';


-- -----------------------------------------------------
-- Table `notification_recipients`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `notification_recipients` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID duy nhất',
  `notification_id` BIGINT UNSIGNED NOT NULL COMMENT 'Liên kết với thông báo',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT 'User đã nhận/đọc thông báo',
  -- Trạng thái đọc
  `is_read` BOOLEAN NOT NULL DEFAULT 0 COMMENT '0=chưa đọc, 1=đã đọc',
  `read_at` DATETIME NULL DEFAULT NULL COMMENT 'Thời điểm đọc (NULL nếu chưa đọc)',
  -- Timestamp
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm nhận thông báo',
  -- Primary Key
  PRIMARY KEY (`id`),
  -- Unique: 1 user chỉ có 1 record per notification
  UNIQUE INDEX `user_notification_UNIQUE` (`user_id` ASC, `notification_id` ASC) VISIBLE,
  -- Indexes
  INDEX `idx_recipients_notification` (`notification_id` ASC) VISIBLE,
  INDEX `idx_recipients_user_read` (`user_id` ASC, `is_read` ASC) VISIBLE,
  -- Foreign Keys
  CONSTRAINT `fk_recipients_notification`
    FOREIGN KEY (`notification_id`) REFERENCES `notifications` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_recipients_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_520_ci
  COMMENT = 'Track trạng thái đọc thông báo của từng user - lazy insert khi user mở thông báo lần đầu';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;