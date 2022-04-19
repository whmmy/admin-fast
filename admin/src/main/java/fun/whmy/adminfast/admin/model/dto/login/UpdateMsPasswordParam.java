package fun.whmy.adminfast.admin.model.dto.login;

import lombok.Data;

@Data
public class UpdateMsPasswordParam {
    private String username;
    private String oldPassword;
    private String newPassword;
}
