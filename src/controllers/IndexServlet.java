package controllers;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import utils.DBUtil;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

    /*
     * dogGet クライアントからデータの要求があった場合に呼び出される、
     * メソッド内にはクライアントに対して要求された内容を出力するような処理を記述する
     *
     * 「doPost」メソッドはクライアントからデータが送られてくる場合に呼び出されます。
     * そこで「doPost」メソッド内にはクライアントから送られてきた情報などを取得するような処理を記述します。
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    EntityManager em = DBUtil.createEntityManager();

	    //ページネーション
	    int page = 1;
	    try {
	        page = Integer.parseInt(request.getParameter("page"));
	    } catch(NumberFormatException e) {}

	    //何件目から取得するか　setFirstResult(15 * (1 - 1))　0件目から15件　（0~14のデータ）
	    //setMaxResults 何件取得するか
	    List<Message> messages = em.createNamedQuery("getAllMessages", Message.class)
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();

	    long messages_count = (long)em.createNamedQuery("getMessagesCount", Long.class)
                .getSingleResult();

	    request.setAttribute("messages", messages);
	    request.setAttribute("messages_count", messages_count);
	    request.setAttribute("page", page);
	    //ここまで

//	    response.getWriter().append(Integer.valueOf(messages.size()).toString());


	    em.close();

	    request.setAttribute("messages", messages);
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }


	    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/messages/index.jsp");
	    rd.forward(request, response);
	}
}
