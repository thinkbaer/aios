package de.thinkbaer.aios.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.undercouch.bson4jackson.BsonFactory;
import de.undercouch.bson4jackson.BsonModule;

public class Utils {

	private static ObjectMapper DEFALUT_JSON_MAPPER;

	private static ObjectMapper DEFALUT_BSON_MAPPER;

	static {
		DEFALUT_JSON_MAPPER = newJsonMapper();
		DEFALUT_BSON_MAPPER = newBsonMapper();
	}

	public static ObjectMapper defaultJsonMapper() {
		return DEFALUT_JSON_MAPPER;
	}

	public static ObjectMapper defaultBsonMapper() {
		return DEFALUT_BSON_MAPPER;
	}

	public static ObjectMapper newJsonMapper() {
		ObjectMapper mapper = new ObjectMapper();
		//mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX"));
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// mapper.setPropertyNamingStrategy(new CamelCaseNamingStrategy());
		return mapper;
	}

	public static ObjectMapper newBsonMapper() {
		ObjectMapper mapper = new ObjectMapper(new BsonFactory());
		mapper.registerModule(new BsonModule());
		
		//mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX"));
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	public static void createDir(String path) throws IOException {
		createDir(path, false);
	}

	public static void clearAndCreateDir(String path) throws IOException {
		createDir(path, true);
	}

	public static void createDir(String path, boolean clearBefore) throws IOException {
		File f = new File(path);

		if (clearBefore && f.exists()) {
			clearDirectory(path);
		}

		if (!f.exists()) {
			f.mkdirs();
		}
	}
	
	
	public static boolean isFilenameValid(String file) {
		Pattern p = Pattern.compile("^(\\p{L}+):\\/\\/");
		Matcher m = p.matcher(file);
		if(m.find()){
			// has some protocol
			if(m.group(1).toLowerCase().contentEquals("file")){
				file = file.substring(m.group(0).length());				
			}else{
				return false;
			}
		}		
	    File f = new File(file);
	    return f.exists();
	  }

	

	public static void clearDirectory(String dir) throws IOException {
		File f = new File(dir);
		if (!f.exists()) {
			throw new IOException("Directory not present or not writable");
		}
		if (f.isDirectory()) {
			deleteFolder(f);
		} else {
			throw new IOException("Is not a directory");
		}
	}

	public static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}
	
	public static String getJarFileName(String location){
		Pattern p = Pattern.compile("\\/([^\\/]+\\.jar)$");
		Matcher m = p.matcher(location);
		if (m.find()) {
			return m.group(1);
		}
		return null;

	}

	public static void copy(File from, File to) throws MalformedURLException, IOException {
		copy(from.toURI().toURL(), to);
	}

	public static void copy(URL from, File to) throws MalformedURLException, IOException {
		if (!to.exists()) {
			ReadableByteChannel rbc;
			rbc = Channels.newChannel(from.openStream());
			FileOutputStream fos = new FileOutputStream(to);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		}
	}

	/*
	static class CamelCaseNamingStrategy extends PropertyNamingStrategy {
		@Override
		public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
			return translate(defaultName);
		}

		@Override
		public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
			return translate(defaultName);
		}

		@Override
		public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
			return translate(defaultName);
		}

		private String translate(String defaultName) {
			char[] nameChars = defaultName.toCharArray();
			StringBuilder nameTranslated = new StringBuilder(nameChars.length * 2);
			for (char c : nameChars) {
				if (Character.isUpperCase(c)) {
					nameTranslated.append("_");
					c = Character.toLowerCase(c);
				}
				nameTranslated.append(c);
			}
			return nameTranslated.toString();
		}
	}
	*/
}
