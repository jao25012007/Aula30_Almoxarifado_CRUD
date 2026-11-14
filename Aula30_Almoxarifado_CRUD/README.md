# Aula 30 — Almoxarifado StarLog com CRUD e SQLite

Abra `Lista_Interativa_Resolvida.html` no navegador para conferir as quatro respostas e os três desafios já preenchidos. Os próprios validadores da lista conferem as respostas ao abrir o arquivo.

## Executar o projeto

Abra esta pasta no VS Code e execute a classe `br.com.starlog.main.Main`. No Windows, você também pode abrir `executar.bat` com o JDK 17 ou mais recente no PATH. Os JARs necessários estão na pasta `lib`.

O programa cria um arquivo SQLite em uma pasta temporária nova a cada execução. O terminal mostra o caminho do arquivo. O roteiro cadastra dois produtos, lista em ordem de nome, atualiza o teclado para R$ 289,90 e 15 unidades, remove o mousepad e informa que o ID 888 está ausente. A listagem final contém apenas o teclado.

## Respostas comentadas

1. **A.** CRUD agrupa as operações Create, Read, Update e Delete, representadas no SQL por INSERT, SELECT, UPDATE e DELETE.
2. **C.** `executeUpdate()` informa quantas linhas foram afetadas. Com o filtro por ID, zero indica que nenhum registro foi encontrado para a operação.
3. **D.** Um DELETE sem WHERE remove todas as linhas da tabela.
4. **B.** A View coleta os dados e delega ao Controller. O Controller valida e chama o contrato DAO. A implementação DAO executa a operação via JDBC.

Os três desafios estão em `ProdutoDAOSqlite.java`. A atualização e a exclusão filtram pelo ID com parâmetros vinculados e verificam as linhas afetadas. A listagem usa `ORDER BY nome ASC` e reaproveita `mapearLinha`.
