package fun.billon.common.util;

/**
 * 各地图API坐标系统比较与转换:
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
 * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系
 * 。谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系;
 * BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class CoordsConverterUtils {

    private static final double PI = 3.14159265358979324;
    private static final double RADIUS = 6378245.0;
    private static final double EE = 0.00669342162296594323;
    private static final double X_PI = 3.14159265358979324 * 3000.0 / 180.0;

    /**
     * 中国经纬度范围
     */
    private static final double CHINA_LON_LEFT = 72.004;
    private static final double CHINA_LON_RIGHT = 137.8347;
    private static final double CHINA_LAT_LEFT = 0.8293;
    private static final double CHINA_LAT_RIGHT = 55.8271;

    public static boolean outOfChina(double lat, double lon) {
        if (lon < CHINA_LON_LEFT || lon > CHINA_LON_RIGHT) {
            return true;
        }
        if (lat < CHINA_LAT_LEFT || lat > CHINA_LAT_RIGHT) {
            return true;
        }
        return false;
    }

    public static double[] wgs2bd(double lat, double lon) {
        double[] wgs2gcj = wgs2gcj(lat, lon);
        double[] gcj2bd = gcj2bd(wgs2gcj[0], wgs2gcj[1]);
        return gcj2bd;
    }

    public static double[] bd2wgs(double bdLat, double bdLon) {
        double[] bd2gcj = bd2gcj(bdLat, bdLon);
        double[] gcj2wgs = gcj2wgs(bd2gcj[0], bd2gcj[1]);
        return gcj2wgs;
    }

    public static double[] gcj2bd(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
        double bdLon = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new double[]{bdLat, bdLon};
    }

    public static double[] bd2gcj(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        double ggLon = z * Math.cos(theta);
        double ggLat = z * Math.sin(theta);
        return new double[]{ggLat, ggLon};
    }

    public static double[] wgs2gcj(double lat, double lon) {
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((RADIUS * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (RADIUS / sqrtMagic * Math.cos(radLat) * PI);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        double[] loc = {mgLat, mgLon};
        return loc;
    }

    public static double[] gcj2wgs(double lat, double lon) {
        double[] gps = transform(lat, lon);
        double latitude = lat * 2 - gps[0];
        double lontitude = lon * 2 - gps[1];
        return new double[]{latitude, lontitude};
    }

    public static double[] transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((RADIUS * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (RADIUS / sqrtMagic * Math.cos(radLat) * PI);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    private static double transformLat(double lat, double lon) {
        double ret = -100.0 + 2.0 * lat + 3.0 * lon + 0.2 * lon * lon + 0.1
                * lat * lon + 0.2 * Math.sqrt(Math.abs(lat));
        ret += (20.0 * Math.sin(6.0 * lat * PI) + 20.0 * Math.sin(2.0 * lat
                * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lon * PI) + 40.0 * Math.sin(lon / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lon / 12.0 * PI) + 320 * Math.sin(lon * PI
                / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double lat, double lon) {
        double ret = 300.0 + lat + 2.0 * lon + 0.1 * lat * lat + 0.1 * lat
                * lon + 0.1 * Math.sqrt(Math.abs(lat));
        ret += (20.0 * Math.sin(6.0 * lat * PI) + 20.0 * Math.sin(2.0 * lat
                * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lat / 12.0 * PI) + 300.0 * Math.sin(lat / 30.0
                * PI)) * 2.0 / 3.0;
        return ret;
    }

}
