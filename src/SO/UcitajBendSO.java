/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SO;

import model.DomainObject;
import util.State;

/**
 *
 * @author Aleksa
 */
public class UcitajBendSO extends SistemOperation{

    @Override
    protected void preconditions(Object entity) throws Exception {
    }

    @Override
    protected Object executeOperation(Object entity) throws Exception {
        DomainObject o = (DomainObject) entity;
        o.setState(State.FindManyToMany);
        return broker.find(o);
    }
    
}
