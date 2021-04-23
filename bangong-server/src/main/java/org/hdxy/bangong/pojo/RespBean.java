package org.hdxy.bangong.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回的json对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private long code;  //返回的状态码
    private String msg; //返回的消息
    private Object obj; //返回的对象

    /**
     * 返回成功的消息
     *
     * @param message 返回的成功信息
     */
    public static RespBean success(String message) {
        return new RespBean(200, message, null);
    }

    /**
     * 返回成功的消息的重载方法
     *
     * @param message 返回的成功信息
     * @param obj     返回携带的对象信息
     * @return
     */
    public static RespBean success(String message, Object obj) {
        return new RespBean(200, message, obj);
    }

    /**
     * @param message 返回的失败信息
     * @return
     */
    public static RespBean error(String message) {
        return new RespBean(500, message, null);
    }

    /**
     * 返回的失败信息的重载方法
     *
     * @param message 返回的失败信息
     * @param obj     返回携带的对象信息
     * @return
     */
    public static RespBean error(String message, Object obj) {
        return new RespBean(500, message, obj);
    }
}
