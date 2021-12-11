package jna;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface JnaLibrary extends Library {
    /**
     * 不同平台依赖库名称不同
     * macOS -> darwin-aarch64/libjna.dylib
     * Linux -> linux-x86-64/libjna.so
     */
    JnaLibrary INSTANCE = Native.load("jna", JnaLibrary.class);

    int add(int a, int b);
}
