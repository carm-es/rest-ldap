package es.carm.openldap.dao.ldap.impl;

import org.ldaptive.Connection;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;
import org.ldaptive.ReturnAttributes;
import org.ldaptive.SearchOperation;
import org.ldaptive.SearchRequest;
import org.ldaptive.SearchResult;
import org.ldaptive.ad.handler.ObjectGuidHandler;
import org.ldaptive.ad.handler.ObjectSidHandler;
import org.ldaptive.pool.PooledConnectionFactory;

import es.carm.openldap.ConnectionError;
import es.carm.openldap.NoDataFound;
import es.carm.openldap.dac.UsuarioDAC;
import es.carm.openldap.dao.FactoryDao;

public class UserFactoryDAOImpl implements FactoryDao<UsuarioDAC> {

    private PooledConnectionFactory connFactory = null;
    private String baseDN = null;
    private String filter = "(&(objectClass=CARMfuncionario)(objectClass=inetOrgPerson)(|(cn=%%ID%%)(DNI=%%ID%%)(uid=%%ID%%)))";

    protected UserFactoryDAOImpl(String baseLDAP) {
        baseDN = baseLDAP;
    }

    public UserFactoryDAOImpl(PooledConnectionFactory conFactory, String baseLDAP, String filterLDAP) {
        connFactory = conFactory;
        baseDN = baseLDAP;
        if ((null != filterLDAP) && (0 < filterLDAP.length())) {
            filter = filterLDAP;
        }
    }

    private String resolveFilter(String id) {
        String retVal = filter;
        if ((null != filter) && (0 < filter.length())) {
            retVal = filter.replaceAll("%%ID%%", id);
        }
        return retVal;
    }

    private UsuarioDAC getByFilter(Connection conn, String filter) throws ConnectionError {
        UsuarioDAC retVal = null;
        try {
            SearchOperation search = new SearchOperation(conn);
            SearchRequest request = new SearchRequest(baseDN, filter);
            request.setReturnAttributes(ReturnAttributes.ALL.value());
            request.setSearchEntryHandlers(new ObjectSidHandler(), new ObjectGuidHandler());

            SearchResult result = search.execute(request).getResult();
            for (LdapEntry entry : result.getEntries()) {
                retVal = es.carm.openldap.dac.dao.entryldap.UserFactory.fromEntry(entry);
            }

        } catch (LdapException e) {
            throw new ConnectionError(e);
        }
        return retVal;
    }

    public UsuarioDAC get(String id) throws NoDataFound, ConnectionError {
        UsuarioDAC retVal = null;

        Connection conn = null;
        try {
            conn = connFactory.getConnection();
        } catch (LdapException e) {
            throw new ConnectionError(e);
        }
        if (null != conn) {
            try {
                conn.open();
            } catch (LdapException e) {
                throw new ConnectionError(e);
            }
            try {
                retVal = this.getByFilter(conn, resolveFilter(id));
            } finally {
                try {
                    conn.close();
                } catch (Exception x) {
                }
            }
        }
        if (null == retVal) {
            throw new NoDataFound();
        }
        return retVal;
    }

}
