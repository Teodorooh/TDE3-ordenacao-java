## 🧮 TDE 03 – Algoritmos de Ordenação (Java)

Implementação e comparação dos algoritmos **Comb Sort**, **Gnome Sort** e **Bucket Sort**, avaliando seu desempenho em relação a algoritmos clássicos de referência: **Bubble Sort (com flag de parada)**, **Selection Sort** e **Cocktail Sort**.

O trabalho foi desenvolvido em **Java puro**, sem o uso de estruturas ou funções prontas da API (`List`, `ArrayList`, `Collections.sort()`, etc.), conforme as restrições do enunciado.

---

## 📘 Estrutura do Projeto

Cada algoritmo solicitado possui seu próprio arquivo `.java`, contendo:
- A **implementação** do algoritmo principal (Comb, Gnome ou Bucket);
- As **versões de comparação** (Bubble Flag, Selection, Cocktail);
- O código de **benchmark** (medições e tabelas);
- O método `main()` que executa automaticamente os testes com três vetores definidos.

Arquivos principais:

- CombSortApp.java
- GnomeSortApp.java
- BucketSortApp.java

## 🧠 Vetores Utilizados

Todos os algoritmos foram testados com os seguintes vetores base:

```java
int[] vetor1 = {12, 18, 9, 25, 17, 31, 22, 27, 16, 13, 19, 23, 20, 30, 14, 11, 15, 24, 26, 28};
int[] vetor2 = {5, 7, 9, 10, 12, 14, 15, 17, 19, 21, 22, 23, 24, 25, 27, 28, 29, 30, 31, 32};
int[] vetor3 = {99, 85, 73, 60, 50, 40, 35, 30, 25, 20, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6};
````

## ⚙️ Como Compilar e Executar

__1.__  Abra o terminal na pasta do projeto e compile os três programas:

  * javac CombSortApp.java GnomeSortApp.java BucketSortApp.java
  
__2.__ Execute cada um separadamente:

- java CombSortApp; 
- java GnomeSortApp; 
- java BucketSortApp;

__3.__ Cada execução imprimirá no console:

- Uma tabela comparando os algoritmos nos três vetores;

- Rankings por tempo, número de trocas e iterações;

- Um resumo textual automático.

## 📊 Estrutura da Saída

![Resultados CombSort](https://github.com/Teodorooh/TDE3-ordenacao-java/blob/main/CombSort.png)

![Resultados GnomeSort](https://github.com/Teodorooh/TDE3-ordenacao-java/blob/main/GnomeSort.png)

![Resultados BucketSort](https://github.com/Teodorooh/TDE3-ordenacao-java/blob/main/BucketSort.png)

 
## 🧾 Análise e Conclusões

* Após a execução dos testes:

  - O Comb Sort apresentou desempenho consistente e tempo total inferior ao Bubble Sort e Selection Sort, aproximando-se de algoritmos de complexidade O(n log n) em alguns casos.

  - O Gnome Sort teve comportamento semelhante ao Insertion Sort, sendo eficiente em listas quase ordenadas, mas lento no pior caso (vetor inverso).

  - O Bucket Sort foi o que mais se beneficiou de dados bem distribuídos (como o vetor 1), demonstrando ótimo desempenho geral, especialmente em listas grandes ou com valores concentrados em faixas previsíveis.

* O experimento confirma que:

  - O Comb Sort é uma evolução direta do Bubble Sort, mantendo simplicidade e ganhando velocidade por meio do ajuste de "gap" (intervalo).

  - O Gnome Sort é didaticamente interessante, mas pouco eficiente em vetores desordenados.

  - O Bucket Sort demonstra a melhor eficiência geral no conjunto testado, sendo ideal quando os dados possuem distribuição uniforme e conhecida.
 
 * Portanto, o Bucket Sort foi o melhor desempenho geral, seguido de perto pelo Comb Sort, enquanto o Gnome Sort destacou-se apenas nos casos quase ordenados.
