package b;

import a.tsk22;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.Objects;

import static a.tsk22.sch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = tsk22.class)
public class tsk24_22 {
    @Autowired private WebApplicationContext cx;
    @Autowired private sch s;

    @Test public void sch() {
        Assertions.assertDoesNotThrow(s::a);
        Assertions.assertEquals(2,
            Objects.requireNonNull(new File("/home/admin/IdeaProjects/sprBoot/src/main/resources/aa")
                .listFiles()).length);
    }

    @SneakyThrows @Test public void pst() {
        val a = MockMvcBuilders.webAppContextSetup(cx).build();
        a.perform(post("http://localhost:8081/actuator/usr")
            .contentType(MediaType.APPLICATION_JSON).content("""
                {"a":"a","b":"b"}""")).andExpect(status().isOk());
    }

    @SneakyThrows @Test public void gt() {
        val a = MockMvcBuilders.webAppContextSetup(cx).build();
        a.perform(get("http://localhost:8081/actuator/usr")).andExpect(content().string("""
            [(4 a a [])]"""));
    }
}
