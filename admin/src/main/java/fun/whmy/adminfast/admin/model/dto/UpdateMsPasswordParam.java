package fun.whmy.adminfast.admin.model.dto;

import lombok.Data;

@Data
public class UpdateMsPasswordParam {
    private String username;
    private String oldPassword;
    private String newPassword;
}
