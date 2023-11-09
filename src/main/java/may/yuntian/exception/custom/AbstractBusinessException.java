package may.yuntian.exception.custom;

/**
 * @Description 自定义异常父类
 * @Date 2023/4/18 14:22
 * @Author maoly
 **/
/**这个地方不要写exception，因为Spring是只对运行时异常进行事务回滚，
    //如果抛出的是exception是不会进行事务回滚的。*/
public abstract class AbstractBusinessException extends RuntimeException{
    private static final long serialVersionUID = 8684333269210029273L;

    /**
     * 返回给客户端的提示信息
     */
    private final String tips;

    public AbstractBusinessException(String tips) {
        super(tips);
        this.tips = tips;
    }

    public AbstractBusinessException(String tips, String message) {
        super(message);
        this.tips = tips;
    }

    public String getTips() {
        return tips;
    }
}
