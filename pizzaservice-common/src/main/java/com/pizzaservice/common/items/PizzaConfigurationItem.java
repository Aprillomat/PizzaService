package com.pizzaservice.common.items;

import com.pizzaservice.api.buissness_objects.PizzaConfiguration;
import com.pizzaservice.api.buissness_objects.PizzaSize;
import com.pizzaservice.api.buissness_objects.Topping;

/**
 * Wrapper for PizzaConfiguration.
 */
public class PizzaConfigurationItem
{
    private PizzaConfiguration pizzaConfiguration;

    public PizzaConfigurationItem( PizzaConfiguration pizzaConfiguration )
    {
        this.pizzaConfiguration = pizzaConfiguration;
    }

    public PizzaConfiguration getPizzaConfiguration()
    {
        return pizzaConfiguration;
    }

    @Override
    public String toString()
    {
        String text = "";
        float totalPrice = 0;

        PizzaSize size = pizzaConfiguration.getPizzaSize();
        PizzaVariationItem variation1 = new PizzaVariationItem( pizzaConfiguration.getPizzaVariation1(), size );
        PizzaVariationItem variation2 = new PizzaVariationItem( pizzaConfiguration.getPizzaVariation2(), size );

        if( pizzaConfiguration.isSplit() )
        {
            text += "1. Belag: " + variation1.toString() + "\n2. Belag: " + variation2.toString();
            totalPrice += Math.max( variation1.getPrice(), variation2.getPrice() );
        }
        else
        {
            text += "Belag: " + variation1.toString();
            totalPrice += variation1.getPrice();
        }

        text += "\n\nGröße: ";
        if( size == PizzaSize.SMALL )
            text += "Small";
        else if( size == PizzaSize.LARGE )
            text += "Large";
        else
            text += "X-Large";

        if( !pizzaConfiguration.getToppings().isEmpty() )
        {
            text += "\n\nToppings:";

            for( Topping topping : pizzaConfiguration.getToppings() )
            {
                ToppingItem toppingItem = new ToppingItem( topping );
                text += "\n+ " + toppingItem.toString();
                totalPrice += topping.getPrice();
            }
        }

        text += "\n\nPreis: " + String.format( "%.2f", totalPrice ) + "€";

        return text;
    }

    public float getTotalPrice()
    {
        float totalPrice = 0;

        PizzaSize size = pizzaConfiguration.getPizzaSize();
        PizzaVariationItem variation1 = new PizzaVariationItem( pizzaConfiguration.getPizzaVariation1(), size );
        PizzaVariationItem variation2 = new PizzaVariationItem( pizzaConfiguration.getPizzaVariation2(), size );

        if( pizzaConfiguration.isSplit() )
            totalPrice += Math.max( variation1.getPrice(), variation2.getPrice() );
        else
            totalPrice += variation1.getPrice();

        for( Topping topping : pizzaConfiguration.getToppings() )
            totalPrice += topping.getPrice();

        return totalPrice;
    }
}
