/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project_gui;

import java.math.BigInteger;

/**
 *
 * @author arnav
 */
public class rsa_enc_dec 
{
    
    byte [] encrypt_decrypt (BigInteger s, BigInteger key1, BigInteger key2)
    {
        byte [] a = (s.modPow(key1,key2)).toByteArray();
        return a;
    }
}
