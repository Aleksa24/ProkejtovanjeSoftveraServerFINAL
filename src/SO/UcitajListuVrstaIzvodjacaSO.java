/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SO;

import model.DomainObject;
import model.VrstaIzvodjaca;

/**
 *
 * @author Aleksa
 */
public class UcitajListuVrstaIzvodjacaSO extends SistemOperation{

    @Override
    protected void preconditions(Object entity) throws Exception {
    }

    @Override
    protected Object executeOperation(Object entity) throws Exception {
        return broker.getAll((DomainObject) entity);
    }
    
}
