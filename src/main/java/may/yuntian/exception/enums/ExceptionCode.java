package may.yuntian.exception.enums;

/**
 * @author maoly
 */

public enum ExceptionCode {

    /**
     * 未知错误
     */
    COMMON_ERROR(500,"未知错误"),

    /**
     * 未登录
     */
    NEVER_LOGIN(10001,"未登录"),


    /**
     * token校验异常
     */
    TOKEN_CHECK(10003,"token校验异常"),

    REPEAT_SUBMIT(20001,"重复提交")
    ;

    private int code;

    private String name;

    ExceptionCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }
    public String getName(int code){
        return name;
    }
}
