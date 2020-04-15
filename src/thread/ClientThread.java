package thread;

import model.Menadzer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Bend;
import model.Izvodjac;
import model.Nastup;
import model.VrstaIzvodjaca;
import transfer.RequestObject;
import transfer.ResponseObject;
import util.Operation;
import util.ResponseStatus;

/**
 *
 * @author Aleksa
 */
public class ClientThread extends Thread {

    private final Socket socket;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    //services:
    private Menadzer loginMenadzer;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

    }
    
    public Socket getSocket() {
        return socket;
    }

    public Menadzer getLoginUser() {
        return loginMenadzer;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                RequestObject requestObject = (RequestObject) objectInputStream.readObject();
                ResponseObject responseObject = handleRequest(requestObject);
                objectOutputStream.writeObject(responseObject);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
                try {
                    socket.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }

    private ResponseObject handleRequest(RequestObject requestObject) {
        int operation = requestObject.getOperation();
        System.out.println(operation);
        switch (operation) {
            case Operation.OPERATION_LOGIN:
                return login((Menadzer) requestObject.getData());
            case Operation.OPERATION_LOGOUT_MENADZER:
                return logout((Menadzer) requestObject.getData());
            case Operation.OPERATION_SAVE_MENADZER:
                return saveMenadzer((Menadzer) requestObject.getData());
            case Operation.OPERATION_GET_ALL_VRSTAIZVODJACA:
                return getAllVrstaIzvodjaca();
            case Operation.OPERATION_SAVE_IZVODJAC:
                return saveIzvodjac((Izvodjac) requestObject.getData());
            case Operation.OPERATION_PRETRAZI_PO_KRITERIJUMU:
                return pretraziPoKriterijumu((String) requestObject.getData());
            case Operation.OPERATION_UCITAJ_IZVODJACA:
                return ucitajIzvodjaca((Izvodjac) requestObject.getData());
            case Operation.OPERATION_UPDATE_IZVODJACA:
                return updateIzvodjac((Izvodjac) requestObject.getData());
            case Operation.OPERATION_UCITAJ_LISTU_IZVODJACA:
                return ucitajListuIzvodjaca();
            case Operation.OPERATION_ZAPAMTI_BEND:
                return zapamtiBend((Bend) requestObject.getData());
            case Operation.OPERATION_PRETRAZI_PO_KRITERIJUMU_BEND:
                return pretraziPoKriterijumuBend((String) requestObject.getData());
            case Operation.OPERATION_UCITAJ_BEND:
                return ucitajBend((Bend) requestObject.getData());
            case Operation.OPERATION_UPDATE_BEND:
                return updateBend((Bend) requestObject.getData());
            case Operation.OPERATION_UCITAJ_LISTU_BENDOVA:
                return ucitajListuBendova();
            case Operation.OPERATION_SAVE_NASTUP:
                return saveNastup((Nastup) requestObject.getData());
            case Operation.OPERATION_PRETRAZI_PO_KRITERIJUMU_NASTUP:
                return pretraziPoKriterijumuNastup((String) requestObject.getData());
            case Operation.OPERATION_UCITAJ_NASTUP:
                return ucitajNastup((Nastup) requestObject.getData());
            case Operation.OPERATION_UPDATE_NASTUP:
                return updateNastup((Nastup) requestObject.getData());

        }
        return null;
    }

    private ResponseObject login(Menadzer menadzerZaLogin) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Menadzer ulogovanMenadzer = controller.Controller.getInstance().loginUser(menadzerZaLogin);
            responseObject.setStatus(ResponseStatus.SUCCESS_LOGIN);
            responseObject.setData(ulogovanMenadzer);
            loginMenadzer = ulogovanMenadzer;
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.FAIL_LOGIN);
            if (ex.getMessage().equals("didnt find object")) {
                responseObject.setErrorMessage("Incorect username or password");
            } else {
                responseObject.setErrorMessage(ex.getMessage());
            }
        }
        return responseObject;
    }

    private ResponseObject logout(Menadzer menadzer) {
        ResponseObject responseObject = new ResponseObject();
        try {
            this.loginMenadzer = null;
            responseObject.setStatus(ResponseStatus.SUCCESS_LOGOUT_MENADZER);
            responseObject.setData(null);
            System.out.println("objekat sa statusom: SUCCESS_LOGOUT_MENADZER");
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR);
            responseObject.setErrorMessage(ex.getMessage());
        }
        return responseObject;
    }

    private ResponseObject saveMenadzer(Menadzer menadzerZaCuvanje) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Menadzer sacuvaniMenadzer = controller.Controller.getInstance().saveMenadzer(menadzerZaCuvanje);
            responseObject.setStatus(ResponseStatus.SUCCESS_SAVE_MENADZER);
            responseObject.setData(sacuvaniMenadzer);
            System.out.println("sacuvani menadzer: " + sacuvaniMenadzer);
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_SAVE_MENADZER);
            responseObject.setErrorMessage(ex.getMessage());
        }
        return responseObject;
    }

    private ResponseObject getAllVrstaIzvodjaca() {
        ResponseObject responseObject = new ResponseObject();
        try {
            List<VrstaIzvodjaca> listaVrstaZaSlanje = controller.Controller.getInstance().getAllVrstaIzvodjaca();
            responseObject.setStatus(ResponseStatus.SUCCESS_GET_ALL_VRSTAIZVODJACA);
            responseObject.setData(listaVrstaZaSlanje);
            System.out.println("Lista vrsta: " + listaVrstaZaSlanje);
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR);
            responseObject.setErrorMessage(ex.getMessage());
        }
        return responseObject;
    }

    private ResponseObject saveIzvodjac(Izvodjac izvodjacZaCuvanje) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Izvodjac sacuvaniIzvodjac = controller.Controller.getInstance().saveIzvodjac(izvodjacZaCuvanje);
            responseObject.setStatus(ResponseStatus.SUCCESS_SAVE_IZVODJAC);
            responseObject.setData(sacuvaniIzvodjac);
            System.out.println("sacuvani izvodjac: " + sacuvaniIzvodjac);
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_SAVE_IZVODJAC);
            responseObject.setErrorMessage(ex.getMessage());
        }
        return responseObject;
    }

    private ResponseObject pretraziPoKriterijumu(String kriterijum) {
        ResponseObject responseObject = new ResponseObject();
        try {
            List<Izvodjac> izvodjaciPoKriterijumu = controller.Controller.getInstance().getIzvodjacePoKriterijumu(kriterijum);
            responseObject.setStatus(ResponseStatus.SUCCESS_PRETRAZIVANJE_PO_KRITERIJUMU);
            responseObject.setData(izvodjaciPoKriterijumu);
            System.out.println("izvodjaci po kriterijumu: " + izvodjaciPoKriterijumu);
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_PRETRAZIVANJE_PO_KRITERIJUMU);
            if (ex.getMessage().equals("any not found")) {
                responseObject.setErrorMessage("Sistem ne moze da nadje Izvodjace po zadatoj vrednosti");
            } else {
                responseObject.setErrorMessage(ex.getMessage());
            }
        }
        return responseObject;
    }

    private ResponseObject ucitajIzvodjaca(Izvodjac ucitajIzvodjaca) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Izvodjac ucitaniIzvodjac = controller.Controller.getInstance().ucitajIzvodjaca(ucitajIzvodjaca);
            responseObject.setStatus(ResponseStatus.SUCCESS_UCITAJ_IZVODJACA);
            responseObject.setData(ucitaniIzvodjac);
            System.out.println("ucitani izvodjac" + ucitaniIzvodjac);
            System.out.println("ucitani izvodjac list" + ucitaniIzvodjac.getListaVrsta());
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_UCITAJ_IZVODJACA);
            responseObject.setErrorMessage(ex.getMessage());

        }
        return responseObject;
    }

    private ResponseObject updateIzvodjac(Izvodjac izvodjacZaUpdate) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Izvodjac izmenjenIzvodjac = controller.Controller.getInstance().updateIzvodjaca(izvodjacZaUpdate);
            responseObject.setStatus(ResponseStatus.SUCCESS_UPDATE_IZVODJACA);
            responseObject.setData(izmenjenIzvodjac);
            System.out.println("izmenjenIzvodjac" + izmenjenIzvodjac);
            System.out.println("izmenjenIzvodjac list" + izmenjenIzvodjac.getListaVrsta());
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_UPDATE_IZVODJACA);
            if (ex instanceof IOException) {
                responseObject.setErrorMessage(ex.getMessage());
                return responseObject;
            }
            responseObject.setErrorMessage("sistem nije izmenio izvodjaca");
        }
        return responseObject;
    }

    private ResponseObject ucitajListuIzvodjaca() {
        ResponseObject responseObject = new ResponseObject();
        try {
            List<Izvodjac> sviIzvodjaci = controller.Controller.getInstance().ucitajListuIzvodjaca();
            responseObject.setStatus(ResponseStatus.SUCCESS_UCITAJ_LISTU_IZVODJACA);
            responseObject.setData(sviIzvodjaci);
            System.out.println("sviIzvodjaci: " + sviIzvodjaci);
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_UCITAJ_LISTU_IZVODJACA);
            if (ex.getMessage().equals("any not found")) {
                responseObject.setErrorMessage("nema izvodjaca");
            } else {
                responseObject.setErrorMessage(ex.getMessage());
            }
        }
        return responseObject;
    }

    private ResponseObject zapamtiBend(Bend bendZaCuvanje) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Bend sacuvaniBend = controller.Controller.getInstance().zapamtiBend(bendZaCuvanje);
            responseObject.setStatus(ResponseStatus.SUCCESS_ZAPAMTI_BEND);
            responseObject.setData(sacuvaniBend);
            System.out.println("sacuvaniBend: " + sacuvaniBend);
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_ZAPAMTI_BEND);
            responseObject.setErrorMessage(ex.getMessage());
        }
        return responseObject;
    }

    private ResponseObject pretraziPoKriterijumuBend(String kriterijum) {
        ResponseObject responseObject = new ResponseObject();
        try {
            List<Bend> bendoviPoKriterijumu = controller.Controller.getInstance().getBendovePoKriterijumu(kriterijum);
            responseObject.setStatus(ResponseStatus.SUCCESS_PRETRAZIVANJE_PO_KRITERIJUMU_BEND);
            responseObject.setData(bendoviPoKriterijumu);
            System.out.println("bendovi po kriterijumu: " + bendoviPoKriterijumu);
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_PRETRAZIVANJE_PO_KRITERIJUMU_BEND);
            if (ex.getMessage().equals("any not found")) {
                responseObject.setErrorMessage("Sistem ne moze da nadje Bendove po zadatoj vrednosti");
            } else {
                responseObject.setErrorMessage(ex.getMessage());
            }
        }
        return responseObject;
    }

    private ResponseObject ucitajBend(Bend bend) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Bend ucitaniBend = controller.Controller.getInstance().ucitajBend(bend);
            responseObject.setStatus(ResponseStatus.SUCCESS_UCITAJ_BEND);
            responseObject.setData(ucitaniBend);
            System.out.println("ucitani bend" + ucitaniBend);
            System.out.println("ucitani bend list" + ucitaniBend.getClanoviBenda());
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_UCITAJ_BEND);
            responseObject.setErrorMessage(ex.getMessage());

        }
        return responseObject;
    }
    
    private ResponseObject updateBend(Bend bendZaUpdate) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Bend izmenjenBend = controller.Controller.getInstance().updateBend(bendZaUpdate);
            responseObject.setStatus(ResponseStatus.SUCCESS_UPDATE_BEND);
            responseObject.setData(izmenjenBend);
            System.out.println("izmenjenIzvodjac" + izmenjenBend);
            System.out.println("izmenjenIzvodjac list" + izmenjenBend.getClanoviBenda());
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_UPDATE_BEND);
            if (ex instanceof IOException) {
                responseObject.setErrorMessage(ex.getMessage());
                return responseObject;
            }
            responseObject.setErrorMessage("sistem nije izmenio bend");
        }
        return responseObject;
    }

    private ResponseObject ucitajListuBendova() {
        ResponseObject responseObject = new ResponseObject();
        try {
            List<Bend> sviBendovi = controller.Controller.getInstance().ucitajListuBendova();
            responseObject.setStatus(ResponseStatus.SUCCESS_UCITAJ_LISTU_BENDOVA);
            responseObject.setData(sviBendovi);
            System.out.println("sviBendovi: " + sviBendovi);
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_UCITAJ_LISTU_BENDOVA);
            if (ex.getMessage().equals("any not found")) {
                responseObject.setErrorMessage("nema bendova");
            } else {
                responseObject.setErrorMessage(ex.getMessage());
            }
        }
        return responseObject;
    }

    private ResponseObject saveNastup(Nastup nastupZaPamcenje) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Nastup sacuvaniNastup = controller.Controller.getInstance().zapamtiNastup(nastupZaPamcenje);
            responseObject.setStatus(ResponseStatus.SUCCESS_ZAPAMTI_NASTUP);
            responseObject.setData(sacuvaniNastup);
            System.out.println("sacuvaniNastup: " + sacuvaniNastup);
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_ZAPAMTI_NASTUP);
            responseObject.setErrorMessage(ex.getMessage());
        }
        return responseObject;
    }

    private ResponseObject pretraziPoKriterijumuNastup(String kriterijum) {
        ResponseObject responseObject = new ResponseObject();
        try {
            List<Nastup> nastupiPoKriterijumu = controller.Controller.getInstance().getNastupePoKriterijumu(kriterijum);
            responseObject.setStatus(ResponseStatus.SUCCESS_PRETRAZIVANJE_PO_KRITERIJUMU_NASTUP);
            responseObject.setData(nastupiPoKriterijumu);
            System.out.println("nastupi po kriterijumu: " + nastupiPoKriterijumu);
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_PRETRAZIVANJE_PO_KRITERIJUMU_NASTUP);
            if (ex.getMessage().equals("any not found")) {
                responseObject.setErrorMessage("Sistem ne moze da nadje Nastupe po zadatoj vrednosti");
            } else {
                responseObject.setErrorMessage(ex.getMessage());
            }
        }
        return responseObject;
    }

    private ResponseObject ucitajNastup(Nastup nastupZaUcitavanje) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Nastup ucitaniNastup = controller.Controller.getInstance().ucitajNastup(nastupZaUcitavanje);
            responseObject.setStatus(ResponseStatus.SUCCESS_UCITAJ_NASTUP);
            responseObject.setData(ucitaniNastup);
            System.out.println("ucitani nastup" + ucitaniNastup);
            System.out.println("ucitani nastup stavke" + ucitaniNastup.getListaStavki());
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_UCITAJ_NASTUP);
            responseObject.setErrorMessage(ex.getMessage());

        }
        return responseObject;
    }

    private ResponseObject updateNastup(Nastup nastupZaUpdate) {
       ResponseObject responseObject = new ResponseObject();
        try {
            Nastup izmenjenNastup = controller.Controller.getInstance().updateIzvodjaca(nastupZaUpdate);
            responseObject.setStatus(ResponseStatus.SUCCESS_UPDATE_NASTUP);
            responseObject.setData(izmenjenNastup);
            System.out.println("izmenjenNastup" + izmenjenNastup);
            System.out.println("izmenjenNastup stavke" + izmenjenNastup.getListaStavki());
        } catch (Exception ex) {
            responseObject.setStatus(ResponseStatus.ERROR_UPDATE_NASTUP);
            if (ex instanceof IOException) {
                responseObject.setErrorMessage(ex.getMessage());
                return responseObject;
            }
            responseObject.setErrorMessage("sistem nije izmenio nastup");
        }
        return responseObject; 
    }

    
}
