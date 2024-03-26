package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.io.Serializable;

@SuppressWarnings("unused")
public final class Holder implements Serializable {
    private static final long serialVersionUID = -4191882307038774199L;
    // Basic static values
    public static int is;
    public static long js;
    public static boolean zs;
    public static short ss;
    public static double ds;
    public static byte bs;
    public static char cs;
    public static float fs;
    public static Object ls;
    // Basic values
    public int i;
    public long j;
    public boolean z;
    public short s;
    public double d;
    public byte b;
    public char c;
    public float f;
    public Object l;
    // Boxed primitives
    public Integer iw;
    public Long jw;
    public Boolean zw;
    public Short sw;
    public Double dw;
    public Byte bw;
    public Character cw;
    public Float fw;
    // One-dimensional arrays
    public int[] ia;
    public long[] ja;
    public boolean[] za;
    public short[] sa;
    public double[] da;
    public byte[] ba;
    public char[] ca;
    public float[] fa;
    public Object[] la;
    // Two-dimensional arrays
    public int[][] iaa;
    public long[][] jaa;
    public boolean[][] zaa;
    public short[][] saa;
    public double[][] daa;
    public byte[][] baa;
    public char[][] caa;
    public float[][] faa;
    public Object[][] laa;
    // Object holding primitive arrays
    public Object iao;
    public Object jao;
    public Object zao;
    public Object sao;
    public Object dao;
    public Object bao;
    public Object cao;
    public Object fao;
    public Object lao;

    public Holder(TagManager manager, boolean taint) {
        this(manager, taint, 7, true, new Object());
    }

    public Holder(TagManager manager, boolean taint, int value, boolean z, Object obj) {
        if (taint) {
            // Static fields
            is = manager.setLabels(value, new Object[] {"is"});
            js = manager.setLabels((long) value, new Object[] {"js"});
            zs = manager.setLabels(z, new Object[] {"zs"});
            ss = manager.setLabels((short) value, new Object[] {"ss"});
            ds = manager.setLabels((double) value, new Object[] {"ds"});
            bs = manager.setLabels((byte) value, new Object[] {"bs"});
            cs = manager.setLabels((char) value, new Object[] {"cs"});
            fs = manager.setLabels((float) value, new Object[] {"fs"});
            ls = manager.setLabels(obj, new Object[] {"ls"});
            // Basic values
            this.i = manager.setLabels(value, new Object[] {"i"});
            this.j = manager.setLabels((long) value, new Object[] {"j"});
            this.z = manager.setLabels(z, new Object[] {"z"});
            this.s = manager.setLabels((short) value, new Object[] {"s"});
            this.d = manager.setLabels((double) value, new Object[] {"d"});
            this.b = manager.setLabels((byte) value, new Object[] {"b"});
            this.c = manager.setLabels((char) value, new Object[] {"c"});
            this.f = manager.setLabels((float) value, new Object[] {"f"});
            this.l = manager.setLabels(obj, new Object[] {"l"});
            // Boxed primitives
            this.iw = manager.setLabels(value, new Object[] {"iw"});
            this.jw = manager.setLabels((long) value, new Object[] {"jw"});
            this.zw = manager.setLabels(z, new Object[] {"zw"});
            this.sw = manager.setLabels((short) value, new Object[] {"sw"});
            this.dw = manager.setLabels((double) value, new Object[] {"dw"});
            this.bw = manager.setLabels((byte) value, new Object[] {"bw"});
            this.cw = manager.setLabels((char) value, new Object[] {"cw"});
            this.fw = manager.setLabels((float) value, new Object[] {"fw"});
            // One-dimensional arrays
            this.ia = new int[] {manager.setLabels(value, new Object[] {"ia"})};
            this.ja = new long[] {manager.setLabels((long) value, new Object[] {"ja"})};
            this.za = new boolean[] {manager.setLabels(z, new Object[] {"za"})};
            this.sa = new short[] {manager.setLabels((short) value, new Object[] {"sa"})};
            this.da = new double[] {manager.setLabels((double) value, new Object[] {"da"})};
            this.ba = new byte[] {manager.setLabels((byte) value, new Object[] {"ba"})};
            this.ca = new char[] {manager.setLabels((char) value, new Object[] {"ca"})};
            this.fa = new float[] {manager.setLabels((float) value, new Object[] {"fa"})};
            this.la = new Object[] {manager.setLabels(obj, new Object[] {"la"})};
            // Two-dimensional arrays
            this.iaa = new int[][] {{manager.setLabels(value, new Object[] {"iaa"})}};
            this.jaa = new long[][] {{manager.setLabels((long) value, new Object[] {"jaa"})}};
            this.zaa = new boolean[][] {{manager.setLabels(z, new Object[] {"zaa"})}};
            this.saa = new short[][] {{manager.setLabels((short) value, new Object[] {"saa"})}};
            this.daa = new double[][] {{manager.setLabels((double) value, new Object[] {"daa"})}};
            this.baa = new byte[][] {{manager.setLabels((byte) value, new Object[] {"baa"})}};
            this.caa = new char[][] {{manager.setLabels((char) value, new Object[] {"caa"})}};
            this.faa = new float[][] {{manager.setLabels((float) value, new Object[] {"faa"})}};
            this.laa = new Object[][] {{manager.setLabels(obj, new Object[] {"laa"})}};
            // Objects holding arrays
            this.iao = new int[] {manager.setLabels(value, new Object[] {"iao"})};
            this.jao = new long[] {manager.setLabels((long) value, new Object[] {"jao"})};
            this.zao = new boolean[] {manager.setLabels(z, new Object[] {"zao"})};
            this.sao = new short[] {manager.setLabels((short) value, new Object[] {"sao"})};
            this.dao = new double[] {manager.setLabels((double) value, new Object[] {"dao"})};
            this.bao = new byte[] {manager.setLabels((byte) value, new Object[] {"bao"})};
            this.cao = new char[] {manager.setLabels((char) value, new Object[] {"cao"})};
            this.fao = new float[] {manager.setLabels((float) value, new Object[] {"fao"})};
            this.lao = new Object[] {manager.setLabels(obj, new Object[] {"lao"})};
        } else {
            // Static fields
            is = value;
            js = value;
            zs = z;
            ss = (short) value;
            ds = value;
            bs = (byte) value;
            cs = (char) value;
            fs = (float) value;
            ls = obj;
            // Basic values
            this.i = value;
            this.j = value;
            this.z = z;
            this.s = (short) value;
            this.d = value;
            this.b = (byte) value;
            this.c = (char) value;
            this.f = (float) value;
            this.l = obj;
            // Boxed primitives
            this.iw = value;
            this.jw = (long) value;
            this.zw = z;
            this.sw = (short) value;
            this.dw = (double) value;
            this.bw = (byte) value;
            this.cw = (char) value;
            this.fw = (float) value;
            // One-dimensional arrays
            this.ia = new int[] {value};
            this.ja = new long[] {(long) value};
            this.za = new boolean[] {z};
            this.sa = new short[] {(short) value};
            this.da = new double[] {(double) value};
            this.ba = new byte[] {(byte) value};
            this.ca = new char[] {(char) value};
            this.fa = new float[] {(float) value};
            this.la = new Object[] {obj};
            // Two-dimensional arrays
            this.iaa = new int[][] {{value}};
            this.jaa = new long[][] {{(long) value}};
            this.zaa = new boolean[][] {{z}};
            this.saa = new short[][] {{(short) value}};
            this.daa = new double[][] {{(double) value}};
            this.baa = new byte[][] {{(byte) value}};
            this.caa = new char[][] {{(char) value}};
            this.faa = new float[][] {{(float) value}};
            this.laa = new Object[][] {{obj}};
            // Objects holding arrays
            this.iao = new int[] {value};
            this.jao = new long[] {(long) value};
            this.zao = new boolean[] {z};
            this.sao = new short[] {(short) value};
            this.dao = new double[] {(double) value};
            this.bao = new byte[] {(byte) value};
            this.cao = new char[] {(char) value};
            this.fao = new float[] {(float) value};
            this.lao = new Object[] {obj};
        }
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(long j) {
        this.j = j;
    }

    public void setZ(boolean z) {
        this.z = z;
    }

    public void setS(short s) {
        this.s = s;
    }

    public void setD(double d) {
        this.d = d;
    }

    public void setB(byte b) {
        this.b = b;
    }

    public void setC(char c) {
        this.c = c;
    }

    public void setF(float f) {
        this.f = f;
    }

    public void setL(Object l) {
        this.l = l;
    }

    public void setIw(Integer iw) {
        this.iw = iw;
    }

    public void setJw(Long jw) {
        this.jw = jw;
    }

    public void setZw(Boolean zw) {
        this.zw = zw;
    }

    public void setSw(Short sw) {
        this.sw = sw;
    }

    public void setDw(Double dw) {
        this.dw = dw;
    }

    public void setBw(Byte bw) {
        this.bw = bw;
    }

    public void setCw(Character cw) {
        this.cw = cw;
    }

    public void setFw(Float fw) {
        this.fw = fw;
    }

    public void setIa(int[] ia) {
        this.ia = ia;
    }

    public void setJa(long[] ja) {
        this.ja = ja;
    }

    public void setZa(boolean[] za) {
        this.za = za;
    }

    public void setSa(short[] sa) {
        this.sa = sa;
    }

    public void setDa(double[] da) {
        this.da = da;
    }

    public void setBa(byte[] ba) {
        this.ba = ba;
    }

    public void setCa(char[] ca) {
        this.ca = ca;
    }

    public void setFa(float[] fa) {
        this.fa = fa;
    }

    public void setLa(Object[] la) {
        this.la = la;
    }

    public void setIaa(int[][] iaa) {
        this.iaa = iaa;
    }

    public void setJaa(long[][] jaa) {
        this.jaa = jaa;
    }

    public void setZaa(boolean[][] zaa) {
        this.zaa = zaa;
    }

    public void setSaa(short[][] saa) {
        this.saa = saa;
    }

    public void setDaa(double[][] daa) {
        this.daa = daa;
    }

    public void setBaa(byte[][] baa) {
        this.baa = baa;
    }

    public void setCaa(char[][] caa) {
        this.caa = caa;
    }

    public void setFaa(float[][] faa) {
        this.faa = faa;
    }

    public void setLaa(Object[][] laa) {
        this.laa = laa;
    }

    public void setIao(Object iao) {
        this.iao = iao;
    }

    public void setJao(Object jao) {
        this.jao = jao;
    }

    public void setZao(Object zao) {
        this.zao = zao;
    }

    public void setSao(Object sao) {
        this.sao = sao;
    }

    public void setDao(Object dao) {
        this.dao = dao;
    }

    public void setBao(Object bao) {
        this.bao = bao;
    }

    public void setCao(Object cao) {
        this.cao = cao;
    }

    public void setFao(Object fao) {
        this.fao = fao;
    }

    public void setLao(Object lao) {
        this.lao = lao;
    }

    public static int getIs() {
        return is;
    }

    public static long getJs() {
        return js;
    }

    public static boolean getZs() {
        return zs;
    }

    public static short getSs() {
        return ss;
    }

    public static double getDs() {
        return ds;
    }

    public static byte getBs() {
        return bs;
    }

    public static char getCs() {
        return cs;
    }

    public static float getFs() {
        return fs;
    }

    public static Object getLs() {
        return ls;
    }

    public int getI() {
        return i;
    }

    public long getJ() {
        return j;
    }

    public boolean getZ() {
        return z;
    }

    public short getS() {
        return s;
    }

    public double getD() {
        return d;
    }

    public byte getB() {
        return b;
    }

    public char getC() {
        return c;
    }

    public float getF() {
        return f;
    }

    public Object getL() {
        return l;
    }

    public Integer getIw() {
        return iw;
    }

    public Long getJw() {
        return jw;
    }

    public Boolean getZw() {
        return zw;
    }

    public Short getSw() {
        return sw;
    }

    public Double getDw() {
        return dw;
    }

    public Byte getBw() {
        return bw;
    }

    public Character getCw() {
        return cw;
    }

    public Float getFw() {
        return fw;
    }

    public int[] getIa() {
        return ia;
    }

    public long[] getJa() {
        return ja;
    }

    public boolean[] getZa() {
        return za;
    }

    public short[] getSa() {
        return sa;
    }

    public double[] getDa() {
        return da;
    }

    public byte[] getBa() {
        return ba;
    }

    public char[] getCa() {
        return ca;
    }

    public float[] getFa() {
        return fa;
    }

    public Object[] getLa() {
        return la;
    }

    public int[][] getIaa() {
        return iaa;
    }

    public long[][] getJaa() {
        return jaa;
    }

    public boolean[][] getZaa() {
        return zaa;
    }

    public short[][] getSaa() {
        return saa;
    }

    public double[][] getDaa() {
        return daa;
    }

    public byte[][] getBaa() {
        return baa;
    }

    public char[][] getCaa() {
        return caa;
    }

    public float[][] getFaa() {
        return faa;
    }

    public Object[][] getLaa() {
        return laa;
    }

    public Object getIao() {
        return iao;
    }

    public Object getJao() {
        return jao;
    }

    public Object getZao() {
        return zao;
    }

    public Object getSao() {
        return sao;
    }

    public Object getDao() {
        return dao;
    }

    public Object getBao() {
        return bao;
    }

    public Object getCao() {
        return cao;
    }

    public Object getFao() {
        return fao;
    }

    public Object getLao() {
        return lao;
    }

    public static void setIs(int is) {
        Holder.is = is;
    }

    public static void setJs(long js) {
        Holder.js = js;
    }

    public static void setZs(boolean zs) {
        Holder.zs = zs;
    }

    public static void setSs(short ss) {
        Holder.ss = ss;
    }

    public static void setDs(double ds) {
        Holder.ds = ds;
    }

    public static void setBs(byte bs) {
        Holder.bs = bs;
    }

    public static void setCs(char cs) {
        Holder.cs = cs;
    }

    public static void setFs(float fs) {
        Holder.fs = fs;
    }

    public static void setLs(Object ls) {
        Holder.ls = ls;
    }

    public static String getBasicName(Class<?> clazz) {
        if (clazz == Integer.TYPE) {
            return "i";
        } else if (clazz == Boolean.TYPE) {
            return "z";
        } else if (clazz == Byte.TYPE) {
            return "b";
        } else if (clazz == Character.TYPE) {
            return "c";
        } else if (clazz == Short.TYPE) {
            return "s";
        } else if (clazz == Double.TYPE) {
            return "d";
        } else if (clazz == Float.TYPE) {
            return "f";
        } else if (clazz == Long.TYPE) {
            return "j";
        } else if (clazz == Object.class) {
            return "l";
        } else {
            throw new IllegalArgumentException();
        }
    }
}
