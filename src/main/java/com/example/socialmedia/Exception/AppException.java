package com.example.socialmedia.Exception;

public class AppException extends RuntimeException {
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    //Lay ma loi lien ket voi ngoai le nay
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    //thiet lap ma loi cho ngoai le nay
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
