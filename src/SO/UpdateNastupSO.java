/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SO;

import java.util.ArrayList;
import java.util.List;
import model.DomainObject;
import model.Nastup;
import model.StavkaNastupa;

/**
 *
 * @author Aleksa
 */
public class UpdateNastupSO extends SistemOperation {

    @Override
    protected void preconditions(Object entity) throws Exception {
        
    }

    @Override
    protected Object executeOperation(Object nastupDO) throws Exception {
        Nastup nastupZaUpdate = (Nastup) nastupDO;
        List<StavkaNastupa> listaZaUpdate = nastupZaUpdate.getListaStavki();
        Nastup updatedNastup = (Nastup) broker.update(nastupZaUpdate);
        //prvo izbrisem stavke pa ubacim opet
        List<DomainObject> stareStavkeZaBrisanje = broker.getAll(new StavkaNastupa("idNastupa = " + updatedNastup.getIdNastupa()));
        
        //brisanje
        for (DomainObject stareStavke : stareStavkeZaBrisanje) {
            broker.delete(stareStavke);
        }
        //ubacivanje novih stavki
        List<StavkaNastupa> listaNovihStavki = new ArrayList<>();
        for (StavkaNastupa noveStavke : listaZaUpdate) {
            listaNovihStavki.add((StavkaNastupa) broker.save(noveStavke.appendIdNastupa(updatedNastup.getIdNastupa())));
        }
        
        return updatedNastup.appendListaStavki(listaNovihStavki);//dodati nove stavke
    }
    
}
