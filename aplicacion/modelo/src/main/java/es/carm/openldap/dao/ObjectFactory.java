package es.carm.openldap.dao;

import es.carm.openldap.dac.UsuarioDAC;

public interface ObjectFactory {
    public FactoryDao<UsuarioDAC> getUsuariosDAO();

    public void configure(String fileProperties);
}
