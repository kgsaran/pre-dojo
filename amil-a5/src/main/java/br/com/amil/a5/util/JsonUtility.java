package br.com.amil.a5.util;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonUtility {

	@SuppressWarnings("unused")
	private static Gson createGson(){
		return new GsonBuilder().disableHtmlEscaping()
		        .registerTypeHierarchyAdapter(Date.class, new DateTimeSerializer())
		        .registerTypeHierarchyAdapter(Date.class, new DateTimeDeserializer())
		        .create();
	}	
	
    private static String convert(String str) {
        char[] caracteres = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char caracter : caracteres) {
            if (caracter >= 128) {
                sb.append("\\u00").append(Integer.toHexString(caracter & 0xFFFF));
            } else {
                sb.append(caracter);
            }
        }
        return sb.toString();
    }	
	
	@SuppressWarnings("rawtypes")
	public static String getJson(List lista) throws Exception {
		String retorno = "";
		try {		
			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm:ss").create();
			retorno = convert(gson.toJson(lista));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return retorno;
	}

	public static String getJson(Object obj) {
		String retorno = "";
		try {		
			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm:ss").create();
			retorno = convert(gson.toJson(obj));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return retorno;
	}
}

class DateTimeSerializer implements JsonSerializer<Date> {
	@Override
	public JsonElement serialize(Date arg0, Type arg1, JsonSerializationContext arg2) {
		// TODO Auto-generated method stub
        return new JsonPrimitive(arg0.getTime() + new SimpleDateFormat("Z").format(arg0));
	}
}

class DateTimeDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    	return new Date(Long.valueOf((json).getAsString().substring(0, 13)));
    }
}
