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

public class rsa_enc_dec 
{    
    byte [] encrypt_decrypt (BigInteger s, BigInteger key1, BigInteger key2)
    {
        byte [] a;
        a = (s.modPow(key1,key2)).toByteArray();
        return a;
    }
}
