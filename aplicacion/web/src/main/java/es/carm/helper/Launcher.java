package es.carm.helper;

import java.io.File;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import es.carm.openldap.dao.ObjectFactory;
import es.carm.openldap.restbox.dac.Settings;

public class Launcher implements ServletContextListener {

  static Logger log = LogManager.getLogger(Launcher.class);

  public void contextDestroyed(ServletContextEvent arg0) {
    log.info("Se detiene la aplicacion");
  }

  public void contextInitialized(ServletContextEvent arg0) {

    try {
      String value = arg0.getServletContext().getInitParameter("log4j2");

      if ((null != value) && (0 < value.length())) {
        File config = new File(value);
        // Se configuró... peeero ¿existe?
        if (config.exists()) {
          LoggerContext context =
              (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);

          context.setConfigLocation(config.toURI());
          Logger logger = LogManager.getLogger(Launcher.class);

          logger.info("Log4j2 configurado desde [" + value + "]");
          logger.info("Se inicia la aplicacion");

          log = logger;

        } else {
          System.err.println("ERROR: Se configuró en el Context log4j2='" + value
              + "', pero el fichero NO EXISTE!!!");
        }
      }
    } catch (Exception x) {
      log.error("Al inicializar LOG4J2 de la aplicación", x);
    }

    // Factoria para LDAP
    try {
      String value = arg0.getServletContext().getInitParameter("daoLdapClass");

      if ((null != value) && (0 < value.length())) {
        Settings.setFactoryClass(value);
      }
    } catch (Exception x) {
      log.error("Al inicializar la instancia 'daoLdapClass'", x);
    }

    // Inicializar la factoria...
    try {
      String value = arg0.getServletContext().getInitParameter("daoLdapConfig");

      if ((null != value) && (0 < value.length())) {
        ObjectFactory factory = Settings.getFactoryDAC();
        factory.configure(value);
      }
    } catch (Exception x) {
      log.error("Al configurar la instancia 'daoLdapClass'", x);
    }
  }

}
