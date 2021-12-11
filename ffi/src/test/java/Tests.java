import jna.JnaLibrary;
import jni.JniLibrary;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class Tests {
    @Test
    void jna() {
        final int v = JnaLibrary.INSTANCE.add(1, 2);
        assertEquals(v, 3);
    }

    @Test
    void jni() {
        final int v = JniLibrary.add(1, 2);
        assertEquals(v, 3);
    }
}
