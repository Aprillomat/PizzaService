package com.pizzaservice.customerpage.fragments;

import com.pizzaservice.api.buissness_objects.PizzaConfiguration;
import com.pizzaservice.api.buissness_objects.PizzaVariation;
import com.pizzaservice.customerpage.MyUtils;
import com.pizzaservice.api.data_access_objects.DataAccessException;
import com.pizzaservice.api.data_access_objects.PizzaVariationDAO;
import com.pizzaservice.common.Utils;
import com.pizzaservice.common.items.PizzaVariationItem;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by philipp on 17.01.17.
 */
public class ChoosePizzaVariationFragment extends Fragment
{
    @FXML ChoiceBox<PizzaVariationItem> cbPizzaVariation1;
    @FXML ChoiceBox<PizzaVariationItem> cbPizzaVariation2;

    @FXML public void actionNext( ActionEvent actionEvent ) { next(); }
    @FXML public void actionAbort( ActionEvent actionEvent ) { abort(); }

    public ChoosePizzaVariationFragment( Fragment oldFragment, boolean split )
    {
        super( split ? "choose_pizza_variations.fxml" : "choose_pizza_variation.fxml", oldFragment );
    }

    @Override
    public void setup()
    {
        try
        {
            // get the pizza variations and create a list of items to display name and price in the choice box
            Collection<PizzaVariationItem> variationItems = getPizzaVariationItems();
            cbPizzaVariation1.setItems( FXCollections.observableArrayList( variationItems ) );

            // if split - set items for second choice box
            if( session.getCurrentPizzaConfiguration().isSplit() )
                cbPizzaVariation2.setItems( FXCollections.observableArrayList( variationItems ) );
        }
        catch( DataAccessException e )
        {
            MyUtils.handleDataAccessException( e, this );
        }
    }

    public ChoosePizzaVariationFragment choose( int pizzaVariationIndex )
    {
        cbPizzaVariation1.getSelectionModel().select( pizzaVariationIndex );
        return this;
    }

    public ChoosePizzaVariationFragment choose( int pizzaVariationIndex1, int pizzaVariationIndex2 )
    {
        cbPizzaVariation1.getSelectionModel().select( pizzaVariationIndex1 );
        cbPizzaVariation2.getSelectionModel().select( pizzaVariationIndex2 );
        return this;
    }

    public Fragment next()
    {
        PizzaVariationItem selectedItem1 = cbPizzaVariation1.getValue();
        if( selectedItem1 == null )
        {
            Utils.showInputErrorMessage( "Es wurde noch keine Variation ausgewählt!" );
            return this;
        }

        PizzaConfiguration currentConfiguration = session.getCurrentPizzaConfiguration();
        currentConfiguration.setPizzaVariation1( selectedItem1.getPizzaVariation() );

        if( currentConfiguration.isSplit() )
        {
            PizzaVariationItem selectedItem2 = cbPizzaVariation2.getValue();
            if( selectedItem2 == null )
            {
                Utils.showInputErrorMessage( "Es wurde noch keine Variation ausgewählt!" );
                return this;
            }

            currentConfiguration.setPizzaVariation2( selectedItem2.getPizzaVariation() );
        }

        return setNewFragment( new ChooseToppingsFragment( this ) );
    }

    public Fragment abort()
    {
        return setNewFragment( new MainMenuFragment( this ) );
    }

    private Collection<PizzaVariationItem> getPizzaVariationItems() throws DataAccessException
    {
        PizzaVariationDAO pizzaVariationDAO = daoBundle.getPizzaVariationDAO();
        Collection<PizzaVariation> variations = pizzaVariationDAO.getPizzaVariations();
        Collection<PizzaVariationItem> variationItems = new ArrayList<>();
        for( PizzaVariation variation : variations )
        {
            PizzaVariationItem variationItem = new PizzaVariationItem(
                variation, session.getCurrentPizzaConfiguration().getPizzaSize() );
            variationItems.add( variationItem );
        }

        return variationItems;
    }
}
