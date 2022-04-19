package fun.whmy.adminfast.admin;

import cn.dev33.satoken.SaManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan("fun.whmy.adminfast.admin")
@SpringBootApplication
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
    }

}
