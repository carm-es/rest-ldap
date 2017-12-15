
## Ficheros de configuración

Estos son los ficheros de configuración del proyecto. Se configuran en el contexto de la aplicación (```context.xml```) y por defecto se configuran en el directorio ``` $CATALINA_BASE/conf/ldap ``` que deberá crearse.

 - ```config-dac.ldap.properties```  Fichero donde configurar la URL del directorio LDAP al que conectar, las credencias, y el *Distinguised Name* de la base donde buscar los usuarios para servir vía REST.
 - ```log4j2.xml``` Fichero XML de configuración Log4j2 para gestionar los logs de la aplicación. Por defecto se escribirá en ```$CATALINA_BASE/logs/ldaplog.log```  con nivel ```DEBUG```

La ubicación de estos ficheros se especifica como parámetros en el ```context.xml```:

```xml
<Parameter 
  name="daoLdapConfig"
  value="${catalina.base}/conf/ldap/config-dac.ldap.properties" />

<Parameter
  name="log4j2"
  value="${catalina.base}/conf/ldap/log4j2.xml" />

```

