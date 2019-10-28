/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project_gui;
/*
import java.io.*;
import java.math.BigInteger;
import java.util.Random;
*/
/**
 *
 * @author arnav
 */
public class Drdl_project {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    //public static void main(String[] args)throws FileNotFoundException
    //{
       // new project_gui ();
    //}
    /*
    public static void main(String[] args)throws IOException {
        // TODO code application logic here
        
        File file = new File("C:\\Users\\arnav\\Desktop\\presentation\\input.flv");
        byte[] input;
        String rsa_private_key_n;
        String rsa_public_key_n;
        String rsa_private_key_d;
        String rsa_public_key_e;
        try (FileInputStream in = new FileInputStream (file)) {
            input = new byte [(int)file.length()];
            in.read(input);
            in.close();
        }
        
        sha_256 digest = new sha_256();
        byte [] hash = digest.hash(input);

        byte [] hash2 = new byte[33];
            hash2 [0]=0;
            for(int x=1;x<33;x++)
                hash2[x]=hash[x-1];
        System.out.println("Hash2");
        for(int x=0;x<hash2.length;x++)
            System.out.printf("%02x",hash2[x]);
        System.out.println();

        FileOutputStream u = new FileOutputStream("hash.txt");
        u.write(hash);
        u.close();
        
        
        Random r = new Random();
        BigInteger key_generate = new BigInteger(128,r);
        byte [] key = key_generate.toByteArray();
        aes_encrption e = new aes_encrption();
        byte [] encrypted = e.encrypt(input, key);
        
        file = new File("Private_key.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        rsa_private_key_n=br.readLine();
        rsa_private_key_d=br.readLine();
        fr.close();
        
        file = new File("Public_key.txt");
        fr=new FileReader(file);
        br=new BufferedReader(fr);
        rsa_public_key_n=br.readLine();
        rsa_public_key_e=br.readLine();
        fr.close();
        
        BigInteger prkn = new BigInteger(rsa_private_key_n);
        BigInteger prkd = new BigInteger(rsa_private_key_d);
        BigInteger pukn = new BigInteger(rsa_public_key_n);
        BigInteger puke = new BigInteger(rsa_public_key_e);
        BigInteger inp = new BigInteger(hash2);
        rsa_enc_dec rsa = new rsa_enc_dec();
        
        byte [] sign = rsa.encrypt_decrypt(inp, prkd, prkn);
        BigInteger keygen = new BigInteger(key);
        byte [] enckey = rsa.encrypt_decrypt(keygen, puke, pukn);
        byte [] delim = {'$','|',']','['};
        file = new File("encrypted.txt");
        FileOutputStream out = new FileOutputStream (file);
        out.write(encrypted);
        out.write(delim);
        out.write(enckey);
        out.write(delim);
        out.write(sign);
        out.close();
        
        String rsa_private_key_n1;
        String rsa_public_key_n1;
        String rsa_private_key_d1;
        String rsa_public_key_e1;
        file = new File("encrypted.txt");
        byte [] enc1;
        int [] delim1 = new int [2];
        int a=0,b=0,c=0;
        try (FileInputStream in = new FileInputStream (file)) {
            enc1 = new byte [(int)file.length()];
            in.read(enc1);
            in.close();
        }
        for(int x=0;x<enc1.length-4;x++)
        {
            if(enc1[x]=='$'&&enc1[x+1]=='|'&&enc1[x+2]==']'&&enc1[x+3]=='[')
                delim1[a++]=x;
        }
        
        byte [] aes1 = new byte [delim1[0]];
        byte [] rsa_key1 = new byte [delim1[1]-delim1[0]-4];
        byte [] rsa_hash1 = new byte [enc1.length-delim1[1]-4];
        
        for(int x=0;x<enc1.length;x++)
        {
            if(x<delim1[0])
                aes1[x]=enc1[x];
            else if(x>=(delim1[0]+4)&&x<delim1[1])
                rsa_key1[b++]=enc1[x];
            else if (x>=(delim1[1]+4))
                rsa_hash1[c++]=enc1[x];
        }
        
        file = new File("Private_key.txt");
        fr = new FileReader(file);
        br = new BufferedReader(fr);
        rsa_private_key_n1=br.readLine();
        rsa_private_key_d1=br.readLine();
        fr.close();
        
        file = new File("Public_key.txt");
        fr=new FileReader(file);
        br=new BufferedReader(fr);
        rsa_public_key_n1=br.readLine();
        rsa_public_key_e1=br.readLine();
        fr.close();

        BigInteger rk = new BigInteger(rsa_key1);
        BigInteger hs = new BigInteger(rsa_hash1);
        BigInteger prkeyd = new BigInteger(rsa_private_key_d1);
        BigInteger prkeyn = new BigInteger(rsa_private_key_n1);
        BigInteger pukeye = new BigInteger(rsa_public_key_e1);
        BigInteger pukeyn = new BigInteger(rsa_public_key_n1);
        rsa_enc_dec r1 = new rsa_enc_dec();
        byte [] key1 = r1.encrypt_decrypt(rk,prkeyd,prkeyn);
        byte [] hash1 = r1.encrypt_decrypt(hs, pukeye, pukeyn);
        for(int x=0;x<hash1.length;x++)
            System.out.printf("%02x",hash1[x]);
        System.out.println();

        
        byte [] hash3 = new byte [hash1.length-1];

        for (int x=0; x<hash1.length-1;x++)
        {
            hash3[x]=hash1[x+1];
        }
        
        aes_decryption ad = new aes_decryption();
        byte [] dec = ad.decrypt(aes1, key1);
        int y=0;
        for(int x=dec.length-1;x>=0;x--)
            if(dec[x]==0)
                y++;
            else
                break;
        byte [] dec1 = new byte [dec.length-y];
        System.arraycopy(dec, 0, dec1, 0, dec1.length);
        System.out.println("asd "+dec1[dec1.length-1]+"www"+input[input.length-2]);
        sha_256 sha = new sha_256();
        byte[] hash_value = sha.hash(dec1);
        int flag =0;
        if(hash1[0]==0)
            for(int x=0;x<hash_value.length;x++)
            {
                if(hash3[x]!=hash_value[x])
                {
                    System.out.println("Unauthorised");
                    flag=1;
                    break;
                }
            }
        else
            for(int x=0;x<hash_value.length;x++)
            {
                if(hash1[x]!=hash_value[x])
                {
                    System.out.println("Unauthorised");
                    flag=1;
                    break;
                }
            }
            
        
        if(flag==0)
            System.out.println("Authorised");
        
        for(int x=0;x<hash_value.length;x++)
            System.out.printf("%02x",hash_value[x]);
        System.out.println();

        for(int x=0;x<hash3.length;x++)
            System.out.printf("%02x",hash3[x]);
        System.out.println();
        
        out = new FileOutputStream("C:\\Users\\arnav\\Desktop\\presentation\\Decrypted.avi");
        out.write(dec1);
        out.close();
         
        //try (FileOutputStream out = new FileOutputStream("Hash.txt")) {
        //   out.write(hash);
        //    out.close();
        //}
    }   
    */
}