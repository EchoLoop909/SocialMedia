package com.example.socialmedia.Exception;

import com.example.socialmedia.Model.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //thuoc tinh dai dien cho ten thm so toi thieu trong validation
    private static final String MIN_ATTRIBUTE = "min";

    //xu ly ngoai len chung khong duoc xu ly boi cac handel cu the
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception){
        log.error("Loi khong xac dinh", exception);
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode()).body(apiResponse);
    }

    //xu ly ngoai le voi cac Exception chua ma loi
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handldingAppException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();

        log.error("Loi khong xac dinh", errorCode);

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    //Xu ly ngoai le khio gia tri dau vao khong hop le
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        //lay thong diep loi thanh ErrorCode
        String enumKey = exception.getFieldError() != null ? exception.getFieldError().getField() : null;
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String,Object> attributes = null;

        if(enumKey != null){
            try{
                errorCode = ErrorCode.valueOf(enumKey);

                //lay cac thuoc tinh validation(nhu min, max)
                var constraintViolation = exception.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);
                attributes = constraintViolation.getConstraintDescriptor().getAttributes();
                log.info("Thuộc tính validation: {}", attributes);
            }catch (Exception e){
                log.warn("Không thể ánh xạ mã lỗi hoặc thuộc tính validation: {}", enumKey,e);
            }
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        // Thay thế placeholder trong thông điệp lỗi nếu có attributes
        apiResponse.setMessage(
                attributes != null
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    //xu ly ngoai len khi nguoi dung khong co quyen truy cap
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        // Ghi log lỗi để debug
        log.error("Lỗi truy cập: {}", exception.getMessage(), exception);
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNAUTHORIZED.getCode());
        apiResponse.setMessage(ErrorCode.UNAUTHORIZED.getMessage());

        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatusCode()).body(apiResponse);
    }

    // Thay thế placeholder trong thông điệp lỗi bằng giá trị từ attributes.
    private String mapAttribute(String message, Map<String, Object> attributes) {
        // Kiểm tra xem thuộc tính MIN_ATTRIBUTE có tồn tại không
        if (attributes.containsKey(MIN_ATTRIBUTE)) {
            String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
            return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
        }
        return message;
    }
}
