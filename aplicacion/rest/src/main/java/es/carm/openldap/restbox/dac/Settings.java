package es.carm.openldap.restbox.dac;

import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.carm.openldap.dao.ObjectFactory;

public class Settings {

    static Logger log = LogManager.getLogger(Settings.class);

    public static final String MIME_LDIF = "text/ldif";
    public static final String MIME_DSML = "text/dsml";
    public static final String MIME_JSON = MediaType.APPLICATION_JSON;
    public static final String MIME_XML = MediaType.APPLICATION_XML;
    public static final String MIME_JPEG = "image/jpeg";

    private static String factoryClass = null;
    private static ObjectFactory factory = null;

    public static boolean isEmpty(String cad) {
        if (null != cad) {
            return 0 >= cad.trim().length();
        }
        return true;
    }

    public static ObjectFactory getFactoryDAC() {
        if (null == factory) {
            createFactoryInstance();
        }
        return factory;
    }

    public static String getFactoryClass() {
        return factoryClass;
    }

    public static void setFactoryClass(String clase) {
        factoryClass = clase;
        createFactoryInstance();
    }

    private static synchronized void createFactoryInstance() {
        if ((null != getFactoryClass()) && (0 < getFactoryClass().trim().length())) {
            try {
                ObjectFactory factoria = (ObjectFactory) Class.forName(getFactoryClass()).newInstance();
                factory = factoria;
            } catch (Exception e) {
                log.error("ERROR: Al instanciar la clase '" + getFactoryClass() + "'", e);
            }
        }
    }

    public static String getMimeSelectorDefault(String accept) {
        if (!isEmpty(accept)) {
            String prefered = null;

            if (accept.contains(",")) {
                // text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
                double lastQ = 0;
                String[] mimes = accept.split(",");
                for (int i = 0; (null != mimes) && (i < mimes.length); i++) {
                    if (!mimes[i].startsWith("*")) {
                        if (mimes[i].contains(";q=")) {
                            String[] q = mimes[i].split(";q=");
                            if ((null != q) && (1 < q.length)) {
                                double newQ = 0;
                                try {
                                    newQ = Double.parseDouble(q[1]);
                                } catch (Exception x) {
                                }
                                if (newQ > lastQ) {
                                    prefered = q[0];
                                    lastQ = newQ;
                                }
                            }
                        } else {
                            if (isEmpty(prefered)) {
                                prefered = mimes[i];
                            }
                        }
                    }
                }
            } else {
                prefered = accept;
            }

            if (!isEmpty(prefered)) {
                if ((MIME_JSON.equalsIgnoreCase(prefered)) || ("text/json".equalsIgnoreCase(prefered))) {
                    return MIME_JSON;

                } else if ((MIME_LDIF.equalsIgnoreCase(prefered)) || ("text/directory".equalsIgnoreCase(prefered))) {
                    return MIME_LDIF;

                } else if ((MIME_XML.equalsIgnoreCase(prefered)) || ("application/xml".equalsIgnoreCase(prefered))) {
                    return MIME_XML;

                } else if ((MIME_DSML.equalsIgnoreCase(prefered)) || ("application/dsml".equalsIgnoreCase(prefered))) {
                    return MIME_DSML;
                }
            }
        }
        return MIME_JSON;
    }

    public static String getMimeResult(String header, String ext) {
        String mimeResult = Settings.MIME_JSON;
        if (!Settings.isEmpty(ext)) {
            if (".json".equalsIgnoreCase(ext)) {
                mimeResult = Settings.MIME_JSON;
            } else if (".ldif".equalsIgnoreCase(ext)) {
                mimeResult = Settings.MIME_LDIF;
            } else if (".xml".equalsIgnoreCase(ext)) {
                mimeResult = Settings.MIME_XML;
            } else if (".dsml".equalsIgnoreCase(ext)) {
                mimeResult = Settings.MIME_DSML;
            }
        } else {
            mimeResult = getMimeSelectorDefault(header);
        }
        return mimeResult;
    }

    public static String getErrorResponse(String msg, String media) {
        String retVal = "";

        if (Settings.MIME_DSML.equalsIgnoreCase(media)) {
            retVal += "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
            retVal += "<!-- ERROR: " + msg + " -->\n";
            retVal += "<dsml:dsml>\n <dsml:directory-entries/>\n</dsml:dsml>\n";

        } else if (Settings.MIME_LDIF.equalsIgnoreCase(media)) {
            retVal += "\n## ERROR: " + msg + "\n\n";

        } else if (Settings.MIME_JSON.equalsIgnoreCase(media)) {
            retVal += "[{\"ERROR\":\"" + msg + "\"}]\n";

        } else {
            retVal += "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
            retVal += "<ERROR>" + msg + "</ERROR>\n";
        }
        return retVal;
    }
}
