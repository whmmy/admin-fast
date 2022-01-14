package fun.whmy.adminfast.admin.model.constant;

public class ErrorCode {
    //普通错误
    public static final int ERROR = 1;


    //用户相关错误 1xxx
    public static final int USER_NOT_ONLICE = 1000;
    public static final int USER_NOT_PERMISSION = 1001;


    //会员相关错误
    public static final int MEMBER_BIND_CODE_ERROR = 2001;
    public static final int MEMBER_BIND_PHONE_ERROR = 2002;

    //生产二维码错误
    public static final int CREATE_QRCODE_ERROR = 3001;


}
