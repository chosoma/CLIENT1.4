package com;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import domain.WdPeriod;

/**
 * 数据库链接参数
 */
public class MyConfigure {

    static String fileName = "propertyInfo\\properties.xml";

    private static String SQLurl;// 数据库连接字符串
    private static String Addr, LocalPort;// 本地端口
    private static DelayBean DelayGPRS;
    private static int Preheat;// 预热时间（毫秒）
    private static boolean VioceWarn;// 报警
    private static boolean RunningVoiceWarn;//正在报警
    private static WdPeriod wdPeriod;
    private static String jfstr;
    private static String gzstr;


    static {
        File file = new File(fileName);
        if (!file.exists()) {
            intiDefault();
            createConfigXML();
        } else {
            try {
                SAXReader reader = new SAXReader();
                Document document = reader.read(file);

                Element root = document.getRootElement();

                // ***********采集***********
                Element collection = root.element("collection");
                Element CommDelay = (Element) collection
                        .selectSingleNode("commDelay");
                String wait = CommDelay.attributeValue("wait");
                String space = CommDelay.attributeValue("space");
                String reply = CommDelay.attributeValue("reply");
                String timeout = CommDelay.attributeValue("timeout");
                DelayGPRS = new DelayBean(wait, space, reply, timeout);

                Element fixed = (Element) collection.selectSingleNode("fixed");
                Preheat = Integer.valueOf(fixed.attributeValue("preheat")
                        .trim());
                Element warning = (Element) collection
                        .selectSingleNode("warning");
                VioceWarn = Boolean.valueOf(warning.attributeValue("voice")
                        .trim());

                Element wdP = (Element) collection.selectSingleNode("wdPeriod");
                byte wd1 = Byte.valueOf(wdP.attributeValue("wd1").trim());
                byte wd2 = Byte.valueOf(wdP.attributeValue("wd2").trim());
                byte jg1 = Byte.valueOf(wdP.attributeValue("jg1").trim());
                byte jg2 = Byte.valueOf(wdP.attributeValue("jg2").trim());
                wdPeriod = new WdPeriod(wd1, wd2, jg1, jg2);

                // ***********通讯***********
                Element communication = root.element("communication");
                Element localport = (Element) communication
                        .selectSingleNode("GPRS");
                Addr = localport.attributeValue("addr").trim();
                LocalPort = localport.attributeValue("localport").trim();

                // ***********数据库***********
                Element database = root.element("database");
                String ip = database.attributeValue("ip");
                String port = database.attributeValue("port");
                String dbname = database.attributeValue("dbname");
                ip = ip == null ? "localhost" : ip;
                port = port == null ? "3306" : port;
                dbname = dbname == null ? "gil" : dbname;
                SQLurl = "jdbc:mysql://" + ip + ":" + port + "/" + dbname + "?characterEncoding=UTF-8";

                Element lib = root.element("start");
                jfstr = lib.attributeValue("jflib");
                System.out.println(jfstr);
                gzstr = lib.attributeValue("gzlib");
                System.out.println(gzstr);

            } catch (DocumentException e1) {
                e1.printStackTrace();
            } catch (Exception e2) {
                file.delete();
                intiDefault();
                createConfigXML();
            }
        }
    }

    /**
     * 设置声音报警
     *
     * @param b
     */
    public static void setVoiceWarn(boolean b) {
        VioceWarn = b;
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(fileName));
            Element warning = (Element) document
                    .selectSingleNode("//collection/warning");
            if (warning != null) {
                warning.addAttribute("voice", String.valueOf(b));
            }
            push2file(document);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否声音报警
     *
     * @return
     */
    public static boolean isVioceWarn() {
        return VioceWarn;
    }

    /**
     * 是否正在报警
     *
     * @return
     */
    public static boolean isRunningVioceWarn() {
        return RunningVoiceWarn;
    }

    public static void setRunningVoiceWarn(boolean runningVoiceWarn) {
        RunningVoiceWarn = runningVoiceWarn;
    }

    /**
     * 设置本地端口
     *
     * @param port
     * @param port2
     */
    public static void setLocalPort(String port, String port2) {
        Addr = port;
        LocalPort = port2;
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(fileName));
            Element choose = (Element) document
                    .selectSingleNode("//communication/GPRS");
            if (choose != null) {
                choose.addAttribute("addr", port);
                choose.addAttribute("localport", port2);
            }
            push2file(document);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单点监听的本地端口
     *
     * @return
     */
    public static String getAddr() {
        return Addr;
    }

    /**
     * 获取gateway的本地端口
     *
     * @return
     */
    public static String getLocalPort() {
        return LocalPort;
    }

    /**
     * 缺省配置
     */
    private static void intiDefault() {
        VioceWarn = false;
        LocalPort = "8088";
        Addr = "localhost";
        String ip = "localhost", port = "3306", dbname = "gil";
        SQLurl = "jdbc:mysql://" + ip + ":" + port + "/" + dbname;
        wdPeriod = new WdPeriod((byte) 40, (byte) 50, (byte) 05, (byte) 01);
        Preheat = 80000;
        DelayGPRS = new DelayBean("30000", "20000", "10000", "300000");
        jfstr = "";
        gzstr = "";
    }

    private static void createConfigXML() {
        // 建立document对象
        Document document = DocumentHelper.createDocument();
        Element property = document.addElement("property");// 添加文档根
        // ***********采集***********
        property.addComment("采集");// 加入一行注释
        Element collection = property.addElement("collection"); // 添加property的子节点
        Element commDelay3 = collection.addElement("commDelay"); // 添加collection的子节点
        commDelay3.addAttribute("wait", DelayGPRS.getWait() + "");
        commDelay3.addAttribute("space", DelayGPRS.getSpace() + "");
        commDelay3.addAttribute("reply", DelayGPRS.getReply() + "");
        commDelay3.addAttribute("timeout", DelayGPRS.getTimeout() + "");

        collection.addComment("预热");// 加入一行注释
        Element fixed = collection.addElement("fixed"); // 添加collection的子节点
        fixed.addAttribute("preheat", Preheat + "");

        collection.addComment(" 报警：声音");// 加入一行注释
        Element warning = collection.addElement("warning"); // 添加collection的子节点
        warning.addAttribute("voice", String.valueOf(VioceWarn));

        collection.addComment(" 温度频率");// 加入一行注释
        Element wd = collection.addElement("wdPeriod"); // 添加collection的子节点
        wd.addAttribute("wd1", wdPeriod.getWd1() + "");
        wd.addAttribute("wd2", wdPeriod.getWd2() + "");
        wd.addAttribute("jg1", wdPeriod.getJg1() + "");
        wd.addAttribute("jg2", wdPeriod.getJg2() + "");

        // ***********通讯***********
        property.addComment("通讯");// 加入一行注释
        Element communication = property.addElement("communication"); // 添加property的子节点
        communication.addComment("GPRS/CDMA");// 加入一行注释
        Element GPRS = communication.addElement("GPRS"); // 添加communication的子节点
        GPRS.addAttribute("addr", Addr);
        GPRS.addAttribute("localport", LocalPort);

        // ***********数据库***********
        property.addComment("数据库");// 加入一行注释
        Element database = property.addElement("database"); // 添加property的子节点
        database.addAttribute("ip", "localhost");
        database.addAttribute("port", "3306");
        database.addAttribute("dbname", "gil");

        Element lib = property.addElement("start");
        lib.addAttribute("jflib", "");
        lib.addAttribute("gzlib", "");

        try {
            push2file(document);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void push2file(Document document) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(new FileOutputStream(fileName), format);
        writer.write(document);
        writer.close();
    }

    /**
     * 获取sql连接URL
     */
    public static String getSQLurl() {
        return SQLurl;
    }

    /**
     * 获取单点DTU通讯延时
     *
     * @return
     */
    public static DelayBean getDelayGPRS() {
        return DelayGPRS;
    }

    /**
     * 获取预热时间
     *
     * @return
     */
    public static int getPreheat() {
        return Preheat;
    }

    /**
     * 获取温度频率
     *
     * @return
     */
    public static WdPeriod getWdPeriod() {
        return wdPeriod;
    }

    /**
     * 设置温度频率
     *
     * @param wdPeriod2
     */
    public static void setWdPeriod(WdPeriod wdPeriod2) {
        wdPeriod = wdPeriod2;
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(fileName));
            Element choose = (Element) document.selectSingleNode("//collection/wdPeriod");
            if (choose != null) {
                choose.addAttribute("wd1", wdPeriod2.getWd1() + "");
                choose.addAttribute("wd2", wdPeriod2.getWd2() + "");
                choose.addAttribute("jg1", wdPeriod2.getJg1() + "");
                choose.addAttribute("jg2", wdPeriod2.getJg2() + "");
            }
            push2file(document);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getJfstr() {
        return jfstr;
    }

    public static String getGzstr() {
        return gzstr;
    }
}
