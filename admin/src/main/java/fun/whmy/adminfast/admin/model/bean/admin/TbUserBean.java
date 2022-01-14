package fun.whmy.adminfast.admin.model.bean.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
public class TbUserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "USER_ID", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 密码
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 电话
     */
    @TableField("PHONE")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 用户角色（1：系统管理员；2：普通用户）
     */
    @TableField("ROLE")
    private Integer role;

    /**
     * 用户token
     */
    @TableField("TOKEN")
    private String token;

    @TableField("COMPANY_ID")
    private long companyId;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 是否删除（0：未删除；1：已删除）
     */
    @TableField("IS_DELETED")
    private Integer isDeleted;

    /**
     * 创建者ID
     */
    @TableField("CREATE_ID")
    private Long createId;

    /**
     * 更新者ID
     */
    @TableField("UPDATE_ID")
    private Long updateId;

    /**
     * 注册时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;


}
