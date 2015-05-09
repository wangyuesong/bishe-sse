function show_up_files(contextPath, attachment_list_grid_id) {
  $.ajax({
    url : contextPath + "/student/document/getAllTempAttachments",
    type : "get",
    success : function(data, textStatus) {
      $("#" + attachment_list_grid_id).datagrid('reload');
    }
  });
};

function delete_one_attachment(contextPath, id, attachment_list_grid_id) {
  $.ajax({
    url : contextPath + "/student/document/deleteOneTempAttachmentByAttachmentId?attachmentId=" + id,
    type : "get",
    success : function(data, textStatus) {
      $("#" + attachment_list_grid_id).datagrid('reload');
    },
    error : function() {
      $.messager.alert("错误", "删除失败，请联系管理员");
    }
  });
};