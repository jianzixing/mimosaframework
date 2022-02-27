import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;

public class JsonTest {
    private String name;
    private String json;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Test
    public void model2string() {
        String cms = "{\"name\":\"bbb\",\"pid\":0,\"templateId\":15,\"json\":{\"text\":\"aaa\",\"name\":\"bbb\",\"m_text\":\"ccc\",\"radio\":\"2\",\"checkbox\":[\"3\",\"2\"],\"pd_radio\":\"2\",\"pd_m_select\":[\"4\",\"3\"],\"date\":\"\",\"number\":\"10\",\"slider\":0}}";
        JsonTest test = ModelObject.toJavaObject(ModelObject.parseObject(cms), JsonTest.class);
        System.out.println(test);
    }
}
