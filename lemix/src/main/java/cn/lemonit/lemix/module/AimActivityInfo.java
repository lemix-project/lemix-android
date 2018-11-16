package cn.lemonit.lemix.module;

/**
 * aim 表示地址
 * type 表示是绝对路径，还是相对路径，还是原生，还是插件模块
 */
public class AimActivityInfo {

    private String type;
    private String aim;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAim() {
        return aim;
    }

    public void setAim(String aim) {
        this.aim = aim;
    }
}
