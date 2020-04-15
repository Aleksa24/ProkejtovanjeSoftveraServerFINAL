/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SO;

import model.DomainObject;
import model.Menadzer;

/**
 *
 * @author Aleksa
 */
public class PrijaviMenadzeraSO extends SistemOperation{

    /**
     * za sada nema preconditions;
     * @param entity
     * @throws Exception 
     */
    @Override
    protected void preconditions(Object entity) throws Exception {
//        Menadzer menadzerZaLogin = (Menadzer) entity;
    }

    /**
     * 
     * @param entity Menadzer za proveru
     * @return nadjen Menadzer u bazi
     * @throws Exception 
     */
    @Override
    protected Object executeOperation(Object entity) throws Exception {
        return broker.find((DomainObject) entity);
    }
    
}
