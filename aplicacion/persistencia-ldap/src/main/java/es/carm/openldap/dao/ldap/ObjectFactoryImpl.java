package es.carm.openldap.dao.ldap;

import org.ldaptive.ConnectionConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ldaptive.BindConnectionInitializer;
import org.ldaptive.Credential;
import org.ldaptive.DefaultConnectionFactory;
import org.ldaptive.pool.BlockingConnectionPool;
import org.ldaptive.pool.PooledConnectionFactory;
import es.carm.openldap.dac.UsuarioDAC;
import es.carm.openldap.dao.FactoryDao;
import es.carm.openldap.dao.ldap.impl.UserFactoryDAOImpl;

public class ObjectFactoryImpl implements es.carm.openldap.dao.ObjectFactory, Serializable {

  static Logger log = LogManager.getLogger(ObjectFactoryImpl.class);

  private static final long serialVersionUID = -8380017861016136801L;

  private static final String CONFIG_DEFAULT_FILE = "ldap-config.properties";

  private BlockingConnectionPool pool = null;

  private String config_Ldap_URL = null;
  private String config_Bind_DN = null;
  private String config_Bind_Password = null;
  private String config_BaseDN = null;
  private String config_FilterSearchUsers = null;
  private boolean useSSL = false;
  private long connectionTimeout = 10000;

  public String toString() {
    String retVal = "";

    retVal += "[LDAP-CONECTION init='" + (pool != null) + "']\n";
    retVal += "  config.ldap.url=" + config_Ldap_URL + "\n";
    retVal += "  config.ldap.bind.user=" + config_Bind_DN + "\n";
    retVal += "  config.ldap.bind.password=" + config_Bind_Password + "\n";
    retVal += "  config.ldap.base=" + config_BaseDN + "\n";
    retVal += "  config.ldap.filter.search.users=" + config_FilterSearchUsers + "\n";
    retVal += "  config.ldap.connection.timeout=" + connectionTimeout + "\n";
    retVal += "  config.ldap.connection.use_ssl=" + useSSL + "\n";

    return retVal;
  }

  private void initialize() {
    if (null != pool) {
      try {
        pool.close();
        pool = null;
      } catch (Exception x) {
      }
    }
    BindConnectionInitializer bind = new BindConnectionInitializer();
    bind.setBindDn(config_Bind_DN);
    bind.setBindCredential(new Credential(config_Bind_Password));

    ConnectionConfig cc = new ConnectionConfig();
    cc.setLdapUrl(config_Ldap_URL);
    cc.setConnectionInitializer(bind);
    cc.setUseSSL(useSSL);
    cc.setConnectTimeout(connectionTimeout);

    DefaultConnectionFactory connectionFactory = new DefaultConnectionFactory();
    connectionFactory.setConnectionConfig(cc);

    pool = new BlockingConnectionPool(connectionFactory);
    pool.initialize();
  }

  private boolean configurate(Properties prop) {
    config_Ldap_URL = prop.getProperty("config.ldap.url");
    config_Bind_DN = prop.getProperty("config.ldap.bind.user");
    config_Bind_Password = prop.getProperty("config.ldap.bind.password");
    config_BaseDN = prop.getProperty("config.ldap.base");
    config_FilterSearchUsers = prop.getProperty("config.ldap.filter.search.users");
    try {
      connectionTimeout = Long.parseLong(prop.getProperty("config.ldap.connection.timeout"));
    } catch (Exception x) {
    }

    if (("yes".equalsIgnoreCase(prop.getProperty("config.ldap.connection.use_ssl")))
        || ("si".equalsIgnoreCase(prop.getProperty("config.ldap.connection.use_ssl")))) {
      useSSL = true;
    }

    return (null != config_Ldap_URL) && (null != config_Bind_DN) && (null != config_Bind_Password)
        && (null != config_BaseDN);
  }

  public ObjectFactoryImpl(Properties prop) {
    if (this.configurate(prop)) {
      this.initialize();
    }
  }

  public ObjectFactoryImpl(String ldapUrl, String dnLogin, String password, String baseLDAP) {
    config_Ldap_URL = ldapUrl;
    config_Bind_DN = dnLogin;
    config_Bind_Password = password;
    config_BaseDN = baseLDAP;

    this.initialize();
  }

  public ObjectFactoryImpl() {
    boolean init = false;
    Properties properties = new Properties();
    try {
      InputStream stream = this.getClass().getResourceAsStream("/" + CONFIG_DEFAULT_FILE);
      if (null == stream) {
        stream = this.getClass().getClassLoader().getResourceAsStream(CONFIG_DEFAULT_FILE);
      }
      if (null != stream) {
        properties.load(stream);
        init = this.configurate(properties);
      } else {
        log.error("ERROR: ObjectFactoryImpl sin configurar!!!!");
      }

    } catch (Exception x) {
      log.error("Al inicializar la factoria", x);
    }
    if (init) {
      this.initialize();
    }
  }

  public FactoryDao<UsuarioDAC> getUsuariosDAO() {
    return new UserFactoryDAOImpl(new PooledConnectionFactory(pool), config_BaseDN,
        config_FilterSearchUsers);
  }

  public void configure(String fileProperties) {
    boolean init = false;
    Properties properties = new Properties();
    try {
      File config = new File(fileProperties);
      if (config.exists()) {
        InputStream stream = null;
        try {
          stream = new FileInputStream(config);
          properties.load(stream);
          init = this.configurate(properties);

        } catch (Exception x) {
          log.error("Al configurar la factoria", x);
        } finally {
          if (null != stream) {
            stream.close();
          }
        }
      } else {
        log.error("No se pudo configurar la aplicación desde el fichero '" + fileProperties + "'");
      }

    } catch (Exception x) {
      log.error("Al leer la configuración de la factoria", x);
    }
    if (init) {
      log.info("La aplicación se configuró desde el fichero '" + fileProperties + "'");
      this.initialize();
    }

  }

}
