package server.services;

import server.database.Database;
import server.exceptions.DatabaseException;
import shared.result.ClearResult;

import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

/** Provides service for clearing all tables in the database. */
public class ClearService extends BaseService {

    /**
     * Drop all of the tables in the database.
     * @return ClearResult object
     */
    public ClearResult clear() {

        String message;
        Database db = new Database();
        try {
            int rowsDeleted = db.clear();
            message = format("%s total rows deleted.", rowsDeleted);
        } catch (DatabaseException e) {

            log.log(SEVERE, e.getMessage(), e);
            message = e.getMessage();
        }
        return new ClearResult(message);
    }

}