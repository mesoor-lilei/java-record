package util;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 决策表工具
 */
public final class DecisionTableUtil {
    private static final Map<String, Class<?>> CACHE_CLASS = new ConcurrentHashMap<>();
    private static final String FRAME = "def call(%s o){\n%s}";
    /**
     * 上方固定行
     * 1：注释行
     */
    private static final int TOP_NUMBER = 1;
    /**
     * 左侧固定行
     * 1：注释行
     */
    private static final int LEFT_NUMBER = 1;

    public static void call(final String fileName, final Object v) {
        final String className = v.getClass().getName();
        final GroovyObject obj;
        try (final GroovyClassLoader loader = new GroovyClassLoader()) {
            final Class<?> clazz;
            if (CACHE_CLASS.containsKey(fileName)) {
                clazz = CACHE_CLASS.get(fileName);
            } else {
                clazz = loader.parseClass(String.format(FRAME, className, buildScript(fileName)));
                CACHE_CLASS.put(fileName, clazz);
            }
            obj = (GroovyObject) clazz.getDeclaredConstructor().newInstance();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        obj.invokeMethod("call", v);
    }

    private static void buildRow(
        final StringBuilder script,
        final CSVRecord row,
        final String conditionPrefix,
        final int conditionIndex,
        final CSVRecord expression
    ) {
        // 条件
        final StringBuilder condition = new StringBuilder();
        // 第一组条件
        condition.append(conditionPrefix);

        // 是否无条件
        boolean conditionFlag = true;
        for (int j = LEFT_NUMBER; j < conditionIndex; j++) {
            final String rowItem = row.get(j);
            // 过滤空值
            if (!rowItem.isEmpty()) {
                condition.append(String.format(expression.get(j), rowItem)).append(" && ");
                conditionFlag = false;
            }
        }
        if (conditionFlag) {
            // 删除最后的 `if (` 字符串
            condition.setLength(condition.length() - "if (".length());
            script.append(condition).append("{\n    ");
        } else {
            // 删除最后的 ` && ` 字符串
            condition.setLength(condition.length() - " && ".length());
            script.append(condition).append(") {\n    ");
        }
        // 动作
        final StringBuilder action = new StringBuilder();
        for (int j = conditionIndex; j < row.size(); j++) {
            final String rowItem = row.get(j);
            // 过滤空值
            if (!rowItem.isEmpty()) {
                action.append(String.format(expression.get(j), rowItem)).append('\n');
            }
        }
        script.append(action).append("}");
    }

    private static StringBuilder buildScript(final String fileName) throws IOException {
        final ClassLoader loader = DecisionTableUtil.class.getClassLoader();
        final URL url = loader.getResource(fileName);
        final FileReader reader = new FileReader(url.getFile());
        final List<CSVRecord> rows = CSVFormat.INFORMIX_UNLOAD_CSV.parse(reader).getRecords();
        final CSVRecord head = rows.get(0);
        // 条件索引
        int conditionIndex = LEFT_NUMBER;
        for (; conditionIndex < head.size(); conditionIndex++) {
            if (!head.get(conditionIndex).equals("condition")) {
                break;
            }
        }
        // 表达式
        final CSVRecord expression = rows.get(1);
        final StringBuilder script = new StringBuilder();
        final int scriptIndex = TOP_NUMBER + 2;
        buildRow(script, rows.get(scriptIndex), "if (", conditionIndex, expression);
        for (int i = scriptIndex + 1; i < rows.size(); i++) {
            buildRow(script, rows.get(i), " else if (", conditionIndex, expression);
        }
        return script;
    }
}
