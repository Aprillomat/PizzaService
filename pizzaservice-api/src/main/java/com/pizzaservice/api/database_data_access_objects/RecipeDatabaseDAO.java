package com.pizzaservice.api.database_data_access_objects;

import com.pizzaservice.api.buissness_objects.Ingredient;
import com.pizzaservice.api.buissness_objects.Recipe;
import com.pizzaservice.api.buissness_objects.RecipeEntry;
import com.pizzaservice.api.data_access_objects.DAOBundle;
import com.pizzaservice.api.data_access_objects.DataAccessException;
import com.pizzaservice.api.data_access_objects.RecipeDAO;
import com.pizzaservice.api.db.Database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by philipp on 19.01.17.
 */
public class RecipeDatabaseDAO extends DatabaseDAO implements RecipeDAO
{
    public RecipeDatabaseDAO( Database database, DatabaseDAOBundle databaseDAOBundle )
    {
        super( database, databaseDAOBundle );
    }

    @Override
    public Recipe findRecipeById( long id ) throws DataAccessException
    {
        try
        {
            Recipe recipe = new Recipe();

            // find recipe with the given id
            String query = "SELECT * FROM recipes WHERE id = ?";
            List<Object> args = new ArrayList<>();
            args.add( id );
            boolean found = database.query( query, args, row -> {} );
            if( !found )
                return null;

            // get recipe entries
            Collection<RecipeEntry> recipeEntries = new ArrayList<>();
            query = "SELECT ingredients.id, ingredients.name, ingredients.price_per_gram, recipe_entries.quantity_in_grams " +
                    "FROM recipe_entries " +
                    "INNER JOIN ingredients ON recipe_entries.id_ingredient = ingredients.id " +
                    "WHERE id_recipe = ?";
            args = new ArrayList<>();
            args.add( id );
            database.query( query, args, row ->
            {
                Ingredient ingredient = new Ingredient();
                ingredient.setId( row.getLong( 1 ) );
                ingredient.setName( row.getString( 2 ) );
                ingredient.setPricePerGramm( row.getFloat( 3 ) );

                RecipeEntry recipeEntry = new RecipeEntry();
                recipeEntry.setRecipe( recipe );
                recipeEntry.setIngredient( ingredient );
                recipeEntry.setQuantityInGrams( row.getFloat( 4 ) );

                recipeEntries.add( recipeEntry );
            } );

            // finally the complete recipe
            recipe.setId( id );
            recipe.setEntries( recipeEntries );
            return recipe;
        }
        catch( Exception e ) { throw handleException( e ); }
    }
}
