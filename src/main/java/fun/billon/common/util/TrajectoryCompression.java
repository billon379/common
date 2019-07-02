package fun.billon.common.util;

import lombok.Data;

import java.util.*;

/**
 * gps点压缩算法
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class TrajectoryCompression {

    /**
     * 压缩使用的点
     */
    @Data
    public static class Point implements Comparable<Point> {

        /**
         * 纬度(gps坐标)
         */
        private double lat;
        /**
         * 经度(gps坐标)
         */
        private double lng;
        /**
         * gps时间
         */
        private Date gpsTime;

        /**
         * 构造方法
         *
         * @param lat 纬度(gps坐标)
         * @param lng 精度(gps坐标)
         */
        public Point(double lat, double lng, Date gpsTime) {
            this.lat = lat;
            this.lng = lng;
            this.gpsTime = gpsTime;
        }

        @Override
        public int compareTo(Point point) {
            return this.getGpsTime().compareTo(point.getGpsTime());
        }
    }

    /**
     * gps点压缩
     *
     * @param points  要压缩的点
     * @param maxSize 最终压缩后的点的个数
     * @return
     * @throws Exception
     */
    public static List<Point> compress(List<Point> points, int maxSize) throws Exception {
        /*
         * 1.相关ArrayList数组和File对象的声明和定义
         */
        // 原纪录经纬度坐标数组
        List<Point> pGPSArrayInit = points;
        // 过滤后的经纬度坐标数组
        List<Point> pGPSArrayFilter = new ArrayList<>();
        // 过滤并排序后的经纬度坐标数组
        List<Point> pGPSArrayFilterSort = new ArrayList<>();
        // 输出原始经纬度点坐标的个数
        System.out.println(pGPSArrayInit.size());

        /*
         * 2.进行轨迹压缩
         */
        // 设定最大距离误差阈值
        double dMax = 1.0;
        // 获取第一个原始经纬度点坐标并添加到过滤后的数组中
        pGPSArrayFilter.add(pGPSArrayInit.get(0));
        // 获取最后一个原始经纬度点坐标并添加到过滤后的数组中
        pGPSArrayFilter.add(pGPSArrayInit.get(pGPSArrayInit.size() - 1));
        // 使用一个点数组接收所有的点坐标，用于后面的压缩
        Point[] enpInit = new Point[pGPSArrayInit.size()];
        Iterator<Point> iInit = pGPSArrayInit.iterator();
        int jj = 0;
        // 将ArrayList中的点坐标拷贝到点数组中
        while (iInit.hasNext()) {
            enpInit[jj] = iInit.next();
            jj++;
        }
        // 起始下标
        int start = 0;
        // 结束下标
        int end = pGPSArrayInit.size() - 1;
        // DP压缩算法
        trajCompress(enpInit, pGPSArrayFilter, start, end, dMax, maxSize);
        // 输出压缩后的点数
        System.out.println(pGPSArrayFilter.size());

        /*
         * 3.对压缩后的经纬度点坐标数据按照ID从小到大排序
         */
        // 使用一个点数组接收过滤后的点坐标，用于后面的排序
        Point[] enpFilter = new Point[pGPSArrayFilter.size()];
        Iterator<Point> iF = pGPSArrayFilter.iterator();
        int i = 0;
        // 将ArrayList中的点坐标拷贝到点数组中
        while (iF.hasNext()) {
            enpFilter[i] = iF.next();
            i++;
        }
        // 进行排序
        Arrays.sort(enpFilter);
        // 将排序后的点坐标写到一个新的ArrayList数组中
        for (int j = 0; j < enpFilter.length; j++) {
            pGPSArrayFilterSort.add(enpFilter[j]);
        }

        /*
         * 4.求压缩率
         */
        // 求压缩率
        double cRate = (double) pGPSArrayFilter.size() / pGPSArrayInit.size() * 100;
        System.out.println(cRate);

        /*
         * 5.生成最终结果文件,将最终结果写入结果文件中，包括过滤后的点的ID，点的个数、平均误差和压缩率
         */
        return pGPSArrayFilterSort;
    }

    /**
     * 函数功能：使用三角形面积（使用海伦公式求得）相等方法计算点pX到点pA和pB所确定的直线的距离
     *
     * @param pA ：起始点
     * @param pB ：结束点
     * @param pX ：第三个点
     * @return distance：点pX到pA和pB所在直线的距离
     */
    public static double distToSegment(Point pA, Point pB, Point pX) {
        double a = Math.abs(geoDist(pA, pB));
        double b = Math.abs(geoDist(pA, pX));
        double c = Math.abs(geoDist(pB, pX));
        double p = (a + b + c) / 2.0;
        double s = Math.sqrt(Math.abs(p * (p - a) * (p - b) * (p - c)));
        double d = s * 2.0 / a;
        return d;
    }

    /**
     * 函数功能：求两个经纬度点之间的距离
     *
     * @param pA ：起始点
     * @param pB ：结束点
     * @return distance：距离
     */
    public static double geoDist(Point pA, Point pB) {
        double radLat1 = rad(pA.lat);
        double radLat2 = rad(pB.lat);
        double deltaLng = rad(pB.lng - pA.lng);
        double top1 = Math.cos(radLat2) * Math.sin(deltaLng);
        double top2 = Math.cos(radLat1) * Math.sin(radLat2)
                - Math.sin(radLat1) * Math.cos(radLat2) * Math.cos(deltaLng);
        double top = Math.sqrt(top1 * top1 + top2 * top2);
        double bottom = Math.sin(radLat1) * Math.sin(radLat2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(deltaLng);
        double delta_sigma = Math.atan2(top, bottom);
        double distance = delta_sigma * 6378137.0;
        return distance;
    }

    /**
     * 函数功能：角度转弧度
     *
     * @param d ：角度
     * @return 返回的是弧度
     */
    public static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 函数功能：根据最大距离限制，采用DP方法递归的对原始轨迹进行采样，得到压缩后的轨迹
     *
     * @param enpInit        ：原始经纬度坐标点数组
     * @param enpArrayFilter ：保持过滤后的点坐标数组
     * @param start          ：起始下标
     * @param end            ：终点下标
     * @param dMax           ：预先指定好的最大距离误差
     */
    public static void trajCompress(Point[] enpInit, List<Point> enpArrayFilter, int start, int end, double dMax, int enSize) {
        // 递归进行的条件
        if (start < end && enpArrayFilter.size() < enSize) {
            // 最大距离
            double maxDist = 0;
            // 当前下标
            int curPt = 0;
            for (int i = start + 1; i < end; i++) {
                // 当前点到对应线段的距离
                double curDist = distToSegment(enpInit[start], enpInit[end], enpInit[i]);
                // 求出最大距离及最大距离对应点的下标
                if (curDist > maxDist) {
                    maxDist = curDist;
                    curPt = i;
                }
            }
            // 若当前最大距离大于最大距离误差
            if (maxDist >= dMax) {
                // 将当前点加入到过滤数组中
                enpArrayFilter.add(enpInit[curPt]);
                // 将原来的线段以当前点为中心拆成两段，分别进行递归处理
                trajCompress(enpInit, enpArrayFilter, start, curPt, dMax, enSize);
                trajCompress(enpInit, enpArrayFilter, curPt, end, dMax, enSize);
            }
        }
    }

}