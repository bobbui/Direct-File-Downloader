package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.*;

import java.math.BigInteger;
import java.util.Enumeration;

public class PBKDF2Params
    extends KeyDerivationFunc
{
    DERObjectIdentifier id;
    ASN1OctetString     octStr;
    DERInteger          iterationCount;
    DERInteger          keyLength;

    PBKDF2Params(
        ASN1Sequence  seq)
    {
        super(seq);

        Enumeration e = seq.getObjects();

        id = (DERObjectIdentifier)e.nextElement();

        ASN1Sequence  params = (ASN1Sequence)e.nextElement();

        e = params.getObjects();

        octStr = (ASN1OctetString)e.nextElement();
        iterationCount = (DERInteger)e.nextElement();

        if (e.hasMoreElements())
        {
            keyLength = (DERInteger)e.nextElement();
        }
        else
        {
            keyLength = null;
        }
    }

    public byte[] getSalt()
    {
        return octStr.getOctets();
    }

    public BigInteger getIterationCount()
    {
        return iterationCount.getValue();
    }

    public BigInteger getKeyLength()
    {
        if (keyLength != null)
        {
            return keyLength.getValue();
        }

        return null;
    }

    public DERObject getDERObject()
    {
        ASN1EncodableVector  v = new ASN1EncodableVector();
        ASN1EncodableVector  subV = new ASN1EncodableVector();

        v.add(id);
        subV.add(octStr);
        subV.add(iterationCount);

        if (keyLength != null)
        {
            subV.add(keyLength);
        }

        v.add(new DERSequence(subV));

        return new DERSequence(v);
    }
}
