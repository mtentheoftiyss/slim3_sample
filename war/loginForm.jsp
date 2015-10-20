<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>LoginForm</title>
</head>
<body>
<div id="page">
  <p><a href="/openIdConnectLogin?openid_identifier=https://accounts.google.com/o/oauth2/auth">Googleでログイン</a></p>
  <p><a href="/openIdLogin?openid_identifier=https://mixi.jp/">Mixiでログイン</a></p>
  <p><a href="/openIdLogin?openid_identifier=http://www.yahoo.co.jp/">Yahooでログイン</a></p>
  <p><a href="/openIdLogin?openid_identifier=http://livedoor.com/">Livedoorでログイン</a></p>
  <p><a href="/openIdLogin?openid_identifier=http://www.hatena.ne.jp/xfan/">はてな(id:xfan)でログイン</a></p>
</div>
</body>
</html>
