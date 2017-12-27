package util;

import java.util.ArrayList;
import java.util.List;

import protocol.ProtocolX;

public class MyDecodeUitl {
    /**
     * 逆转义
     */
    public static byte[] Decrypt(byte[] source) {
        List<Byte> btlist = new ArrayList<>();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ProtocolX.TURN) {
                switch (source[i + 1]) {
                    case (ProtocolX.HEADT): {
                        btlist.add(ProtocolX.HEAD);
                        i++;
                        break;
                    }
                    case (ProtocolX.TAILT): {
                        btlist.add(ProtocolX.TAIL);
                        i++;
                        break;
                    }
                    case (ProtocolX.TURNT): {
                        btlist.add(ProtocolX.TURN);
                        i++;
                        break;
                    }
                    case (ProtocolX.IST1T): {
                        btlist.add(ProtocolX.IST1);
                        i++;
                        break;
                    }
                    case (ProtocolX.IST2T): {
                        btlist.add(ProtocolX.IST2);
                        i++;
                        break;
                    }
                    default: {
                        btlist.add(ProtocolX.TURN);
                    }

                }

            } else {
                btlist.add(source[i]);
            }

        }
        byte[] ret = new byte[btlist.size()];
        int i = 0;
        for (byte data : btlist) {
            ret[i] = data;
            i++;
        }
        return ret;
    }

    /**
     * 转义
     */
    public static byte[] Encryption(byte[] source) {
        List<Byte> btlist = new ArrayList<>();
        btlist.add(ProtocolX.TAIL);// 头
//        btlist.add(ProtocolX.HEAD);// 头
        for (byte aSource : source) {
            switch (aSource) {
                case (ProtocolX.HEAD): {
                    btlist.add(ProtocolX.TURN);
                    btlist.add(ProtocolX.HEADT);
                    break;
                }
                case (ProtocolX.TAIL): {
                    btlist.add(ProtocolX.TURN);
                    btlist.add(ProtocolX.TAILT);
                    break;
                }
                case (ProtocolX.TURN): {
                    btlist.add(ProtocolX.TURN);
                    btlist.add(ProtocolX.TURNT);
                    break;
                }
                case (ProtocolX.IST1): {
                    btlist.add(ProtocolX.TURN);
                    btlist.add(ProtocolX.IST1T);
                    break;
                }
                case (ProtocolX.IST2): {
                    btlist.add(ProtocolX.TURN);
                    btlist.add(ProtocolX.IST2T);
                    break;
                }
                default: {
                    btlist.add(aSource);
                }
            }
        }
        btlist.add(ProtocolX.HEAD);// 尾
//        btlist.add(ProtocolX.TAIL);
        byte[] ret = new byte[btlist.size()];
        int i = 0;
        for (byte data : btlist) {
            ret[i] = data;
            i++;
        }
        return ret;
    }
}
