package fun.whmy.adminfast.admin.model.bean.admin;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_user")
public class TbUserBeanExtend extends TbUserBean {

    private String companyName;


}
