/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rsa_encryption;

/**
 *
 * @author arnav
 */
import java.math.BigInteger;
import java.util.Random;

public class rsa_keygen 
{
    public BigInteger[][] generate()
    {
        Random r = new Random();
        BigInteger p = new BigInteger(1024,10,r);
        BigInteger q = new BigInteger(1024,10,r);
        if(p.equals(q))
            q = q.nextProbablePrime();
        BigInteger n = p.multiply(q);
        BigInteger pn = n.subtract((p.add(q)).subtract(BigInteger.ONE));
        BigInteger e = new BigInteger(32,10,r);
        if(e.mod(pn).equals(BigInteger.ZERO))
            e = e.nextProbablePrime();
        BigInteger d = e.modInverse(pn);
        BigInteger [][] key = {{e,n},{d,n}};
        return key;
    }
    
}
