package may.yuntian.external.oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mi
 */
@Data
@TableName("ces")
public class IpEntity implements Serializable {

    private static final long serialVersionUID = 42L;

    /**
     * id
     */
    @TableId(type = IdType.INPUT)
    private Integer id;

    /**
     * ip
     */
    private String ip;

}
