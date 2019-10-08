package es.carm.openldap.dao;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import es.carm.openldap.dac.UsuarioDAC;

public class UserTest {

    public static UsuarioDAC loadFromFile(String fileTest) {
        UserTest dummy = new UserTest();

        ClassLoader classLoader = dummy.getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileTest).getFile());
        es.carm.openldap.dac.dao.ldif.UserFactory ldifFactory = new es.carm.openldap.dac.dao.ldif.UserFactory();
        return ldifFactory.fromFile(file);
    }

    public static String toXML(UsuarioDAC user) {
        es.carm.openldap.dac.dao.xml.UserFactory xmlFactory = new es.carm.openldap.dac.dao.xml.UserFactory();
        return xmlFactory.toString(user);
    }

    @Test
    public void carga_desde_fichero() {
        UsuarioDAC ejemplo = loadFromFile("user1.ldif");

        // System.err.println( "XML=\n"+ toXML(ejemplo) +"\n\n" );
        assertTrue(null != ejemplo);

        es.carm.openldap.dac.dao.dsml.UserFactory dsmlFactory = new es.carm.openldap.dac.dao.dsml.UserFactory();
        String dsml = dsmlFactory.toString(ejemplo);
        // System.err.println( "DSML=\n"+ dsml +"\n\n" );
        assertTrue((null != dsml) && (0 < dsml.length()));

        es.carm.openldap.dac.dao.json.UserFactory jsonFactory = new es.carm.openldap.dac.dao.json.UserFactory();
        String json = jsonFactory.toString(dsmlFactory.fromString(dsml));
        // System.err.println( "JSON=\n"+ dsml +"\n\n" );
        assertTrue((null != json) && (0 < json.length()));

        es.carm.openldap.dac.dao.ldif.UserFactory ldifFactory = new es.carm.openldap.dac.dao.ldif.UserFactory();
        String ldif = ldifFactory.toString(dsmlFactory.fromString(dsml));
        // System.err.println( "LDIF=\n"+ ldif +"\n\n" );
        assertTrue((null != ldif) && (0 < ldif.length()));

    }

    @Test
    public void carga_desde_fichero2() {
        UsuarioDAC ejemplo = loadFromFile("user2.ldif");

        // System.err.println( "XML=\n"+ toXML(ejemplo) +"\n\n" );
        assertTrue(null != ejemplo);

        es.carm.openldap.dac.dao.dsml.UserFactory dsmlFactory = new es.carm.openldap.dac.dao.dsml.UserFactory();
        String dsml = dsmlFactory.toString(ejemplo);
        // System.err.println( "DSML=\n"+ dsml +"\n\n" );
        assertTrue((null != dsml) && (0 < dsml.length()));

        es.carm.openldap.dac.dao.json.UserFactory jsonFactory = new es.carm.openldap.dac.dao.json.UserFactory();
        String json = jsonFactory.toString(dsmlFactory.fromString(dsml));
        // System.err.println( "JSON=\n"+ dsml +"\n\n" );
        assertTrue((null != json) && (0 < json.length()));

        es.carm.openldap.dac.dao.ldif.UserFactory ldifFactory = new es.carm.openldap.dac.dao.ldif.UserFactory();
        String ldif = ldifFactory.toString(dsmlFactory.fromString(dsml));
        // System.err.println( "LDIF=\n"+ ldif +"\n\n" );
        assertTrue((null != ldif) && (0 < ldif.length()));

    }

}
