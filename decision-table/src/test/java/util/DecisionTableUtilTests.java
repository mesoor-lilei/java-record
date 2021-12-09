package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.DecisionTableUtil.call;

final class DecisionTableUtilTests {
    @Test
    void test() {
        class UserLevel {
            public Integer level;
            private Integer age;
            private Integer sex;

            public void setAge(final Integer age) {
                this.age = age;
            }

            public void setSex(final Integer sex) {
                this.sex = sex;
            }

            public Integer getLevel() {
                return level;
            }
        }

        final UserLevel v = new UserLevel();
        v.setSex(0);
        v.setAge(15);
        call("user-level.csv", v);
        assertEquals(v.getLevel(), 1);

        v.setSex(1);
        call("user-level.csv", v);
        assertEquals(v.getLevel(), 2);
    }
}
