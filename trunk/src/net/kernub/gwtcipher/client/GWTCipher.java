package net.kernub.gwtcipher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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

	public interface Strings extends Constants
	{
		public static final Strings inst = GWT.create( Strings.class );

		@DefaultStringValue( "Ok" )
		String infoBoxOkButton();

		@DefaultStringValue( "Info" )
		String infoBoxCaption();

		@DefaultStringValue( "Password" )
		String password();

		@DefaultStringValue( "Confirm password" )
		String confirmPassword();

		@DefaultStringValue( "Input" )
		String input();

		@DefaultStringValue( "Output" )
		String output();

		@DefaultStringValue( "Clear" )
		String clearButton();

		@DefaultStringValue( "Encrypt" )
		String encryptButton();

		@DefaultStringValue( "Decrypt" )
		String decryptButton();

		// Should be a message with exception message added
		@DefaultStringValue( "Passwords does not match." )
		String errorPasswordNotMatch();

		@DefaultStringValue( "Passwords format error." )
		String errorPasswordFormat();

		@DefaultStringValue( "Error while encrypting." )
		String errorEncrypting();

		@DefaultStringValue( "Error while decrypting.<br>\nIs it an encrypted input?" )
		String errorDecrypting();
	};

	private class InfoBox extends DialogBox
	{
		private final HTML infoHTML = new HTML();

		public InfoBox()
		{
			infoHTML.addStyleName( "GWTCipher_InfoBox_infoHTML" );

			final Button okButton = new Button( Strings.inst.infoBoxOkButton() );
			okButton.addStyleName( "GWTCipher_InfoBox_okButton" );
			okButton.addClickHandler( new ClickHandler() {
				public void onClick( ClickEvent event ) {
					hide();
				}
			});

			final FlowPanel panel = new FlowPanel();
			panel.add( infoHTML );
			panel.add( okButton );

			setText( Strings.inst.infoBoxCaption() );
			setAnimationEnabled( true );
			setGlassEnabled( true );
			setWidget( panel );
		}

		public void showInfo( final String txt )
		{
			infoHTML.setHTML( txt );
			center();
		}
	};

  public void onModuleLoad() {
		final AES aes = new AES();

		final InfoBox infoBox = new InfoBox();
		final Label passwordLabel = new Label( Strings.inst.password() );
		final Label confirmPasswordLabel = new Label( Strings.inst.confirmPassword() );
		final Label inputLabel = new Label( Strings.inst.input() );
		final Label outputLabel = new Label( Strings.inst.output() );
		final PasswordTextBox passwordTextBox1 = new PasswordTextBox();
		final PasswordTextBox passwordTextBox2 = new PasswordTextBox();

		final TextArea area1 = new TextArea();
		area1.setVisibleLines( 15 );
		area1.setCharacterWidth( 80 );
		area1.setReadOnly( false );

		final TextArea area2 = new TextArea();
		area2.setVisibleLines( 15 );
		area2.setCharacterWidth( 80 );
		area2.setReadOnly( true );

		final Button clearButton = new Button( Strings.inst.clearButton() );
		clearButton.addStyleName( "GWTCipher_button" );
		final Button encryptButton = new Button( Strings.inst.encryptButton() );
		encryptButton.addStyleName( "GWTCipher_button" );
		final Button decryptButton = new Button( Strings.inst.decryptButton() );
		decryptButton.addStyleName( "GWTCipher_button" );

		final FlowPanel passwordPanel = new FlowPanel();
		passwordPanel.addStyleName( "GWTCipher_passwordPanel" );
		passwordPanel.add( passwordLabel );
		passwordPanel.add( passwordTextBox1 );
		passwordPanel.add( confirmPasswordLabel );
		passwordPanel.add( passwordTextBox2 );

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

		class Handlers implements ClickHandler, ChangeHandler
		{
			private boolean passwordChanged = false;

			public void onChange( ChangeEvent event )
			{
				if( event.getSource() == passwordTextBox1 || event.getSource() == passwordTextBox2 )
				{
					passwordChanged = true;
				}
			}

			private boolean updateKey()
			{
				if( !passwordChanged ) return true;

				if( !passwordTextBox1.getText().equals( passwordTextBox2.getText() ) ) {
					infoBox.showInfo( Strings.inst.errorPasswordNotMatch() );
					return false;
				}

				try {
					aes.setKey( passwordTextBox1.getText() );
				} catch( Exception e ) {
					infoBox.showInfo( Strings.inst.errorPasswordFormat() );
					return false;
				}

				passwordChanged = false;
				return true;
			}

			public void onClick( ClickEvent event )
			{
				if( event.getSource() == encryptButton )
				{
					area2.setText( "" );
					if( !updateKey() ) return;
					try {
						area2.setText( aes.encryptBase64( area1.getText() ) );
					} catch( Exception e ) {
						infoBox.showInfo( Strings.inst.errorEncrypting() );
					}
				}
				else if( event.getSource() == decryptButton )
				{
					area2.setText( "" );
					if( !updateKey() ) return;
					try {
						area2.setText( aes.decryptBase64( area1.getText() ) );
					} catch( Exception e ) {
						infoBox.showInfo( Strings.inst.errorDecrypting() );
					}
				}
				else if( event.getSource() == clearButton )
				{
					passwordTextBox1.setText( "" );
					passwordTextBox2.setText( "" );
					area1.setText( "" );
					area2.setText( "" );
				}
			}
		};

		Handlers handler = new Handlers();
		passwordTextBox1.addChangeHandler( handler );
		passwordTextBox2.addChangeHandler( handler );
		clearButton.addClickHandler( handler );
		encryptButton.addClickHandler( handler );
		decryptButton.addClickHandler( handler );

		RootPanel.get().add( panel );
  }
}
