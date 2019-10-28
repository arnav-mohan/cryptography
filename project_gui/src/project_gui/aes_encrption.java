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
public class aes_encrption 
{
    byte state[][] = new byte [4][4];
    byte round_key[][] = new byte [4][4];
    
    public byte[][] create_exponent_table()
    {
        byte x,y;
        byte exponent[][]=new byte[16][16];
        byte prev=0x01;
        for(x=0;x<16;x++)
            for(y=0;y<16;y++)
            {
                if(x==0&&y==0)
                    exponent[0][0]=1;
                else
                {
                    if((prev&(0x80))==0)
                        exponent[x][y]=(byte)((prev<<1)^prev);
                    else
                        exponent[x][y]=(byte)((prev<<1)^prev^0x11b);
                }
                prev=exponent[x][y];
            }
        return exponent;
    }


    public byte[][] create_log_table(byte exponent_table[][])
    {
        byte x,y;
        byte log[][]=new byte[16][16];
        for(x=0;x<16;x++)
            for(y=0;y<16;y++)
            {
                byte b=exponent_table[x][y];
                byte a=(byte)((b&0xf0)>>4);
                b&=0x0f;
                log[a][b]=(byte)((x<<4)|y);
            }
        return log;
    }


    public byte[][] create_s_box(byte exponent_table[][], byte log_table[][])
    {
        byte x,y;
        byte box[][]=new byte[16][16];
        for(x=0;x<16;x++)
            for(y=0;y<16;y++)
            {
                byte b=(byte)(0xff-log_table[x][y]);
                byte a=(byte)((b&0xf0)>>4);
                b&=0x0f;
                byte d,s;
                s=d=exponent_table[a][b];
                for(int z=0;z<4;z++)
                {
                    if((s&0x80)!=0)
                        s=(byte)(((s<<1)^0x100)|0x01);
                    else
                        s=(byte)(s<<1);
                    d^=s;
                }
                box[x][y]=(byte)(d^0x63);
            }
        box[0][0]=0x63;
        return box;
    }


    public byte s_box_val(byte n, byte s_box[][])
    {
        byte a=(byte)((n&0xf0)>>4);
        n&=0x0f;
        return s_box[a][n];
    }


    private void subBytes(byte s_box[][])
    {
        for(int x=0;x<4;x++)
            for(int y=0;y<4;y++)
                state[x][y]=s_box_val(state[x][y], s_box);
    }


    private void shiftRows()
    {
        for(int x=1;x<4;x++)
            for(int y=0;y<x;y++)
            {
                byte a=state[x][0];
                state[x][0]=state[x][1];
                state[x][1]=state[x][2];
                state[x][2]=state[x][3];
                state[x][3]=a;
            }
    }


    private void mixColumns()
    {
        byte mixed[]=new byte[4];
        for(int y=0;y<4;y++)
        {
            for(int x=0;x<4;x++)
                mixed[x]=(byte)(((state[x][y]<<1)^(((state[x][y]<<1&0x100)!=0x00)?0x11b:0x00))^(state[(x+1)%4][y]^(state[(x+1)%4][y]<<1)^(((state[(x+1)%4][y]<<1&0x100)!=0x00)?0x11b:0x00))^state[(x+2)%4][y]^state[(x+3)%4][y]);
            for(int x=0;x<4;x++)
                state[x][y]=mixed[x];
        }
    }


    private void makeRoundKey(byte s_box[][],int round_no)
    {
        byte rcon[]={0x01,0x02,0x04,0x08,0x10,0x20,0x40,(byte)0x80,(byte)0x1b,0x36};
        round_key[0][0]=(byte)(s_box_val(round_key[1][3],s_box)^round_key[0][0]^rcon[round_no]);
        round_key[1][0]=(byte)(s_box_val(round_key[2][3],s_box)^round_key[1][0]);
        round_key[2][0]=(byte)(s_box_val(round_key[3][3],s_box)^round_key[2][0]);
        round_key[3][0]=(byte)(s_box_val(round_key[0][3],s_box)^round_key[3][0]);

        for(int x=1;x<4;x++)
            for(int y=0;y<4;y++)
                round_key[y][x]^=round_key[y][x-1];
    }


    private void addRoundKey()
    {
        for(int x=0;x<4;x++)
            for(int y=0;y<4;y++)
                state[x][y]^=round_key[x][y];
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

    private void display_key()
    {
        for(int a=0;a<4;a++)
        {
            for(int c=0;c<4;c++)
                System.out.printf("%03x  ", round_key[a][c]);
            System.out.println();
        }
        System.out.println();
    }


    public byte[] encrypt(byte [] input, byte [] key)
    {
        int a,b,c,d=0,e=0,f=0,g=0,x=0;
        byte exponent_table[][]=create_exponent_table();
        byte log_table[][]=create_log_table(exponent_table);
        byte s_box[][]=create_s_box(exponent_table,log_table);
        f=input.length;
        g=f+(f%16==0?0:16)-(f%16);
        byte [] enc = new byte [g];
        for(b=0;b<4;b++)
            for(c=0;c<4;c++)
                round_key[c][b]=key[b*4+c];
        for(a=0;a<f;a+=16)
        {
            for(b=0;b<4;b++)
                for(c=0;c<4;c++)
                {
                    if(d<f)
                        state[c][b]=input[d++];
                    else
                        state[c][b]=0;
                }
            addRoundKey();
            for(x=0;x<9;x++)
            {
                subBytes(s_box);
                shiftRows();
                mixColumns();
                makeRoundKey(s_box,x);
                addRoundKey();
            }
            subBytes(s_box);
            shiftRows();
            makeRoundKey(s_box,9);
            addRoundKey();
            for(b=0;b<4;b++)
                for(c=0;c<4;c++)
                    enc[e++]=state[c][b];
        }
        return enc ;
    }

}
