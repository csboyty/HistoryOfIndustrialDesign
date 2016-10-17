package zhongyi.hid;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.stream.XMLStreamConstants;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;
import org.junit.Before;
import org.junit.Test;

import com.zhongyi.hid.util.StaxXmlUtil;


public class StaxTest {

	File htmlFile;
	File correctHtmlFile;

	@Before
	public void setUp() throws IOException {
		htmlFile = new File("postContent.xml");
		System.out.println(htmlFile.getCanonicalPath());
		correctHtmlFile = new File("postContent.rev.xml");
		System.out.println(correctHtmlFile.getCanonicalPath());
	}

	@Test
	public void testStax() throws Exception {
		XMLInputFactory2 ifact = StaxXmlUtil.xmlInputFactory2();
		XMLOutputFactory2 of = StaxXmlUtil.xmlOutputFactory2();
		XMLStreamReader2 sr = (XMLStreamReader2) ifact
				.createXMLStreamReader(htmlFile);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(
				correctHtmlFile));
		XMLStreamWriter2 sw = (XMLStreamWriter2) of.createXMLStreamWriter(out,
				"UTF-8");
		try {
			for (int type = sr.getEventType(); type != XMLStreamConstants.END_DOCUMENT; type = sr
					.next()) {
				if (type == XMLStreamConstants.CHARACTERS) {
					sw.copyEventFromReader(sr, false);
				} else if (type == XMLStreamConstants.CDATA) {
					sw.writeCharacters(sr.getText());
				} else if (type == XMLStreamConstants.END_ELEMENT) {
					sw.writeEndElement();
				} else if (type == XMLStreamConstants.START_ELEMENT) {
					if ("img".equals(sr.getName().getLocalPart())) {
						sw.writeStartElement("","image");
						int attributeCount = sr.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							String attrName = sr.getAttributeName(i)
									.getLocalPart();
							String attrValue = sr.getAttributeValue(i);
							if ("src".equalsIgnoreCase(attrName)) {

								attrValue = "images/"
										+ FilenameUtils.getName(attrValue);
							}
							sw.writeAttribute(attrName, attrValue);
						}

					} else {
						sw.copyEventFromReader(sr, false);
					}
				}

				else {
					sw.copyEventFromReader(sr, false);
				}

			}
			sw.flush();
		} finally {
			sr.close();
			sw.close();
			IOUtils.closeQuietly(out);
		}
	}

}
