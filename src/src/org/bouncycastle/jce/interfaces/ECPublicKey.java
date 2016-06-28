package org.bouncycastle.jce.interfaces;

import org.bouncycastle.math.ec.ECPoint;

import java.security.PublicKey;

/**
 * interface for elliptic curve public keys.
 */
public interface ECPublicKey
    extends ECKey, PublicKey
{
    /**
     * return the public point Q
     */
    public ECPoint getQ();
}
