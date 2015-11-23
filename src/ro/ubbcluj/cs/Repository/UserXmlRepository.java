package ro.ubbcluj.cs.Repository;

import data.repository.AbstractRepository;
import org.w3c.dom.*;
import org.xml.sax.SAXParseException;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.domain.UserValidator;
import ro.ubbcluj.cs.exceptions.RepositoryException;
import ro.ubbcluj.cs.utils.repository.UserRepositoryUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by ZUZU on 17.11.2015.
 */
public class UserXmlRepository extends AbstractRepository<User> {
    String filename;
    public UserXmlRepository(String filename) throws Exception {
        this.filename = filename;
        validator = new UserValidator();
        entities = new ArrayList<>();
        entities = (ArrayList<User>) findAllFromFile();
        repositoryUtils = new UserRepositoryUtils(this);
        lastID = entities.get(entities.size()-1).getUserID();
    }

    @Override
    public Iterable<User> findAll() throws Exception {
        return entities;
    }

    public Iterable<User> findAllFromFile() throws Exception {
        log.log(Level.FINEST, "Getting all from file");
        File inputFile = new File(filename);
        inputFile.createNewFile();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        try {
            Document doc = builder.parse(inputFile);
            doc.normalize();
            NodeList nodeList = doc.getElementsByTagName("user");
            for (int i = 0; i<nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int  id = Integer.parseInt(element.getAttribute("id"));
                    String username = element.getElementsByTagName("username").item(0).getTextContent();
                    String password = element.getElementsByTagName("password").item(0).getTextContent();
                    User user = new User(username,password);
                    user.setUserID(id);
                    entities.add(user);
                }
            }
        }
        catch (SAXParseException e) {
            log.log(Level.FINE, "Parse error occured in user repository");
        }
        return entities;
    }
    @Override
    public void close() throws Exception{
        saveAll();
        super.close();
    }

    private void saveAll() throws Exception{
        log.log(Level.FINEST, "Saving in file");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("users");
        doc.appendChild(rootElement);
        for (int i = 0; i<entities.size(); i++) {
            Element user = doc.createElement("user");
            user.setAttribute("id", String.valueOf(entities.get(i).getUserID()));
            rootElement.appendChild(user);
            Element username = doc.createElement("username");
            username.setTextContent(entities.get(i).getUsername());
            user.appendChild(username);
            Element password = doc.createElement("password");
            password.setTextContent(entities.get(i).getPassword());
            user.appendChild(password);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", new Integer(4));
        Transformer transformer= transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource domSource = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filename));
        transformer.transform(domSource, result);
    }
}
