package fun.whmy.adminfast.admin.model.basic;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultBean {
    private int code;
    private String msg;
    private Map<String, Object> value;

    public ResultBean() {
        this.code = 0;
    }

    public ResultBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultBean(int code) {
        this.code = code;
    }

    public void addEntry(String key, Object object) {
        if (value == null) {
            this.value = new HashMap<>();
        }
        this.value.put(key, object);
    }
}
