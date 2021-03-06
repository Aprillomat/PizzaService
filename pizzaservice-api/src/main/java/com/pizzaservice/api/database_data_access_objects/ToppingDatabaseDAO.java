package com.pizzaservice.api.database_data_access_objects;

import com.pizzaservice.api.buissness_objects.Topping;
import com.pizzaservice.api.buissness_objects.Recipe;
import com.pizzaservice.api.data_access_objects.DAOBundle;
import com.pizzaservice.api.data_access_objects.DataAccessException;
import com.pizzaservice.api.data_access_objects.RecipeDAO;
import com.pizzaservice.api.data_access_objects.ToppingDAO;
import com.pizzaservice.api.db.Database;
import com.pizzaservice.api.db.Row;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by philipp on 10.01.17.
 */
public class ToppingDatabaseDAO extends DatabaseDAO implements ToppingDAO
{
    public static final int COLUMN_ID = 1;
    public static final int COLUMN_NAME = 2;
    public static final int COLUMN_PRICE = 3;
    public static final int COLUMN_ID_RECIPE = 4;

    private static int queryCounter = 0;

    private Hashtable<Long, Topping> cache = new Hashtable<>();

    public ToppingDatabaseDAO( Database database, DatabaseDAOBundle databaseDAOBundle )
    {
        super( database, databaseDAOBundle );
    }

    @Override
    public Topping findToppingById( long id ) throws DataAccessException
    {
        if( id == 0 )
            return null;

        Topping cachedTopping = cache.get( id );
        if( cachedTopping != null )
            return cachedTopping;

        try
        {
            System.out.println( "Topping query number: " + ++queryCounter );

            Topping topping = new Topping();

            String query = "SELECT * FROM toppings WHERE id = ?";
            List<Object> args = new ArrayList<>();
            args.add( id );
            boolean found = database.query( query, args, row -> processTopping( topping, row ) );
            if( !found )
                return null;

            cache.put( topping.getId(), topping );

            return topping;
        }
        catch( Exception e ) { throw handleException( e ); }
    }

    @Override
    public Collection<Topping> getToppings() throws DataAccessException
    {
        System.out.println( "Topping query number: " + ++queryCounter );

        try
        {
            cache = new Hashtable<>();
            Collection<Topping> toppings = new ArrayList<>();

            String query = "SELECT * FROM toppings";
            database.query( query, new ArrayList<>(), row ->
            {
                Topping topping = new Topping();
                processTopping( topping, row );

                toppings.add( topping );
                cache.put( topping.getId(), topping );
            } );

            return toppings;
        }
        catch( Exception e ) { throw handleException( e ); }
    }

    private void processTopping( Topping topping, Row row ) throws SQLException, DataAccessException
    {
        topping.setId( row.getLong( COLUMN_ID ) );
        topping.setName( row.getString( COLUMN_NAME ) );
        topping.setPrice( row.getFloat( COLUMN_PRICE ) );
        processRecipe( topping, row );
    }

    private void processRecipe( Topping topping, Row row ) throws SQLException, DataAccessException
    {
        long idRecipe = row.getLong( COLUMN_ID_RECIPE );

        RecipeDatabaseDAO recipeDatabaseDAO = databaseDAOBundle.getRecipeDatabaseDAO();

        Recipe recipe = recipeDatabaseDAO.findRecipeById( idRecipe );
        if( recipe == null )
            throw new DataAccessException( this, "Could not find id_recipe: " + idRecipe + "!" );

        topping.setRecipe( recipe );
    }
}
