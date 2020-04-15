/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SO;

import java.util.ArrayList;
import java.util.List;
import model.DomainObject;
import model.Izvodjac;
import model.Nastup;
import model.StavkaNastupa;
import util.State;

/**
 *
 * @author Aleksa
 */
public class UcitajNastupSO extends SistemOperation {

    @Override
    protected void preconditions(Object entity) throws Exception {

    }

    @Override
    protected Object executeOperation(Object entity) throws Exception {
        Nastup nastupZaBazu = (Nastup) entity;
        nastupZaBazu.setState(State.Default);//stavljam meni odgovarajuci tip
        Nastup ucitaniNastup = (Nastup) broker.find(nastupZaBazu);
        
        //mora za svaku stavku da se ucita izvodjac
        List<DomainObject> ucitaneStavkeIzvodjaca = 
                broker.getAll(new StavkaNastupa("idNastupa = " + ucitaniNastup.getIdNastupa()));
        List<StavkaNastupa> popunjeneStavkeNastupa = new ArrayList<>();
        for (DomainObject domainObject : ucitaneStavkeIzvodjaca) {
            popunjeneStavkeNastupa.add((StavkaNastupa) domainObject);
        }
        //sada mora da se ucitaju odgovarajuci izvodjaci iz baze
        for (StavkaNastupa stavkaNastupa : popunjeneStavkeNastupa) {
            stavkaNastupa.getIzvodjac().setState(State.Default);
            stavkaNastupa.setIzvodjac(
                broker.find(stavkaNastupa.getIzvodjac()));
        }
        
        return ucitaniNastup.appendListaStavki(popunjeneStavkeNastupa);
    }

}
