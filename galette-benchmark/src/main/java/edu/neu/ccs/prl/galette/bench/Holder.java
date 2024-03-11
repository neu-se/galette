package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.io.Serializable;

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
        if (taint) {
            // Static fields
            is = manager.setLabels(7, new Object[] {"is"});
            js = manager.setLabels(7L, new Object[] {"js"});
            zs = manager.setLabels(true, new Object[] {"zs"});
            ss = manager.setLabels((short) 7, new Object[] {"ss"});
            ds = manager.setLabels(7.0, new Object[] {"ds"});
            bs = manager.setLabels((byte) 7, new Object[] {"bs"});
            cs = manager.setLabels((char) 7, new Object[] {"cs"});
            fs = manager.setLabels(7.0f, new Object[] {"fs"});
            ls = manager.setLabels(new Object(), new Object[] {"ls"});
            // Basic values
            this.i = manager.setLabels(7, new Object[] {"i"});
            this.j = manager.setLabels(7L, new Object[] {"j"});
            this.z = manager.setLabels(true, new Object[] {"z"});
            this.s = manager.setLabels((short) 7, new Object[] {"s"});
            this.d = manager.setLabels(7.0, new Object[] {"d"});
            this.b = manager.setLabels((byte) 7, new Object[] {"b"});
            this.c = manager.setLabels((char) 7, new Object[] {"c"});
            this.f = manager.setLabels(7.0f, new Object[] {"f"});
            this.l = manager.setLabels(new Object(), new Object[] {"l"});
            // Boxed primitives
            this.iw = manager.setLabels(7, new Object[] {"iw"});
            this.jw = manager.setLabels(7L, new Object[] {"jw"});
            this.zw = manager.setLabels(true, new Object[] {"zw"});
            this.sw = manager.setLabels((short) 7, new Object[] {"sw"});
            this.dw = manager.setLabels(7.0, new Object[] {"dw"});
            this.bw = manager.setLabels((byte) 7, new Object[] {"bw"});
            this.cw = manager.setLabels((char) 7, new Object[] {"cw"});
            this.fw = manager.setLabels(7.0f, new Object[] {"fw"});
            // One-dimensional arrays
            this.ia = new int[] {manager.setLabels(7, new Object[] {"ia"})};
            this.ja = new long[] {manager.setLabels(7L, new Object[] {"ja"})};
            this.za = new boolean[] {manager.setLabels(true, new Object[] {"za"})};
            this.sa = new short[] {manager.setLabels((short) 7, new Object[] {"sa"})};
            this.da = new double[] {manager.setLabels(7.0, new Object[] {"da"})};
            this.ba = new byte[] {manager.setLabels((byte) 7, new Object[] {"ba"})};
            this.ca = new char[] {manager.setLabels((char) 7, new Object[] {"ca"})};
            this.fa = new float[] {manager.setLabels(7.0f, new Object[] {"fa"})};
            this.la = new Object[] {manager.setLabels(new Object(), new Object[] {"la"})};
            // Two-dimensional arrays
            this.iaa = new int[][] {{manager.setLabels(7, new Object[] {"iaa"})}};
            this.jaa = new long[][] {{manager.setLabels(7L, new Object[] {"jaa"})}};
            this.zaa = new boolean[][] {{manager.setLabels(true, new Object[] {"zaa"})}};
            this.saa = new short[][] {{manager.setLabels((short) 7, new Object[] {"saa"})}};
            this.daa = new double[][] {{manager.setLabels(7.0, new Object[] {"daa"})}};
            this.baa = new byte[][] {{manager.setLabels((byte) 7, new Object[] {"baa"})}};
            this.caa = new char[][] {{manager.setLabels((char) 7, new Object[] {"caa"})}};
            this.faa = new float[][] {{manager.setLabels(7.0f, new Object[] {"faa"})}};
            this.laa = new Object[][] {{manager.setLabels(new Object(), new Object[] {"laa"})}};
            // Objects holding arrays
            this.iao = new int[] {manager.setLabels(7, new Object[] {"iao"})};
            this.jao = new long[] {manager.setLabels(7L, new Object[] {"jao"})};
            this.zao = new boolean[] {manager.setLabels(true, new Object[] {"zao"})};
            this.sao = new short[] {manager.setLabels((short) 7, new Object[] {"sao"})};
            this.dao = new double[] {manager.setLabels(7.0, new Object[] {"dao"})};
            this.bao = new byte[] {manager.setLabels((byte) 7, new Object[] {"bao"})};
            this.cao = new char[] {manager.setLabels((char) 7, new Object[] {"cao"})};
            this.fao = new float[] {manager.setLabels(7.0f, new Object[] {"fao"})};
            this.lao = new Object[] {manager.setLabels(new Object(), new Object[] {"lao"})};
        } else {
            // Static fields
            is = 7;
            js = 7;
            zs = true;
            ss = 7;
            ds = 7;
            bs = 7;
            cs = 7;
            fs = 7;
            ls = new Object();
            // Basic values
            this.i = 7;
            this.j = 7;
            this.z = true;
            this.s = 7;
            this.d = 7;
            this.b = 7;
            this.c = 7;
            this.f = 7;
            this.l = new Object();
            // Boxed primitives
            this.iw = 7;
            this.jw = 7L;
            this.zw = true;
            this.sw = 7;
            this.dw = 7.0;
            this.bw = 7;
            this.cw = 7;
            this.fw = 7.0f;
            // One-dimensional arrays
            this.ia = new int[] {7};
            this.ja = new long[] {7};
            this.za = new boolean[] {true};
            this.sa = new short[] {7};
            this.da = new double[] {7};
            this.ba = new byte[] {7};
            this.ca = new char[] {7};
            this.fa = new float[] {7};
            this.la = new Object[] {new Object()};
            // Two-dimensional arrays
            this.iaa = new int[][] {{7}};
            this.jaa = new long[][] {{7}};
            this.zaa = new boolean[][] {{true}};
            this.saa = new short[][] {{7}};
            this.daa = new double[][] {{7}};
            this.baa = new byte[][] {{7}};
            this.caa = new char[][] {{7}};
            this.faa = new float[][] {{7}};
            this.laa = new Object[][] {{new Object()}};
            // Objects holding arrays
            this.iao = new int[] {7};
            this.jao = new long[] {7};
            this.zao = new boolean[] {true};
            this.sao = new short[] {7};
            this.dao = new double[] {7};
            this.bao = new byte[] {7};
            this.cao = new char[] {7};
            this.fao = new float[] {7};
            this.lao = new Object[] {new Object()};
        }
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
            throw new AssertionError();
        }
    }
}
