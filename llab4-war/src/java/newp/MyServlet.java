/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;

import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;

import javax.jms.TextMessage;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Анюта
 */
@WebServlet(name = "MyServlet", urlPatterns = {"/toServ"})
public class MyServlet extends HttpServlet {

    @Resource(lookup = "MyFactory")
    private javax.jms.TopicConnectionFactory factory;

    @Resource(lookup = "MyTopic")
    private javax.jms.Queue topic;

    @EJB
    private JPALocal jpa;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MyServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MyServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String login = request.getParameter("login").trim();
        try (PrintWriter out = response.getWriter()) {
            if (login == null || login.isEmpty() || login.length() > 10) {
                out.println("OUUU");
                return;
            }

            HttpSession hs = request.getSession(false);
            if (hs != null) {
                hs.invalidate();
            }
            hs = request.getSession(true);

            hs.setAttribute("user", login.trim());

            RequestDispatcher requestD = request.getRequestDispatcher("/message.jsp");
            requestD.forward(request, response);
            // response.sendRedirect("message.jsp");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // Получение коллекции всех параметров запроса
        Map<String, String[]> map = request.getParameterMap();
        StringBuilder sb = new StringBuilder();
        try (PrintWriter out = response.getWriter()) {
            // Получение ссылки на сессию.
            HttpSession hs = request.getSession(false);
            if (hs == null) {
                // Если сессия пользователя уже закрыта,...
                // то пользователю возвращается форма ввода логина
                response.sendRedirect("message.jsp");
                return;
            }
            String user = hs.getAttribute("user").toString();
            String message = null;
            // Перебор полученной коллекции параметров запроса
            for (Map.Entry<String, String[]> m : map.entrySet()) {
                String key = m.getKey();

                switch (key) {
                    case "message": // Обработка собственно сообщения.
                        // Проверка корректности сообщения
                        if (m.getValue() != null && m.getValue().length > 0) {
                            message = m.getValue()[0].trim();
                        }
                        break;

                    case "save_text": // Обработка собственно сообщения.
                        // Проверка корректности сообщения
                        if (m.getValue() != null && m.getValue().length > 0) {
                            message = m.getValue()[0].trim();
                        }
                        break;

                    case "save_numb": // Команда на отправку сообщения.

                        return;
                    case "summ": // Команда перехода на форму отправки сообщения

                        return;
                    case "list": // Команда на вывод списка сообщения пользователя.

                        return;
                    case "return": // Команда на выход из приложения.
                        hs.invalidate();
                        response.sendRedirect("index.jsp");
                        return;
                    default: // Обработка неизвестной команды.
                        ;
                }
            }
            out.println(user);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void sendMessage(String login, String message, String type) {
        TopicConnection con;
        TopicSession ses;
        try {
            con = factory.createTopicConnection();
            ses = con.createTopicSession(true, 0);
            MessageProducer sender = ses.createProducer(topic);
            if (type.equals("text")) {
                TextMessage msg = ses.createTextMessage(message);
                msg.setStringProperty("login", login);
                sender.send(msg);
            } else {
                Integer numb = Integer.parseInt(message);
                ObjectMessage msg = ses.createObjectMessage(numb);
                msg.setStringProperty("login", login);
                sender.send(msg);
            }
        } catch (Exception e) {
            System.out.println("ошибка");
        }
    }
    private String getList(String login) {
        StringBuilder sb = new StringBuilder();
        // Обращение к JPA-компоненту с запросом списка сообщений.
        List<Messages> c = jpa.getList(login);
        if (c == null) {
            System.out.println("ошибка листа");
        }
        sb.append(login)
                .append("--->");
        // Перебор коллекции сообщений и формирование HTML-код с его 
        // представлением.
        for (Messages m : c) {
            sb.append("<li>")
                    .append(m.getId())
                    .append(".")
                    .append(m.getMessage())
                    .append("</li>\n");
        }
        sb.append("</ol>");
        return sb.toString();
    }
}
