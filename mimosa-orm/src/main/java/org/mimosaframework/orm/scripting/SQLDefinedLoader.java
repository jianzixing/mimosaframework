package org.mimosaframework.orm.scripting;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SQLDefinedLoader {
    private Map<String, DynamicSqlSource> sqlSourceMap;
    private List<XMapper> mappers;
    private SQLDefiner sqlDefiner;
    private DefinerConfigure configure;

    public SQLDefinedLoader(DefinerConfigure configure) {
        this.configure = configure;
    }

    public void load(List<String> pkg) {
        for (String path : pkg) {
            if (sqlDefiner == null) {
                sqlDefiner = new XMLSQLDefiner(this.configure);
            }
            if (mappers == null) {
                mappers = new ArrayList<>();
            }


            URL url = null;
            if (path.endsWith(".xml")) {
                url = this.getClass().getResource(path);
                if (url == null) {
                    throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                            SQLDefinedLoader.class, "not_fount_resource", path));
                }
            } else {
                url = this.getClass().getResource("/" + path.replace(".", "/"));
                if (url == null) {
                    throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                            SQLDefinedLoader.class, "not_fount_resource", path));
                }
            }
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                findClassLocal(path);
            } else if ("jar".equals(protocol)) {
                findClassJar(path);
            }
        }
    }

    public XMapper getMapper(String name) {
        if (mappers != null) {
            for (XMapper mapper : mappers) {
                if (mapper.getMapperName() != null && mapper.getMapperName().equals(name)) {
                    return mapper;
                }
            }
        }
        return null;
    }

    public DynamicSqlSource getDynamicSqlSource(String name) {
        if (this.sqlSourceMap != null) {
            return this.sqlSourceMap.get(name);
        }
        return null;
    }

    private void addMapper(XMapper mapper) {
        this.mappers.add(mapper);
        if (mapper != null) {
            Map<String, SqlNode> nodeMap = mapper.getActions();
            if (nodeMap != null) {
                Iterator<Map.Entry<String, SqlNode>> iterator = nodeMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, SqlNode> entry = iterator.next();
                    if (this.sqlSourceMap == null) {
                        this.sqlSourceMap = new HashMap<>();
                    }
                    this.sqlSourceMap.put(mapper.getMapperName() + "." + entry.getKey(), new DynamicSqlSource(this.configure, entry.getValue()));
                }
            }
        }
    }

    private void findClassLocal(final String packName) {
        URI url = null;
        try {
            if (packName.endsWith(".xml")) {
                url = this.getClass().getResource(packName).toURI();
            } else {
                url = SQLDefinedLoader.class.getResource("/" + packName.replace(".", "/")).toURI();
            }
        } catch (Exception e1) {
            throw new RuntimeException(Messages.get(LanguageMessageFactory.PROJECT,
                    SQLDefinedLoader.class, "not_fount_resource", packName));
        }

        final File file = new File(url);
        if (file.isFile()) {
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                XMapper mapper = sqlDefiner.getDefiner(is, file.getName());
                addMapper(mapper);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            file.listFiles(new FileFilter() {

                public boolean accept(File filterFile) {
                    if (filterFile.isDirectory()) {
                        findClassLocal(packName + "." + filterFile.getName());
                    }
                    if (filterFile.getName().endsWith(".xml")) {
                        InputStream is = null;
                        try {
                            is = new FileInputStream(filterFile);
                            XMapper mapper = sqlDefiner.getDefiner(is, filterFile.getName());
                            addMapper(mapper);
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } finally {
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void findClassJar(final String packName) {
        String pathName = packName.replace(".", "/");
        JarFile jarFile = null;
        try {
            URL url = SQLDefinedLoader.class.getResource(pathName);
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            jarFile = jarURLConnection.getJarFile();
        } catch (IOException e) {
            throw new RuntimeException("not font jar file");
        }

        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();

            if (jarEntryName.contains(pathName) && !jarEntryName.equals(pathName + "/")) {
                if (jarEntry.isDirectory()) {
                    String clazzName = jarEntry.getName().replace("/", ".");
                    int endIndex = clazzName.lastIndexOf(".");
                    String prefix = null;
                    if (endIndex > 0) {
                        prefix = clazzName.substring(0, endIndex);
                    }
                    findClassJar(prefix);
                }
                if (jarEntry.getName().endsWith(".xml")) {
                    InputStream is = null;
                    try {
                        String fileName = jarEntry.getName();
                        is = jarFile.getInputStream(jarEntry);
                        XMapper mapper = sqlDefiner.getDefiner(is, fileName);
                        addMapper(mapper);
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

    }
}
