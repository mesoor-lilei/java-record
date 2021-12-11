import jna.JnaLibrary;
import jni.JniLibrary;

public final class Main {
    public static void main(final String[] args) {
        System.out.println(JniLibrary.add(1, 2));
        System.out.println(JnaLibrary.INSTANCE.add(1, 2));
    }
}
