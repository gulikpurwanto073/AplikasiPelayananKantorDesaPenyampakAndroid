package com.sona.ktp.util.api;

public class UtilsApi {
    public static final String IP = "http://192.168.43.196/ktp/"; //THETERING IPHONE
//    public static final String IP = "http://192.168.43.196/ktp/"; //THETERING IPHONE
//    public static final String IP = "http://172.20.10.10/ktp/"; //THETERING IPHONE AYU
//    public static final String IP = "http://10.176.96.39/ktp/"; //WIFI WARTECH ASIA
//    public static final String IP = "http://10.10.7.48/ktp/"; //WIFI KANTOR WK

    public static final String BASE_URL_API = IP+"api/";
    public static final String BASE_URL_FILE_USER = IP+"assets/upload/user/";
    public static final String BASE_URL_FILE_LAMPIRAN = IP+"assets/upload/lampiran/";

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
