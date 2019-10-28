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
public class aes_decryption 
{
    short state[][] = new short[4][4];
    short round_key[][] = new short[4][44];

    private short[][] inv_create_exponent_table()
    {
        short x,y;
        short exponent[][]=new short[16][16];
        short prev=0x01;
        for(x=0;x<16;x++)
            for(y=0;y<16;y++)
            {
                if(x==0&&y==0)
                    exponent[0][0]=1;
                else
                {
                    if((prev&(0x80))==0)
                        exponent[x][y]=(short)((prev<<1)^prev);
                    else
                        exponent[x][y]=(short)((prev<<1)^prev^0x11b);
                }
                prev=exponent[x][y];
            }
        return exponent;
    }

    private short[][] inv_create_log_table(short exponent_table[][])
    {
        short x,y;
        short log[][]=new short[16][16];
        for(x=0;x<16;x++)
            for(y=0;y<16;y++)
            {
                short b=exponent_table[x][y];
                short a=(short)((b&0xf0)>>4);
                b&=0x0f;
                log[a][b]=(short)((x<<4)|y);
            }
        return log;
    }

    private short[][] inv_create_s_box(short exponent_table[][], short log_table[][])
    {
        short x,y;
        short box[][]=new short[16][16];
        for(x=0;x<16;x++)
            for(y=0;y<16;y++)
            {
                short n=(short)((x<<4)|y);
                short b=(short)(0xff-log_table[x][y]);
                short a=(short)((b&0xf0)>>4);
                b&=0x0f;
                short d,s;
                s=d=exponent_table[a][b];
                for(int z=0;z<4;z++)
                {
                    if((s&0x80)!=0)
                        s=(short)(((s<<1)^0x100)|0x01);
                    else
                        s=(short)(s<<1);
                    d^=s;
                }
                box[x][y]=(short)(d^0x63);
            }
        box[0][0]=(short)0x63;
        return box;
    }

    private static short inv_s_box_val(short n, short s_box[][])
    {
        short a=(short)((n&0xf0)>>4);
        n&=0x0f;
        return s_box[a][n];
    }

    private void display_state()
    {
        for(int a=0;a<4;a++)
        {
            for(int b=0;b<4;b++)
                System.out.printf("%03x  ", state[a][b]);
            System.out.println();
        }
        System.out.println();
    }

    private short[][] invert_s_box(short s_box[][])
    {
        short inv_s_box[][]=new short[16][16];
        for(int x=0;x<16;x++)
            for(int y=0;y<16;y++)
            {
                short n=s_box[x][y];
                short a=(short)((n&0xf0)>>4);
                n&=0x0f;
                inv_s_box[a][n]=(short)((x<<4)|y);
            }
        return inv_s_box;
    }

    private void make_round_key(short inv_s_box[][], short [][]key)
    {
        int v=0;
        short rcon[]={0x01,0x02,0x04,0x08,0x10,0x20,0x40,0x80,0x1b,0x36};
        for(int z=10;z>=0;z--)
        {
            for(int x=0;x<4;x++)
                for(int y=0;y<4;y++)
                    round_key[x][4*z+y]=key[x][y];
            if(z==0)
                break;
            key[0][0]=(short)(inv_s_box_val(key[1][3],inv_s_box)^key[0][0]^rcon[v++]);
            key[1][0]=(short)(inv_s_box_val(key[2][3],inv_s_box)^key[1][0]);
            key[2][0]=(short)(inv_s_box_val(key[3][3],inv_s_box)^key[2][0]);
            key[3][0]=(short)(inv_s_box_val(key[0][3],inv_s_box)^key[3][0]);
            for(int x=1;x<4;x++)
                for(int y=0;y<4;y++)
                    key[y][x]^=key[y][x-1];
        }
    }

    private void addRoundKey(int round_no)
    {
        for(int x=0;x<4;x++)
            for(int y=0;y<4;y++)
                state[x][y]^=round_key[x][4*round_no+y];
    }

    private void invSubByte(short inv_s_box[][])
    {
        for(int x=0;x<4;x++)
            for(int y=0;y<4;y++)
                state[x][y]=inv_s_box_val(state[x][y], inv_s_box);
    }

    private void invShiftRows()
    {
        for(int x=1;x<4;x++)
            for(int y=0;y<x;y++)
            {
                short a=state[x][3];
                state[x][3]=state[x][2];
                state[x][2]=state[x][1];
                state[x][1]=state[x][0];
                state[x][0]=a;
            }
    }

    private short mod(short n)
    {
        for(int x=2;x>=0;x--)
        {
            if((n&0x100<<x)!=0)
                n^=0x11b<<x;
        }
        return n;
    }

    private  void invMixColumns()
    {
        short mixed[]=new short[4];
        for(int y=0;y<4;y++)
        {
            for(short x=0;x<4;x++)
                mixed[x]=(short)((mod((short)(state[x][y]<<3))^(short)mod((short)(state[x][y]<<2))^mod((short)(state[x][y]<<1)))^(mod((short)(state[(x+1)%4][y]<<3))^(short)mod((short)(state[(x+1)%4][y]<<1))^(state[(x+1)%4][y]))^(mod((short)(state[(x+2)%4][y]<<3))^(short)mod((short)(state[(x+2)%4][y]<<2))^(state[(x+2)%4][y]))^((short)mod((short)(state[(x+3)%4][y]<<3))^(state[(x+3)%4][y])));

            for(int x=0;x<4;x++)
                state[x][y]=mixed[x];
        }
    }

    public byte[] decrypt(byte [] input, byte [] generated_key)
    {
        int a,b,c,d=0,e=0,f=0;
        short exponent_table[][]=inv_create_exponent_table();
        short log_table[][]=inv_create_log_table(exponent_table);
        short s_box[][]=inv_create_s_box(exponent_table,log_table);
        short inv_s_box[][]=invert_s_box(s_box);
        short [][] key = new short [4][4];
        for(b=0;b<4;b++)
            for(c=0;c<4;c++)
                key[c][b]=(short)(generated_key[b*4+c]&0xff);
        f=input.length;
        byte [] dec = new byte [f];
        
        for(a=0;a<f;a+=16)
        {
            int x=0;
            for(b=0;b<4;b++)
                for(c=0;c<4;c++)
                    state[c][b]=(short)(input[d++]&0xff);
            make_round_key(s_box,key);
            addRoundKey(x++);
            for(;x<10;x++)
            {
                invSubByte(inv_s_box);
                invShiftRows();
                addRoundKey(x);
                invMixColumns();
            }
            invShiftRows();
            invSubByte(inv_s_box);
            addRoundKey(x);
            for(b=0;b<4;b++)
                for(c=0;c<4;c++)
                    dec[e++]=(byte)state[c][b];
        }
        return dec;
    }

}
