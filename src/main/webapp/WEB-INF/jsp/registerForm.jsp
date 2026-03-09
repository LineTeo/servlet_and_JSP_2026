<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー登録</title>
<style>
  /* 各入力行のスタイル */
  .form-item {
    display: flex;          /* 横並びにする */
    align-items: center;    /* 縦方向の中央揃え */
    margin-bottom: 10px;    /* 行間の余白 */
  }

  /* ラベルのスタイル */
  .form-item label {
    width: 100px;           /* ラベルの幅を固定して左端を揃える */
    flex-shrink: 0;         /* 幅を縮ませない */
  }

  /* 自己紹介など、複数行入力のスタイル調整 */
  .form-item.align-top {
    align-items: flex-start; /* ラベルを上側に配置 */
  }

  /* 自己紹介欄のサイズ指定 */
  .large-field {
    width: 300px;           /* 横幅 */
    height: 100px;          /* 高さ */
    resize: vertical;       /* ユーザーが縦方向にのみサイズ変更可能にする */
  }
</style>

</head>
<body>

<form action="RegisterUser" method="post">
  <div class="form-item">
    <label for="id">ログインID：</label>
    <input type="text" id="id" name="id">
  </div>

  <div class="form-item">
    <label for="pass">パスワード：</label>
    <input type="password" id="pass" name="pass">
  </div>

  <div class="form-item">
    <label for="name">名前：</label>
    <input type="text" id="name" name="name">
  </div>

  <div class="form-item align-top">
    <label for="intro">自己紹介：</label>
    <textarea id="intro" name="intro" class="large-field"></textarea>
  </div>

  <div class="form-item">
    <label></label> <input type="submit" value="確認">
  </div>
</form>

</body>
</html>