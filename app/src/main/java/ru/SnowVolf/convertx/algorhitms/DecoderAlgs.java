package ru.SnowVolf.convertx.algorhitms;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Snow Volf on 21.02.2017, 8:42
 */

public class DecoderAlgs {
    /**
     * Расшифровка Unicode
     */
    public static String unescapeJava(String tracer) {
        char aChar;
        int len = tracer.length();
        StringBuilder outBuffer = new StringBuilder(len);
        for (int x = 0; x < len;) {
            aChar = tracer.charAt(x++);
            if (aChar == '\\') {
                aChar = tracer.charAt(x++);
                if (aChar == 'u') {
                    // Читаем массив
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = tracer.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    /**
     * Расшифровка XML
     */
    public static String unescapeXml(final String xml) {
        //Отыскиваем escape-символы с помощью регулярки
        Pattern xmlEntityRegex = Pattern.compile("&(#?)([^;]+);");
        //Matcher требует StringBuffer вместо StringBuilder
        StringBuffer unescapedOutput = new StringBuffer(xml.length());

        Matcher m = xmlEntityRegex.matcher(xml);
        Map<String,String> builtinEntities = null;
        //Переменные
        String entity;
        String hashmark;
        String ent;
        int code;
        //Пока найдено
        while (m.find()) {
            ent = m.group(2);
            hashmark = m.group(1);
            if ((hashmark != null) && (hashmark.length() > 0)) {
                code = Integer.parseInt(ent);
                entity = Character.toString((char) code);
            } else {
                //must be a non-numerical entity
                if (builtinEntities == null) {
                    builtinEntities = buildBuiltinXMLEntityMap();
                }
                entity = builtinEntities.get(ent);
                if (entity == null) {
                    //Не знаем что за символ -- бросаем его нахрен
                    entity = "&" + ent + ';';
                }
            }
            m.appendReplacement(unescapedOutput, entity);
        }
        m.appendTail(unescapedOutput);

        return unescapedOutput.toString();
    }

    private static Map<String,String> buildBuiltinXMLEntityMap() {
        //По HashMap сопоставяем escape и заменяем на нормальные символы
        Map<String, String> entities = new HashMap<>(10);
        entities.put("lt", "<");
        entities.put("gt", ">");
        entities.put("amp", "&");
        entities.put("apos", "'");
        entities.put("quot", "\"");
        return entities;
    }

    public static String getNormalDate(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
        return format.format(date);
    }


}
