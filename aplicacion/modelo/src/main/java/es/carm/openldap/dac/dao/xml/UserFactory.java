package es.carm.openldap.dac.dao.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import es.carm.openldap.Factory;
import es.carm.openldap.dac.UsuarioDAC;

public final class UserFactory implements Factory<UsuarioDAC>, Serializable {

  private static final long serialVersionUID = 4252257301230055829L;

  public UsuarioDAC fromString(String xmlData) {
    try {
      JAXBContext jc = JAXBContext.newInstance(UsuarioDAC.class);
      Unmarshaller u = jc.createUnmarshaller();
      return (UsuarioDAC) u.unmarshal(new StringReader(xmlData));
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return null;
  }

  public UsuarioDAC fromFile(File xmlFile) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(xmlFile));
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      while (line != null) {
        sb.append(line);
        sb.append("\n");
        line = br.readLine();
      }
      return fromString(sb.toString());

    } catch (Exception x) {
      System.err.println("ERROR: En la lectura del fichero '" + xmlFile + "'");
      x.printStackTrace(System.err);

    } finally {
      if (null != br) {
        try {
          br.close();
        } catch (Exception x) {
        }
      }
    }
    return null;
  }

  public UsuarioDAC fromFile(String xmlFile) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(xmlFile));
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      while (line != null) {
        sb.append(line);
        sb.append("\n");
        line = br.readLine();
      }
      return fromString(sb.toString());

    } catch (Exception x) {
      System.err.println("ERROR: En la lectura del fichero '" + xmlFile + "'");
      x.printStackTrace(System.err);

    } finally {
      if (null != br) {
        try {
          br.close();
        } catch (Exception x) {
        }
      }
    }
    return null;
  }

  public UsuarioDAC fromURL(String urlFile) {
    BufferedReader in = null;
    String content = "";
    try {
      URL oracle = new URL(urlFile);
      in = new BufferedReader(new InputStreamReader(oracle.openStream()));

      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        content += inputLine;
      }
      return fromString(content);

    } catch (Exception x) {

    } finally {
      if (null != in) {
        try {
          in.close();
        } catch (Exception v) {
        }
      }
    }
    return null;
  }

  private String getXML(UsuarioDAC user, boolean xmlDeclare) {
    try {
      JAXBContext jc = JAXBContext.newInstance(UsuarioDAC.class);

      Marshaller marshaller = jc.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", xmlDeclare);
      StringWriter writer = new StringWriter();
      marshaller.marshal(user, writer);
      return writer.toString();
    } catch (JAXBException e) {
      e.printStackTrace(System.err);
    }
    return null;
  }

  public String toString(UsuarioDAC user) {
    return getXML(user, false);
  }

  public static String serialize(UsuarioDAC object) {
    UserFactory factory = new UserFactory();
    return factory.getXML(object, true);
  }

}
