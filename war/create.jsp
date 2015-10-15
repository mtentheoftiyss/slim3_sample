<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
  <form method="post" action="postEntry">
    <table>
      <thead><tr><td colspan="2">新しい記事の投稿</td></tr></thead>
      <tbody>
        <tr>
          <td class="label">タイトル</td>
          <td class="elem">
            <input type="text" ${f:text("subject")} class="normal ${f:errorClass('subject', 'err')}" />
            <span class="err">${errors.subject}</span>
          </td>
        </tr>
        <tr>
          <td>お名前</td>
          <td>
            <input type="text" ${f:text("userName")} class="normal ${f:errorClass('userName', 'err')}" />
            <span class="err">${errors.userName}</span>
          </td>
        </tr>
        <tr>
          <td>本文</td>
          <td>
            <textarea name="text" class="largetext ${f:errorClass('text', 'err')}">${f:h(text)}</textarea>
            <div class="err">${errors.text}</div>
          </td>
        </tr>
        <tr>
          <td>編集用パスワード</td>
          <td>
            <input type="password" ${f:text("password")} class="password ${f:errorClass('password', 'err')}" />※未設定の場合は後から編集できません。
            <div class="err">${errors.password}</div>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <input type="submit" value=" 投稿する " class="button" />
            <input type="reset" value="リセット" class="button" />
          </td>
        </tr>
      </tbody>
    </table>
  </form>
  <p><a href="index">戻る</a></p>
</div>
</body>
</html>
