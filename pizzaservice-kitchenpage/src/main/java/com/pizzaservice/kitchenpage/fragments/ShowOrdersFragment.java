package com.pizzaservice.kitchenpage.fragments;

import com.pizzaservice.api.buissness_objects.Order;
import com.pizzaservice.api.buissness_objects.OrderState;
import com.pizzaservice.api.buissness_objects.Store;
import com.pizzaservice.api.data_access_objects.DataAccessException;
import com.pizzaservice.api.data_access_objects.OrderDAO;
import com.pizzaservice.kitchenpage.MyUtils;
import com.pizzaservice.kitchenpage.views.OrderView;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by philipp on 26.01.17.
 */
public class ShowOrdersFragment extends Fragment
{
    @FXML VBox vbContainer;

    @FXML public void actionUpdateOrders()
    {
        updateOrders();
    }

    private List<OrderView> orderViews;

    public ShowOrdersFragment( Fragment oldFragment )
    {
        super( "show_orders.fxml", oldFragment );
    }

    public void setup()
    {
        updateOrders();
    }

    public ShowOrdersFragment updateOrders()
    {
        vbContainer.getChildren().clear();
        orderViews = new ArrayList<>();

        try
        {
            Store store = session.getStore();

            OrderDAO orderDAO = daoBundle.getOrderDAO();
            orderDAO.getOrdersOfStore( store );

            Collection<Order> orders = store.getOrders();
            for( Order order : orders )
            {
                // show only new or cooking orders
                if( order.getState() != OrderState.NEW && order.getState() != OrderState.COOKING )
                    continue;

                OrderView orderView = new OrderView( order );
                orderView.setOnOrderStateChangedListener( this::orderStateChanged );

                orderViews.add( orderView );
                vbContainer.getChildren().add( orderView );
            }
        }
        catch( DataAccessException e )
        {
            MyUtils.handleDataAccessException( e );
        }

        return this;
    }

    private void orderStateChanged( OrderView orderView )
    {
        Order order = orderView.getOrder();

        try
        {
            OrderDAO orderDAO = daoBundle.getOrderDAO();
            orderDAO.updateOrder( order );
        }
        catch( DataAccessException e )
        {
            MyUtils.handleDataAccessException( e );
        }

        OrderState state = order.getState();
        if( state == OrderState.FINISHED )
        {
            orderViews.remove( orderView );
            vbContainer.getChildren().remove( orderView );
        }
    }
}
