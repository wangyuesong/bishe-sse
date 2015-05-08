package sse.dao.base;

/**  
* @Project: sse
* @Title: Dao.java
* @Package sse.dao.base
* @Description: TODO
* @author YuesongWang
* @date 2015年5月8日 上午10:50:15
* @version V1.0  
*/
public interface Dao<K, E> {
    /** 
     * Description: 增加
     * @param @param entity
     * @return void
     */
    void persist(E entity);
    /** 
     * Description: 更新
     * @param @param entity
     * @return void
     */
    void merge(E entity);
    /** 
     * Description: 删除
     * @param @param entity
     * @return void
     */
    void remove(E entity);
    /** 
     * Description: 查找
     * @param @param id
     * @param @return
     * @return E
     */
    E findById(K id);
}