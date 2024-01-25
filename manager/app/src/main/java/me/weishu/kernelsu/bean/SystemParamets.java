package me.weishu.kernelsu.bean;

public class SystemParamets {
    private String deviceFactor;
    private String IMEI1;

    private String IMEI2;

    private String androidID;

    private String androidVer;//33
    private String androidSdk;//13

    private String appDPI;

    private String appInstallTime;

    private String appVersion;

    private String bluetoothMac;

    private String bluetoothName;

    private String board;

    private String device;

    private String deviceName;

    private String hardware;

    private String ip;

    private String model;

    private String phoneNumber;

    private String radioVersion;

    private String resolution;

    private String serial;

    private String wifiBSSID;

    private String wifiMac;

    private String wifiName;

    //及基带
    private String baseBand;


    private String simType;
    private String lunage;
    private String uiName;
    private String builderUser;
    private String manufacturer;
    private String displayId;
    private String productName;
    private String brand;


    public String getAndroidSdk() {
        return androidSdk;
    }

    public void setAndroidSdk(String androidSdk) {
        this.androidSdk = androidSdk;
    }

    public String getDeviceFactor() {
        return deviceFactor;
    }

    public void setDeviceFactor(String deviceFactor) {
        this.deviceFactor = deviceFactor;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBuilderUser() {
        return builderUser;
    }

    public void setBuilderUser(String builderUser) {
        this.builderUser = builderUser;
    }

    public String getUiName() {
        return uiName;
    }

    public void setUiName(String uiName) {
        this.uiName = uiName;
    }

    public String getLunage() {
        return lunage;
    }

    public void setLunage(String lunage) {
        this.lunage = lunage;
    }

    public String getSimType() {
        return simType;
    }

    public void setSimType(String simType) {
        this.simType = simType;
    }

    public String getBaseBand() {
        return baseBand;
    }

    public void setBaseBand(String baseBand) {
        this.baseBand = baseBand;
    }

    public String getAndroidID() {
        return this.androidID;
    }

    public String getAndroidVer() {
        return this.androidVer;
    }

    public String getAppDPI() {
        return this.appDPI;
    }

    public String getAppInstallTime() {
        return this.appInstallTime;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public String getBluetoothMac() {
        return this.bluetoothMac;
    }

    public String getBluetoothName() {
        return this.bluetoothName;
    }

    public String getBoard() {
        return this.board;
    }

    public String getDevice() {
        return this.device;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getHardware() {
        return this.hardware;
    }

    public String getIMEI1() {
        return this.IMEI1;
    }

    public String getIMEI2() {
        return this.IMEI2;
    }

    public String getIp() {
        return this.ip;
    }

    public String getModel() {
        return this.model;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getRadioVersion() {
        return this.radioVersion;
    }

    public String getResolution() {
        return this.resolution;
    }

    public String getSerial() {
        return this.serial;
    }

    public String getWifiBSSID() {
        return this.wifiBSSID;
    }

    public String getWifiMac() {
        return this.wifiMac;
    }

    public String getWifiName() {
        return this.wifiName;
    }

    public void setAndroidID(String paramString) {
        this.androidID = paramString;
    }

    public void setAndroidVer(String paramString) {
        this.androidVer = paramString;
    }

    public void setAppDPI(String paramString) {
        this.appDPI = paramString;
    }

    public void setAppInstallTime(String paramString) {
        this.appInstallTime = paramString;
    }

    public void setAppVersion(String paramString) {
        this.appVersion = paramString;
    }

    public void setBluetoothMac(String paramString) {
        this.bluetoothMac = paramString;
    }

    public void setBluetoothName(String paramString) {
        this.bluetoothName = paramString;
    }

    public void setBoard(String paramString) {
        this.board = paramString;
    }

    public void setDevice(String paramString) {
        this.device = paramString;
    }

    public void setDeviceName(String paramString) {
        this.deviceName = paramString;
    }

    public void setHardware(String paramString) {
        this.hardware = paramString;
    }

    public void setIMEI1(String paramString) {
        this.IMEI1 = paramString;
    }

    public void setIMEI2(String paramString) {
        this.IMEI2 = paramString;
    }

    public void setIp(String paramString) {
        this.ip = paramString;
    }

    public void setModel(String paramString) {
        this.model = paramString;
    }

    public void setPhoneNumber(String paramString) {
        this.phoneNumber = paramString;
    }

    public void setRadioVersion(String paramString) {
        this.radioVersion = paramString;
    }

    public void setResolution(String paramString) {
        this.resolution = paramString;
    }

    public void setSerial(String paramString) {
        this.serial = paramString;
    }

    public void setWifiBSSID(String paramString) {
        this.wifiBSSID = paramString;
    }

    public void setWifiMac(String paramString) {
        this.wifiMac = paramString;
    }

    public void setWifiName(String paramString) {
        this.wifiName = paramString;
    }

    @Override
    public String toString() {
        return "{" +
                "deviceFactor='" + deviceFactor + '\'' +
                ", IMEI1='" + IMEI1 + '\'' +
                ", IMEI2='" + IMEI2 + '\'' +
                ", androidID='" + androidID + '\'' +
                ", androidVer='" + androidVer + '\'' +
                ", androidSdk='" + androidSdk + '\'' +
                ", appDPI='" + appDPI + '\'' +
                ", appInstallTime='" + appInstallTime + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", bluetoothMac='" + bluetoothMac + '\'' +
                ", bluetoothName='" + bluetoothName + '\'' +
                ", board='" + board + '\'' +
                ", device='" + device + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", hardware='" + hardware + '\'' +
                ", ip='" + ip + '\'' +
                ", model='" + model + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", radioVersion='" + radioVersion + '\'' +
                ", resolution='" + resolution + '\'' +
                ", serial='" + serial + '\'' +
                ", wifiBSSID='" + wifiBSSID + '\'' +
                ", wifiMac='" + wifiMac + '\'' +
                ", wifiName='" + wifiName + '\'' +
                ", baseBand='" + baseBand + '\'' +
                ", simType='" + simType + '\'' +
                ", lunage='" + lunage + '\'' +
                ", uiName='" + uiName + '\'' +
                ", builderUser='" + builderUser + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", displayId='" + displayId + '\'' +
                ", productName='" + productName + '\'' +
                ", brand='" + brand + '\'' +
                '}';
    }
}
