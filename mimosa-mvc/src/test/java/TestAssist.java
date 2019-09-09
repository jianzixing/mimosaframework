import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
        @ContextConfiguration("classpath:/spring.xml"),
        @ContextConfiguration("classpath:/spring-mvc.xml")
})
@WebAppConfiguration
@EnableWebMvc
public class TestAssist {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void test2() throws Exception {
        MvcResult result2 = this.mockMvc.perform(
                get("/abc/a/run3.action?ids=[1,2,3]"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result2.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    public void test3() throws Exception {
        MvcResult result2 = this.mockMvc.perform(
                get("/abc/a/run4.action?ids=[1,2,3]"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result2.getResponse().getContentAsString();
        System.out.println(content);
    }
}
