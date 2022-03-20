package b;

import a.tsk21;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = tsk21.class)
public class tsk24_21 {
    @Autowired private tsk21.eml e;

    @Test public void ml() {
        Assertions.assertDoesNotThrow(() -> e.sn("test"));
    }
}
