# AI CODING GUIDELINES: CLOTHES STORE API

Bạn đang đóng vai trò là một Senior Java/Spring Boot Developer. Trước khi viết hoặc refactor bất kỳ đoạn code nào trong dự án này, bạn **BẮT BUỘC** phải tuân thủ các quy tắc dưới đây.

## 1. Cấu trúc dự án (Architecture & Packaging)
Dự án được tổ chức theo module (VD: `catalog`, `inventory`). Trong mỗi module sẽ có đầy đủ các layer: `controller`, `service`, `repository`, `entity`, `dto`, `mapper`.
- Khi tạo mới một tính năng, phải đặt đúng file vào đúng module và đúng layer.
- DTO được chia thành 2 thư mục rõ ràng: `request` (cho input) và `response` (cho output). Không dùng chung một DTO cho cả hai chiều.

## 2. Quy chuẩn Mapping Dữ liệu (Tuyệt đối tuân thủ)
Dự án có sử dụng các class Mapper riêng trong package `mapper` (ví dụ: `BrandMapper`, `CategoryMapper`, `ProductMapper`).
- **KHÔNG ĐƯỢC** map dữ liệu thủ công (dùng `getter`/`setter` hoặc `Builder`) trong Service layer khi chuyển đổi giữa `Entity` và `DTO`.
- **BẮT BUỘC** inject và sử dụng các Mapper đã có sẵn để thực hiện việc này.

## 3. Quy chuẩn Xử lý Ngoại lệ (Exception Handling)
Dự án sử dụng `GlobalExceptionHandler` và các Custom Exception trong package `vn.fernirx.clothes.common.exception`.
- **Tuyệt đối không** ném `RuntimeException` chung chung.
- **`ResourceNotFoundException`**: Constructor chỉ nhận tên resource. Class này đã tự động format chuỗi `"%s not found"`. KHÔNG ĐƯỢC nối chuỗi ID (VD: KHÔNG dùng `new ResourceNotFoundException("Product with id " + id)`).
- Sử dụng đúng các Exception có sẵn khi cần thiết: `ResourceAlreadyExistsException`, `ResourceInUseException`, `TokenException`.

## 4. Định dạng API Trả về (Response Wrapper)
Mọi API trả về từ `Controller` đều phải được bọc trong các class Response chuẩn của dự án nằm tại `vn.fernirx.clothes.common.response`:
- Trả về dữ liệu thành công bình thường: Sử dụng `SuccessResponse<T>`.
- Trả về dữ liệu phân trang: Sử dụng `PageResponse<T>`.
- **Không bao giờ** trả về trực tiếp Entity hoặc DTO trần (raw object) từ Controller.

## 5. Phân trang và Constants
- Khi cần lấy các hằng số (constants) cho phân trang, bảo mật (security) hoặc validate, bắt buộc ưu tiên tìm trong các file `PaginationConstants`, `SecurityConstants`, `ValidationConstants` thuộc package `vn.fernirx.clothes.common.constant` thay vì hardcode chuỗi hoặc số trực tiếp trong code.
- Sử dụng `PaginationUtil` trong `vn.fernirx.clothes.common.util` cho các logic xử lý liên quan đến Pageable nếu cần.

## 6. Lớp Entity & Database
- Kế thừa `BaseEntity` (nằm trong `vn.fernirx.clothes.common.entity`) cho các Entity mới nếu chúng có chung các trường audit cơ bản (như createdAt, updatedAt...).