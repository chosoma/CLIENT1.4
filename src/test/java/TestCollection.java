import java.util.ArrayList;
import java.util.List;

public class TestCollection {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        strings.add("0");
        strings.add("1");
        strings.add("2");
        for (int i = 0; i < strings.size(); i++) {
            System.out.println(strings.remove(0));
            i--;
        }
    }
}
