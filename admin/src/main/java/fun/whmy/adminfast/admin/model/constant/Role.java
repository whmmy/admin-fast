package fun.whmy.adminfast.admin.model.constant;

public enum Role {
    SYSTEM_MANAGER(1L, "系统管理", "systemManage", "系统管理", 0L, 1, 0),
    SYSTEM_MANAGER_USER(2L, "用户管理", "User", "用户管理", 1L, 2, 0),
    SYSTEM_MANAGER_USER_EDIT(3L, "用户管理_更新权限", "", "用户管理_更新权限", 2L, 3, 0),
    SYSTEM_MANAGER_RESOURCE(4L, "资源管理", "resourceManager", "资源管理", 1L, 2, 0),
    SYSTEM_MANAGER_ROLE(5L, "角色管理", "roleManager", "角色管理", 1L, 2, 0),
    SYSTEM_MANAGER_COMPANY(5L, "公司管理", "managerCompany", "公司管理", 1L, 2, 0),
    ;
    public Long id; //对应数据库id 需手动自增输入
    public String name; //资源名称
    public String code; // 对映route 中的name字段 仅 type 为1，2的需要设置设置
    public String description; //资源描述
    public Long categoryId; //上级资源名称 type为1的时候填0
    public int type; // type 为 1 则表示模块 如系统管理 2 为具体菜单/接口权限 3为菜单内的按钮权限 0则是保留为任何用户都可访问（但需要带有有效token）
    public int sort; //排序 0-99

    Role() {
    }

    Role(Long id, String name, String code, String description, Long categoryId, int type, int sort) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.categoryId = categoryId;
        this.type = type;
        this.sort = sort;
    }

    public int getRole() {
        return id.intValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

}
