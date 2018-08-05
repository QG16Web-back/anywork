package com.qg.anywork.exception.question;

/**
 * @author FunriLy
 * @date 2017/7/13
 * From small beginnings comes great things.
 */
public class ExcelReadException extends RuntimeException {

    public ExcelReadException(String message) {
        super(message);
    }

    public ExcelReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
