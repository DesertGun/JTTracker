package ee.desertgun.jttracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Properties;

@Configuration
public class JavaMailConfig {

  @Bean
  public JavaMailSender getJavaMailSender(){
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    javaMailSender.setHost("localhost");
    javaMailSender.setPort(25);

    javaMailSender.setUsername("admin@domain.de");
    javaMailSender.setPassword("password");

    Properties props = javaMailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    return javaMailSender;
  }

  @Bean
  public ResourceBundleMessageSource emailMessageSource() {
    final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("templates/html");
    return messageSource;
  }

  @Bean
  public TemplateEngine emailTemplateEngine() {
    final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(htmlTemplateResolver());
    templateEngine.addTemplateResolver(stringTemplateResolver());
    templateEngine.setTemplateEngineMessageSource(emailMessageSource());
    return templateEngine;
  }


  private ITemplateResolver htmlTemplateResolver() {
    final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setOrder(2);
    templateResolver.setPrefix("/templates/html/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCharacterEncoding("UTF8");
    templateResolver.setCacheable(false);
    return templateResolver;
  }

  private ITemplateResolver stringTemplateResolver() {
    final StringTemplateResolver templateResolver = new StringTemplateResolver();
    templateResolver.setOrder(3);
    // No resolvable pattern, will simply process as a String template everything not previously matched
    templateResolver.setTemplateMode("HTML");
    templateResolver.setCacheable(false);
    return templateResolver;
  }



}
