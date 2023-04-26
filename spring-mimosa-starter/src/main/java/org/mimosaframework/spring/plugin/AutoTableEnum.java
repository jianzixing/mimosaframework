package org.mimosaframework.spring.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * https://www.bbsmax.com/A/8Bz8qOvNJx/
 * desc 自动创建表结构的枚举类
 * @goal generate_table
 *
 */
public class AutoTableEnum extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("generate_table start run");
    }
}
