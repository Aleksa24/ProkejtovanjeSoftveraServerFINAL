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
public class PretraziIzvodjaceSO extends SistemOperation{

    @Override
    protected void preconditions(Object entity) throws Exception {
    }

    @Override
    protected Object executeOperation(Object izvodjacSaKriterijumom) throws Exception {
        return broker.getAll((DomainObject) izvodjacSaKriterijumom);
    }
    
}
