package cn.lemonit.lemix.mixmodule;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 调用startUpMixModule方法时需要传递的参数
 */
public class StartUpMixModuleParameter implements Parcelable {

    private String packageKey;
    private String startPager;
    private String json;
    private String moduleKey;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageKey);
        dest.writeString(this.startPager);
        dest.writeString(this.json);
        dest.writeString(this.moduleKey);
    }

    public StartUpMixModuleParameter() {
    }

    protected StartUpMixModuleParameter(Parcel in) {
        this.packageKey = in.readString();
        this.startPager = in.readString();
        this.json = in.readString();
        this.moduleKey = in.readString();
    }

    public static final Creator<StartUpMixModuleParameter> CREATOR = new Creator<StartUpMixModuleParameter>() {
        @Override
        public StartUpMixModuleParameter createFromParcel(Parcel source) {
            return new StartUpMixModuleParameter(source);
        }

        @Override
        public StartUpMixModuleParameter[] newArray(int size) {
            return new StartUpMixModuleParameter[size];
        }
    };

    public String getPackageKey() {
        return packageKey;
    }

    public StartUpMixModuleParameter setPackageKey(String packageKey) {
        this.packageKey = packageKey;
        return this;
    }

    public String getStartPager() {
        return startPager;
    }

    public StartUpMixModuleParameter setStartPager(String startPager) {
        this.startPager = startPager;
        return this;
    }

    public String getJson() {
        return json;
    }

    public StartUpMixModuleParameter setJson(String json) {
        this.json = json;
        return this;
    }

    public String getModuleKey() {
        return moduleKey;
    }

    public StartUpMixModuleParameter setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
        return this;
    }

}
