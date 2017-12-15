
## Aplicación
Código fuente de la aplicación Java que ofrece consulta de usuarios de un directorio OpenLDAP via REST. 

El proyecto está organizado en cuatro módulos:

 1. ```modelo/``` (*```modelo-dac-XXX.jar```*) que implementa el modelo de objetos de las consultas a partir de un fichero ```.xsd```
 2. ```persistencia-ldap/``` (*```daoimpl-ldap-XXX.jar```*), que implementa el patrón DAO, para el modelo, usando las librerías http://www.ldaptive.org/ 
 3. ```rest/```  (*```dac-rest-XXX.jar```*), implementa el servicio REST de consulta de usuarios en LDAP en diferentes formatos: *.ldif*, *.json*, *.xml* y *.dsml*.  
 4. ```web/```  (*```ldap.war ```*), implementa la aplicación Web para Tomcat que permite consultas via REST  http://SERVIDOR:PUERTO/ldap/rest/dac/usuarios.json/LOGIN_O_NIF

----------

#### Compilación
La compilación requiere Java 1.7  o superior y Maven 3.3 o superior.  Para compilarlo ejecute:

```bash
git clone https://github.com/carm-es/rest-ldap.git

cd rest-ldap/aplicacion
mvn package
```
El *war* de la aplicación quedará  en ```web/target/ldap.war``` 

----------

#### Despliegue
La aplicación requiere Tomcat 7 o superior.  Para desplegarla:

 - Asegúrese de **haber creado los ficheros de configuración** en ```$CATALINA_BASE/conf/ldap``` 
 - **Copie** el fichero  ```web/target/ldap.war```  en el directorio  ```$CATALINA_BASE/webapps/``` 

----------

#### Comprobación
Acceda desde un navegador a  http://SERVIDOR:PUERTO/ldap/rest/dac/usuarios.json/LOGIN_O_NIF


