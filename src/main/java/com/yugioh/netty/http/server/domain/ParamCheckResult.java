package com.yugioh.netty.http.server.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Create By lieber
 * @Description 参数校验结果
 * @Date Create in 2018/6/8 10:46
 * @Modify By
 */
@Data
public class ParamCheckResult implements Serializable {

    /**
     * 校验是否成功
     */
    private boolean success;
    /**
     * 错误信息
     */
    private String message;

    public boolean getSuccess() {
        return success;
    }

    public ParamCheckResult() {
    }

    public ParamCheckResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
