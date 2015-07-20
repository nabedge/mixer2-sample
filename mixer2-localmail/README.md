HTML mail sample with FakeSMTP. see
https://nilhcem.github.io/FakeSMTP/

Start FakeSMTP.
```
java -jar fakeSMTP.x.x.jar
```
Set port:1025, directory:any directory, and start server.
Run Main.java.
You will get HTML mail which values are replaced by Mixer2.

Mixer2を用いて加工したHTMLメールを送るアプリケーションです。
実行すると、Main.java内のHOST_NAME, PORTで指定した宛先にSMTPプロトコルでメールを送信します。
FakeSMTPを用いることで、ローカル環境でメールの送信を確認することができます。
https://nilhcem.github.io/FakeSMTP/

使用例:
FakeSMTPの公式サイトからjarをダウンロードし、ホーム直下にjarが有ると仮定します。
以下のコマンドでFakeSMTPがGUIで立ち上がります。
```
java -jar fakeSMTP.x.x.jar
```
ポート番号を1025、メッセージ保存先を任意のディレクトリに指定し、サーバー起動を押してください。

この状態でMainを実行すると、HTMLメールを受け取ることができます。
