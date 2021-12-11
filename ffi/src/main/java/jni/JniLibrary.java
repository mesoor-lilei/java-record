package jni;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
import static org.apache.commons.lang3.SystemUtils.IS_OS_MAC_OSX;

/**
 * 生成头文件（注意执行此命令时注释掉此类 import 的非标准库依赖）
 *
 * <blockquote><pre>
 *     javac -h . JniLibrary.java
 * </pre></blockquote>
 *
 * <p> 生成动态链接库
 * <p> macOS
 * <blockquote><pre>
 *     g++ jni.cpp -I $JAVA_HOME/include -I $JAVA_HOME/include/darwin -fPIC -shared -o ../../resources/darwin-aarch64/jni.dylib
 * </pre></blockquote>
 *
 * <p> Linux
 *
 * <blockquote><pre>
 *     g++ jni.cpp -I $JAVA_HOME/include -I $JAVA_HOME/include/linux -fPIC -shared -o ../../resources/linux-x86-64/jni.so
 * </pre></blockquote>
 */
public final class JniLibrary {
    static {
        if (IS_OS_LINUX) {
            // 路径必须从 `/` 开始
            load("/linux-x86-64/jni.so");
        } else if (IS_OS_MAC_OSX) {
            load("/darwin-aarch64/jni.dylib");
        } else {
            throwLoadError();
        }
    }

    private static void throwLoadError() {
        throw new UnsatisfiedLinkError("Unable to load library 'jni'");
    }

    private static void load(final String path) {
        // 加载 JAR 包中的库文件需将库文件转为外置文件
        try (final InputStream in = JniLibrary.class.getResourceAsStream(path)) {
            final File temp = File.createTempFile("jni", null);
            FileUtils.copyInputStreamToFile(in, temp);
            System.load(temp.getAbsolutePath());
        } catch (final IOException e) {
            e.printStackTrace();
            throwLoadError();
        }
    }

    public static native int add(int a, int b);

    private static void call_text(final String text) {
        System.out.println("call test -> " + text);
    }

    private void call() {
        System.out.println("call");
    }
}
