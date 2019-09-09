import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mimosaframework.core.utils.StringTools;
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
        @ContextConfiguration("classpath:/spring-mvc-test.xml")
})
@WebAppConfiguration
@EnableWebMvc
public class TestMimosaMapping {
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
    public void test1() throws Exception {
        MvcResult result2 = this.mockMvc.perform(
                get("/admin/Hello/getName.action"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result2.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    public void test2() throws Exception {
        MvcResult result2 = this.mockMvc.perform(
                get("/admin/hello/set_json.action")
                        .param("name", "me")
//                        .param("object", "{\"name\":\"yak\"}")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result2.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    public void test3() throws Exception {
        MvcResult result2 = this.mockMvc.perform(
                get("/admin/Hello/response.action"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result2.getResponse().getContentAsString();
        System.out.println(content);
    }


    @Test
    public void test4() throws Exception {
        MvcResult result2 = this.mockMvc.perform(
                get("/admin/hello/setpath/yangankang.action"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result2.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    public void testDump() {
        System.out.println(StringTools.humpToLine("HelloWorld"));
        System.out.println(StringTools.humpToLine("HelloWORLD"));
        System.out.println(StringTools.humpToLine("HelloWORLDFull"));
        System.out.println(StringTools.humpToLine("helloWORLDFull"));
        System.out.println(StringTools.humpToLine("HELLOWORLD"));
        System.out.println(StringTools.humpToLine("_HELLOWorld"));
    }

    @Test
    public void test5() throws Exception {
        MvcResult result2 = this.mockMvc.perform(
                get("/admin/Hello/list.action").param("strings", "['a','b','c']"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result2.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    public void test6() throws Exception {
        MvcResult result2 = this.mockMvc.perform(
                get("/admin/curd/add.action").param("object", "{a:'abc'}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result2.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    public void test7() throws Exception {
        MvcResult result2 = this.mockMvc.perform(
                get("/admin/Hello/del.action").param("ids", "[1,2,'3']"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result2.getResponse().getContentAsString();
        System.out.println(content);
    }
}
