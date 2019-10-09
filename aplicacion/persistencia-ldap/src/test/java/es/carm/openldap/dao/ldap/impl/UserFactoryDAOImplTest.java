package es.carm.openldap.dao.ldap.impl;

import static org.junit.Assert.assertTrue;
import java.io.File;
import org.junit.Test;
import es.carm.openldap.dac.UsuarioDAC;
import es.carm.openldap.dao.ldap.ObjectFactoryImpl;

public class UserFactoryDAOImplTest {

  public static UsuarioDAC loadFromFile(String fileTest) {
    UserFactoryDAOImplTest dummy = new UserFactoryDAOImplTest();

    ClassLoader classLoader = dummy.getClass().getClassLoader();
    File file = new File(classLoader.getResource(fileTest).getFile());
    es.carm.openldap.dac.dao.ldif.UserFactory ldifFactory =
        new es.carm.openldap.dac.dao.ldif.UserFactory();
    return ldifFactory.fromFile(file);
  }

  public static String toXML(UsuarioDAC user) {
    es.carm.openldap.dac.dao.xml.UserFactory xmlFactory =
        new es.carm.openldap.dac.dao.xml.UserFactory();
    return xmlFactory.toString(user);
  }

  @Test
  public void busqueda_por_nombre() {
    ObjectFactoryImpl factory = new ObjectFactoryImpl();

    es.carm.openldap.dao.FactoryDao<UsuarioDAC> userFactory = factory.getUsuariosDAO();

    UsuarioDAC user = null;

    try {
      user = userFactory.get("ibm78m");
    } catch (es.carm.openldap.NoDataFound e) {
      e.printStackTrace(System.err);
    } catch (es.carm.openldap.ConnectionError e) {
      e.printStackTrace(System.err);
    }

    assertTrue(null != user);
    // System.err.println( toXML(user) );

  }

  @Test
  public void busqueda_por_nif() {
    ObjectFactoryImpl factory = new ObjectFactoryImpl();

    es.carm.openldap.dao.FactoryDao<UsuarioDAC> userFactory = factory.getUsuariosDAO();

    UsuarioDAC user = null;

    try {
      user = userFactory.get("34823178M");
    } catch (es.carm.openldap.NoDataFound e) {
      e.printStackTrace(System.err);
    } catch (es.carm.openldap.ConnectionError e) {
      e.printStackTrace(System.err);
    }

    assertTrue(null != user);
    // System.err.println( toXML(user) );

  }

}
