package cn.lemonit.lemix.mixmodule;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 插件信息
 */
public class MixModuleInfo implements Parcelable {
    /**
     * 插件下载路径
     */
    private String mixModuleURL;
    /**
     * 插件名称
     */
    private String moduleName;
    /**
     * 插件图片路径
     */
    private String imageURL;
    /**
     * 插件识别码
     */
    private String mixModuleIdentifier;
    /**
     * 版本识别
     */
    private String packageTime;

    public String getMixModuleURL() {
        return mixModuleURL;
    }

    public MixModuleInfo setMixModuleURL(String mixModuleURL) {
        this.mixModuleURL = mixModuleURL;
        return this;
    }

    public String getModuleName() {
        return moduleName;
    }

    public MixModuleInfo setModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public String getImageURL() {
        return imageURL;
    }

    public MixModuleInfo setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public String getMixModuleIdentifier() {
        return mixModuleIdentifier;
    }

    public MixModuleInfo setMixModuleIdentifier(String mixModuleIdentifier) {
        this.mixModuleIdentifier = mixModuleIdentifier;
        return this;
    }

    public String getPackageTime() {
        return packageTime;
    }

    public MixModuleInfo setPackageTime(String packageTime) {
        this.packageTime = packageTime;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mixModuleURL);
        dest.writeString(this.moduleName);
        dest.writeString(this.imageURL);
        dest.writeString(this.mixModuleIdentifier);
        dest.writeString(this.packageTime);
    }

    public MixModuleInfo() {
    }

    protected MixModuleInfo(Parcel in) {
        this.mixModuleURL = in.readString();
        this.moduleName = in.readString();
        this.imageURL = in.readString();
        this.mixModuleIdentifier = in.readString();
        this.packageTime = in.readString();
    }

    public static final Parcelable.Creator<MixModuleInfo> CREATOR = new Parcelable.Creator<MixModuleInfo>() {
        @Override
        public MixModuleInfo createFromParcel(Parcel source) {
            return new MixModuleInfo(source);
        }

        @Override
        public MixModuleInfo[] newArray(int size) {
            return new MixModuleInfo[size];
        }
    };
}
