package org.gudy.azureus2.ui.console.util;

import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

/**
 * utility class to encrypt strings. this class was taken from the examples at:
 * http://www.devx.com/Java/10MinuteSolution/21385/0/page/2 
 */
public class StringEncrypter
{
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	public static final String DES_ENCRYPTION_SCHEME = "DES";
	public static final String DEFAULT_ENCRYPTION_KEY = "Azureus users love their sensitive information to be encrypted";
	
	private KeySpec              keySpec;
	private SecretKeyFactory     keyFactory;
	private Cipher               cipher;
	
	private static final String     UNICODE_FORMAT               = "UTF8";
	
	public StringEncrypter( String encryptionScheme ) throws EncryptionException
	{
		this( encryptionScheme, DEFAULT_ENCRYPTION_KEY );
	}
	
	public StringEncrypter( String encryptionScheme, String encryptionKey ) throws EncryptionException
	{
		if ( encryptionKey == null )
			throw new IllegalArgumentException( "encryption key was null" );
		if ( encryptionKey.trim().length() < 24 )
			throw new IllegalArgumentException(
			"encryption key was less than 24 characters" );
		
		try
		{
			byte[] keyAsBytes = encryptionKey.getBytes( UNICODE_FORMAT );
			
			if ( encryptionScheme.equals( DESEDE_ENCRYPTION_SCHEME) )
			{
				keySpec = new DESedeKeySpec( keyAsBytes );
			}
			else if ( encryptionScheme.equals( DES_ENCRYPTION_SCHEME ) )
			{
				keySpec = new DESKeySpec( keyAsBytes );
			}
			else
			{
				throw new IllegalArgumentException( "Encryption scheme not supported: "
						+ encryptionScheme );
			}
			
			keyFactory = SecretKeyFactory.getInstance( encryptionScheme );
			cipher = Cipher.getInstance( encryptionScheme );
			
		}
		catch (InvalidKeyException e)
		{
			throw new EncryptionException( e );
		}
		catch (UnsupportedEncodingException e)
		{
			throw new EncryptionException( e );
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new EncryptionException( e );
		}
		catch (NoSuchPaddingException e)
		{
			throw new EncryptionException( e );
		}
		
	}
	
	public String encrypt( String unencryptedString ) throws EncryptionException
	{
		if ( unencryptedString == null || unencryptedString.trim().length() == 0 )
			throw new IllegalArgumentException(
			"unencrypted string was null or empty" );
		
		try
		{
			SecretKey key = keyFactory.generateSecret( keySpec );
			cipher.init( Cipher.ENCRYPT_MODE, key );
			byte[] cleartext = unencryptedString.getBytes( UNICODE_FORMAT );
			byte[] ciphertext = cipher.doFinal( cleartext );
			
			return new String( Base64.encode( ciphertext ));
		}
		catch (Exception e)
		{
			throw new EncryptionException( e );
		}
	}
	
	public String decrypt( String encryptedString ) throws EncryptionException
	{
		if ( encryptedString == null || encryptedString.trim().length() <= 0 )
			throw new IllegalArgumentException( "encrypted string was null or empty" );
		
		try
		{
			SecretKey key = keyFactory.generateSecret( keySpec );
			cipher.init( Cipher.DECRYPT_MODE, key );
			byte[] cleartext = Base64.decode( encryptedString );
			byte[] ciphertext = cipher.doFinal( cleartext );
			
			return bytes2String( ciphertext );
		}
		catch (Exception e)
		{
			throw new EncryptionException( e );
		}
	}
	
	private static String bytes2String( byte[] bytes )
	{
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
		{
			stringBuffer.append( (char) bytes[i] );
		}
		return stringBuffer.toString();
	}
	
	public static class EncryptionException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8767982102667004210L;

		public EncryptionException( Throwable t )
		{
			super( t );
		}
	}
}
