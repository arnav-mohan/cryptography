/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project_gui;

/**
 *
 * @author arnav
 */
public class sha_256 
{
    int [] h = {0x6a09e667,0xbb67ae85,0x3c6ef372,0xa54ff53a,0x510e527f,0x9b05688c,0x1f83d9ab,0x5be0cd19};
    final int [] k = {  0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
                        0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
                        0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
                        0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
                        0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
                        0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
                        0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
                        0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2};
    
    public byte [] hash(byte [] input)
    {
        int n = input.length;
        int s = (int)n%64;
        int p = (s<56?(64-s):(128-s));
        byte [] data = new byte [n+p];
        System.arraycopy(input, 0, data, 0, n);
        data[n]=(byte)0x80;
        for(int x=1;x<p;x++)
            data[n+x]=0;
        for(int i=3;i>=0;i--)
        {
            byte q=(byte)((n<<3&(0xff<<(i*8)))>>>(i*8));
            data[n+p-i-1] = q;
        }
        int [] e = new int[4];
        int [] w = new int [64];
        int u = 0;
        while(true)
        {
            int x=0;
            try
            {
                while(x<16)
                {
                    for(int y=0;y<4;y++)
                        e[y]=data[u++]&0xff;
                    w[x++] = (e[0]<<24) | (e[1]<<16) | (e[2]<<8) | e[3];
                }
            }
            catch(ArrayIndexOutOfBoundsException ex)
            {
                break;
            }
            for(int i=16;i<64;i++)
            {
                int s0 = r_rot(w[i-15],7) ^ r_rot(w[i-15],18) ^ (w[i-15] >>> 3);
                int s1 = r_rot(w[i-2],17) ^ r_rot(w[i-2],19) ^ (w[i-2] >>> 10);
                w[i] = (w[i-16] + s0 + w[i-7] + s1);
            }
            int [] r = new int [8];
            
            System.arraycopy(h, 0, r, 0, 8);
            for (int i=0;i<64;i++)
            {
                int S1 = r_rot(r[4],6) ^ r_rot(r[4],11) ^ r_rot(r[4],25);
                int ch = (r[4] & r[5]) ^ ((~ r[4]) & r[6]);
                int temp1 = (r[7] + S1 + ch + k[i] + w[i])%0x80000000;
                int S0 = r_rot(r[0],2) ^ r_rot(r[0],13) ^ r_rot(r[0],22);
                int maj = (r[0] & r[1]) ^ (r[0] & r[2]) ^ (r[1] & r[2]);
                int temp2 = S0 + maj;
                for(int j=7;j>0;j--)
                {
                    r[j] = r[j-1];
                    if(j==4)
                        r[4] += temp1;
                    if(j==1)
                        r[0] = temp1 + temp2;
                }
            }
            for(int i=0;i<8;i++)
                h[i]+=r[i];
        }
        byte [] hash_value = new byte [32];
        hash_value[0]=0;
        for(int i=0;i<8;i++)
        {
            for(int j=3;j>=0;j--)
            {
                byte q=(byte)((h[i]&(0xff<<(j*8)))>>>(j*8));
                hash_value [i*4+(3-j)] = q;
            }
        }
        return hash_value;
    }
    
    private int r_rot(int a, int b)
    {
        for(int x=0;x<b;x++)
        {
            if((a&1)==1)
                a=(a>>>1)|0x80000000;
            else
                a=(a>>>1);
        }
        return a;
    }    
}