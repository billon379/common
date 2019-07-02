package fun.billon.common.model;

import fun.billon.common.util.HarverSine;
import lombok.Data;

import java.util.Map;

/**
 * 区域，距离指定中心点特定范围的矩形区域
 */
@Data
public class RegionModel {

    /**
     * 坐标点
     */
    @Data
    public static class Point {
        /**
         * 纬度
         */
        private double latitude;
        /**
         * 经度
         */
        private double longitude;

        /**
         * 构造方法
         *
         * @param latitude  纬度
         * @param longitude 经度
         */
        public Point(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    /**
     * 区域中心点(gps坐标)
     */
    private Point center;
    /**
     * 范围(单位km)
     */
    private double distance;

    /**
     * 距离该区域中心点指定范围的矩形区域
     */
    /**
     * 左上角
     */
    private Point leftTop;
    /**
     * 右上角
     */
    private Point rightTop;
    /**
     * 左下角
     */
    private Point leftBottom;
    /**
     * 右下角
     */
    private Point rightBottom;

    /**
     * 构造方法
     *
     * @param center   区域中心点(gps坐标)
     * @param distance 范围(单位km)
     */
    public RegionModel(Point center, double distance) {
        this.center = center;
        this.distance = distance;
        //获取距离中心点指定访问的矩形区域
        Map<String, double[]> map = HarverSine.region(center.getLatitude(), center.getLongitude(), distance);

        //设置矩形区域左上角
        double[] leftTopPoints = map.get("leftTopPoint");
        leftTop = new Point(leftTopPoints[0], leftTopPoints[1]);

        //设置矩形区域右上角
        double[] rightTopPoints = map.get("rightTopPoint");
        rightTop = new Point(rightTopPoints[0], rightTopPoints[1]);

        //设置矩形区域左下角
        double[] leftBottomPoints = map.get("leftBottomPoint");
        leftBottom = new Point(leftBottomPoints[0], leftBottomPoints[1]);

        //设置矩形区域右下角
        double[] rightBottomPoints = map.get("rightBottomPoint");
        rightBottom = new Point(rightBottomPoints[0], rightBottomPoints[1]);
    }

}