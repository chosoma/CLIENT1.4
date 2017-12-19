package com;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import sun.swing.SwingUtilities2;

import javax.xml.bind.DatatypeConverter;

public class MyLgoInfo {

    static String fileName ="propertyInfo\\logoInfo.xml";
    private static final String HEXES = "0123456789ABCDEF";
    static byte key = 16;
    public static String SoftName, CompanyName, CopyrightName;

    static {
        File file = new File(fileName);
        if (!file.exists()) {
            intiDefault();
        } else {
            try {
                SAXReader reader = new SAXReader();
                Document document = reader.read(file);

                Element root = document.getRootElement();

                Element collection = root.element("logo");
                // 软件名
                Element softnameE = (Element) collection
                        .selectSingleNode("softname");
                String softname = softnameE.getText();
                try {
                    SoftName = MyDecode(softname);
                } catch (NumberFormatException e) {
                    SoftName = "37393C103F5E5C595E5510345564555364595E57";
                }
                // 公司名
                Element companyE = (Element) collection
                        .selectSingleNode("company");
                String company = companyE.getText();
                try {
                    CompanyName = MyDecode(company);
                } catch (NumberFormatException e) {
                    CompanyName = "47454839104438393E3744353B10445553585E5F5C5F5769103C64541E";
                }
                // 版权
                Element copyrightE = (Element) collection
                        .selectSingleNode("copyright");
                String copyright = copyrightE.getText();
                try {
                    CopyrightName = MyDecode(copyright);
                } catch (NumberFormatException e) {
                    CopyrightName = "47454839104438393E3744353B10445553585E5F5C5F5769103C64541E";
                }

            } catch (DocumentException e1) {
                e1.printStackTrace();
            } catch (Exception e2) {
                // TODO 配置文件出现异常，是否要恢复到默认配置？
                e2.printStackTrace();
                file.delete();
                intiDefault();
            }

        }
    }

    /**
     * 获取明文字符串的字节数组，将每个字节减去 key，再转成密文HEX字符串
     *
     * @param msg
     * @return
     */
    public static String MyCode(String msg) {
        msg = msg.trim();
        if (msg == null || msg.equals("")) {
            throw new NumberFormatException("加密内容不能为空或者空字符串");
        }
        byte[] b = msg.getBytes();
        int len = b.length;
        final StringBuilder hex = new StringBuilder(2 * len);
        for (int i = 0; i < len; i++) {
            b[i] -= key;
            hex.append(HEXES.charAt((b[i] & 0xF0) >> 4)).append(HEXES.charAt((b[i] & 0x0F)));
        }
        return hex.toString();
    }


    private static void intiDefault() {
        SoftName = "GIL在线检测系统";
        CompanyName = "无锡讯泰科技有限公司";
        CopyrightName = "无锡讯泰科技有限公司";
    }

    /**
     * 将密文的HEX字符串转换成字节数组，并将每个字节加key，转换成明文字符串
     *
     * @param msg
     * @return
     */
    /**
     * GBK编码格式
     * public static String MyDecode(String msg) {
     * msg = msg.trim();
     * if (msg == null || msg.equals("")) {
     * throw new NumberFormatException("解密内容不能为空或者空字符串");
     * }
     * int len = msg.length();
     * byte[] ret = new byte[len / 2];
     * for (int i = 0; i < len; i += 2) {
     * ret[i / 2] = (byte) Integer.parseInt(msg.substring(i, i + 2), 16);
     * ret[i / 2] += key;
     * }
     * return new String(ret);
     * }
     */

    public static String MyDecode(String msg) {//UTF-8
        msg = msg.trim();
        if (msg == null || msg.equals("")) {
            throw new NumberFormatException("解密内容不能为空或者空字符串");
        }
        int len = msg.length();
        byte[] ret = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            ret[i / 2] = (byte) Integer.parseInt(msg.substring(i, i + 2), 16);
            ret[i / 2] += key;
        }
        return new String(ret);
    }

    /**
     * 获取字体
     *
     * @param fontname
     * @param style
     * @param title
     * @param width
     * @param height
     * @return
     */
    public static Font getTitleFont(String fontname, int style, String title,
                                    int width, int height) {
        int size, w = 0;
        Font font = null;
        if (height > 0) {
            size = height;
            while (size >= 12) {
                font = new Font(fontname, style, size);
                GlyphVector v = font.createGlyphVector(SwingUtilities2
                                .getFontMetrics(null, font).getFontRenderContext(),
                        title);
                Shape shape = v.getOutline();
                Rectangle bounds = shape.getBounds();
                w = bounds.x + bounds.width + 2;
                if (w > width) {
                    size--;
                } else {
                    break;
                }
            }
        } else {
            size = 12;
            while (width > w) {
                font = new Font(fontname, style, size);
                GlyphVector v = font.createGlyphVector(SwingUtilities2
                                .getFontMetrics(null, font).getFontRenderContext(),
                        title);
                Shape shape = v.getOutline();
                Rectangle bounds = shape.getBounds();
                w = bounds.x + bounds.width + 2;
                size++;
            }
            size = font.getSize();
            if (size > 12) {
                size--;
            }
            font = new Font(fontname, style, size);
        }
        return font;
    }
}
