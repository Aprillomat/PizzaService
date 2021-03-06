package com.pizzaservice.api.database_data_access_objects;

import com.pizzaservice.api.buissness_objects.Store;
import com.pizzaservice.api.data_access_objects.DAOBundle;
import com.pizzaservice.api.db.Row;
import com.pizzaservice.api.buissness_objects.Address;
import com.pizzaservice.api.data_access_objects.DataAccessException;
import com.pizzaservice.api.data_access_objects.StoreDAO;
import com.pizzaservice.api.db.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by philipp on 10.01.17.
 */
public class StoreDatabaseDAO extends DatabaseDAO implements StoreDAO
{
    public static final int COLUMN_ID = 1;
    public static final int COLUMN_STREET = 2;
    public static final int COLUMN_HOUSE_NUMBER = 3;
    public static final int COLUMN_POST_CODE = 4;
    public static final int COLUMN_CITY = 5;
    public static final int COLUMN_COUNTRY = 6;

    public StoreDatabaseDAO( Database database, DatabaseDAOBundle databaseDAOBundle )
    {
        super( database, databaseDAOBundle );
    }

    @Override
    public Collection<Store> getStores() throws DataAccessException
    {
        try
        {
            Collection<Store> stores = new ArrayList<>();

            String query = "SELECT * FROM stores";
            database.query( query, new ArrayList<>(), row ->
            {
                Store store = new Store();

                store.setId( row.getLong( COLUMN_ID ) );
                processAddress( store, row );
                // get these ones later because they may be large
                store.setStockItems( new ArrayList<>() );
                store.setOrders( new ArrayList<>() );

                stores.add( store );
            } );

            return stores;
        }
        catch( Exception e ) { throw handleException( e ); }
    }

    private void processAddress( Store store, Row row ) throws SQLException
    {
        Address address = new Address();
        address.setStreet( row.getString( COLUMN_STREET ) );
        address.setHouseNumber( row.getString( COLUMN_HOUSE_NUMBER ) );
        address.setPostcode( row.getString( COLUMN_POST_CODE ) );
        address.setCity( row.getString( COLUMN_CITY ) );
        address.setCountry( row.getString( COLUMN_COUNTRY ) );

        store.setAddress( address );
    }
}
