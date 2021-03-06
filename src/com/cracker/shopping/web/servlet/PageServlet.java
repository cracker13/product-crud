package com.cracker.shopping.web.servlet;

import com.cracker.shopping.dao.IProductDAO;
import com.cracker.shopping.dao.IProductDirDAO;
import com.cracker.shopping.dao.impl.ProductDAOImpl;
import com.cracker.shopping.dao.impl.ProductDirDAOImpl;
import com.cracker.shopping.page.PageResult;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/page")
public class PageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IProductDAO dao;
    private IProductDirDAO dirDAO;

    @Override
    public void init() throws ServletException {
        dao = new ProductDAOImpl();
        dirDAO = new ProductDirDAOImpl();
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // -----------------------------------
        Integer currentPage = 1;
        // 接受用户传入的当前页
        String sCurrentPage = req.getParameter("currentPage");
        if (StringUtils.isNotBlank(sCurrentPage)) {
            currentPage = Integer.valueOf(sCurrentPage);
        }
        Integer pageSize = 5;
        // 接受用户传入的每页多少条
        String sPageSize = req.getParameter("pageSize");
        if (StringUtils.isNotBlank(sPageSize)) {
            pageSize = Integer.valueOf(sPageSize);
        }
        PageResult pageResult = dao.query(currentPage, pageSize);
        req.setAttribute("pageResult", pageResult);
        // -----------------------------------
        req.setAttribute("dirs", dirDAO.list());
        req.getRequestDispatcher("/WEB-INF/views/product/list.jsp").forward(req, resp);
    }
}
