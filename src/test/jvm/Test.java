package test.jvm;

/**
 * @author colaz
 * @date 2019/6/20
 **/
public class Test {


    public static void main(String[] args) {
        while (true) {
            int[] arr= new int[200];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = i;
            }
        }

    }
}
