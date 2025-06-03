package com.example.socialmedia.Exception;


import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Lỗi chung, không xác định
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    // Lỗi khi dữ liệu đầu vào không hợp lệ
    INVALID_KEY(1001, "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    // Lỗi khi người dùng đã tồn tại
    USER_EXISTED(1002, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    // Lỗi khi tên người dùng không đạt độ dài tối thiểu
    USERNAME_INVALID(1003, "Tên người dùng phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    // Lỗi khi mật khẩu không đạt độ dài tối thiểu
    INVALID_PASSWORD(1004, "Mật khẩu phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    // Lỗi khi người dùng không tồn tại
    USER_NOT_EXISTED(1005, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    // Lỗi khi chưa xác thực
    UNAUTHENTICATED(1006, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    // Lỗi khi không có quyền truy cập
    UNAUTHORIZED(1007, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    // Lỗi khi ngày sinh không hợp lệ
    INVALID_DOB(1008, "Tuổi phải ít nhất {min}", HttpStatus.BAD_REQUEST),
    // Lỗi khi ID không được tìm thấy
    ID_NOTFOUND(1009, "ID không được tìm thấy", HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatus statusCode;
}
