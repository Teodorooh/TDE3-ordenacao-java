public class CombSortApp {

    // --------- Métricas ----------
    static class Metrics {
        long comps;      // comparações
        long swaps;      // trocas (swap)
        long loops;      // iterações de laço
        long nanos;      // tempo
    }

    // --------- Util ----------
    static int[] copy(int[] a){ int[] c=new int[a.length]; for(int i=0;i<a.length;i++) c[i]=a[i]; return c; }
    static void swap(int[] a,int i,int j,Metrics m){ int t=a[i]; a[i]=a[j]; a[j]=t; m.swaps++; }

    // --------- VETORES DO TRABALHO ----------
    static final int[] V1 = {12,18,9,25,17,31,22,27,16,13,19,23,20,30,14,11,15,24,26,28};
    static final int[] V2 = {5,7,9,10,12,14,15,17,19,21,22,23,24,25,27,28,29,30,31,32};
    static final int[] V3 = {99,85,73,60,50,40,35,30,25,20,15,14,13,12,11,10,9,8,7,6};

    // ===================== ALGORITMOS =====================

    // COMB SORT (shrink padrão 1.3)
    static Metrics combSort(int[] a){
        Metrics m = new Metrics();
        long t0 = System.nanoTime();

        int n = a.length;
        int gap = n;
        boolean sorted = false;
        while(!sorted){
            // loop externo
            m.loops++;
            gap = (int)Math.floor(gap/1.3);
            if(gap<=1){ gap=1; sorted=true; }
            for(int i=0;i+gap<n;i++){
                m.loops++;
                m.comps++; // A[i] > A[i+gap] ?
                if(a[i] > a[i+gap]){
                    swap(a, i, i+gap, m);
                    sorted = false;
                }
            }
        }

        m.nanos = System.nanoTime() - t0;
        return m;
    }

    // BUBBLE com FLAG (parada antecipada)
    static Metrics bubbleFlag(int[] a){
        Metrics m = new Metrics();
        long t0 = System.nanoTime();

        int n = a.length;
        boolean trocou = true;
        for(int pass=0; pass<n-1 && trocou; pass++){
            m.loops++;
            trocou = false;
            for(int i=0; i<n-1-pass; i++){
                m.loops++;
                m.comps++;
                if(a[i] > a[i+1]){
                    swap(a, i, i+1, m);
                    trocou = true;
                }
            }
        }

        m.nanos = System.nanoTime() - t0;
        return m;
    }

    // SELECTION SORT
    static Metrics selectionSort(int[] a){
        Metrics m = new Metrics();
        long t0 = System.nanoTime();

        int n=a.length;
        for(int i=0;i<n-1;i++){
            m.loops++;
            int min=i;
            for(int j=i+1;j<n;j++){
                m.loops++;
                m.comps++;
                if(a[j]<a[min]) min=j;
            }
            if(min!=i) swap(a,i,min,m);
        }

        m.nanos = System.nanoTime() - t0;
        return m;
    }

    // COCKTAIL SORT
    static Metrics cocktailSort(int[] a){
        Metrics m = new Metrics();
        long t0 = System.nanoTime();

        int start=0, end=a.length-1;
        boolean trocou=true;
        while(trocou){
            m.loops++;
            trocou=false;
            for(int i=start;i<end;i++){
                m.loops++;
                m.comps++;
                if(a[i]>a[i+1]){ swap(a,i,i+1,m); trocou=true; }
            }
            if(!trocou) break;
            trocou=false; end--;
            for(int i=end;i>start;i--){
                m.loops++;
                m.comps++;
                if(a[i-1]>a[i]){ swap(a,i-1,i,m); trocou=true; }
            }
            start++;
        }

        m.nanos = System.nanoTime() - t0;
        return m;
    }

    // ===================== BENCH / TABELA =====================

    static class Row {
        String name;
        Metrics v1,v2,v3;
        Row(String n, Metrics a, Metrics b, Metrics c){name=n;v1=a;v2=b;v3=c;}
    }

    static Metrics runOn(int[] base, java.util.function.Function<int[],Metrics> alg){
        return alg.apply(copy(base));
    }

    static void printHeader(String title){
        System.out.println("\n================ " + title + " ================\n");
        System.out.printf("%-14s | %-28s | %-28s | %-28s%n",
                "Algoritmo",
                "V1: tempo(ms)/cmp/swaps/loops",
                "V2: tempo(ms)/cmp/swaps/loops",
                "V3: tempo(ms)/cmp/swaps/loops");
        System.out.println("-".repeat(108));
    }

    static void printRow(Row r){
        System.out.printf("%-14s | %7.3f/%-7d/%-6d/%-6d | %7.3f/%-7d/%-6d/%-6d | %7.3f/%-7d/%-6d/%-6d%n",
                r.name,
                r.v1.nanos/1e6, r.v1.comps, r.v1.swaps, r.v1.loops,
                r.v2.nanos/1e6, r.v2.comps, r.v2.swaps, r.v2.loops,
                r.v3.nanos/1e6, r.v3.comps, r.v3.swaps, r.v3.loops
        );
    }

    static void printRanking(Row[] rows){
        System.out.println("\n>>> Ranking por tempo (ms) — somando V1+V2+V3:");
        java.util.Arrays.sort(rows, (a,b) -> {
            double ta = (a.v1.nanos+a.v2.nanos+a.v3.nanos);
            double tb = (b.v1.nanos+b.v2.nanos+b.v3.nanos);
            return Double.compare(ta, tb);
        });
        for(int i=0;i<rows.length;i++){
            double tms = (rows[i].v1.nanos+rows[i].v2.nanos+rows[i].v3.nanos)/1e6;
            System.out.printf("%d) %-12s  %.3f ms%n", i+1, rows[i].name, tms);
        }

        System.out.println("\n>>> Menos trocas (swaps) — somando V1+V2+V3:");
        java.util.Arrays.sort(rows, (a,b) -> Long.compare(
                (a.v1.swaps+a.v2.swaps+a.v3.swaps),
                (b.v1.swaps+b.v2.swaps+b.v3.swaps)
        ));
        for(int i=0;i<rows.length;i++){
            long s = (rows[i].v1.swaps+rows[i].v2.swaps+rows[i].v3.swaps);
            System.out.printf("%d) %-12s  %d swaps%n", i+1, rows[i].name, s);
        }

        System.out.println("\n>>> Menos iterações (loops) — somando V1+V2+V3:");
        java.util.Arrays.sort(rows, (a,b) -> Long.compare(
                (a.v1.loops+a.v2.loops+a.v3.loops),
                (b.v1.loops+b.v2.loops+b.v3.loops)
        ));
        for(int i=0;i<rows.length;i++){
            long s = (rows[i].v1.loops+rows[i].v2.loops+rows[i].v3.loops);
            System.out.printf("%d) %-12s  %d loops%n", i+1, rows[i].name, s);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        printHeader("COMB SORT — comparação com Bubble(Flag), Selection e Cocktail");

        Row[] rows = new Row[] {
            new Row("CombSort",
                    runOn(V1, CombSortApp::combSort),
                    runOn(V2, CombSortApp::combSort),
                    runOn(V3, CombSortApp::combSort)),
            new Row("BubbleFlag",
                    runOn(V1, CombSortApp::bubbleFlag),
                    runOn(V2, CombSortApp::bubbleFlag),
                    runOn(V3, CombSortApp::bubbleFlag)),
            new Row("Selection",
                    runOn(V1, CombSortApp::selectionSort),
                    runOn(V2, CombSortApp::selectionSort),
                    runOn(V3, CombSortApp::selectionSort)),
            new Row("Cocktail",
                    runOn(V1, CombSortApp::cocktailSort),
                    runOn(V2, CombSortApp::cocktailSort),
                    runOn(V3, CombSortApp::cocktailSort))
        };

        for (Row r : rows) printRow(r);
        printRanking(rows);

        // Observação/descrição automática de "quem foi melhor"
        System.out.println("Análise curta:");
        System.out.println("- O ranking por tempo indica o mais rápido no conjunto (V1+V2+V3).");
        System.out.println("- O ranking por swaps mostra quem movimenta menos elementos.");
        System.out.println("- O ranking por loops evidencia quem roda menos iterações.");
        System.out.println("=> Use esses três critérios para justificar sua conclusão no relatório.\n");
    }
}
