package org.mimosaframework.orm.scripting;

import org.mimosaframework.orm.i18n.I18n;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
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

    public void load(List<String> pkg) throws IOException, URISyntaxException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        for (String path : pkg) {
            if (sqlDefiner == null) {
                sqlDefiner = new XMLSQLDefiner(this.configure);
            }
            if (mappers == null) {
                mappers = new ArrayList<>();
            }


            Enumeration<URL> url = null;
            if (path.endsWith(".xml")) {
                url = classLoader.getResources(path);
                if (url == null) {
                    throw new IllegalArgumentException(I18n.print("not_fount_resource", path));
                }
            } else {
                path = path.replace(".", "/");
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                url = classLoader.getResources(path);
                if (url == null) {
                    throw new IllegalArgumentException(I18n.print("not_fount_resource", path));
                }
            }
            if (url != null) {
                path = "/" + path;
                while (url.hasMoreElements()) {
                    URL item = url.nextElement();
                    String protocol = item.getProtocol();
                    if ("file".equals(protocol)) {
                        findClassLocal(item.toURI(), path);
                    } else if ("jar".equals(protocol)) {
                        findClassJar(item, path);
                    }
                }
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

    private void findClassLocal(URI uri, final String pathName) throws URISyntaxException {
        final File file = new File(uri);
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
                        try {
                            findClassLocal(filterFile.toURI(), pathName);
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
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

    private void findClassJar(URL url, String pathName) {
        JarFile jarFile = null;
        try {
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            jarFile = jarURLConnection.getJarFile();
        } catch (IOException e) {
            throw new RuntimeException("not font jar file");
        }

        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();

            if (jarEntryName.startsWith(pathName.substring(1)) && jarEntry.getName().endsWith(".xml")) {
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
