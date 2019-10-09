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

  /*
   * dn: uid=ibm78m,ou=usuariosCARM,dc=CARM,dc=ES displayName: BARRANCOS MARTINEZ IGNACIO givenName:
   * IGNACIO objectClass: top objectClass: person objectClass: inetOrgPerson objectClass:
   * organizationalPerson objectClass: posixAccount objectClass: shadowAccount objectClass:
   * CARMfuncionario carmAPELLIDO2: MARTINEZ uid: ibm78m carmAPELLIDO1: BARRANCOS mail:
   * ignacio.barrancos@carm.es cn: ibm78m DNI: 34823178M gecos: BARRANCOS MARTINEZ IGNACIO
   * description: Cuenta de acceso para ibm78m homeDirectory: /home/ibm78m sn: BARRANCOS MARTINEZ
   * loginShell: /bin/bash gidNumber: 100 uidNumber: 113119 carmActivo: A carmCodPuesto: PG00066
   * carmResponsableOrg: N Localidad: MURCIA carmFuncionario: S Edificio: EDIFICIO ADMINISTRATIVO
   * INFANTE telephoneNumber: 986357898 roomNumber: P4 - 437 CodigoPostal: 30011 st: 11
   * carmNOrgCompleta: 1;94;116;2622;1467;2777 carmN1AdOrgtxt: COMUNIDAD AUTONOMA DE MURCIA
   * carmN4AdOrgtxt: CENTRO REGIONAL DE INFORMATICA carmN5AdOrgtxt: SERVICIO INTEGRACION
   * APLICACIONES CORPORATIVAS carmDenPuesto: ANALISTA DE APLICACIONES carmN2AdOrgtxt: CONSEJERIA
   * HACIENDA Y ADMINISTRACIONES PUBLICAS carmOrgCompleta: COMUNIDAD AUTONOMA DE MURCIA;CONSEJERIA
   * HACIENDA Y ADMINISTRA CIONES PUBLICAS;DIRECCION GENERAL INFORMATICA, PATRIMONIO Y
   * TELECOM.;CENTRO R EGIONAL DE INFORMATICA;SERVICIO INTEGRACION APLICACIONES CORPORATIVAS;ESTRUC.
   * PROV. (HORAGES) DE PLATAFORMAS carmN3AdOrgtxt: DIRECCION GENERAL INFORMATICA, PATRIMONIO Y
   * TELECOM. userPassword:: e0NSWVBUfSQ1JFVFV3hsSmRUJHhKY3E2MFlRY21BWS91NGJ2N1VUU1haNDZCV1Q
   * 1VEgzdmFIeS56ejF5NTY= ntPassword: ddddddddddddd
   */

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
