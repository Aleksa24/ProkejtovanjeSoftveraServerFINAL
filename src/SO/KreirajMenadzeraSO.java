/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SO;

import model.DomainObject;

/**
 *
 * @author Aleksa
 */
public class KreirajMenadzeraSO extends SistemOperation{

    /**
     * za sada nista nije implementirano
     * @param entity
     * @throws Exception 
     */
    @Override
    protected void preconditions(Object entity) throws Exception {
        //ne znam kakva provera bi mogla da se izvrsi za sada
    }

    @Override
    protected Object executeOperation(Object entity) throws Exception {
        return broker.save((DomainObject) entity);
    }
    
}
