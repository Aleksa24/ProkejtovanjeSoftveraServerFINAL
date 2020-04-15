/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SO;

import model.DomainObject;
import model.Izvodjac;
import model.Menadzer;
import model.Nastup;
import model.StavkaNastupa;
import util.State;

/**
 *
 * @author Aleksa
 */
public class ZapamtiNastupSO extends SistemOperation {

    @Override
    protected void preconditions(Object entity) throws Exception {
    }

    @Override
    protected Object executeOperation(Object entity) throws Exception {
        Nastup nastup = (Nastup) entity;
        nastup = (Nastup) broker.save(nastup);
        //mora da se ucita menadzer sa tim idMenadzer
        nastup.setMenadzer((Menadzer) broker.find(nastup.getMenadzer().appendState(State.Default)));

        for (StavkaNastupa stavkaNastupa : nastup.getListaStavki()) {//moram da pitam treba mi kasnije koji je tip da bih napravio objekat
            System.out.println("ZapamtiNastupSO: Kada se cuva ima ovu vrednost tipIzvodjaca: " + stavkaNastupa.getTipIzvodjaca());
//            if (stavkaNastupa.getIzvodjac() instanceof Izvodjac) {
//                tipIzvodjaca = "IZVODJAC";
//            } else {
//                tipIzvodjaca = "BEND";
//            }
            stavkaNastupa = (StavkaNastupa) broker.save(stavkaNastupa
                    .appendIdNastupa(nastup.getIdNastupa()));//dodajem id nastupa koji se kreirao da bi ubacio u tabelu
        }
        return nastup;
    }

}
