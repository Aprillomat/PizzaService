package com.pizzaservice.orderpage.fragments;

import com.pizzaservice.orderpage.fragment_fxml.FragmentURLs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;

/**
 * Created by philipp on 17.01.17.
 */
public class ChooseSplitFragment extends Fragment
{
    private ToggleGroup splitGroup;

    @FXML
    RadioButton radSplitYes;

    @FXML
    RadioButton radSplitNo;

    public ChooseSplitFragment( Fragment oldFragment )
    {
        super( FragmentURLs.CHOOSE_SPLIT, oldFragment );
    }

    @Override
    public void setup()
    {
        splitGroup = new ToggleGroup();
        radSplitYes.setToggleGroup( splitGroup );
        radSplitNo.setToggleGroup( splitGroup );
        radSplitNo.setSelected( true );
    }

    @FXML
    public void actionNext( ActionEvent actionEvent ) throws IOException
    {
        if( radSplitYes.isSelected() )
        {
            session.getCurrentPizzaConfiguration().setSplit( true );
            setNewFragment( new ChoosePizzaVariationFragment( this, true ) );
        }
        else
        {
            session.getCurrentPizzaConfiguration().setSplit( false );
            setNewFragment( new ChoosePizzaVariationFragment( this, false ) );
        }
    }

    @FXML
    public void actionAbort( ActionEvent actionEvent ) throws IOException
    {
        setNewFragment( new MainMenuFragment( this ) );
    }
}
