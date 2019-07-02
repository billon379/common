package fun.billon.common.util;

import fun.billon.common.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件处理工具类
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class FileUtil {

    public static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 复制文件
     *
     * @param src  源文件
     * @param dest 目标文件
     * @return 是否复制成功
     */
    public static boolean copyFile(String src, String dest) {
        boolean result = false;
        File srcFile = new File(src);
        File destFile = new File(dest);
        if (srcFile.exists()) {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                File parentDir = destFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                byte[] buf = new byte[4 * 1024];
                int len = -1;
                while ((len = fis.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                result = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                if (destFile != null && destFile.exists()) {
                    destFile.delete();
                }
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    /**
     * 复制目录
     *
     * @param oldPath 老目录
     * @param newPath 新目标
     * @return 是否复制成功
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            // 如果文件夹不存在 则建立新文件夹
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/ " + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                    LOGGER.debug("[COPY_FILE:" + temp.getPath() + "复制文件成功!]");
                }
                // 如果是子文件夹
                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/ " + file[i], newPath + "/ " + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错 ");
            e.printStackTrace();
        }
    }

    /**
     * 保存文件
     */
    public static boolean saveFile(String path, InputStream in) {
        boolean ret = false;
        FileOutputStream fos = null;
        File tmp = null;
        try {
            File f = new File(path);
            if (f.exists()) {
                LOGGER.error("文件已存在");
                return false;
            }
            tmp = new File(path + ".tmp");
            File dir = tmp.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fos = new FileOutputStream(tmp);
            byte[] buf = new byte[4 * 1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.close();

            if (tmp.renameTo(f)) {
                LOGGER.info("修改文件成功");
            } else {
                LOGGER.info("修改文件失败");
            }
            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (tmp.exists()) {
                tmp.delete();
            }
        }
        return ret;
    }

    /**
     * 将InputStream转换为String(UTF-8编码)
     *
     * @param is InputStream
     * @return String
     */
    public static String inputStream2String(InputStream is) {
        if (is == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4 * 1024];
        int len = 0;
        try {
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            sb.append(baos.toString("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取文件的Base64编码字串
     *
     * @param url 文件路径
     */
    public static String base64(String url) throws IOException {
        URL netUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) netUrl.openConnection();
        connection.connect();
        if (connection.getResponseCode() == HttpClient.OK) {
            InputStream in = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[2 * 1024];
            int len = -1;
            while ((len = in.read(buf)) > -1) {
                baos.write(buf, 0, len);
            }
            connection.disconnect();
            in.close();
            return new String(Base64Utils.encode(baos.toByteArray()));
        }
        return null;
    }
}
