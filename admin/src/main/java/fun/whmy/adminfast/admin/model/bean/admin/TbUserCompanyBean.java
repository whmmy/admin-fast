package fun.whmy.adminfast.admin.model.bean.admin;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_user_company")
public class TbUserCompanyBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    private String name;

    private String address;

    private String contactName;

    private String contactPhone;

    /**
     * 保留字段
     */
    private Integer type;

    private Date createTime;

    private Integer isdelete;

    private String remark;

    /**
     * 鉴权token
     */
    private String token;


}
