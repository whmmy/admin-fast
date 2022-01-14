package fun.whmy.adminfast.admin.generator;

import fun.whmy.adminfast.admin.model.constant.Role;

import java.util.Scanner;

public class AuthResSqlGenerator {

    public static void main(String[] args) {
        //创建Scanner对象，接受从控制台输入
        Scanner input = new Scanner(System.in);

        System.out.println("请输入资源表ums_resource 当前最大ID:");
        //接受String类型
        int maxB = input.nextInt();


        System.out.println("请在数据库执行以下生成脚本");

        for (Role role : Role.values()) {
            if (role.getId() == null) continue;
            if (role.getId() <= maxB) continue;
            System.out.println(String.format("INSERT INTO `tb_user_resource`(`id`, `name`,`code`,`description`,`category_id`,`type`,`sort`) " +
                    "VALUES (%s,  '%s',  '%s',  '%s',  %s,  %s, %s);", role.getId(), role.getName(), role.getCode(), role.getDescription(), role.getCategoryId(), role.getType(), role.getSort()));
        }


    }
}
