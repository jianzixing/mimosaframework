package org.mimosaframework.orm.scripting;

import org.mimosaframework.orm.scripting.tags.MixedSqlNode;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public interface SQLDefiner {
    XMapper getDefiner(InputStream inputStream, String fileName) throws ParserConfigurationException, IOException, SAXException;

    MixedSqlNode parseSqlNode(String action, String sql) throws ParserConfigurationException, IOException, SAXException;
}
