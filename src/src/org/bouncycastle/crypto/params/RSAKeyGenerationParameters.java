package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSAKeyGenerationParameters
    extends KeyGenerationParameters
{
	private BigInteger publicExponent;
    private int certainty;

    public RSAKeyGenerationParameters(
		BigInteger		publicExponent,
        SecureRandom    random,
        int             strength,
        int             certainty)
    {
        super(random, strength);

		this.publicExponent = publicExponent;
        this.certainty = certainty;
    }

	public BigInteger getPublicExponent()
	{
		return publicExponent;
	}

    public int getCertainty()
    {
        return certainty;
    }
}
