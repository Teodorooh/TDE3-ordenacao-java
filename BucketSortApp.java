public class BucketSortApp {

    static class Metrics { long comps, swaps, loops, nanos; }
    static int[] copy(int[] a){ int[] c=new int[a.length]; for(int i=0;i<a.length;i++) c[i]=a[i]; return c; }
    static void swap(int[] a,int i,int j,Metrics m){ int t=a[i]; a[i]=a[j]; a[j]=t; m.swaps++; }

    static final int[] V1 = {12,18,9,25,17,31,22,27,16,13,19,23,20,30,14,11,15,24,26,28};
    static final int[] V2 = {5,7,9,10,12,14,15,17,19,21,22,23,24,25,27,28,29,30,31,32};
    static final int[] V3 = {99,85,73,60,50,40,35,30,25,20,15,14,13,12,11,10,9,8,7,6};

    // insertion sort simples (usado dentro do bucket)
    static void insertion(int[] a, int len, Metrics m){
        for(int i=1;i<len;i++){
            m.loops++;
            int key = a[i];
            int j = i-1;
            while(j>=0){
                m.loops++; m.comps++;
                if(a[j] > key){ a[j+1]=a[j]; j--; m.swaps++; } else break;
            }
            a[j+1]=key;
        }
    }

    // BUCKET SORT com matriz de buckets (sem coleções)
    static Metrics bucketSort(int[] a){
        Metrics m = new Metrics(); long t0=System.nanoTime();
        int n = a.length; if(n<=1){ m.nanos=System.nanoTime()-t0; return m; }

        int min=a[0], max=a[0];
        for(int i=1;i<n;i++){ m.loops++; if(a[i]<min) min=a[i]; if(a[i]>max) max=a[i]; }

        int B = Math.max(2, n/2); // qtd de buckets (ajustável)
        int[][] buckets = new int[B][n]; // cada bucket pode receber até n elems (limite superior)
        int[] size = new int[B];

        long range = (long)max - (long)min + 1L;
        for(int i=0;i<n;i++){
            m.loops++;
            // distribuição proporcional ao range
            int bi = (int)(((long)(a[i]-min) * (long)B) / range);
            if(bi==B) bi=B-1; // borda
            buckets[bi][ size[bi]++ ] = a[i];
        }

        // ordena cada bucket e concatena
        int k=0;
        for(int b=0;b<B;b++){
            m.loops++;
            if(size[b]>1) insertion(buckets[b], size[b], m);
            for(int i=0;i<size[b];i++){ m.loops++; a[k++]=buckets[b][i]; }
        }

        m.nanos=System.nanoTime()-t0; return m;
    }

    // ---- baselines ----
    static Metrics bubbleFlag(int[] a){
        Metrics m=new Metrics(); long t0=System.nanoTime();
        int n=a.length; boolean trocou=true;
        for(int pass=0; pass<n-1 && trocou; pass++){
            m.loops++; trocou=false;
            for(int i=0;i<n-1-pass;i++){
                m.loops++; m.comps++;
                if(a[i]>a[i+1]){ swap(a,i,i+1,m); trocou=true; }
            }
        }
        m.nanos=System.nanoTime()-t0; return m;
    }
    static Metrics selectionSort(int[] a){
        Metrics m=new Metrics(); long t0=System.nanoTime();
        int n=a.length;
        for(int i=0;i<n-1;i++){
            m.loops++; int min=i;
            for(int j=i+1;j<n;j++){ m.loops++; m.comps++; if(a[j]<a[min]) min=j; }
            if(min!=i) swap(a,i,min,m);
        }
        m.nanos=System.nanoTime()-t0; return m;
    }
    static Metrics cocktailSort(int[] a){
        Metrics m=new Metrics(); long t0=System.nanoTime();
        int start=0,end=a.length-1; boolean trocou=true;
        while(trocou){
            m.loops++; trocou=false;
            for(int i=start;i<end;i++){ m.loops++; m.comps++; if(a[i]>a[i+1]){ swap(a,i,i+1,m); trocou=true; } }
            if(!trocou) break;
            trocou=false; end--;
            for(int i=end;i>start;i--){ m.loops++; m.comps++; if(a[i-1]>a[i]){ swap(a,i-1,i,m); trocou=true; } }
            start++;
        }
        m.nanos=System.nanoTime()-t0; return m;
    }

    // ---- bench infra ----
    static class Row { String name; Metrics v1,v2,v3; Row(String n,Metrics a,Metrics b,Metrics c){name=n;v1=a;v2=b;v3=c;} }
    static Metrics runOn(int[] base, java.util.function.Function<int[],Metrics> f){ return f.apply(copy(base)); }
    static void header(){
        System.out.println("\n================ BUCKET SORT — comparação =================\n");
        System.out.printf("%-14s | %-28s | %-28s | %-28s%n",
                "Algoritmo","V1: tempo/cmp/swaps/loops","V2: tempo/cmp/swaps/loops","V3: tempo/cmp/swaps/loops");
        System.out.println("-".repeat(108));
    }
    static void row(Row r){
        System.out.printf("%-14s | %7.3f/%-7d/%-6d/%-6d | %7.3f/%-7d/%-6d/%-6d | %7.3f/%-7d/%-6d/%-6d%n",
                r.name,
                r.v1.nanos/1e6, r.v1.comps, r.v1.swaps, r.v1.loops,
                r.v2.nanos/1e6, r.v2.comps, r.v2.swaps, r.v2.loops,
                r.v3.nanos/1e6, r.v3.comps, r.v3.swaps, r.v3.loops
        );
    }
    static void ranking(Row[] rows){
        System.out.println("\n>>> Ranking por tempo (V1+V2+V3):");
        java.util.Arrays.sort(rows,(a,b)->Double.compare(
                (a.v1.nanos+a.v2.nanos+a.v3.nanos),(b.v1.nanos+b.v2.nanos+b.v3.nanos)));
        for(int i=0;i<rows.length;i++){
            double t=(rows[i].v1.nanos+rows[i].v2.nanos+rows[i].v3.nanos)/1e6;
            System.out.printf("%d) %-12s %.3f ms%n",i+1,rows[i].name,t);
        }
        System.out.println();
    }

    public static void main(String[] args){
        header();
        Row[] rows = new Row[]{
                new Row("BucketSort",  runOn(V1,BucketSortApp::bucketSort),  runOn(V2,BucketSortApp::bucketSort),  runOn(V3,BucketSortApp::bucketSort)),
                new Row("BubbleFlag",  runOn(V1,BucketSortApp::bubbleFlag),  runOn(V2,BucketSortApp::bubbleFlag),  runOn(V3,BucketSortApp::bubbleFlag)),
                new Row("Selection",   runOn(V1,BucketSortApp::selectionSort),runOn(V2,BucketSortApp::selectionSort),runOn(V3,BucketSortApp::selectionSort)),
                new Row("Cocktail",    runOn(V1,BucketSortApp::cocktailSort), runOn(V2,BucketSortApp::cocktailSort), runOn(V3,BucketSortApp::cocktailSort))
        };
        for(Row r: rows) row(r);
        ranking(rows);
        System.out.println("Análise curta: bucket sort tende a ir bem com faixas uniformes; justifique com as métricas.\n");
    }
}
