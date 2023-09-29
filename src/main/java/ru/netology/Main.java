package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileNameXML = "data.xml";
        String fileNameJSON = "data2.json";
        List<Employee> list = parseXML(fileNameXML);
        String json = listToJson(list);
        writeString(json);
    }

    public static List<Employee> parseXML(String fileNameXML) {
        List<Employee> employeeList = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fileNameXML);
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element employee = (Element) node;
                    NodeList nlId = employee.getElementsByTagName("id");
                    for (int j = 0; j < nlId.getLength(); j++) {
                        employeeList.add(new Employee(Long.parseLong(nlId.item(j).getTextContent()),
                                employee.getElementsByTagName("firstName").item(j).getTextContent(),
                                employee.getElementsByTagName("lastName").item(j).getTextContent(),
                                employee.getElementsByTagName("country").item(j).getTextContent(),
                                Integer.parseInt(employee.getElementsByTagName("age").item(j).getTextContent())));
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
        return employeeList;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    private static void writeString(String json, String fileNameJSON) {
        try (FileOutputStream fos = new FileOutputStream(fileNameJSON)) {
            byte[] bytes = json.getBytes();
            fos.write(bytes, 0, bytes.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}