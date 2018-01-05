import data.FormatTransfer;

public class TestFlout {
    public static void main(String[] args) {
//        float f = -0.5f;
//
//        float f1 = 75.7f;
//
//        System.out.println((int) ((f1 - 0.0) * 10) / 10.0);
//        System.out.println((float) (f1 - 125.0));
//
//        System.out.println((int) Math.floor(f));
//
//        byte[] bytes = new byte[4];
//        bytes[0] = 0;
//        bytes[1] = 0;
//        bytes[2] = (byte) 0xff;
//        bytes[3] = (byte) 0xff;
//        System.out.println(FormatTransfer.bytesL2Float2(bytes, 0, 4, 1));
//        newScale();/**/
        int i = 5;
        i += i *= i /= i -= i++;
        System.out.println(i);

    }

    public static void newScale() {

        float f1 = 75.7f;

        String str = "00 FF 7F 47";
        System.out.println((int) ((f1 - 125.0) * 10) / 10.0);

        for (float f = 0.0f; f <= 125.0; f += 0.1) {
            float f2 = f1 - f;
            int i1 = (int) Math.round(f2 * 10);
            float f3 = (float) (i1 / 10.0);
            System.out.println(f + ":" + f3);
        }

    }
}
