/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SO;

import Exception.ValidatorException;
import broker.DBBroker;
import database.connection.ConnectionFactory;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import model.DomainObject;
import validator.Validator;

/**
 *
 * @author Aleksa
 */
public abstract class SistemOperation {

    protected DBBroker broker;

    public SistemOperation() {
        broker = new DBBroker();
    }

    public final Object execute(Object entity) throws Exception {
        try {
            preconditions(entity);
            startTransaction();
            Object result = executeOperation(entity);
            commitTransaction();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            rollbackTransaction();
            throw ex;
        }
    }

    protected abstract void preconditions(Object entity) throws Exception;
    protected abstract Object executeOperation(Object entity) throws Exception;
    
    private void startTransaction() throws SQLException {
        ConnectionFactory.getInstance().getConnection().setAutoCommit(false);
    }

    private void commitTransaction() throws SQLException {
        ConnectionFactory.getInstance().getConnection().commit();
    }

    private void rollbackTransaction() throws SQLException {
        ConnectionFactory.getInstance().getConnection().rollback();
    }
}
