@echo off
cd /d "%~dp0"
chcp 65001 > nul
if not exist bin mkdir bin
javac --release 17 -encoding UTF-8 -cp "lib/*" -d bin src\br\com\starlog\model\Produto.java src\br\com\starlog\dao\ProdutoDAO.java src\br\com\starlog\factory\ConexaoFactory.java src\br\com\starlog\main\Main.java src\br\com\starlog\dao\ProdutoDAOSqlite.java
if errorlevel 1 goto fim
java -cp "bin;lib/*" br.com.starlog.main.Main
:fim
pause
