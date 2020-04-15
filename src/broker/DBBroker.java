/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package broker;

import database.connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.DomainObject;
import util.State;

/**
 *
 * @author Aleksa
 */
public class DBBroker {

    public DomainObject find(DomainObject domainObject) throws SQLException, Exception {
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "SELECT " + domainObject.getObjectAtributes() + " FROM " + domainObject.getTabbleName() + " WHERE " + domainObject.getWhereCondition();
            System.out.println("find query: " + query);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                State state = domainObject.getState();//cuvam stanje od starog objekta, posto se u konstuktoru moze promeniti
                domainObject = domainObject.createObjectFromResaultSet(rs);
                domainObject.setState(state);
            } else {
                throw new Exception("didnt find object");
            }
            //ako objekat ima listu ona mora da se popuni, slucaj za many to many
            if (domainObject.getState().equals(State.FindManyToMany)) {
                query = "SELECT * FROM " + domainObject.getTabbleName()
                        + " o1 JOIN " + domainObject.getAgregateTableName()
                        + " a ON (o1." + domainObject.getIdColumnName()
                        + " = a." + domainObject.getDomainObjectAgregateIdColumnName() + ") JOIN "
                        + domainObject.getConectedTableName() + " o2 ON (o2."
                        + domainObject.getConectedTebleID() + " = a."
                        + domainObject.getAgregateConectedTableID() + ") WHERE "
                        + "o1." + domainObject.getIdColumnName() + "= " + domainObject.getID();
                System.out.println("query for connectedObject: " + query);
                rs = statement.executeQuery(query);
                while (rs.next()) {
                    domainObject.addToList(
                            domainObject.createConnectedDomainObjectFromResultSet(rs));
                }
            }
            return domainObject;
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param domainObject
     * @return
     * @throws SQLException
     */
    public DomainObject save(DomainObject domainObject) throws SQLException {
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "INSERT INTO " + domainObject.getTabbleName()
                    + " (" + domainObject.getObjectAtributes() + ") VALUES ("
                    + domainObject.getAtributeValues() + ")";
            System.out.println("save query: " + query);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                Long id = rs.getLong(1);
                domainObject.setId(id);
            }
            if (domainObject.getState().equals(State.ManyToMany)) {
                insertAgregateTableRows(domainObject, statement);
            }
            statement.close();
            return domainObject;

        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<DomainObject> getAll(DomainObject domainObject) throws SQLException, Exception {
        try {
            List<DomainObject> list = new ArrayList<>();
//            domainObject.setState();
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String query = "SELECT " + domainObject.getObjectAtributes() + " FROM " + domainObject.getTabbleName();
            if (domainObject.getState().equals(State.KriterijumskaPretraga)
                    || domainObject.getState().equals(State.ManyToMany)
                    || domainObject.getState().equals(State.GetAll)) {
                query = "SELECT " + domainObject.getObjectAtributes() + " FROM " + domainObject.getTabbleName() + " WHERE " + domainObject.getWhereCondition();
            }
            System.out.println("getAll query: " + query);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                list.add(domainObject.createObjectFromResaultSet(rs));
            }
            if (list.size() == 0) {
                throw new Exception("any not found");
            }
            statement.close();
            return list;
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Object update(DomainObject domainObject) throws SQLException, Exception {
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            domainObject.setState(State.Update);
            String query = "UPDATE " + domainObject.getTabbleName() + " SET " + domainObject.getUpdateAtributes() + " WHERE " + domainObject.getWhereCondition();
            Statement statement = connection.createStatement();
            System.out.println("update query:" + query);
            statement.executeUpdate(query);
            if (domainObject.getState().equals(State.ManyToMany)) {//ako je many to many onda mora da izbrise u agregatnoj tabeli pa da ubaci opet
                updateAgregateTable(domainObject);
            }
            statement.close();
            //mora da opet pronadje objekat u bazi, samo sto je sada izmenjena lista ucitana,tj potvrdjuje da je ucitano iz baze
            return find(domainObject);
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * brise elemente u agregatnoj tabeli pa ubacuje nove iz liste
     *
     * @param domainObject
     * @throws SQLException
     */
    private void updateAgregateTable(DomainObject domainObject) throws SQLException {
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String deleteAgregateRowsQuery = "DELETE FROM " + domainObject.getAgregateTableName() + " WHERE " + domainObject.getDomainObjectAgregateIdColumnName()+ "=" + domainObject.getID();
            Statement statement = connection.createStatement();
            System.out.println("deleteAgregateRowsQuery query:" + deleteAgregateRowsQuery);
            statement.executeUpdate(deleteAgregateRowsQuery);

            insertAgregateTableRows(domainObject, statement);

            statement.close();
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void insertAgregateTableRows(DomainObject domainObject, Statement statement) throws SQLException {
        for (DomainObject otherObject : domainObject.getList()) {
            String insertAgregateRowsQuery = "INSERT INTO " + domainObject.getAgregateTableName() + " ("
                    + domainObject.getDomainObjectAgregateIdColumnName() + ", " + domainObject.getAgregateConectedTableID()
                    + ") VALUES (" + domainObject.getID() + ", " + otherObject.getID() + ")";
            System.out.println("insertAgregateRowsQuery query: " + insertAgregateRowsQuery);
            statement.executeUpdate(insertAgregateRowsQuery);
        }
        //posto znam da je ovo stanje many to many, postavicu stanje FindManyToMany 
//        zato sto ce u update da se koristi metoda Find za koju ce mi trebati to stanje
        domainObject.setState(State.FindManyToMany);
    }

    public void delete(DomainObject domainObject) throws SQLException{
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            String deleteQuery = "DELETE FROM " + domainObject.getTabbleName()+ " WHERE " + domainObject.getWhereCondition();
            Statement statement = connection.createStatement();
            System.out.println("delete query:" + deleteQuery);
            statement.executeUpdate(deleteQuery);

            insertAgregateTableRows(domainObject, statement);

            statement.close();
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }

}
