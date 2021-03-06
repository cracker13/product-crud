package com.cracker.shopping.web.servlet;

import com.cracker.shopping.dao.IProductDAO;
import com.cracker.shopping.dao.IProductDirDAO;
import com.cracker.shopping.dao.impl.ProductDAOImpl;
import com.cracker.shopping.dao.impl.ProductDirDAOImpl;
import com.cracker.shopping.page.PageResult;
import com.cracker.shopping.query.ProductQueryObject;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {
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
        ProductQueryObject qo = new ProductQueryObject();
        // 把请求参数信息封装到查询对象中
        this.request2Object(req, qo);
        req.setAttribute("qo", qo); // 把查询数据回显在表单中
        PageResult pageResult = dao.query(qo);
        // 把所有的商品分类信息共享给JSP
        req.setAttribute("dirs", dirDAO.list());
        // -----------------------------------
        req.setAttribute("pageResult", pageResult);
        req.getRequestDispatcher("/WEB-INF/views/product/list.jsp").forward(req, resp);
    }

    private void request2Object(HttpServletRequest req, ProductQueryObject qo) {
         String name = req.getParameter("name");
         String minSalePrice = req.getParameter("minSalePrice");
         String maxSalePrice = req.getParameter("maxSalePrice");
         String dirId = req.getParameter("dirId");
         String keyword = req.getParameter("keyword");

         // 接受用户传入的currentPage和pageSize
        String currentPage = req.getParameter("currentPage");
        String pageSize = req.getParameter("pageSize");
        if (StringUtils.isNotBlank(currentPage)) {
            qo.setCurrentPage(Integer.valueOf(currentPage));
        }
        if (StringUtils.isNotBlank(pageSize)) {
            qo.setPageSize(Integer.valueOf(pageSize));
        }
         if (StringUtils.isNotBlank(keyword)) {
             qo.setKeyword(keyword);
         }
         if (StringUtils.isNotBlank(name)) {
             qo.setName(name);
         }
         if (StringUtils.isNotBlank(minSalePrice)) {
             qo.setMinSalePrice(new BigDecimal(minSalePrice));
         }
         if (StringUtils.isNotBlank(maxSalePrice)) {
             qo.setMaxSalePrice(new BigDecimal(maxSalePrice));
         }
         if (StringUtils.isNotBlank(dirId)) {
             qo.setDirId(Long.valueOf(dirId));
         }
    }
}
