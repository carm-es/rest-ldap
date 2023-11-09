package es.carm.openldap.dac.dao.entryldap;

import org.ldaptive.LdapAttribute;
import org.ldaptive.LdapEntry;
import es.carm.openldap.dac.Func;
import es.carm.openldap.dac.UsuarioDAC;

public class UserFactory {

  protected static boolean isEmpty(String str) {
    if ((null != str) && (!"".equalsIgnoreCase(str))) {
      return (0 >= str.trim().length());
    }
    return true;
  }

  public static LdapEntry toEntry(UsuarioDAC user) {
    LdapEntry retVal = null;

    if (null != user) {
      retVal = new LdapEntry(user.getDn());

      LdapAttribute objectClass = new LdapAttribute("objectClass");
      objectClass.addStringValue("top", "inetOrgPerson", "CARMfuncionario");
      retVal.addAttribute(objectClass);

      if (!isEmpty(user.getLogincarm())) {
        LdapAttribute attr = new LdapAttribute("uid");
        attr.addStringValue(user.getLogincarm());
        retVal.addAttribute(attr);
      }

      if (!isEmpty(user.getNombre())) {
        LdapAttribute attr = new LdapAttribute("givenName");
        attr.addStringValue(user.getNombre());
        retVal.addAttribute(attr);
      }
      if (!isEmpty(user.getApellido1())) {
        LdapAttribute attr = new LdapAttribute("carmAPELLIDO1");
        attr.addStringValue(user.getApellido1());
        retVal.addAttribute(attr);
      }
      if (!isEmpty(user.getApellido2())) {
        LdapAttribute attr = new LdapAttribute("carmAPELLIDO2");
        attr.addStringValue(user.getApellido2());
        retVal.addAttribute(attr);
      }
      if (!isEmpty(user.getApellidos())) {
        LdapAttribute attr = new LdapAttribute("sn");
        attr.addStringValue(user.getApellidos());
        retVal.addAttribute(attr);
      }
      if (!isEmpty(user.getCorreo())) {
        LdapAttribute attr = new LdapAttribute("mail");
        attr.addStringValue(user.getCorreo());
        retVal.addAttribute(attr);
      }
      if (!isEmpty(user.getTelefono())) {
        LdapAttribute attr = new LdapAttribute("telephoneNumber");
        attr.addStringValue(user.getTelefono());
        retVal.addAttribute(attr);
      }

      if (!isEmpty(user.getDni())) {
        LdapAttribute attr = new LdapAttribute("DNI");
        attr.addStringValue(user.getDni());
        retVal.addAttribute(attr);
      }

      String funcVal = "N";
      if (null != user.getEsFuncionario()) {
        if (Func.S.equals(user.getEsFuncionario())) {
          funcVal = "S";
        }
      }
      LdapAttribute attr = new LdapAttribute("carmFuncionario");
      attr.addStringValue(funcVal);
      retVal.addAttribute(attr);
    }
    return retVal;
  }

  public static UsuarioDAC fromEntry(LdapEntry entry) {
    UsuarioDAC retVal = null;

    if (null != entry) {
      boolean isUser = false;
      boolean isPerson = false;
      for (String obClass : entry.getAttribute("objectClass").getStringValues()) {
        if ("CARMfuncionario".equalsIgnoreCase(obClass)) {
          isUser = true;
        } else if ("inetOrgPerson".equalsIgnoreCase(obClass)) {
          isPerson = true;
        }
      }

      if ((isUser) && (isPerson)) {
        String DN = entry.getDn();
        String login = entry.getAttribute("uid").getStringValue();
        if ((!isEmpty(DN)) && (!isEmpty(login))) {

          retVal = new UsuarioDAC();

          retVal.setDn(DN);
          retVal.setLogincarm(login);

          try {
            if (!isEmpty(entry.getAttribute("givenName").getStringValue())) {
              retVal.setNombre(entry.getAttribute("givenName").getStringValue());
            }
          } catch (Exception x) {
          }

          try {
            if (!isEmpty(entry.getAttribute("carmAPELLIDO1").getStringValue())) {
              retVal.setApellido1(entry.getAttribute("carmAPELLIDO1").getStringValue());
            }
          } catch (Exception x) {
          }

          try {
            if (!isEmpty(entry.getAttribute("carmAPELLIDO2").getStringValue())) {
              retVal.setApellido2(entry.getAttribute("carmAPELLIDO2").getStringValue());
            }
          } catch (Exception x) {
          }

          try {
            if (!isEmpty(entry.getAttribute("sn").getStringValue())) {
              retVal.setApellidos(entry.getAttribute("sn").getStringValue());
            }
          } catch (Exception x) {
          }

          try {
            if (!isEmpty(entry.getAttribute("mail").getStringValue())) {
              retVal.setCorreo(entry.getAttribute("mail").getStringValue());
            }
          } catch (Exception x) {
          }

          try {
            if (!isEmpty(entry.getAttribute("telephoneNumber").getStringValue())) {
              retVal.setTelefono(entry.getAttribute("telephoneNumber").getStringValue());
            }
          } catch (Exception x) {
          }

          try {
            if (!isEmpty(entry.getAttribute("DNI").getStringValue())) {
              retVal.setDni(entry.getAttribute("DNI").getStringValue());
            }
          } catch (Exception x) {
          }

          Func val = Func.N;
          try {
            if (!isEmpty(entry.getAttribute("carmFuncionario").getStringValue())) {
              if ("S".equalsIgnoreCase(entry.getAttribute("carmFuncionario").getStringValue())) {
                val = Func.S;
              }
            }
          } catch (Exception x) {
          }
          retVal.setEsFuncionario(val);
        }
      }
    }
    return retVal;
  }
}
