package ru.SnowVolf.convertx.algorhitms;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.security.MessageDigest.getInstance;

/**
 * Created by Snow Volf on 18.02.2017.
 */

public class ChecksumAlgs {
    /**
     * В оф документации по Android есть заметка, что SHA-224 добавили в API 1, в API 9 убрали,
     * потом снова вернули в API 22.
     * Почему так -- хз. Не стал генерить еще один catch, их тут и так достаточно.
     *
     * Поэтому просто показываем сообщение, если алгоритм в getInstance()
     * не существует.
     */
    private static String errAlg = "Неизвестный алгоритм. SDK < 22";

    public static String MD5(String MD) {
        MessageDigest messageDigest;
        byte[] digest;//new byte[0] можно не использовать

        try {
            messageDigest = getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(MD.getBytes());
            digest = messageDigest.digest();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return errAlg;
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String result = bigInt.toString(16);

        while( result.length() < 32 ){
            result = "0" + result;
        }
        return result;
    }

    public static String SHA1(String SHA1) {
        MessageDigest messageDigest;
        byte[] digest;
        try {
            messageDigest = getInstance("SHA-1");
            messageDigest.reset();
            messageDigest.update(SHA1.getBytes());
            digest = messageDigest.digest();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return errAlg;
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String result = bigInt.toString(16);

        while( result.length() < 32 ){
            result = "0" + result;
        }
        return result;
    }

    public static String SHA224(String SHA224) {
        MessageDigest messageDigest;
        byte[] digest;

        try {
            messageDigest = getInstance("SHA-224");
            messageDigest.reset();
            messageDigest.update(SHA224.getBytes());
            digest = messageDigest.digest();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return errAlg;
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String result = bigInt.toString(16);

        while( result.length() < 32 ){
            result = "0" + result;
        }
        return result;
    }

    public static String SHA384(String SHA384) {
        MessageDigest messageDigest;
        byte[] digest;

        try {
            messageDigest = getInstance("SHA-384");
            messageDigest.reset();
            messageDigest.update(SHA384.getBytes());
            digest = messageDigest.digest();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return errAlg;
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String result = bigInt.toString(16);

        while( result.length() < 32 ){
            result = "0" + result;
        }
        return result;
    }

    public static String SHA512(String SHA512) {
        MessageDigest messageDigest;
        byte[] digest;

        try {
            messageDigest = getInstance("SHA-512");
            messageDigest.reset();
            messageDigest.update(SHA512.getBytes());
            digest = messageDigest.digest();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return errAlg;
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String result = bigInt.toString(16);

        while( result.length() < 32 ){
            result = "0" + result;
        }
        return result;
    }

}
