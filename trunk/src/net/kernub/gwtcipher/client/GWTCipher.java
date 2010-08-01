package net.kernub.gwtcipher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.FlowPanel;

import net.kernub.gwtcipher.client.provider.AES;

public class GWTCipher implements EntryPoint {

	/*
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Remote Procedure Call");
    dialogBox.setAnimationEnabled(true);
    dialogBox.setWidget(dialogVPanel);
        dialogBox.hide();
        dialogBox.center();
	*/

  public void onModuleLoad() {
		final AES aes = new AES();

		final Label passwordLabel = new Label( "Password (if <16,24,32 it is padded with white spaces)" );
		final Label inputLabel = new Label( "Input" );
		final Label outputLabel = new Label( "Output" );
		final PasswordTextBox passwordTextBox = new PasswordTextBox();
		// todo: add changeHandler to detect when it has change and avoid calling aes "setKey()" each time

		final TextArea area1 = new TextArea();
		area1.setVisibleLines( 15 );
		area1.setCharacterWidth( 80 );
		area1.setReadOnly( false );

		final TextArea area2 = new TextArea();
		area2.setVisibleLines( 15 );
		area2.setCharacterWidth( 80 );
		area2.setReadOnly( true );

		final Button clearButton = new Button( "Clear" );
		clearButton.addStyleName( "GWTCipher_button" );
		final Button encryptButton = new Button( "Encrypt" );
		encryptButton.addStyleName( "GWTCipher_button" );
		final Button decryptButton = new Button( "Decrypt" );
		decryptButton.addStyleName( "GWTCipher_button" );

		final FlowPanel passwordPanel = new FlowPanel();
		passwordPanel.addStyleName( "GWTCipher_passwordPanel" );
		passwordPanel.add( passwordLabel );
		passwordPanel.add( passwordTextBox );

		final FlowPanel buttonPanel = new FlowPanel();
		buttonPanel.addStyleName( "GWTCipher_buttonPanel" );
		buttonPanel.add( clearButton );
		buttonPanel.add( encryptButton );
		buttonPanel.add( decryptButton );

		final FlowPanel panel = new FlowPanel();
		panel.addStyleName( "GWTCipher_panel" );
		panel.add( passwordPanel );
		panel.add( inputLabel );
		panel.add( area1 );
		panel.add( buttonPanel );
		panel.add( outputLabel );
		panel.add( area2 );

		class Handlers implements ClickHandler
		{
			public void onClick( ClickEvent event )
			{
				if( event.getSource() == encryptButton )
				{
					area2.setText( "" );
					try {
						aes.setKey( passwordTextBox.getText() );
					} catch( Exception e ) {
						// todo: replace with dialogBox
						Window.alert( "Password format mistmatch:\n" + e.getMessage() );
						return;
					}

					area2.setText( aes.encryptBase64( area1.getText() ) );
				}
				else if( event.getSource() == decryptButton )
				{
					area2.setText( "" );
					try {
						aes.setKey( passwordTextBox.getText() );
					} catch( Exception e ) {
						// todo: replace with dialogBox
						Window.alert( "Password format mistmatch:\n" + e.getMessage() );
						return;
					}

					area2.setText( aes.decryptBase64( area1.getText() ) );
				}
				else if( event.getSource() == clearButton )
				{
					passwordTextBox.setText( "" );
					area1.setText( "" );
					area2.setText( "" );
				}
			}
		};

		Handlers handler = new Handlers();
		clearButton.addClickHandler( handler );
		encryptButton.addClickHandler( handler );
		decryptButton.addClickHandler( handler );

		RootPanel.get().add( panel );
  }
}
