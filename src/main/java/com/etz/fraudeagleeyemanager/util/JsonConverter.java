package com.etz.fraudeagleeyemanager.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonConverter {
    
	@SuppressWarnings("unchecked")
	public static <T> T jsonToObject(String jsonStr, Class<?> responseClassObject) {
		ObjectMapper objMapper = new ObjectMapper();
		T JavaObject = null;
		try {
			JavaObject = (T) objMapper.readValue(jsonStr, responseClassObject);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return JavaObject;
	}
	
	public static String objectToJson(Object object) {
		ObjectMapper objMapper = new ObjectMapper()
				.enable(SerializationFeature.INDENT_OUTPUT);
				//.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
		
		String json = "";
		try {
			json = objMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
		}
		return json;
	}

    /*private static Gson getGson(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat(dateFormat.toPattern());
        gsonBuilder.registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(),  new MapDeserializerDoubleAsIntFix());
        return  gsonBuilder.create();
    }*/

}
