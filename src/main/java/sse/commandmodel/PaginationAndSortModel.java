/**  
 * @Project: sse
 * @Title: PaginationAndSortModel.java
 * @Package sse.utils
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月5日 上午11:49:43
 * @version V1.0  
 */
/**
 * 
 */
package sse.commandmodel;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuesongwang
 *
 * 用来将Httprequest中的分页和排序信息放入这个结构体
 */
public class PaginationAndSortModel {

    int page;
    int rows;
    String sort;
    String order;

    public PaginationAndSortModel(HttpServletRequest request)
    {
        setPage(Integer.parseInt(request.getParameter("page")));
        setRows(Integer.parseInt(request.getParameter("rows")));
        setSort(request.getParameter("sort"));
        setOrder(request.getParameter("order"));
    }

    public PaginationAndSortModel(int page, int rows, String sort, String order) {
        super();
        this.page = page;
        this.rows = rows;
        this.sort = sort;
        this.order = order;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
