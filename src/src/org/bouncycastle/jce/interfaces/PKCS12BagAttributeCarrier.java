package org.bouncycastle.jce.interfaces;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;

import java.util.Enumeration;

/**
 * allow us to set attributes on objects that can go into a PKCS12 store.
 */
public interface PKCS12BagAttributeCarrier
{
    public void setBagAttribute(
        DERObjectIdentifier oid,
        DEREncodable        attribute);

    public DEREncodable getBagAttribute(
        DERObjectIdentifier oid);

    public Enumeration getBagAttributeKeys();
}
