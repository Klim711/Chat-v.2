package controller;

import model.Message;
import model.MessageStorage;
import util.ServletUtil;
import util.XMLHistoryUtil;
import util.MessageUtil;

import static util.MessageUtil.MESSAGES;
import static util.MessageUtil.TOKEN;
import static util.MessageUtil.getIndex;
import static util.MessageUtil.getToken;
import static util.MessageUtil.jsonToMessage;
import static util.MessageUtil.stringToJson;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(ChatServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            loadHistory();
        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
            logger.error(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet");
        String token = request.getParameter(TOKEN);
        logger.info("Token " + token);

        if (token != null && !"".equals(token)) {
            int index = getIndex(token);
            logger.info("Index " + index);
            String messages = formResponse();
            response.setContentType(ServletUtil.APPLICATION_JSON);
            PrintWriter out = response.getWriter();
            out.print(messages);
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "'token' parameter needed");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost");
        String data = ServletUtil.getMessageBody(request);
        logger.info(data);
        try {
            JSONObject json = stringToJson(data);
            Message message = jsonToMessage(json);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date date = new Date();
            message.setDate(sdf.format(date));
            message.sout();

            //MessageStorage.addMessage(message);
            XMLHistoryUtil.addData(message);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ParseException | ParserConfigurationException | SAXException | TransformerException e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPut");
        String data = ServletUtil.getMessageBody(request);
        logger.info(data);
        try {
            JSONObject json = stringToJson(data);
            Message message = jsonToMessage(json);
            String id = message.getId();
            Message messageToUpdate = XMLHistoryUtil.getMessageById(id);
            if (messageToUpdate != null) {
                messageToUpdate.setText(message.getText());
                XMLHistoryUtil.updateData(messageToUpdate);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Task does not exist");
            }
        } catch (ParseException | ParserConfigurationException | SAXException | TransformerException | XPathExpressionException e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doDelete");
        String data = ServletUtil.getMessageBody(request);
        logger.info(data);
        try {
            JSONObject json = stringToJson(data);
            Message message = jsonToMessage(json);
            String id = message.getId();
            Message messageToUpdate = XMLHistoryUtil.getMessageById(id);
            if (messageToUpdate != null) {
                messageToUpdate.setDeleted(message.isDeleted());
                XMLHistoryUtil.updateData(messageToUpdate);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Task does not exist");
            }
        } catch (ParseException | ParserConfigurationException | SAXException | TransformerException | XPathExpressionException e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private String formResponse()  {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(MESSAGES, XMLHistoryUtil.getMessages());
            jsonObject.put(TOKEN, getToken(0));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return jsonObject.toJSONString();
    }

    private void loadHistory() throws SAXException, IOException, ParserConfigurationException, TransformerException  {
        if (XMLHistoryUtil.doesStorageExist()) {
            ArrayList<Message> messages = new ArrayList<>();
            messages.addAll(XMLHistoryUtil.getMessages());
            //MessageStorage.addAll(messages);
            for(int i = 0; i < messages.size(); ++i) {
                messages.get(i).sout();
            }
        } else {
            XMLHistoryUtil.createStorage();
            addStubData();
        }
    }

    private void addStubData() throws ParserConfigurationException, TransformerException {
        Message[] stubMessages = {
                new Message("1", "Admin","Hello, World", false, "00-00-0000"),
                new Message("2", "Admin", "How are you?", false, "00-00-0000"),
                new Message("3", "Admin", "I'm fine too!)", false, "00-00-0000")};
        //MessageStorage.addAll(stubMessages);
        for (Message message : stubMessages) {
            try {
                XMLHistoryUtil.addData(message);
            } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
                logger.error(e);
            }
        }
    }
}