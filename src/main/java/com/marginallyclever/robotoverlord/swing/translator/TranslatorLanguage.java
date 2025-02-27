package com.marginallyclever.robotoverlord.swing.translator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A collection of strings for a single translation of the application.
 *
 * @author Dan Royer
 * @since 1.0.0
 */
public class TranslatorLanguage {
	private static final Logger logger = LoggerFactory.getLogger(TranslatorLanguage.class);
	private String name = "";
	private String author = "";
	private final Map<String, String> strings = new HashMap<>();


	/**
	 * @param language_file
	 */
	public void loadFromString(String language_file) {
		final DocumentBuilder db = getDocumentBuilder();
		if (db == null) {
			return;
		}
		Document dom = null;
		try {
			//Using factory get an instance of document builder
			//parse using builder to get DOM representation of the XML file
			dom = db.parse(language_file);
		} catch (SAXException | IOException e) {
			logger.error(e.getMessage());
		}
		if (dom == null) {
			return;
		}
		load(dom);
	}

	/**
	 * @param inputStream
	 */
	public void loadFromInputStream(InputStream inputStream) {
		final DocumentBuilder db = getDocumentBuilder();
		if (db == null) {
			return;
		}
		try {
			Document dom = db.parse(inputStream);
			load(dom);
		} catch (SAXException | IOException e) {
			logger.error( e.getMessage() );
		}
	}

	private void load(Document dom) {
		final Element docEle = dom.getDocumentElement();

		name = docEle.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
		author = docEle.getElementsByTagName("author").item(0).getFirstChild().getNodeValue();

		NodeList nl = docEle.getElementsByTagName("string");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {

				//get the element
				Element el = (Element) nl.item(i);
				String key = getTextValue(el, "key");
				String value = getTextValue(el, "value");

				// store key/value pairs into a map
				//logger.info(language_file +"\t"+key+"\t=\t"+value);
				strings.put(key, value);
			}
		}
	}

	private DocumentBuilder getDocumentBuilder() {
		DocumentBuilder db = null;
		try {
			db = buildDocumentBuilder().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error( e.getMessage() );
		}
		if(db == null) {
			return null;
		}
		return db;
	}

	private DocumentBuilderFactory buildDocumentBuilder() {
		return DocumentBuilderFactory.newInstance();
	}

	public String get(String key) {
		String x = strings.get(key);
		if (x == null) x = "Missing:"+key;
		return x;
	}


	/**
	 * <p>
	 * When a newline character "\n" was being read in from an xml file,
	 * it was being escaped ("\\n") and thus not behaving as an actual newline.
	 * This method replaces any "\\n" with "\n".
	 * </p>
	 * <p>
	 * <p>
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is 'name' I will return John
	 * </p>
	 *
	 * @param ele     XML element
	 * @param tagName name of 'tag' or child XML element of ele
	 * @return text value of tagName
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		assert textVal != null;
		textVal = textVal.replace("\\n", "\n");
		return textVal;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}
}
