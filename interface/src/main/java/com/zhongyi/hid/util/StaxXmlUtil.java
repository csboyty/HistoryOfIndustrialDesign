package com.zhongyi.hid.util;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;

public class StaxXmlUtil {

	private static final XMLInputFactory2 xmlInputFactory2;

	private static final XMLOutputFactory2 xmlOutputFactory2;

	public static XMLInputFactory2 xmlInputFactory2() {
		return xmlInputFactory2;
	}

	public static XMLOutputFactory2 xmlOutputFactory2() {
		return xmlOutputFactory2;
	}

	static {
		xmlInputFactory2 = createXMLInputFactory2();
		xmlOutputFactory2 = createXMLOutputFactory2();
	}

	private static XMLInputFactory2 createXMLInputFactory2() {
		System.setProperty("javax.xml.stream.XMLInputFactory",
				"com.ctc.wstx.stax.WstxInputFactory");

		XMLInputFactory f = XMLInputFactory.newInstance();

		f.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
		f.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES , Boolean.FALSE);
		// f.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
		// f.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
		f.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);

		f.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.TRUE);
		f.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);

		f.setProperty(XMLInputFactory2.P_REPORT_PROLOG_WHITESPACE, Boolean.TRUE);
		return (XMLInputFactory2) f;
	}

	private static XMLOutputFactory2 createXMLOutputFactory2() {
		System.setProperty("javax.xml.stream.XMLOutputFactory",
				"com.ctc.wstx.stax.WstxOutputFactory");
		XMLOutputFactory f = XMLOutputFactory.newInstance();
		f.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.FALSE);
		return (XMLOutputFactory2) f;
	}
	

}
