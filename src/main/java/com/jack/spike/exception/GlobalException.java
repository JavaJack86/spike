package com.jack.spike.exception;


import com.jack.spike.result.CodeMsg;
/**
 * @Author Jack
 * @Date 2019/4/26 9:54
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }

}
