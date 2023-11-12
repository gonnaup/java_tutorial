package org.gonnaup.tutorial.springdoc.mail;

import org.springframework.context.annotation.PropertySource;

/**
 * @author gonnaup
 * @version created at 2023/11/8 下午10:35
 */
@PropertySource("mail")
public class MailProperties {

    private String from;

    private String to;

    private String cc;

}
