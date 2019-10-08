package es.carm.openldap.restbox.dac;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.carm.openldap.ConnectionError;
import es.carm.openldap.NoDataFound;
import es.carm.openldap.dac.Func;
import es.carm.openldap.dac.UsuarioDAC;
import es.carm.openldap.dao.FactoryDao;
import es.carm.openldap.dao.ObjectFactory;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

@Stateless
@Path("/dac")
public class ApiRest {

    static Logger log = LogManager.getLogger(ApiRest.class);

    @GET
    @Path("/usuarios{formato:(.json|.xml|.dsml|.ldif)*}/{userid:.*}")
    @Produces({ "text/json", "application/json", "text/xml", "application/xml", "application/directory", "text/ldif",
            "text/dsml" })
    public Response getUsers(@HeaderParam("Accept") String header, @PathParam("userid") String id,
            @PathParam("formato") String ext, @QueryParam("offset") int offsetParam,
            @QueryParam("limit") int limitParam, @QueryParam("order") String orderParam) {

        // Preparar la respuesta, en base a lo que acepta el cliente, y la extensión que nos han pedido...
        String mimeResult = Settings.getMimeResult(header, ext);
        String contentType = mimeResult + "; charset=utf-8";

        // Comprobar si tenemos factoria configurada...
        ObjectFactory instFactory = Settings.getFactoryDAC();
        if (null == instFactory) {
            return Response.serverError().type(mimeResult)
                    .entity(Settings.getErrorResponse("No se configuró LDAP", mimeResult))
                    .header("Content-Type", contentType).header("Access-Control-Allow-Origin", "*").build();
        }

        // Obtener la factoria de grupos...
        FactoryDao<UsuarioDAC> factory = instFactory.getUsuariosDAO();
        if (null == factory) {
            return Response.serverError().type(mimeResult)
                    .entity(Settings.getErrorResponse("No se pudo obtener FactoryDao<UsuarioDAC> de la configuración",
                            mimeResult))
                    .header("Content-Type", contentType).header("Access-Control-Allow-Origin", "*").build();
        }

        // Saber si nos han pasado o no, el grupo...
        List<UsuarioDAC> lista = null;

        if ((null != id) && (0 < id.trim().length())) {
            String userDecoded = id;
            try {
                userDecoded = new String(URLDecoder.decode(id, "ISO-8859-1").getBytes("ISO-8859-1"));
            } catch (Exception x) {
            }
            // Buscar solo uno...
            UsuarioDAC ldapUsr = null;

            try {
                ldapUsr = factory.get(id);

            } catch (NoDataFound e) {

            } catch (ConnectionError e) {
                log.error("Error de conexion al directorio", e);
                return Response.serverError().type(mimeResult)
                        .entity(Settings.getErrorResponse("Error al buscar '" + id + "' en el directorio", mimeResult))
                        .header("Content-Type", contentType).header("Access-Control-Allow-Origin", "*").build();

            } catch (Exception x) {
                log.error("Error indeterminado, quizás se deba a que no está configurado el POOL", x);
                return Response.serverError().type(mimeResult)
                        .entity(Settings.getErrorResponse("La aplicación no está bien configurada", mimeResult))
                        .header("Content-Type", contentType).header("Access-Control-Allow-Origin", "*").build();
            }

            if (null == ldapUsr) {
                // Vale, a ver si lo pasaron codificao (con espacios, tildes...)
                try {
                    ldapUsr = factory.get(userDecoded);
                } catch (Exception e2) {
                }
            }

            if (null != ldapUsr) {
                lista = new ArrayList<UsuarioDAC>();
                lista.add(ldapUsr);
            }
        }

        if ((null != lista) && (0 < lista.size())) {
            return Response.ok(getResult(lista.get(0), mimeResult), mimeResult).header("Content-Type", contentType)
                    .header("Access-Control-Allow-Origin", "*").build();

        }

        // Salida sin resultado...
        return Response.status(Response.Status.NOT_FOUND).type(mimeResult)
                .entity(Settings.getErrorResponse("No se encontró '" + id + "' en el directorio", mimeResult))
                .header("Content-Type", contentType).header("Access-Control-Allow-Origin", "*").build();

    }

    private String getResult(UsuarioDAC user, String media) {

        if (Settings.MIME_DSML.equalsIgnoreCase(media)) {
            return es.carm.openldap.dac.dao.dsml.UserFactory.serialize(user);

        } else if (Settings.MIME_LDIF.equalsIgnoreCase(media)) {
            return es.carm.openldap.dac.dao.ldif.UserFactory.serialize(user);

        } else if (Settings.MIME_XML.equalsIgnoreCase(media)) {
            return es.carm.openldap.dac.dao.xml.UserFactory.serialize(user);
        }
        // return es.carm.openldap.dac.dao.json.UserFactory.serialize(user);
        return getJson(user);
    }

    private static boolean isEmpty(String cad) {
        if (null != cad) {
            if (!"null".equalsIgnoreCase(cad)) {
                if (0 < cad.trim().length()) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getJson(UsuarioDAC user) {
        String retVal = null;
        if (null != user) {
            retVal = "{";
            if (!isEmpty(user.getLogincarm())) {
                retVal += "\"logincarm\":\"" + user.getLogincarm() + "\"";
            }
            if (!isEmpty(user.getNombre())) {
                if (2 < retVal.length()) {
                    retVal += ",";
                }
                retVal += "\"nombre\":\"" + user.getNombre() + "\"";
            }
            if (!isEmpty(user.getApellido1())) {
                if (2 < retVal.length()) {
                    retVal += ",";
                }
                retVal += "\"apellido1\":\"" + user.getApellido1() + "\"";
            }
            if (!isEmpty(user.getApellido2())) {
                if (2 < retVal.length()) {
                    retVal += ",";
                }
                retVal += "\"apellido2\":\"" + user.getApellido2() + "\"";
            }
            if ((!isEmpty(user.getApellido2())) && (!isEmpty(user.getApellido2()))) {
                if (2 < retVal.length()) {
                    retVal += ",";
                }
                retVal += "\"apellidos\":\"" + user.getApellido1() + " " + user.getApellido2() + "\"";
            }
            if (!isEmpty(user.getDni())) {
                if (2 < retVal.length()) {
                    retVal += ",";
                }
                retVal += "\"dni\":\"" + user.getDni() + "\"";
            }
            if (!isEmpty(user.getCorreo())) {
                if (2 < retVal.length()) {
                    retVal += ",";
                }
                retVal += "\"correo\":\"" + user.getCorreo() + "\"";
            }
            if (!isEmpty(user.getTelefono())) {
                if (2 < retVal.length()) {
                    retVal += ",";
                }
                retVal += "\"telefono\":\"" + user.getTelefono() + "\"";
            }
            if (null != user.getEsFuncionario()) {
                if (2 < retVal.length()) {
                    retVal += ",";
                }
                if (Func.N.equals(user.getEsFuncionario())) {
                    retVal += "\"tipousuario\":\"Persona física\"";
                } else {
                    retVal += "\"tipousuario\":\"Funcionario\"";
                }
                retVal += ",\"esFuncionario\":\"" + user.getEsFuncionario() + "\"";
            }
            retVal += "}";
        }
        return retVal;
    }

}
