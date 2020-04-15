/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import SO.KreirajMenadzeraSO;
import SO.PretraziBendoveSO;
import SO.PretraziIzvodjaceSO;
import SO.PretraziNastupeSO;
import SO.PrijaviMenadzeraSO;
import SO.SistemOperation;
import SO.UcitajBendSO;
import SO.UcitajIzvodjacaSO;
import SO.UcitajListuBendovaSO;
import SO.UcitajListuIzvodjacaSO;
import SO.UcitajListuVrstaIzvodjacaSO;
import SO.UcitajNastupSO;
import SO.UpdateBendSO;
import SO.UpdateIzvodjacaSO;
import SO.UpdateNastupSO;
import SO.ZapamtiBendSO;
import SO.ZapamtiIzvodjacaSO;
import SO.ZapamtiNastupSO;
import java.util.List;
import model.Bend;
import model.Izvodjac;
import model.Menadzer;
import model.Nastup;
import model.VrstaIzvodjaca;
import util.State;

/**
 *
 * @author student1
 */
public class Controller {

    private static Controller instance;

    private Controller() {
        
    }
    
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public Menadzer loginUser(Menadzer menadzer) throws Exception{
        SistemOperation prijaviMenadzera = new PrijaviMenadzeraSO();
        return (Menadzer) prijaviMenadzera.execute(menadzer);
    }
    /**
     * ne radi nista za sada
     * @param menadzer 
     */
    public void logout(Menadzer menadzer) {
        //moze nesto da se desi ubuduce
    }

    public Menadzer saveMenadzer(Menadzer menadzerZaCuvanje) throws Exception{
        SistemOperation prijaviMenadzera = new KreirajMenadzeraSO();
        return (Menadzer) prijaviMenadzera.execute(menadzerZaCuvanje);
    }

    public List<VrstaIzvodjaca> getAllVrstaIzvodjaca() throws Exception{
        SistemOperation ucitaj = new UcitajListuVrstaIzvodjacaSO();
        return (List<VrstaIzvodjaca>) ucitaj.execute(new VrstaIzvodjaca());
    }

    public Izvodjac saveIzvodjac(Izvodjac izvodjacZaCuvanje) throws Exception{
        SistemOperation zapamtiIzvodjaca = new ZapamtiIzvodjacaSO();
        return (Izvodjac) zapamtiIzvodjaca.execute(izvodjacZaCuvanje);
    }

    public List<Izvodjac> getIzvodjacePoKriterijumu(String kriterijum) throws Exception {
        SistemOperation pretraziIzvodjace = new PretraziIzvodjaceSO();
        return (List<Izvodjac>) pretraziIzvodjace.execute(new Izvodjac(kriterijum));
    }

    public Izvodjac ucitajIzvodjaca(Izvodjac ucitajIzvodjaca) throws Exception {
        SistemOperation ucitajIzvodjacaSO = new UcitajIzvodjacaSO();
        return (Izvodjac) ucitajIzvodjacaSO.execute(ucitajIzvodjaca);
    }

    public Izvodjac updateIzvodjaca(Izvodjac izvodjacZaUpdate) throws Exception {
        SistemOperation updateIzvodjacaSO = new UpdateIzvodjacaSO();
        return (Izvodjac) updateIzvodjacaSO.execute(izvodjacZaUpdate);
    }

    public List<Izvodjac> ucitajListuIzvodjaca() throws Exception {
        SistemOperation ucitajListuIzvodjacaSO = new UcitajListuIzvodjacaSO();
        return (List<Izvodjac>) ucitajListuIzvodjacaSO.execute(new Izvodjac());
    }

    public Bend zapamtiBend(Bend bendZaCuvanje) throws Exception {
        SistemOperation zapamtiBendSO = new ZapamtiBendSO();
        return (Bend) zapamtiBendSO.execute(bendZaCuvanje);
    }

    public List<Bend> getBendovePoKriterijumu(String kriterijum) throws Exception {
        SistemOperation pretraziBendoveSO = new PretraziBendoveSO();
        return (List<Bend>) pretraziBendoveSO.execute(new Bend(kriterijum));
    }

    public Bend ucitajBend(Bend bend) throws Exception {
        SistemOperation ucitajBendSO = new UcitajBendSO();
        return (Bend) ucitajBendSO.execute(bend);
    }

    public Bend updateBend(Bend bendZaUpdate) throws Exception {
        SistemOperation updateBendSO = new UpdateBendSO();
        return (Bend) updateBendSO.execute(bendZaUpdate);
    }

    public List<Bend> ucitajListuBendova() throws Exception {
        SistemOperation ucitajListuBendovaSO = new UcitajListuBendovaSO();
        return (List<Bend>) ucitajListuBendovaSO.execute(new Bend());
    }

    public Nastup zapamtiNastup(Nastup nastupZaPamcenje) throws Exception {
        SistemOperation zapamtiNastupSO = new ZapamtiNastupSO();
        return (Nastup) zapamtiNastupSO.execute(nastupZaPamcenje);
    }

    public List<Nastup> getNastupePoKriterijumu(String kriterijum) throws Exception {
        SistemOperation pretraziBendoveSO = new PretraziNastupeSO();
        return (List<Nastup>) pretraziBendoveSO.execute(new Nastup(kriterijum));
    }

    public Nastup ucitajNastup(Nastup nastupZaUcitavanje) throws Exception {
        SistemOperation ucitajNastupSO = new UcitajNastupSO();
        return (Nastup) ucitajNastupSO.execute(nastupZaUcitavanje);
    }

    public Nastup updateIzvodjaca(Nastup nastupZaUpdate) throws Exception {
        SistemOperation updateNastupSO = new UpdateNastupSO();
        return (Nastup) updateNastupSO.execute(nastupZaUpdate);
    }


}
