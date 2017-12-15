package es.carm.openldap.dac.dao.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import org.ldaptive.LdapEntry;
import org.ldaptive.SearchResult;
import org.ldaptive.io.JsonReader;
import org.ldaptive.io.JsonWriter;

import es.carm.openldap.Factory;
import es.carm.openldap.dac.UsuarioDAC;

public class UserFactory implements Factory<UsuarioDAC> {
	
	
	public UsuarioDAC fromString(String data) {
		StringReader reader = new StringReader(data);
		JsonReader jsonReader = new JsonReader(reader);
		
		try {
			SearchResult result = jsonReader.read();
			if (null!=result) {
				return es.carm.openldap.dac.dao.entryldap.UserFactory.fromEntry( result.getEntry() );
			}
		}catch(Exception x){
			x.printStackTrace( System.err );
		}
		return null;
    }

	public UsuarioDAC fromFile(File file) {				
		try {
			FileReader reader = new FileReader(file);
			JsonReader jsonReader = new JsonReader(reader);
			
			SearchResult result = jsonReader.read();
			if (null!=result) {
				return es.carm.openldap.dac.dao.entryldap.UserFactory.fromEntry( result.getEntry() );
			}
		}catch(Exception x){
			x.printStackTrace( System.err );
		}
		return null;
	}

	public UsuarioDAC fromFile(String pathFile) {
		return fromFile(new File(pathFile));
	}
	
	public UsuarioDAC fromURL(String urlFile) {
		BufferedReader in=null;
    	String content="";
    	try {
    		URL oracle = new URL(urlFile);
    		in = new BufferedReader( new InputStreamReader(oracle.openStream()));

    		String inputLine;
    		while ((inputLine = in.readLine()) != null) {
    			content += inputLine;    			
    		}
    		return fromString( content );
    		
    	}catch(Exception x ) {
    		
    	} finally {
    		if ( null != in ) { 
        		try { in.close(); } catch (Exception v ) { }
        	} 
    	}
    	return null;        
	}

	public String toString(UsuarioDAC object) {
		StringWriter writer = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(writer);
		try {			
			LdapEntry entry=es.carm.openldap.dac.dao.entryldap.UserFactory.toEntry( object );
			SearchResult result=new SearchResult( entry );
			jsonWriter.write(result);
			
			return writer.toString();
		}catch(Exception x) {
			x.printStackTrace(System.err);
		}
		return null;
	}
	
	public static String serialize(UsuarioDAC object) {
		UserFactory factory = new UserFactory();
		return factory.toString(object);
	}
		

}
