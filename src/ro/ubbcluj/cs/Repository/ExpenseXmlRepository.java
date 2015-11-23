package ro.ubbcluj.cs.Repository;

import data.repository.AbstractRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;
import ro.ubbcluj.cs.domain.Date;
import ro.ubbcluj.cs.domain.Expense;
import ro.ubbcluj.cs.domain.ExpenseValidator;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.exceptions.RepositoryException;
import ro.ubbcluj.cs.utils.repository.ExpenseRepositoryUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by ZUZU on 17.11.2015.
 */
public class ExpenseXmlRepository extends AbstractRepository<Expense>{
    String filename;
    public ExpenseXmlRepository(String filename) throws Exception {
        this.filename = filename;
        validator = new ExpenseValidator();
        entities = new ArrayList<>();
        entities = (ArrayList<Expense>) findAllFromFile();
        repositoryUtils = new ExpenseRepositoryUtils(this);
        lastID = entities.get(entities.size()-1).getExpenseID();
    }

    @Override
    public Iterable<Expense> findAll() {
        return entities;
    }

    public Iterable<Expense> findAllFromFile() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File inputFile = new File(filename);
        inputFile.createNewFile();
        try {
            Document doc = builder.parse(inputFile);
            doc.normalize();
            NodeList nodeList = doc.getElementsByTagName("expense");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String sid = element.getAttribute("id");
                    int id = Integer.parseInt(sid);
                    Expense.Type type = Expense.Type.valueOf(element.getElementsByTagName("type").item(0).getTextContent());
                    float price = Float.parseFloat(element.getElementsByTagName("price").item(0).getTextContent());
                    String title = element.getElementsByTagName("title").item(0).getTextContent();
//                    Node userNode = element.getElementsByTagName("user");
                    Element userElement = (Element) element.getElementsByTagName("user").item(0);
                    int userId = Integer.parseInt(userElement.getAttribute("id"));
                    String password = userElement.getElementsByTagName("password").item(0).getTextContent();
                    String username = userElement.getElementsByTagName("username").item(0).getTextContent();
                    User user = new User(username, password);
                    user.setUserID(userId);
                    Element dateElement = (Element) element.getElementsByTagName("date").item(0);
                    int day = Integer.parseInt(dateElement.getElementsByTagName("day").item(0).getTextContent());
                    int month = Integer.parseInt(dateElement.getElementsByTagName("month").item(0).getTextContent());
                    int year = Integer.parseInt(dateElement.getElementsByTagName("year").item(0).getTextContent());
                    Date date = new Date(day, month, year);
                    Expense expense = new Expense(date, price, type, user, title);
                    expense.setExpenseID(id);
                    entities.add(expense);
                }
            }
        }
        catch (SAXParseException e) {
            log.log(Level.WARNING, "Parse error occurred in expense repository");
        }
        return entities;
    }

    public void saveAll() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("expenses");
        doc.appendChild(rootElement);
        for (int i=0; i<entities.size(); i++) {
            Expense ex = entities.get(i);
            Element expense = doc.createElement("expense");
            rootElement.appendChild(expense);
            expense.setAttribute("id", String.valueOf(ex.getExpenseID()));
            Element title = doc.createElement("title");
            title.setTextContent(ex.getTitle());
            Element price = doc.createElement("price");
            price.setTextContent(String.valueOf(ex.getPrice()));
            Element type = doc.createElement("type");
            type.setTextContent(String.valueOf(ex.getType()));
            Element user = userElement(doc, ex.getUser());
            Element date = dateElement(doc, ex.getDate());
            expense.appendChild(title);
            expense.appendChild(price);
            expense.appendChild(type);
            expense.appendChild(user);
            expense.appendChild(date);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", "4");
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource domSource = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filename));
        transformer.transform(domSource,result);
    }


    private Element dateElement(Document doc, Date date) {
        Element element = doc.createElement("date");
        Element day = doc.createElement("day");
        day.setTextContent(String.valueOf(date.getDay()));
        Element month = doc.createElement("month");
        month.setTextContent(String.valueOf(date.getMonth()));
        Element year = doc.createElement("year");
        year.setTextContent(String.valueOf(date.getYear()));
        element.appendChild(day);
        element.appendChild(month);
        element.appendChild(year);
        return element;
    }

    private Element userElement(Document doc, User user) {
        Element element = doc.createElement("user");
        element.setAttribute("id", String.valueOf(user.getUserID()));
        Element username = doc.createElement("username");
        username.setTextContent(user.getUsername());
        Element password = doc.createElement("password");
        password.setTextContent(user.getPassword());
        element.appendChild(username);
        element.appendChild(password);
        return element;
    }

    @Override
    public void close() throws Exception{
        saveAll();
        super.close();
    }
}
