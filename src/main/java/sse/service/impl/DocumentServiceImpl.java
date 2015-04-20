package sse.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.dao.impl.DocumentDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.Document;
import sse.jsonmodel.DocumentModel;
import sse.pageModel.DataGrid;
import sse.pageModel.WillModel;

@Service
public class DocumentServiceImpl {

    @Autowired
    private DocumentDaoImpl documentDaoImpl;

    @Autowired
    private WillDaoImpl willDaoImpl;

    public DataGrid<DocumentModel> findDocumentsForPagingByCreatorId(int page, int pageSize, String sort, String order,
            Integer creatorId)
    {
        DataGrid<DocumentModel> dg = new DataGrid<>();
        List<Document> documents = documentDaoImpl.findDocumentsForPagingByCreatorId(page, pageSize, sort, order,
                creatorId);
        List<DocumentModel> documentModels = new LinkedList<DocumentModel>();
        for (Document d : documents)
        {
            DocumentModel dm = new DocumentModel();
            dm.setCreator(d.getCreator().getName());
            dm.setDocumentCommentsCount(d.getDocumentComments().size());
            dm.setDocumentType(d.getDocumenttype().getTypeName());
            dm.setId(d.getId());
            dm.setLastModifiedBy(d.getLastModifiedBy().getName());
            dm.setName(d.getName());
            documentModels.add(dm);
        }
        dg.setRows(documentModels);
        dg.setTotal(documentDaoImpl.findDocumentsForCount());
        return dg;
    }

    public HashMap<String, String> findPreviousWills(int studentId)
    {
        return willDaoImpl.findPreviousSelectionByStudentId(studentId);
    }

    public void updateSelection(WillModel model, int studentId)
    {
        willDaoImpl.updateSelection(model, studentId);
    }

}
