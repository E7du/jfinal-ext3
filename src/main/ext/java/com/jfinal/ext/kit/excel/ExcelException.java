package com.jfinal.ext.kit.excel;

public class ExcelException extends RuntimeException {

	private static final long serialVersionUID = -3631194189331161688L;

	public ExcelException(String message) {
		super(message);
	}

    public ExcelException(Throwable cause) {
        super(cause);
    }


}
