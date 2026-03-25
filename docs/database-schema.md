# 🗄️ Tài Liệu Database Schema: Hệ Thống E-commerce Quần Áo
**Phiên bản:** 1.2 (Cập nhật đầy đủ field) | **Hệ quản trị:** MySQL 8+ | **Công cụ Migration:** Flyway

---

## 1. 🔐 Core & Authentication (Xác Thực & Người Dùng)

### 📄 Bảng: `users` (Tài khoản)
| Các trường (Fields) | Ý nghĩa chính |
| :--- | :--- |
| `id`, `email`, `password_hash` | Định danh và thông tin đăng nhập cơ bản |
| `provider`, `provider_id` | Phục vụ đăng nhập qua mạng xã hội (Google, Local) |
| `role` | Phân quyền (USER, ADMIN) |
| `is_verified`, `is_active` | Trạng thái tài khoản |
| `last_login`, `created_at`, `updated_at` | Lưu vết thời gian hoạt động |

### 📄 Bảng: `user_profiles` (Hồ sơ người dùng)
| Các trường (Fields) | Ý nghĩa chính |
| :--- | :--- |
| `id`, `user_id` | Khóa chính và khóa ngoại liên kết 1-1 với `users` |
| `first_name`, `last_name`, `date_of_birth` | Thông tin cá nhân |
| `avatar_url`, `avatar_public_id` | Link ảnh và ID để quản lý trên Cloudinary/S3 |
| `shipping_name`, `shipping_phone` | Tên và SĐT người nhận hàng mặc định |
| `shipping_street`, `shipping_ward`, `shipping_district`, `shipping_province` | Địa chỉ giao hàng mặc định |
| `created_at`, `updated_at` | Lưu vết thời gian |

### 📄 Các bảng phụ trợ xác thực
* **`guest_sessions`**: `id`, `guest_token`, `created_at`, `last_active`, `expires_at`
* **`token_blacklist`**: `id`, `user_id`, `token`, `reason`, `expires_at`, `created_at`

---

## 2. 🛍️ Product Catalog (Danh Mục Sản Phẩm)

### 📄 Bảng: `products` (Thông tin chung sản phẩm)
| Các trường (Fields) | Ý nghĩa chính |
| :--- | :--- |
| `id`, `brand_id`, `code` | Định danh, thương hiệu và mã sản phẩm |
| `name`, `slug`, `description` | Tên, đường dẫn SEO và mô tả |
| `gender`, `material`, `origin_country` | Giới tính, chất liệu, xuất xứ |
| `base_price`, `original_price`, `cost_price` | Giá bán, giá gốc (gạch ngang), giá vốn |
| `is_new`, `is_on_sale`, `is_active` | Các cờ (flag) trạng thái hiển thị |
| `sold_count`, `view_count` | Thống kê số lượng bán và lượt xem |
| `created_at`, `updated_at` | Lưu vết thời gian |

### 📄 Bảng: `product_variants` (Biến thể sản phẩm)
| Các trường (Fields) | Ý nghĩa chính |
| :--- | :--- |
| `id`, `product_id`, `sku` | Định danh và mã vạch biến thể |
| `size`, `color`, `color_hex` | Kích cỡ, tên màu và mã màu Hex |
| `price` | Giá bán riêng (nếu khác base_price) |
| `stock_quantity`, `min_stock_level` | Tồn kho thực tế và mức cảnh báo sắp hết hàng |
| `display_order`, `is_active` | Thứ tự hiển thị và trạng thái |
| `created_at`, `updated_at` | Lưu vết thời gian |

### 📄 Các bảng phân loại và hình ảnh
* **`product_images`**: `id`, `product_id`, `color`, `color_hex`, `image_url`, `image_public_id` (Dùng cho Cloudinary), `is_primary`, `created_at`
* **`brands`**: `id`, `name`, `slug`, `description`, `logo_url`, `logo_public_id`, `is_active`, `created_at`, `updated_at`
* **`categories`**: `id`, `parent_id`, `name`, `slug`, `description`, `display_order`, `is_active`, `created_at`, `updated_at`
* **`product_categories`**: `product_id`, `category_id`, `created_at`

---

## 3. 📦 Inventory & Suppliers (Kho Hàng & Nhà Cung Cấp)

### 📄 Bảng: `inventory_transactions` (Sổ kho bất biến)
| Các trường (Fields) | Ý nghĩa chính |
| :--- | :--- |
| `id`, `variant_id`, `created_by` | Định danh, biến thể và người thực hiện |
| `type`, `reference_type`, `reference_id` | Loại biến động (IN/OUT) và nguồn gốc (Đơn hàng/Phiếu nhập) |
| `quantity`, `old_stock`, `new_stock` | Số lượng biến động và tồn kho trước/sau |
| `note`, `created_at` | Ghi chú lý do và thời gian tạo |

### 📄 Các bảng nhập hàng và nhà cung cấp
* **`suppliers`**: `id`, `name`, `code`, `email`, `phone`, `contact_person`, `address`, `is_active`, `notes`, `created_at`, `updated_at`
* **`purchases`**: `id`, `supplier_id`, `created_by`, `received_by`, `purchase_code`, `supplier_invoice_no`, `subtotal`, `discount_amount`, `tax_amount`, `shipping_cost`, `total_cost`, `payment_status`, `status`, `notes`, `issues`, `created_at`, `confirmed_at`, `received_at`, `updated_at`
* **`purchase_items`**: `id`, `purchase_id`, `variant_id`, `quantity_ordered`, `quantity_received`, `unit_cost`, `line_total`, `quality_status`, `defective_qty`, `defect_reason`, `is_received`, `received_date`, `notes`, `created_at`, `updated_at`
* **`stock_adjustments`**: `id`, `created_by`, `code`, `type`, `status`, `reason`, `notes`, `created_at`, `confirmed_at`, `updated_at`
* **`stock_adjustment_items`**: `id`, `adjustment_id`, `variant_id`, `quantity_change`, `quantity_before`, `quantity_after`, `note`, `created_at`

---

## 4. 🛒 Orders & Shopping (Đơn Hàng & Mua Sắm)

### 📄 Bảng: `orders` (Thông tin tổng đơn hàng)
| Các trường (Fields) | Ý nghĩa chính |
| :--- | :--- |
| `id`, `user_id`, `guest_token` | Định danh và người mua (User hoặc Guest) |
| `code`, `status` | Mã đơn hàng và Trạng thái đơn |
| `payment_status`, `payment_method` | Trạng thái và phương thức thanh toán |
| `recipient_name`, `recipient_phone` | Tên và SĐT người nhận thực tế |
| `shipping_street`, `shipping_ward`, `shipping_district`, `shipping_province` | Địa chỉ giao hàng thực tế |
| `subtotal`, `shipping_fee`, `discount_amount`, `total_amount` | Các khoản tiền tệ |
| `coupon_code` | Mã giảm giá đã áp dụng |
| `note`, `admin_note`, `cancelled_reason`, `cancelled_by` | Ghi chú của khách, của admin và lý do hủy |
| `created_at`, `updated_at`, `cancelled_at`, `confirmed_at`, `estimated_delivery_date`, `delivered_at`, `expired_at` | Tracking các mốc thời gian |
| `tracking_number` | Mã vận đơn giao hàng |

### 📄 Bảng: `order_items` (Chi tiết đơn hàng - Snapshot)
| Các trường (Fields) | Ý nghĩa chính |
| :--- | :--- |
| `id`, `order_id`, `variant_id` | Định danh |
| `product_code`, `product_name` | Sao chép Tên và Mã sản phẩm tại thời điểm mua |
| `variant_sku`, `variant_size`, `variant_color` | Sao chép SKU, Size, Màu tại thời điểm mua |
| `quantity`, `unit_price`, `discount_amount`, `subtotal` | Số lượng, đơn giá và tổng tiền |
| `created_at` | Thời gian tạo |

### 📄 Các bảng thanh toán, giỏ hàng, khuyến mãi
* **`order_status_history`**: `id`, `order_id`, `changed_by`, `old_status`, `new_status`, `created_at`
* **`payments`**: `id`, `order_id`, `amount`, `status`, `transaction_id`, `response_code`, `response_message`, `paid_at`, `created_at`, `updated_at`
* **`carts`**: `id`, `user_id`, `guest_token`, `created_at`, `updated_at`
* **`cart_items`**: `id`, `cart_id`, `variant_id`, `quantity`, `created_at`, `updated_at`
* **`coupons`**: `id`, `code`, `description`, `discount_type`, `discount_value`, `min_order_amount`, `max_discount_amount`, `usage_limit`, `used_count`, `user_usage_limit`, `start_date`, `end_date`, `is_active`, `created_at`, `updated_at`
* **`coupon_usage`**: `id`, `coupon_id`, `user_id`, `order_id`, `email`, `phone`, `discount_amount`, `used_at`

---

## 5. 💬 User Engagement (Tương Tác Khách Hàng)

* **`product_reviews`**: `id`, `product_id`, `user_id`, `rating`, `comment`, `is_approved`, `created_at`, `updated_at`
* **`product_comments`**: `id`, `product_id`, `user_id`, `parent_id`, `content`, `is_approved`, `created_at`, `updated_at`
* **`wishlists`**: `id`, `user_id`, `product_id`, `created_at`
* **`notifications`**: `id`, `target_type`, `target_user_id`, `type`, `title`, `message`, `link`, `created_at`
* **`notification_recipients`**: `id`, `notification_id`, `user_id`, `is_read`, `read_at`, `created_at`