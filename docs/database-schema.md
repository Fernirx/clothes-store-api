# Database Schema: Clothes E-commerce Database
Version: 1.0

## 1. CORE & AUTHENTICATION TABLES

### `users`
Bảng quản lý tài khoản người dùng - chứa thông tin xác thực và phân quyền.
* `id` (BIGINT, PK, AUTO_INCREMENT): ID duy nhất của người dùng.
* `email` (VARCHAR, UNIQUE, NOT NULL): Email đăng nhập, duy nhất.
* `password_hash` (VARCHAR, NULL): Mật khẩu đã hash.
* `provider` (ENUM, NOT NULL, Dflt 'LOCAL'): Phương thức đăng nhập (LOCAL, GOOGLE).
* `provider_id` (VARCHAR, NULL): ID từ nhà cung cấp OAuth.
* `role` (ENUM, NOT NULL, Dflt 'USER'): Phân quyền (USER, ADMIN).
* `is_verified` (BOOLEAN, Dflt 0): Trạng thái xác thực email.
* `is_active` (BOOLEAN, Dflt 1): Trạng thái tài khoản.

### `user_profiles`
Thông tin cá nhân và địa chỉ giao hàng của người dùng.
* `id` (BIGINT, PK, AUTO_INCREMENT): ID duy nhất của profile.
* `user_id` (BIGINT, FK -> users.id, UNIQUE): Liên kết 1-1 với users.
* `first_name`, `last_name`, `avatar_url`, `date_of_birth`: Thông tin cá nhân.
* `shipping_name`, `shipping_phone`, `shipping_street`, `shipping_ward`, `shipping_district`, `shipping_province`: Địa chỉ giao hàng.

### `guest_sessions` & `token_blacklist`
* **guest_sessions**: Phiên làm việc của khách chưa đăng ký (`guest_token`, `expires_at`).
* **token_blacklist**: Danh sách JWT token bị vô hiệu hóa (`token`, `reason`, `expires_at`).

---

## 2. PRODUCT CATALOG TABLES

### `categories`
Danh mục sản phẩm quần áo - hỗ trợ phân cấp đa tầng.
* `id` (BIGINT, PK, AUTO_INCREMENT): ID danh mục.
* `parent_id` (BIGINT, FK -> categories.id, NULL): ID danh mục cha.
* `name`, `slug` (UNIQUE), `description`: Thông tin hiển thị.
* `display_order`, `is_active`: Cấu hình hiển thị.

### `brands`
Thương hiệu quần áo.
* `id` (BIGINT, PK, AUTO_INCREMENT).
* `name`, `slug` (UNIQUE), `description`, `logo_url`: Thông tin thương hiệu.
* `is_active`: Trạng thái kinh doanh.

### `products`
Sản phẩm quần áo - thông tin chung.
* `id` (BIGINT, PK, AUTO_INCREMENT).
* `brand_id` (BIGINT, FK -> brands.id).
* `code`, `slug` (UNIQUE), `name`, `description`: Thông tin cơ bản.
* `gender` (ENUM), `material`, `origin_country`: Thuộc tính.
* `base_price`, `original_price`, `cost_price`: Cấu trúc giá.
* `is_new`, `is_on_sale`, `is_active`: Trạng thái.
* `sold_count`, `view_count`: Thống kê.

### `product_variants`
Biến thể sản phẩm - mỗi tổ hợp size+màu có giá và tồn kho riêng.
* `id` (BIGINT, PK, AUTO_INCREMENT).
* `product_id` (BIGINT, FK -> products.id).
* `size`, `color`, `color_hex`: Thuộc tính phân loại (UNIQUE constraint trên product_id + size + color).
* `price`: Giá riêng (NULL = dùng base_price).
* `sku` (UNIQUE), `stock_quantity`, `min_stock_level`: Quản lý kho.

### `product_images` & `product_categories`
* **product_images**: Ảnh sản phẩm nhóm theo màu (`product_id`, `color`, `image_url`, `is_primary`).
* **product_categories**: Quan hệ n-n giữa sản phẩm và danh mục (`product_id`, `category_id`).

---

## 3. INVENTORY & SUPPLIER MANAGEMENT

### `suppliers`
* `id`, `name` (UNIQUE), `code` (UNIQUE), `email`, `phone`, `address`, `is_active`.

### `purchases` & `purchase_items`
* **purchases**: Phiếu nhập hàng (`supplier_id`, `purchase_code`, `total_cost`, `payment_status`, `status`: DRAFT/CONFIRMED/RECEIVED/COMPLETED/CANCELLED).
* **purchase_items**: Chi tiết nhập (`purchase_id`, `variant_id`, `quantity_ordered`, `quantity_received`, `unit_cost`, `quality_status`).

### `inventory_transactions`
Lịch sử xuất nhập kho bất biến.
* `variant_id`, `type` (IN/OUT/ADJUST), `quantity`, `old_stock`, `new_stock`.
* `reference_type` (ORDER/PURCHASE_ITEM/RETURN/ADJUSTMENT), `reference_id`.

### `stock_adjustments` & `stock_adjustment_items`
Phiếu điều chỉnh kho thủ công (kiểm kê, xuất hủy).

---

## 4. ORDER & SHOPPING TABLES

### `carts` & `cart_items`
* **carts**: Giỏ hàng hỗ trợ user và guest (`user_id`, `guest_token`).
* **cart_items**: Item trong giỏ (`cart_id`, `variant_id`, `quantity`).

### `orders`
* `id`, `user_id`, `guest_token`, `code` (UNIQUE).
* `status`: PENDING/CONFIRMED/PROCESSING/SHIPPING/DELIVERED/CANCELLED/REFUNDED.
* `payment_status`, `payment_method` (VNPAY/COD).
* Thông tin giao hàng: `recipient_name`, `recipient_phone`, `shipping_street`...
* Tài chính: `subtotal`, `shipping_fee`, `discount_amount`, `total_amount`, `coupon_code`.

### `order_items` & `order_status_history`
* **order_items**: Snapshot lúc đặt hàng (`order_id`, `variant_id`, `product_name`, `variant_sku`, `unit_price`, `quantity`).
* **order_status_history**: Audit trail lưu lịch sử đổi trạng thái đơn hàng (`old_status`, `new_status`).

### `payments`
Giao dịch VNPay (`order_id`, `amount`, `status`, `transaction_id`, `response_code`).

### `coupons` & `coupon_usage`
* **coupons**: `code`, `discount_type` (PERCENTAGE/FIXED_AMOUNT), `discount_value`, `usage_limit`, `start_date`, `end_date`.
* **coupon_usage**: Lịch sử dùng mã (`coupon_id`, `order_id`, `email`, `phone` để check spam).

---

## 5. USER ENGAGEMENT & CONTENT TABLES

* **product_reviews**: Đánh giá 1-5 sao (`product_id`, `user_id`, `rating`, `comment`, `is_approved`).
* **product_comments**: Bình luận hỗ trợ lồng nhau (`product_id`, `user_id`, `parent_id`, `content`).
* **wishlists**: Danh sách yêu thích (`user_id`, `product_id`).
* **notifications** & **notification_recipients**: Hệ thống thông báo cá nhân và broadcast (`target_type`, `type`, `title`, `message`, `is_read`).