package fun.billon.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * haversine公式计算球面两点间的距离。
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class HarverSine {

    /**
     * 地球半径平均值，千米
     */
    public static final double EARTH_RADIUS = 6371.0;

    public static double haverSine(double theta) {
        double v = Math.sin(theta / 2);
        return v * v;
    }

    /**
     * 用haversine公式计算球面两点间的距离。 给定的经度1，纬度1；经度2，纬度2. 计算2个经纬度之间的距离。
     *
     * @param lat1 纬度1
     * @param lng1 经度1
     * @param lat2 纬度2
     * @param lng2 经度2
     * @return 距离（公里、千米）
     */
    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        // 经纬度转换成弧度
        lat1 = convertDegreesToRadians(lat1);
        lng1 = convertDegreesToRadians(lng1);
        lat2 = convertDegreesToRadians(lat2);
        lng2 = convertDegreesToRadians(lng2);
        // 差值
        double vLon = Math.abs(lng1 - lng2);
        double vLat = Math.abs(lat1 - lat2);
        // circle就是一个球体上的切面，它的圆心即是球心的一个周长最大的圆
        double h = haverSine(vLat) + Math.cos(lat1) * Math.cos(lat2) * haverSine(vLon);
        double distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(h));
        return distance;
    }

    /**
     * 将角度换算为弧度。
     *
     * @param degrees 角度
     * @return 弧度
     */
    public static double convertDegreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    public static double convertRadiansToDegrees(double radian) {
        return radian * 180.0 / Math.PI;
    }

    /**
     * 计算以指定经纬度为中心，特定范围的矩形区域，返回矩形区域的四个点的坐标
     *
     * @param lat      纬度
     * @param lng      经度
     * @param distance 距离
     * @return 计算以指定经纬度为中心，特定范围的矩形区域，返回矩形区域的四个点的坐标
     */
    public static Map<String, double[]> region(double lat, double lng, double distance) {
        Map<String, double[]> squareMap = new HashMap<>(4);
        // 计算经度弧度,从弧度转换为角度
        double dLng = 2 * (Math.asin(Math.sin(distance / (2 * EARTH_RADIUS)) / Math.cos(Math.toRadians(lat))));
        dLng = Math.toDegrees(dLng);
        // 计算纬度角度
        double dLat = distance / EARTH_RADIUS;
        dLat = Math.toDegrees(dLat);
        // 正方形
        double[] leftTopPoint = {lat + dLat, lng - dLng};
        double[] rightTopPoint = {lat + dLat, lng + dLng};
        double[] leftBottomPoint = {lat - dLat, lng - dLng};
        double[] rightBottomPoint = {lat - dLat, lng + dLng};
        squareMap.put("leftTopPoint", leftTopPoint);
        squareMap.put("rightTopPoint", rightTopPoint);
        squareMap.put("leftBottomPoint", leftBottomPoint);
        squareMap.put("rightBottomPoint", rightBottomPoint);
        return squareMap;
    }

}
