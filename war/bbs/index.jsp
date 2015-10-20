<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>slim3_sample</title>
<link href="/css/bbs.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="page">
  <h1>slim3_sample</h1>
  <div class="err">${errors.message}</div>
  <table>
    <thead>
      <tr>
        <td class="subject">タイトル</td>
        <td class="userName">投稿者</td>
        <td class="post_date">投稿日時</td>
        <td class="last_comment_id">コメント数</td>
        <td class="last_comment_time">最終コメント日時</td>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="head" items="${headList}" varStatus="hs">
        <tr>
          <td><a href="read?key=${f:h(head.key)}">${f:h(head.subject)}</a></td>
          <td>${f:h(head.userName)}</td>
          <td><fmt:formatDate value="${head.postDate}" pattern="yyyy/MM/dd HH:mm:ss" /></td>
          <td>${head.lastCommentId}</td>
          <td><fmt:formatDate value="${head.lastCommentDate}" pattern="yyyy/MM/dd HH:mm:ss" /></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  <p><a href="/bbs/create">新しい記事を投稿する</a></p>
</div>
</body>
</html>
