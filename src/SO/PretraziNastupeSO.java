/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SO;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.DomainObject;
import model.Menadzer;
import model.Nastup;

/**
 *
 * @author Aleksa
 */
public class PretraziNastupeSO extends SistemOperation{

    @Override
    protected void preconditions(Object entity) throws Exception {
        
    }

    @Override
    protected Object executeOperation(Object entity) throws Exception {
        List<DomainObject> pomocna = broker.getAll((DomainObject) entity);
        System.out.println("======dobijeni svi nastupi");
        List<Nastup> listaNastupa = new ArrayList<>();
        System.out.println("velicina liste: " + pomocna.size());
        for (DomainObject domainObject : pomocna) {
            Nastup nastup = (Nastup) domainObject;
            Menadzer menadzerSaSvimPodacima = (Menadzer) broker.find(nastup.getMenadzer());
            System.out.println("menadzeSaSvimPodacima: " + menadzerSaSvimPodacima);
            listaNastupa.add(nastup.appendMenadzer(menadzerSaSvimPodacima));
        }
        return listaNastupa;
    }
    
}
