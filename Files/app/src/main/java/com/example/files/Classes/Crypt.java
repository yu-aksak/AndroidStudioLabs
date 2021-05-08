package com.example.files.Classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class Crypt {
    // write/read key

    private static final String KEY = "keyB.key";


    public static void GenerateKeyPair(Context context, String algorithm) throws Exception {
        WriteKey(context, KeyGenerator.getInstance(algorithm).generateKey());
    }

    private static void WriteKey(Context context, Key key) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput(KEY, Context.MODE_PRIVATE));

            out.writeObject(key);

            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Key ReadKey(Context context) {
        try {
            ObjectInputStream in = new ObjectInputStream(context.openFileInput(KEY));
            return (Key) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // lib AES

    public static String DecryptAES(Context context, String str) {
        try {
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, ReadKey(context));
            return new String(cipher.doFinal(Base64.decode(str.getBytes("windows-1251"), Base64.DEFAULT)), "windows-1251");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String EncryptAES(Context context, String str) {
        try {
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, ReadKey(context));
            return new String(Base64.encode(cipher.doFinal(str.getBytes("windows-1251")), Base64.DEFAULT), "windows-1251");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    // lib Blowfish

    public static String DecryptBlowfish(Context context, String str) {
        try {
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, ReadKey(context));
            return new String(cipher.doFinal(Base64.decode(str.getBytes("windows-1251"), Base64.DEFAULT)), "windows-1251");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String EncryptBlowfish(Context context, String str) {
        try {
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, ReadKey(context));
            return new String(Base64.encode(cipher.doFinal(str.getBytes("windows-1251")), Base64.DEFAULT), "windows-1251");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    // vigener

    private static final String keyword = "ключ".toUpperCase();
    private static final String CHARACTERS_RU = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЬЫЪЭЮЯабвгдеёжзийклмнопрстуфхцчшщьыъэюя";
    private static final String CHARACTERS_EN = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz 1234567890[]{}<>;'.,!@#$%^&*()_+-=/!№:?";
    private static int N = CHARACTERS_RU.length();
    private static int K = CHARACTERS_EN.length();

    private static int search(char[] arr, char c) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == c) return i;
        }
        return -1;
    }

    private static String EncryptVigener(String str) {
        String result = "";

        int keyword_index = 0;

        for (char symbol : str.toCharArray()) {
            if (CHARACTERS_RU.contains(String.valueOf(symbol))) {
                int c = (search(CHARACTERS_RU.toCharArray(), symbol) +
                        search(CHARACTERS_RU.toCharArray(), keyword.charAt(keyword_index))) % N;

                result += CHARACTERS_RU.charAt(c);
            } else if (CHARACTERS_EN.contains(String.valueOf(symbol))) {
                int c = (search(CHARACTERS_EN.toCharArray(), symbol) +
                        search(CHARACTERS_RU.toCharArray(), keyword.charAt(keyword_index))) % K;

                result += CHARACTERS_EN.charAt(c);
            } else
                result += symbol;

            keyword_index++;

            if ((keyword_index + 1) == keyword.length())
                keyword_index = 0;
        }

        return result;
    }

    private static String DecryptVigener(String str) {
        String result = "";

        int keyword_index = 0;

        for (char symbol : str.toCharArray()) {
            if (CHARACTERS_RU.contains(String.valueOf(symbol))) {
                int p = (search(CHARACTERS_RU.toCharArray(), symbol) + N -
                        search(CHARACTERS_RU.toCharArray(), keyword.charAt(keyword_index))) % N;

                result += CHARACTERS_RU.charAt(p);
            } else if (CHARACTERS_EN.contains(String.valueOf(symbol))) {
                int p = (search(CHARACTERS_EN.toCharArray(), symbol) + K -
                        search(CHARACTERS_RU.toCharArray(), keyword.charAt(keyword_index))) % K;

                result += CHARACTERS_EN.charAt(p);
            } else
                result += symbol;

            keyword_index++;

            if ((keyword_index + 1) == keyword.length())
                keyword_index = 0;
        }

        return result;
    }

    // huffman

    public static String EncryptHuffman(Context context, String str){
        TreeMap<Character, Integer> frequencies = countFrequency(str);

        ArrayList<CodeTreeNode> codeTreeNodes = new ArrayList<>();
        for(Character c: frequencies.keySet()) {
            codeTreeNodes.add(new CodeTreeNode(c, frequencies.get(c)));
        }

        CodeTreeNode tree = huffman(codeTreeNodes);

        TreeMap<Character, String> codes = new TreeMap<>();
        for(Character c: frequencies.keySet()) {
            codes.put(c, tree.getCodeForCharacter(c, ""));
        }

        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            encoded.append(codes.get(str.charAt(i)));
        }

        saveToFile(context, frequencies);
        return encoded.toString();
    }

    public static String DecryptHuffman(Context context, String str){
        TreeMap<Character, Integer> frequencies = new TreeMap<>();
        StringBuilder encoded2 = new StringBuilder(str);
        ArrayList<CodeTreeNode> codeTreeNodes = new ArrayList<>();

        loadFromFile(context, frequencies);

        for(Character c: frequencies.keySet()) {
            codeTreeNodes.add(new CodeTreeNode(c, frequencies.get(c)));
        }
        CodeTreeNode tree2 = huffman(codeTreeNodes);

        String decoded = huffmanDecode(encoded2.toString(), tree2);
        return decoded.toString();
    }

    private static final String COMPRESSED = "compressed.huf";

    private static void saveToFile(Context context, Map<Character, Integer> frequencies) {
        try {
            FileOutputStream os = context.openFileOutput(COMPRESSED, Context.MODE_PRIVATE);
            os.write(frequencies.size());
            for (Character character: frequencies.keySet()) {
                os.write(character);
                os.write(frequencies.get(character));
            }
            os.flush();
            os.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadFromFile(Context context, Map<Character, Integer> frequencies) {
        try {
            FileInputStream os = context.openFileInput(COMPRESSED);
            int frequencyTableSize = os.read();
            for (int i = 0; i < frequencyTableSize; i++) {
                frequencies.put((char) os.read(), os.read());
            }
            os.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static TreeMap<Character, Integer> countFrequency(String text) {
        TreeMap<Character, Integer> freqMap = new TreeMap<>();
        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i);
            Integer count = freqMap.get(c);
            freqMap.put(c, count != null ? count + 1 : 1);
        }
        return freqMap;
    }

    private static CodeTreeNode huffman(ArrayList<CodeTreeNode> codeTreeNodes) {
        while (codeTreeNodes.size() > 1) {
            Collections.sort(codeTreeNodes);
            CodeTreeNode left = codeTreeNodes.remove(codeTreeNodes.size() - 1);
            CodeTreeNode right = codeTreeNodes.remove(codeTreeNodes.size() - 1);

            CodeTreeNode parent = new CodeTreeNode(null, right.weight + left.weight, left, right);
            codeTreeNodes.add(parent);
        }
        return codeTreeNodes.get(0);
    }

    private static String huffmanDecode(String encoded, CodeTreeNode tree) {
        StringBuilder decoded = new StringBuilder();

        CodeTreeNode node = tree;
        for (int i = 0; i < encoded.length(); i++) {
            node = encoded.charAt(i) == '0' ? node.left : node.right;
            if (node.content != null) {
                decoded.append(node.content);
                node = tree;
            }
        }
        return decoded.toString();
    }

    private static class CodeTreeNode implements Comparable<CodeTreeNode> {

        Character content;
        int weight;
        CodeTreeNode left;
        CodeTreeNode right;

        public CodeTreeNode(Character content, int weight) {
            this.content = content;
            this.weight = weight;
        }

        public CodeTreeNode(Character content, int weight, CodeTreeNode left, CodeTreeNode right) {
            this.content = content;
            this.weight = weight;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(CodeTreeNode o) {
            return o.weight - weight;
        }

        public String getCodeForCharacter(Character ch, String parentPath) {
            if (content == ch) {
                return parentPath;
            } else {
                if (left != null) {
                    String path = left.getCodeForCharacter(ch, parentPath + 0);
                    if (path != null) {
                        return path;
                    }
                }
                if (right != null) {
                    String path = right.getCodeForCharacter(ch, parentPath + 1);
                    if (path != null) {
                        return path;
                    }
                }
            }
            return null;
        }
    }



    // choose encrypt/decrypt

    public static String Encrypt(Context context, int c, String str) {
        switch (c) {
            case 0:
                str = EncryptAES(context, str);
                break;
            case 1:
                str = EncryptBlowfish(context, str);
                break;
            case 2:
                str = EncryptVigener(str);
                break;
            case 3:
                str = EncryptHuffman(context,str);
                break;
            default:
                break;
        }
        return str;
    }

    public static String Decrypt(Context context, int c, String str) {
        switch (c) {
            case 0:
                str = DecryptAES(context, str);
                break;
            case 1:
                str = DecryptBlowfish(context, str);
                break;
            case 2:
                str = DecryptVigener(str);
                break;
            case 3:
                str = DecryptHuffman(context, str);
                break;
            default:
                break;
        }
        return str;
    }
}
